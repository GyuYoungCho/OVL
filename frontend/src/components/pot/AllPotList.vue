/<template>
  <section class="allPotList">
    <v-divider></v-divider>
    <div :disabled="avail" :class="{'disabledArea': avail}" @click="openDetailModal(true)" >
      <!-- 1. 프로필 사진, 유저네임, 마커와 장소가 있는 영역 -->
      <div class="allPotListName">
        <profile-name :user="potitem.userid"></profile-name>
        <v-spacer></v-spacer>
        <div class="allPotListNameIcon">
          <button v-if="avail" class="notBtnComp mr-2">마감</button>
          <v-icon class="allPotListNameMarker" small>mdi-map-marker</v-icon>
        </div>
        <span>{{this.potitem.restaurant_name}}</span>
      </div>
      <!-- 2. 채식팟에 content 가 담기는 영역 -->
      <div class="allPotListContent">
        <span>{{potitem.content}}</span>
      </div>
      <!-- 3. 달력 / 시간 / 인원수 / 아이콘 / 참여버튼 영역 -->
      <article class="allPotListDetail">
        <!-- (1) 달력 -->
        <div class="vetIconsWrap">
          <v-icon class="veticons mr-2">mdi-calendar-month</v-icon>
          <span class="vetIconsWrapSpan">{{this.meet_date}}</span>
        </div>
        <v-divider vertical></v-divider>
        <!-- (2) 시간 -->
        <div class="vetIconsWrap">
          <v-icon class="veticons mr-2">mdi-clock-time-nine-outline</v-icon>
          <span class="vetIconsWrapSpan">{{this.meet_time}}</span>
        </div>
        <v-divider vertical></v-divider>
        <!-- (3) 인원수 -->
        <div class="vetIconsWrap">
          <v-icon class="veticons mr-2">mdi-account-outline</v-icon>
          <span class="vetIconsWrapSpan">{{this.potitem.pot_count}}명 / {{this.potitem.total_people}}명</span>
        </div>
        <v-divider vertical></v-divider>
        <!-- (4) 아이콘 -->
        <div class="vetIconsWrap">
          <img src="@/assets/icon/meat.png" v-if="btnActive[4]" /> 
          <img src="@/assets/icon/fish.png" v-else-if="btnActive[3]" /> 
          <img src="@/assets/icon/milk.png" v-else-if="btnActive[2]" /> 
          <img src="@/assets/icon/egg.png" v-else-if="btnActive[1]" /> 
          <img src="@/assets/icon/vege.png" v-else /> 
        </div>
        <div></div>
        <div></div>
        <div></div>
        <!-- (5) 참여버튼 -->
        <div class="vetIconsWrap">
           <button class="vetParticipate" @click.stop="openAttendModal(true)">참여하기</button>
        </div>
      </article>
    </div>  
  </section>
</template>

<script>
import moment from 'moment';
import { mapGetters, mapActions} from 'vuex';
import ProfileName from '@/components/basic/ProfileName.vue';

export default {
    components:{
        ProfileName,
    },
    data() {
        return {
            // 아이콘 표현을 위한 것들
            allSteps: [
                "과일채소", "계란","유제품","생선","고기"
            ],
            btnActive: {0:false,1:false,2:false,3:false,4:false},
            
            snackbar : false,
        };
    },
    props: {  // listt 화면에서 pot을 받아옴
        potitem: Object,
    },
    created(){
        this.stepToIcon(this.potitem.step)
    },
    computed: {
        
        ...mapGetters("user", ["userinfo"]),
        
        meet_date(){//날짜 출력
            return moment(this.potitem.time).format("ddd MM/DD")
        },
        meet_time(){ // 시간출력
            return moment(this.potitem.time).format("HH:mm")
        },
        avail(){ // 팟의 인원, 날짜에 따라 참여 가능을 결정할 변수
            
            return (this.potitem.total_people <= this.potitem.pot_count) || 
                    ((new Date(this.potitem.time)).getTime() < (new Date()).getTime())
        }
        
    },
    methods: {
        ...mapActions('pot', ['selectPot','potAttendUsers']),
        // pot과 참여목록을 store에 넣고 vetPartyDetail 모달이 열림
        openDetailModal(val){
            this.selectPot(this.potitem)
            this.potAttendUsers(this.potitem.potid)
            this.$emit('openDetailModal', val)
        },
        // 바로 참여할거냐 물어봄(list화면의 openAtttendMoal Method를 열어 message modal을 띄운다)
        openAttendModal(val){
            this.selectPot(this.potitem)
            this.potAttendUsers(this.potitem.potid)
            this.$emit('openAttendModal', val)
        },
        
        // step을 icon 변환
        stepToIcon(item) {
            for (let i=0;i<5;i++){
                if(this.allSteps[i]==item){
                    this.btnActive[i] = true;
                    break
                }
            }
        },
    },
    
}
</script>

<style>

</style>