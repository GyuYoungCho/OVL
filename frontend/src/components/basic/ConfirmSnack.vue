<template>
  <div>
    <v-snackbar v-model="avail" color="#004627" style="text-align:center"
        :timeout="timeout"
        rounded="pill"
        centered>
        {{ text }}
      </v-snackbar>
  </div>
</template>

<script>

export default {

  props:{
    snackbar : Boolean,
    text : String,
  },
  watch:{
    snackbar(){
      this.avail = this.snackbar
    }
  },
  data(){
    return{
      timeout : 2000,
      avail : false,
    }
  },
  
}
</script>

<style scoped>

</style>