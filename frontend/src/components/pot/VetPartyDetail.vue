<template>
  <div>
    <v-dialog
          v-model="pot_detail_modal"
          
          transition="dialog-bottom-transition"
          max-width="1000px">
        <v-card tile>
          <v-toolbar color="#004627" dark>
          <v-toolbar-title >{{this.potitem.title}}</v-toolbar-title>
            <profile-name :user="potitem.userid"></profile-name>
            <v-spacer></v-spacer>
            <v-btn icon dark @click="cancelDetail" justify="end">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-toolbar>
            <v-col xs="7">
              <v-row>
                <v-col>
                  <v-row>
                    <v-icon>mdi-map-marker</v-icon>
                    <span>{{this.potitem.restaurant_name}}</span>
                    <v-icon>mdi-map-marker</v-icon>
                    <span>{{this.potitem.restaurant_name}}</span>
                    <v-icon>mdi-map-marker</v-icon>
                    <span>{{this.potitem.restaurant_name}}</span>
                  </v-row>
                </v-col>
                <v-col class ="md-3" justify="end">
                    <v-icon>mdi-account-outline</v-icon>
                </v-col>
                <v-col>
                  <profile-name v-for="(auser, index) in potattendusers" :key="index" :user="auser"></profile-name>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <span>{{this.potitem.content}}</span>
                </v-col>
                <v-col v-if="mypot">
                  <v-btn dark @click="potModify()" justify="end">
                    <span>수정</span>
                  </v-btn>
                  <v-btn dark @click="openDeleteModal(true)" justify="end">
                    <span>삭제</span>
                  </v-btn>
                </v-col>
                <v-col v-else>
                  <v-btn v-if="attendme(potattendusers)" dark @click="openAttendModal(true)" justify="end">
                    <span>참여하기</span>
                  </v-btn>
                  <v-btn v-else dark @click="openCancelModal(true)" justify="end">
                    <span>참여취소</span>
                  </v-btn>
                </v-col>
              </v-row>
            </v-col>
          </v-card>
    </v-dialog>
    <attend-modal :potitem="potitem" :modalopen="AttendModalT" :rows="potattendusers.length"
              @openAttendModal="openAttendModal" @openSnackBar="openSnackBar"></attend-modal>
    <cancel-modal :potitem="potitem" :modalopen="CancelModalT"
              @openCancelModal="openCancelModal" @openSnackBar="openSnackBar"></cancel-modal>
    <delete-modal :potitem="potitem" :modalopen="DeleteModalT"
              @openDeleteModal="openDeleteModal" @openSnackBar="openSnackBar"></delete-modal>
  </div>
</template>

<script>
import axios from "axios";
import API from '@/api/index.js'
import potAPI from '@/api/pot.js'
import { mapGetters, mapActions } from 'vuex';
import ProfileName from '@/components/basic/ProfileName.vue';
import AttendModal from '@/components/pot/AttendModal.vue'
import CancelModal from '@/components/pot/CancelModal.vue'
import DeleteModal from '@/components/pot/DeleteModal.vue'

export default {
  components: { 
    ProfileName,
    AttendModal,
    CancelModal,
    DeleteModal,
  },
  props:{
    pot_detail_modal : Boolean,
    potitem : Object,
  },
  data(){
    return{
      mypot : false,
      attendpot : false,
      CancelModalT : false,
      AttendModalT : false,
      DeleteModalT : false,
      potattendusers : [],
    }
  },
  computed:{
    ...mapGetters("user", ["userinfo"]),
    ...mapGetters("pot", ["selectpot"]),
    rows() {
        return this.potattendusers.length
    },
  },
  created(){
    this.mypot = false
    this.attendpot = false
    this.selectPot(this.potitem)
    this.potAttendUsers(this.potitem.potid)
    if(this.userinfo.userid == this.potitem.userid.userid) {
      this.mypot = true
    }else{
      
      this.potattendusers.forEach((item) => {
        console.log(item.userid.userid)
          if (item.userid == this.userinfo.userid) {
            this.attendpot = true
          }
    });
    }
  },
  methods:{
    ...mapActions("pot", ["selectPot"]),

    cancelDetail() {
      this.$emit('openDetailModal', false)
    },

    openAttendModal(val){
        this.AttendModalT = val;
    },
    openCancelModal(val){
        this.CancelModalT = val;
    },
    openDeleteModal(val){
        this.DeleteModalT = val;
    },
    potModify(){
        this.selectPot(this.potitem)
        this.$router.push({path:"/vetparty_create/1"});
    },

    potAttendUsers(pot_id) {
        axios.get(API.url + potAPI.attendcount(pot_id))
            .then((res) => {
            this.potattendusers = res.data
        })
        .catch((error) => {
            console.log(error);
        });
    },
    attendme(users) {
        let flag = true;
        if(users && users.length!=0){
          users.forEach((item) => {
            console.log(item.userid == this.userinfo.userid)
            // console.log(this.userinfo.userid)
            if (item.userid == this.userinfo.userid) {
              flag = false
            }
          });
        }
        return flag
    },

    openSnackBar(val, sign){
      console.log(sign)
        this.$emit('openSnackBar', val, sign)
    }
  },

}
</script>

<style>

</style>