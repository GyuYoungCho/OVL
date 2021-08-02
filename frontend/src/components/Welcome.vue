<template>
  <div class="bg-vegan">
    <v-container>
      <Carousel />
      <h1 class="text-center">Welcome Page</h1>
      <h2 class="text-center vertical-center">슬로건과 기업 소개 써 넣을 자리</h2>
      <!-- <img src="C:\Users\multicampus\git\S05P12A606\backend\src\main\resources\static\post\15\13726747712500.png" alt=""> -->
      <div class="text-center">
        <v-btn
          class="mx-3"
          @click="onJoinBtnClick"
        >
          Join
        </v-btn>
        <v-btn
          class="mx-3"
          @click="onLoginBtnClick"
        >
          Login
        </v-btn>
      </div>
    </v-container>
  </div>
</template>

<script>
import Carousel from './Carousel.vue'
export default {
  components: {
    Carousel,
  },
  data: () => ({
  }),
  methods: {
    onJoinBtnClick () {
      this.$router.push({ name: "Signup" })
    },
    onLoginBtnClick () {
      this.$router.push({ name: "Login" })
    }
  }
}
</script>

<style scoped>
  .bg-vegan {
    background: url('../assets/image/background1.jpg') center; 
    background-size: cover;
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    overflow: auto;
  }
  .vertical-center {
    margin-top: 100px;
    margin-bottom: 100px;
  }

</style>