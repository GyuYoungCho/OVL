<template>
  <div>
        <v-container>
          <!-- Mypage Profile 페이지로 이동 -->
        <ProfilePage v-if="isLogin"/>
        <JoinUs v-else/>
        </v-container>
  </div>
</template>

<script>

import ProfilePage from '@/components/user/Profilepage.vue'
import JoinUs from '@/components/JoinUs.vue'
import {mapGetters, mapState} from "vuex"

export default {
    computed:{
    ...mapGetters('user',["userinfo","isLogin"]),
    ...mapState('user', ["isLogin"]),

  },
  components: {
    ProfilePage, JoinUs,
  },
  created(){
    this.$store.dispatch("user/getTokenUserInfo");
  },

  
}
</script>

<style>

</style>