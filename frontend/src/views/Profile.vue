<template>
  <div>
    <h1>프로필 페이지</h1>
      <Userinfo v-if="isLogin"/>
        <JoinUs v-else/>
  </div>
</template>

<script>
import Userinfo from '@/components/user/Userinfo.vue'
import JoinUs from '@/components/JoinUs.vue'
import {mapGetters} from "vuex"

export default {
    computed:{
    ...mapGetters(["user/userinfo","user/isLogin"]),
    //...mapState(["user/isLogin"]),
    isLogin: function() {
      return this.$store.state.user.isLogin
    }
  },
  components: {
    Userinfo, JoinUs,
  },
  // data() {
  //   //isLogin: false;
  //   // return{
  //   //   isLogin: true,
  //   // }
  // },
  created(){
    let token = localStorage.getItem("access-token");
    console.log("무어ㅑ뭐야" + token);

    this.$store.dispatch("user/getUserInfo");

  },

  
}
</script>

<style>

</style>