/<template>
    <div class="mt-3">
    <div style="text-align: left" :disabled="avail" class="potlist"
        :class="{ 'abledArea': !avail, 'disabledArea': avail , }"
        @click="openDetailModal(true)" >
        <v-divider></v-divider>
        
        <v-container class="d-flex pt-0 pb-0 pl-0">
        
            <profile-name :user="potitem.userid"></profile-name>
        
        <v-row align="center" justify="end">
        <button v-if="avail" class="py-0 px-3 mt-0 notBtnComp" style="font-size:12px"
                >마감</button>
        
        <v-icon>mdi-map-marker</v-icon>
        
            <span class="p">{{this.potitem.restaurant_name}}</span>
        
        </v-row>
        
        </v-container>
    
    <v-row>
        <v-container class="d-flex pb-1 ml-3">
            <v-list-item-subtitle v-text="potitem.title"></v-list-item-subtitle>
        </v-container>
    </v-row>
    <v-row class="mt-4">
        <v-container class="d-flex subinfo mb-2 pt-0 pb-0">
            <v-col cols="3" class="pa-0">
                <v-icon>mdi-calendar-month</v-icon>
                <span>{{this.meet_date}}</span>
            </v-col>
            <v-divider vertical class="mx-1"></v-divider>
            <v-col cols="2" class="pa-0">
                <v-icon>mdi-clock-time-nine-outline</v-icon>
                <span>{{this.meet_time}}</span>
            </v-col>
            <v-divider vertical class="mx-2"></v-divider>
            <v-col cols="auto" class="pa-0">
            <v-icon>mdi-account-outline</v-icon>
            <span>{{this.potitem.pot_count}}명/</span>
            <span>{{this.potitem.total_people}}명</span>
            </v-col>
            <v-divider vertical class="mx-2"></v-divider>
            <v-col cols="1" class="pa-0">
            <div class="icon_frame">
                <v-img src="@/assets/icon/meat.png" alt="" v-if="btnActive[4]"> </v-img>
                <v-img src="@/assets/icon/fish.png" alt="" v-else-if="btnActive[3]"> </v-img>
                <v-img src="@/assets/icon/milk.png" alt="" v-else-if="btnActive[2]"> </v-img>
                <v-img src="@/assets/icon/egg.png" alt="" v-else-if="btnActive[1]"> </v-img>
                <v-img src="@/assets/icon/vege.png" alt="" v-else> </v-img>
            </div>
            </v-col>
            <v-spacer></v-spacer>
            <v-col cols="2" class="pa-0">
                <button class="py-0 px-3 BtnComp mt-0" style="font-size:12px"
                    @click.stop="openAttendModal(true)">참여</button>
            </v-col>
        </v-container>
    </v-row>
   
    </div>  
    </div>
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