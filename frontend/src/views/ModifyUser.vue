<template>
  <div>
        <v-container>
          <!-- 정보 수정을 위한 페이지로 이동 -->
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
    this.$store.dispatch("user/getUserInfo");
  },

  
}
</script>

<style>

</style>