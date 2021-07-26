<template>
  <div class="bg-vegan">
    <v-container>
      <h1 class="text-center">Welcome Page</h1>
      <h2 class="text-center vertical-center">슬로건과 기업 소개 써 넣을 자리</h2>
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
export default {
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
    background-image: url('../assets/image/background1.jpg');
    height: 700px;
  }
  .vertical-center {
    margin-top: 100px;
    margin-bottom: 100px;
  }
</style>