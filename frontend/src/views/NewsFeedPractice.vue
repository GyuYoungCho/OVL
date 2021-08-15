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
  }
}
</script>

<style>

</style>