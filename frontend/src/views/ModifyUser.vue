<template>
  <div>
        <v-container>
    <h1>회원 정보 수정</h1>
      <UserModify v-if="isLogin"/>
        <JoinUs v-else/>
            </v-container>
  </div>
</template>

<script>
import UserModify from '@/components/user/UserModify.vue'
import JoinUs from '@/components/JoinUs.vue'
import {mapGetters, mapState} from "vuex"

export default {
    computed:{
    ...mapGetters('user',["userinfo","isLogin"]),
    ...mapState('user', ["isLogin"]),
 
  },
  components: {
    UserModify, JoinUs,
  },
  created(){
    let token = localStorage.getItem("access-token");
    console.log("무어ㅑ뭐야" + token);

    this.$store.dispatch("user/getUserInfo");


  },

  
}
</script>

<style>

</style>