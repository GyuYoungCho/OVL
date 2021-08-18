<template>
  <v-dialog v-model="modalDetail" @click:outside="cancelDetail()" scrollable max-width="300">
    <v-card>
      <!-- 모달의 초록색 상단바 영역 -->
      <v-toolbar color="#49784B" dark dense>
        <v-toolbar-title >{{this.selectpot.title}}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon dark @click="cancelDetail()" justify="end">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-toolbar>
      <!-- 모달 body 부분 -->
      <v-container class="vetModal">
        <div class="vetModalContent">
          {{this.selectpot.content}}
        </div>
        <div class="vetIconsWrap mt-2">
          <v-icon class="veticons mr-2">mdi-map-marker</v-icon>
          <span class="vetModalSpan">{{this.selectpot.restaurant_name}}</span>
        </div>
        <div class="vetIconsWrap mt-2">
          <v-icon class="veticons mr-2">mdi-calendar-month</v-icon>
          <span class="vetModalSpan">{{this.meet_date}}</span> 
        </div>
        <div class="vetIconsWrap mt-2">
          <v-icon class="veticons mr-2">mdi-clock-time-nine-outline</v-icon>
          <span class="vetModalSpan">{{this.meet_time}}</span>
        </div>
        <div class="d-flex mt-2">
          <div class="mt-2">
            <v-icon class="veticons mr-2">mdi-account-outline</v-icon>
          </div>
          <div>
            <ProfileName v-for="(auser, index) in potattendusers" :key="index" :user="auser" />
          </div>
        </div>
        <!-- 버튼 영역 -->
        <article class="vetModalButtonArea mt-2">
          <div v-if="mypot">
            <button class="vetModalButton" @click="potModify()">수정</button>
            <button class="vetModalButton" @click="openMessageModal('delete')">삭제</button>
          </div>
          <div v-else>
            <button class="vetModalButton" v-if="attendme" @click="openMessageModal('attend')">참여하기</button>
            <button class="vetModalButton" v-else @click="openMessageModal('cancel')">참여취소</button>
          </div>
        </article>
      </v-container>
              
           
    </v-card>
  </v-dialog>
</template>

<script>
import moment from "moment"
import { mapGetters} from 'vuex';
import ProfileName from "@/components/basic/ProfileName.vue";

export default {
  data(){
    return{
      modals : true,
    }
  },
  components: {
    ProfileName
  },

  props:{
    modalDetail : Boolean,
  },
  computed:{
    ...mapGetters("user", ["userinfo"]),
    ...mapGetters("pot", ["selectpot","potattendusers"]),
    meet_date(){
        return moment(this.selectpot.time).format("ddd MM/DD")
    },
    meet_time(){
        return moment(this.selectpot.time).format("HH:mm")
    },
    attendme(){
        let flag = true;  // 내가 참여 중인지 확인
        if(this.potattendusers.length!=0){
          this.potattendusers.forEach((item) => {
            if (item.userid == this.userinfo.userid) {
              flag = false
            }
          });
        }
        return flag
    },
    mypot(){ // 내가 만든 것인지 확인
     
      if(this.selectpot.length!=0 && this.userinfo.userid == this.selectpot.userid.userid) {
        return true
      }else{
        return false
      }
    }
  },
  methods:{

    cancelDetail() {
      this.$emit('openDetailModal', false)
    },

    openMessageModal(sign){ // 참여 삭제 취소에 대한 sign을 보냄
        this.$emit('openMessageModal', true, sign)
    },
    potModify(){ //팟 수정 화면 이동!
        // this.selectPot(this.potitem)
        this.$router.push({path:"/vetparty_create/1"});
    },

  },

}
</script>

<style>

</style>