<template>
  <div>
      <v-card
      rounded="pill" class="search_bar">
        <v-row>
        <v-text-field class="pt-0"
          v-model="search" clearable height="20px" rounded
          hide-details
        ></v-text-field>
        <v-btn icon><v-icon color="white" dark>mdi-magnify</v-icon></v-btn>
        </v-row>
    </v-card>
  </div>
</template>

<script>
export default {
    data(){
        return {
            search : '',
        }
    },

    methods:{
        sendkeyword(){
            this.$emit('searchPots', this.search)
        }
    }
}
</script>

<style>

</style>