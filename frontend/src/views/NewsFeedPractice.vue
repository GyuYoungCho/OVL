<template>
  <div>
      <v-container class="px-6 mt-4">
        <div class="filtering d-flex justify-center">
        <v-btn-toggle mandatory dense borderless rounded v-model="toggle">
            <v-btn small color="primary" :ripple="false" text>ALL</v-btn>
            <v-btn small color="primary" :ripple="false" text>ME</v-btn>
            <v-btn small color="primary" :ripple="false" text>FOLLOW</v-btn>
        </v-btn-toggle>
        </div>

        <div class="mt-4" v-for="(data, index) in feedDatas" :key="'mainCard' + index">
        
        </div>
        <infinite-loading @infinite="infiniteHandler" ref="infiniteLoading" spinner="circles">
        <div slot="no-more" class="mt-4">
            
        </div>
        <!-- <div slot="spinner"></div> -->
        <div slot="no-results" class="mt-4">
            
        </div>
        </infinite-loading>
      </v-container>
  </div>
</template>

<script>
import InfiniteLoading from 'vue-infinite-loading';
import { mapGetters } from 'vuex';
export default {
  components: {
    InfiniteLoading,
  },
  computed:{
    ...mapGetters("user", (["userinfo"]))
  },
  // infiniteHandler($state) {
  //     axios.get('/board/list/'+this.loadNum,{
  //         headers: {
  //             authorization: this.$store.getters.auth_token
  //         }
  //     })
  //     .then(res => {
  //           if(res.data.totalPages == this.loadNum){
  //               $state.complete();
  //           }else{
  //               setTimeout(() => {
  //                   const data = res.data.content;
  //                   for(let key in data){
  //                       this.movies.push(data[key])
  //                   }
  //                   this.loadNum++;
  //                   $state.loaded();
  //               }, 1000)
  //           }
  //     })
  //     .catch(err => {
  //         console.log(err)
  //         alert('에러');
  //         localStorage.clear();
  //         this.$store.state.loginState = false;
  //         this.$store.state.token = null;
  //         this.$router.push('/');
  //     })
  //   }
}
</script>

<style>

</style>