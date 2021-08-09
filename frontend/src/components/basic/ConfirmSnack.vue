<template>
  <div>
    <v-snackbar v-model="snackbar"
        :timeout="timeout"
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
  data(){
    return{
      timeout : 1000,
    }
  },
  
}
</script>