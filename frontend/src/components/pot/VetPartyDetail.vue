<template>
  <div>
    <v-dialog
          v-model="modalDetail"
          height="500"
          @click:outside="cancelDetail()"
          transition="dialog-bottom-transition"
          max-width="1000px">
        <v-card tile>
          <v-toolbar color="#004627" dark>
          <v-toolbar-title >{{this.selectpot.title}}</v-toolbar-title>
            <profile-name :user="selectpot.userid" class="ml-3 mt-1"></profile-name>
            <v-spacer></v-spacer>
            <v-btn icon dark @click="cancelDetail()" justify="end">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-toolbar>
            <v-col>
              <v-row>
                <v-col cols="6" class="mt-3">
                  <v-row>
                    <v-icon>mdi-map-marker</v-icon>
                    <span>{{this.selectpot.restaurant_name}}</span>
                  </v-row>
                  <v-row class="mt-5">
                    <v-icon>mdi-calendar-month</v-icon>
                    <span>{{this.meet_date}}</span> 
                  </v-row>
                  <v-row class="mt-5">
                    <v-icon>mdi-clock-time-nine-outline</v-icon>
                    <span>{{this.meet_time}}</span>
                  </v-row>
                </v-col>
                <v-col cols="1" class="mt-1" justify="end">
                    <v-icon>mdi-account-outline</v-icon>
                </v-col>
                <v-col cols="3">
                  <profile-name v-for="(auser, index) in potattendusers" :key="index" :user="auser"></profile-name>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="7">
                  <span>{{this.selectpot.content}}</span>
                </v-col>
                <v-col v-if="mypot" justify="end">
                  <v-btn dark @click="potModify()"  width="10">
                    <span>수정</span>
                  </v-btn>
                  <v-btn dark @click="openMessageModal('delete')" justify="end" width="10">
                    <span>삭제</span>
                  </v-btn>
                </v-col>
                <v-col v-else>
                  <v-btn v-if="attendme" dark @click="openMessageModal('attend')" justify="end">
                    <span>참여하기</span>
                  </v-btn>
                  <v-btn v-else dark @click="openMessageModal('cancel')" justify="end">
                    <span>참여취소</span>
                  </v-btn>
                </v-col>
              </v-row>
            </v-col>
          </v-card>
    </v-dialog>
    
  </div>
</template>

<script>
import moment from "moment"
import { mapGetters} from 'vuex';
import ProfileName from '@/components/basic/ProfileName.vue';

export default {
  data(){
    return{
      modals : true,
    }
  },
  components: { 
    ProfileName,
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
    rows() {
        return this.potattendusers.length
    },
    detailable(){
      return this.modals && this.modalDetail
    },
    attendme(){
        let flag = true;
        if(this.potattendusers.length!=0){
          this.potattendusers.forEach((item) => {
            if (item.userid == this.userinfo.userid) {
              flag = false
            }
          });
        }
        return flag
    },
    mypot(){
     
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

    openMessageModal(sign){
        this.$emit('openMessageModal', true, sign)
    },
    potModify(){
        // this.selectPot(this.potitem)
        this.$router.push({path:"/vetparty_create/1"});
    },

  },

}
</script>

<style>

</style>