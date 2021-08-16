<template>
  <div>
    <v-card rounded="pill" class="search_bar">
      <v-row height="20">
        <v-col class="pa-0" cols="4">
          <v-select class="pa-0 ma-0 ml-2" v-model="ord"
            flat solo rounded justify="start" :items="order"
            background-color="rgba(255,255,255,0)" item-color="#004627"
            color="white" append-icon="mdi-menu-down-outline"></v-select>
        </v-col>
        <v-col class="pa-0 ma-0">
          <v-text-field class="pt-0"
            v-model="search" clearable height="20px" rounded
            hide-details
          ></v-text-field>
        </v-col>
      <v-btn icon><v-icon color="white" small dark>mdi-magnify</v-icon></v-btn>
      </v-row>
    </v-card>
  </div>
</template>

<script>
export default {
    data(){
      return{
        search : '',
        order : [
        "닉네임", "식당",
        ],
        ord : '닉네임',
      }
    },
    
    watch:{
      search : function(val){
          this.$emit('searchKeyword', val)
      },
      ord : function(val){
          this.search = ''
          this.$emit('selectOrd', val)
      },
    }
}
</script>

<style>

</style>