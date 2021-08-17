<template>
  <v-container class="vet-wrap">
    <!-- 달력 -->
    <div class="vetIconsWrap ml-2">
      <v-icon class="veticons mr-2">mdi-calendar-month</v-icon>
      <span class="vetIconsWrapSpan">{{this.meet_date}}</span>    
    </div>
    <v-divider vertical></v-divider>
    <!-- 시간  -->
    <div class="vetIconsWrap">
      <v-icon class="veticons mr-2">mdi-clock-time-nine-outline</v-icon>
      <span class="vetIconsWrapSpan">{{this.meet_time}}</span>
    </div>
    <v-divider vertical></v-divider>
    <!-- 인원수 -->
    <div class="vetIconsWrap">
      <v-icon class="veticons mr-2">mdi-account-outline</v-icon>
      <span class="vetIconsWrapSpan">{{this.userpot.pot_count}}명 / {{this.userpot.total_people}}명 </span>
    </div>
    <v-divider vertical></v-divider>
    <!-- 아이콘 -->
    <div class="restrictor">
      <img src="@/assets/icon/meat.png" v-if="btnActive[4]" />
      <img src="@/assets/icon/fish.png" v-else-if="btnActive[3]" />
      <img src="@/assets/icon/milk.png" v-else-if="btnActive[2]" />
      <img src="@/assets/icon/egg.png" v-else-if="btnActive[1]" />
      <img src="@/assets/icon/vege.png" v-else />
    </div>
    <v-divider vertical></v-divider>
    <!-- 설정 -->
    <div @click="openDetailModal(true)">
        <v-icon class="veticons">mdi-dots-horizontal</v-icon>
    </div>
    <div></div>
  </v-container>
</template>

<script>
import moment from 'moment';
import { mapGetters, mapActions} from 'vuex';

export default {
    data() {
        return {
            allSteps: [
                "과일채소", "계란","유제품","생선","고기"
            ],
            
            btnActive: {0:false,1:false,2:false,3:false,4:false},
            modalOpen : false,
        };
    },
    props: {
        userpot: Object,
    },

    computed: {
        ...mapGetters("user", ["userinfo"]),
        meet_date(){
            return moment(this.userpot.time).format("ddd MM/DD")
        },
        meet_time(){
            return moment(this.userpot.time).format("HH:mm")
        },
    },
    created(){
        this.stepToIcon(this.userpot.step)
    },

    methods: {
        ...mapActions('pot', ['selectPot','potAttendUsers']),
        
        openDetailModal(val){
            this.selectPot(this.userpot)
            this.potAttendUsers(this.userpot.potid)
            this.$emit('openDetailModal', val)
        },
        stepToIcon(item) {
            for (let i=0;i<5;i++){
                if(this.allSteps[i]==item){
                    this.btnActive[i] = true;
                    break
                }
            }
        },
        colorChange(flag) {
            this.isColor = flag;
        },

    },
    
}
</script>

<style>

</style>