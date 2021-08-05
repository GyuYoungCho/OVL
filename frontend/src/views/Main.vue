<template>
  <div>
    <!-- <h1>MAIN PAGE</h1> -->
    <Newsfeed v-if="isLogin" />
    <Welcome v-else />
  </div>
</template>

<script>
import Newsfeed from '@/components/Newsfeed.vue'
import Welcome from '@/components/Welcome.vue'
import {mapState} from "vuex";

export default {
  components: {
    Newsfeed, Welcome,
  },
  data: () => ({
    isLoggedIn: false,
  }),
  computed: {
    ...mapState("user", (["isLogin"]))
  }
}
</script>

<style>

</style>