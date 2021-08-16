<template>
  <div>
          <!-- Mypage Profile 페이지로 이동 -->
        <ProfilePage v-if="this.isUser"/>
        <Welcome v-else/>
  </div>
</template>

<script>

import ProfilePage from '@/components/user/Profilepage.vue'
//import JoinUs from '@/components/JoinUs.vue'
import {mapGetters, mapState, mapActions} from "vuex"
import API from "@/api/index.js";
import userAPI from "@/api/user.js";
import axios from 'axios'
import Welcome from '@/components/Welcome.vue'

export default {
  data() {
    return {
      isUser: false,
    }
  },

  computed:{
    ...mapGetters('user',["userinfo"]),
    ...mapActions("challenge", ["fetchChallengeList", "challengeAttend"]),
    ...mapState('user', ["isLogin"]),

  },
  components: {
    ProfilePage, Welcome,
  },
  created(){
    //userid 검증
    //console.log(this.$route.params.userid)
    if(this.$route.params.userid === this.userinfo.userid){
      //로그인 user == 프로필 확인 user 같음
      this.isUser = true;

    }
    if(this.$route.params.userid != this.userinfo.userid){
      axios.get(API.url+userAPI.select(this.$route.params.userid))
    .then((res)=>{
      //console.log(res)
        if(res.data == ""){
            //console.log("다른사람이고 회원가입 안했음")
            this.isUser = false;
        }else{
            //console.log("가입은 했네")
            this.$router.push({name: 'OtherProfile', params: {userid: this.$route.params.userid}})
        }
    }).catch((err)=>{
        console.log(err)
        })
    }
    this.$store.dispatch("user/getUserInfo");

  },
  // mounted(){
  //   console.log(new Date(this.userinfo.challengeId.start_date))
  // }

  
}
</script>

<style>

</style>