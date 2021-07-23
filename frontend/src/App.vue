<template>
  <v-app>
    <Navbar />
    <RouterView/>
  </v-app>
</template>

<script>
import "./assets/css/index.scss"
import Navbar from "@/components/Navbar"

export default {
  name: 'App',
  components: {
    Navbar
  },
  data: () => ({
    //
  }),
};
</script>
