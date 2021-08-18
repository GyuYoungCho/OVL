<template>
  <div>
    <v-card class="search_bar">
      <v-row class="ma-0">
        <v-col class="pa-0 ma-0" cols="3"> 
          <v-select class="pa-0 ma-0 ml-2" v-model="ord" 
            flat solo rounded justify="start" :items="order"
            background-color="rgba(255,255,255,0)" item-color="#004627"
            color="white" append-icon="mdi-menu-down-outline"
          ></v-select>
        </v-col>
        <v-col class="py-0 pr-0 ma-0 ">
          <v-text-field class="pt-0" 
            v-model="search" clearable height="20px" rounded
            hide-details
            placeholder="검색어를 입력하세요."
          ></v-text-field>
        </v-col>
      <v-btn icon><v-icon small color="white" dark>mdi-magnify</v-icon></v-btn>
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
        "최신순", "인기순", "댓글순"
      ],
      ord : '최신순',
    }
  },
  watch:{
    search : function(val){
      if(val==null) val=''
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