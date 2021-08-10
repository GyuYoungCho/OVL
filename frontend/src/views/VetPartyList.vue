<template>
  <div>
    <v-container>
      <section  class="vetparty">
      <pot-search :search="search" class="mt-3"></pot-search>
      <map-view class="mt-6"></map-view>
     
     <v-list class="user-potlist mt-5 px-0" v-if="userpots && userpots.length!=0" color="#EBF4ED">
        <user-pot-list v-for="(userpot, index) in userpots" :key="index" :userpot="userpot">
          
        </user-pot-list>
      </v-list>

      <v-list v-if="searchpots.length==0">
        <!-- <all-pot-list v-for="(potitem, index) in potitems" :key="index" :potitem="potitem"
          @openSnackBarTop="openSnackBarTop">
        </all-pot-list> -->
      </v-list>
      
      <v-list v-else>
        <all-pot-list v-for="(potitem, index) in searchpots" :key="index" :potitem="potitem"
        @openSnackBarTop="openSnackBarTop">
            
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
      searchpots : [],
      overlay : false,

      snackbar : false,
      message : "안녕 난 디폴트야" ,
    }
  },
  computed:{
    ...mapGetters("pot", ['potitems','userpots']),
    ...mapGetters("user", ['userinfo']),
  },
  created() {
      this.$nextTick(function(){
        this.$store.dispatch("pot/setUsersPots", this.userinfo.userid)
        this.$store.dispatch("pot/setPotItems")
        this.$store.dispatch("pot/selectPot",[])
      })
      console.log(this.userpots)
      this.snackbar = false,
      this.overlay = false,
      this.searchpots = this.potitems
  },
  methods:{
    ...mapActions("pot", ['setPotItems',"setUsersPots"]),
    searchPots(keyword){
      keyword
    },
    overlayChange(val){
      this.overlay = val
    },
    openSnackBarTop(val, sign){
      console.log("final")
      if(sign=="attend"){
        this.message = "참여되었습니다."
      }else if(sign=="cancel"){
        this.message = "참여가 취소되었습니다."
      }else if(sign=="delete"){
        this.message = "팟이 삭제되었습니다."
      }else if(sign=="not_attend"){
        this.message = "인원이 차서 참여할 수 없어요ㅠㅠ"
      }

      this.$nextTick(function(){
        this.$store.dispatch("pot/setUsersPots", this.userinfo.userid)
        this.$store.dispatch("pot/setPotItems")
        this.$store.dispatch("pot/selectPot",[])
      })

      this.searchpots = this.potitems
      this.snackbar = val
      this.overlay = val
      setTimeout(() => {
        
        this.overlay = false
        this.snackbar = false
      }, 1000)
      this.$router.go();
    }
  },
  
}
</script>

<style>

</style>