<template>
  <div>
    <!-- <h1>MAIN PAGE</h1> -->
    <Newsfeed v-if="isLoggedIn" />
    <Welcome v-else />
  </div>
</template>

<script>
import Newsfeed from '@/components/Newsfeed.vue'
import Welcome from '@/components/Welcome.vue'

export default {
  components: {
    Newsfeed, Welcome,
  },
  data: () => ({
    isLoggedIn: false,
  })
}
</script>

<style>

</style>