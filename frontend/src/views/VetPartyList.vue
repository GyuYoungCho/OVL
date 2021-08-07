<template>
  <div>
    <v-container>
      <section  class="vetparty">
      <h4>채식팟</h4>
      <map-view></map-view>
      <v-list >
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

export default {
  components: { 
    AllPotList, 
    UserPotList, 
    MapView
  },
  data(){
    return{
      
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
      this.$store.dispatch("pot/setPotItems")
      this.$store.dispatch("pot/setUsersPots",2)
      //  this.setUsersPots(userinfo.userid);
  },
  method:{
    ...mapActions("pot", ['setPotItems',"setUsersPots"]),

  }
}
</script>

<style>

</style>