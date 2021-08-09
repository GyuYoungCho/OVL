<template>
  <div>
        <v-container>
      <h1>프로필 페이지</h1>
        <Userinfo v-if="isLogin"/>
        <JoinUs v-else/>
        </v-container>
  </div>
</template>

<script>
import Userinfo from '@/components/user/Userinfo.vue'
import JoinUs from '@/components/JoinUs.vue'
import {mapGetters, mapState} from "vuex"

export default {
    computed:{
    ...mapGetters('user',["userinfo","isLogin"]),
    ...mapState('user', ["isLogin"]),

  },
  components: {
    Userinfo, JoinUs,
  },
  created(){
    this.$store.dispatch("user/getTokenUserInfo");

  },
  mounted(){
    console.log(new Date(this.userinfo.challengeId.start_date))
  }

  
}
</script>

<style>

</style>