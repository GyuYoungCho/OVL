<template>
  <div class="welcome-bg">
    <v-container class="welcome-container">
      <h1 class="welcome-text text-center">"live vegan, life together"</h1>
      <img class="ovl-welcome" src="@/assets/image/OVLwelcome.png" alt="">
      <div class="welcome-button-box">
        <v-btn
          class="welcomeBtn1"
          @click="onJoinBtnClick"
        >
          Join
        </v-btn>
        <v-btn
          class="welcomeBtn2"
          @click="onLoginBtnClick"
        >
          Login
        </v-btn>
      </div>
    </v-container>
  </div>
</template>

<script>
// import Carousel from './Carousel.vue'
export default {
  components: {
    // Carousel,
  },
  data: () => ({
  }),
  methods: {
    onJoinBtnClick () {
      this.$router.push({ name: "Tutorial" })
    },
    onLoginBtnClick () {
      this.$router.push({ name: "Login" })
    }
  }
}
</script>

<style>

</style>