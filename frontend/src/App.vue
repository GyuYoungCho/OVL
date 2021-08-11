<template>
  <v-app>
    <Navbar v-if="isLogin" />
    <RouterView/>
  </v-app>
</template>

<script>
import "./assets/css/index.scss"
import Navbar from "@/components/Navbar"
import { mapGetters } from 'vuex'
import moment from 'moment'
moment.locale('ko')

export default {
  name: 'App',
  components: {
    Navbar
  },
  data: () => ({
    //
  }),
  computed: {
    ...mapGetters('user', ['isLogin',])
  }
};
</script>

<style>
</style>
