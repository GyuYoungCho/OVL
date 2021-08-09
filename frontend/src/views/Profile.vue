<template>
  <div>
        <v-container>
          <!-- Mypage Profile 페이지로 이동 -->
          
        <ProfilePage v-if="this.isUser"/>
        <JoinUs v-else/>
        </v-container>
  </div>
</template>

<script>

import ProfilePage from '@/components/user/Profilepage.vue'
import JoinUs from '@/components/JoinUs.vue'
import {mapGetters, mapState} from "vuex"
import axios from "axios";
import API from "@/api/index.js";
import userAPI from "@/api/user.js";

export default {
  data() {
    return {
      isUser: false,
    }
  },

  computed:{
    ...mapGetters('user',["userinfo","isLogin"]),
    ...mapState('user', ["isLogin"]),

  },
  components: {
    ProfilePage, JoinUs,
  },
  created(){
    //userid 검증
    console.log(this.$route.params.userid)
    axios.get(API.url+userAPI.select(this.$route.params.userid))
      .then((res)=>{
          console.log("야야야ㅑㅑ 성공했다야")
          if(res.data != null){
              this.isUser = true;
          }else{
            this.isUser = false;
          }

      }).catch((err)=>{
        console.log(err);
        this.isUser = false;
      })
    if(this.$route.params.userid != this.userinfo.userid){
        console.log("param test!!!!!!!!")
      // axios.GET(API.url+userAPI.select(this.$route.params.userid))
      // .then((res)=>{
      //     console.log("야야야ㅑㅑ 성공했다야")
      //     if(res.data != null){
      //         this.isUser = true;
      //     }else{
      //       this.isUser = false;
      //     }

      // }).catch((err)=>{
      //   console.log(err);
      //   this.isUser = false;
      // })
      //this.$router.push({ name: 'Main' })
    }

    this.$store.dispatch("user/getTokenUserInfo");

  },
  mounted(){
    console.log(new Date(this.userinfo.challengeId.start_date))
  }

  
}
</script>

<style>

</style>