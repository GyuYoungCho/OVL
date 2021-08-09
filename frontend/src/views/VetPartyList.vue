<template>
  <div>
    <v-container>
      <section  class="vetparty">
      <pot-search :search="search" class="mt-3"></pot-search>
      <map-view class="mt-6"></map-view>
     
     <v-list class="user-potlist mt-5 px-0" v-if="userpots" color="#EBF4ED">
        <user-pot-list v-for="(userpot, index) in userpots" :key="index" :userpot="userpot">
          
        </user-pot-list>
      </v-list>

      <v-list v-if="searchpots.length==0">
        <all-pot-list v-for="(potitem, index) in potitems" :key="index" :potitem="potitem">
        </all-pot-list>
      </v-list>
      
      <v-list v-else>
        <all-pot-list v-for="(potitem, index) in searchpots" :key="index" :potitem="potitem">
            
        </all-pot-list>
      </v-list>
      <confirm-snack :snackbar="snackbar" :text="message"></confirm-snack>
      </section>
      <v-overlay :value="overlay"></v-overlay>
      
    </v-container>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import AllPotList from '@/components/pot/AllPotList.vue'
import UserPotList from '@/components/pot/UserPotList.vue'
import MapView from '@/components/basic/MapView.vue';
import PotSearch from '@/components/pot/PotSearch.vue';
import ConfirmSnack from '@/components/basic/ConfirmSnack.vue';
// import AttendModal from '@/components/pot/AttendModal.vue'

export default {
  components: { 
    AllPotList, 
    UserPotList, 
    MapView,
    PotSearch,
    ConfirmSnack,
    // AttendModal,
  },
  data(){
 
    return{
      search : '',
      allSteps: [
        "과일채소", "계란","유제품","생선","고기"
      ],
      snackbar : false,
      message : "참여되었습니다" ,
      searchpots : [],
      overlay : false,
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
      this.searchpots = []
  },
  method:{
    ...mapActions("pot", ['setPotItems',"setUsersPots"]),
    searchPots(keyword){
      keyword
    },
    overlayChange(val){
      this.overlay = val
    },

    openSnackBar(){
        this.snackbar = true

        setTimeout(() => {
          this.AttendModalT = false
          this.overlay = false
          this.snackbar = false
          this.redirect()
        }, 1000)
        this.$router.go();
    },
  },

}
</script>

<style>

</style>