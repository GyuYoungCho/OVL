<template>
  <div>
    <v-container>
      <section  class="vetparty">
      <pot-search :search="search" class="mt-3"></pot-search>
      <map-view class="mt-6"></map-view>
      <v-list class="user-potlist mt-5 px-0" v-if="userpots" color="#EBF4ED">
        <user-pot-list v-for="(userpot, index) in userpots" :key="index" :userpot="userpot" />
      </v-list>
      <v-list>
        <all-pot-list v-for="(potitem, index) in potitems" :key="index" :potitem="potitem" />
      </v-list>
      </section>
    </v-container>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import AllPotList from '@/components/pot/AllPotList.vue'
import UserPotList from '@/components/pot/UserPotList.vue'
import MapView from '@/components/basic/MapView.vue';
import PotSearch from '@/components/pot/PotSearch.vue';

export default {
  components: { 
    AllPotList, 
    UserPotList, 
    MapView,
    PotSearch,
  },
  data(){
    return{
      search : '',
      allSteps: [
        "과일채소", "계란","유제품","생선","고기"
      ],
      modalOpen : false,
    }
  },
  computed:{
    ...mapGetters("pot", ['potitems','userpots']),
    ...mapGetters("user", ['userinfo']),
  },
  created() {
      this.$store.dispatch("pot/setUsersPots", this.userinfo.userid)
      this.$store.dispatch("pot/setPotItems")
      //  this.setUsersPots(userinfo.userid);
  },
  method:{
    ...mapActions("pot", ['setPotItems',"setUsersPots"]),
  },
}
</script>

<style>

</style>