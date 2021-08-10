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
            <v-row>
              <v-col>
                  <v-icon>mdi-map-marker</v-icon>
                  <span>{{this.potitem.restaurant_name}}</span>
                  <v-icon>mdi-map-marker</v-icon>
                  <span>{{this.potitem.restaurant_name}}</span>
                  <v-icon>mdi-map-marker</v-icon>
                  <span>{{this.potitem.restaurant_name}}</span>
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
                <v-btn icon dark @click="cancelDetail" justify="end">
                  <v-icon>mdi-close</v-icon>
                </v-btn>
              </v-col>
              <v-col v-else>
                <v-btn dark @click="potcancel()" justify="end">
                  <span>참여 취소</span>
                </v-btn>
              </v-col>
            </v-row>
          </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from "axios";
import API from '@/api/index.js'
import potAPI from '@/api/pot.js'
import { mapGetters, mapActions} from 'vuex';
import ProfileName from '@/components/basic/ProfileName.vue';

export default {
  components: { 
    ProfileName 
  },
  props:{
    pot_detail_modal : Boolean,
    potitem : Object,
  },
  data(){
    return{
      mypot : false,
      attendpot : false,
    }
  },
  computed:{
    ...mapGetters("pot", ["potattendusers"]),
    ...mapGetters("user", ["userinfo"]),
  },
  created(){
    this.mypot = false
    this.attendpot = false
    if(this.userinfo.userid == this.potitem.userid) {
      this.mypot = true
    }
    if(this.potattendusers.includes(this.userinfo.userid)){
      this.attendpot = true
    }
  },
  methods:{
    ...mapActions("pot",["setPotAttendUsers"]),
    cancelDetail() {
      this.$emit('openDetailModal', false)
    },

    potattend() {
        axios.get(API.url + potAPI.attend(this.potitem.potid,this.userinfo.userid))
        .then((res) => {
            if (res.data === "success") {
                this.confirmModal()
            }
        })
        .catch((error) => {
            alert("참여 ㄴ");
            console.log(error);
        })

        this.$emit('openSnackBar', true)
    },

    potcancel() {
      axios.delete(API.url + potAPI.attend_cancel(this.potitem.potid, this.userinfo.userid))
        .then((res) => {
          if (res.data === "success") {
            
            this.$router.push({ name: "VetPartyList" })
          }
        })
        .catch((error) => {
          alert("삭제 ㄴ");
          console.log(error);
        })
    },


    potmod() {
      axios.put(API.url + potAPI.modify(1))
        .then((res) => {
          if (res.data === "success") {
            alert("수정 성공");
            this.$router.push({ name: "VetPartyList" })
          }
        })
        .catch((error) => {
          alert("수정 ㄴ");
          console.log(error);
        })
    },

    potdel() {
      axios.delete(API.url + potAPI.delete(1))
        .then((res) => {
          if (res.data === "success") {
            alert("삭제 성공");
            this.$router.push({ name: "VetPartyList" })
          }
        })
        .catch((error) => {
          alert("삭제 ㄴ");
          console.log(error);
        })
    },
  },

  mounted(){

  }
}
</script>

<style>

</style>