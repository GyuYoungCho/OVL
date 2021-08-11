<template>
  <div>
    <v-container>
      <section  class="vetparty">
      <pot-search class="mt-3" @searchKeyword="searchKeyword"
        @selectOrd="selectOrd"></pot-search>
      <map-view class="mt-6"></map-view>
     
     <v-list class="user-potlist mt-5 px-0" v-if="userpots && userpots.length!=0" color="#EBF4ED">
        <user-pot-list v-for="(userpot, index) in userpots" :key="index" :userpot="userpot"
        @openDetailModal="openDetailModal">
          
        </user-pot-list>
      </v-list>

      
      
      <v-list>
        <all-pot-list v-for="(potitem, index) in searchpots" :key="index" :potitem="potitem"
        @openDetailModal="openDetailModal" @openAttendModal="openAttendModal">
            
        </all-pot-list>
      </v-list>
      <confirm-snack :snackbar="snackbar" :text="message"></confirm-snack>
      <vet-party-detail :modalDetail="modalDetail"
          @openMessageModal="openMessageModal" @openDetailModal="openDetailModal"></vet-party-detail>
      
      <message-modal :modalMessage="modalMessage" :sign="sign"
              @openMessageModal="openMessageModal" @openSnackBar="openSnackBar"></message-modal>
      </section>
      <v-overlay :value="overlay" ></v-overlay>
      
    </v-container>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import AllPotList from '@/components/pot/AllPotList.vue'
import UserPotList from '@/components/pot/UserPotList.vue'
import MapView from '@/components/basic/MapView.vue';
import PotSearch from '@/components/basic/PotSearch.vue';
import ConfirmSnack from '@/components/basic/ConfirmSnack.vue';
import MessageModal from '@/components/pot/MessageModal.vue'
import VetPartyDetail from '@/components/pot/VetPartyDetail.vue';


export default {
  components: { 
    AllPotList, 
    UserPotList, 
    MapView,
    PotSearch,
    ConfirmSnack,
    VetPartyDetail,
    MessageModal,
  },
  data(){
 
    return{
      search : '',
      ord : '',
      allSteps: [
        "과일채소", "계란","유제품","생선","고기"
      ],
      order : [
        "닉네임", "식당",
      ],
      
      overlay : false,
      modalDetail : false,
      modalMessage : false,
      snackbar : false,
      message : "안녕 난 디폴트야" ,
      sign : '',
    }
  },
  computed:{
    ...mapGetters("pot", ['potitems','passpotitems','userpots','selectpot']),
    ...mapGetters("user", ['userinfo']),

    searchpots() {
      const search = this.search.toLowerCase()
      const allitems = [
        ...this.potitems,
        ...this.passpotitems
      ];
      
      if (!search) return allitems
      if(this.ord==this.order[1]){
        return allitems.filter(item => {
          const text = item.restaurant_name.toLowerCase()

          return text.indexOf(search) > -1
        })
      }else{
        return allitems.filter(item => {
          const text = item.userid.nickname.toLowerCase()

          return text.indexOf(search) > -1
        })
      }
    },
  },
  created() {
      
      this.modalDetail = false
      this.modalMessage = false
      this.snackbar = false
      this.overlay = false
  },
  methods:{
    ...mapActions("pot", ['setPotItems',"setUsersPots"]),
    
    searchKeyword(val){
      this.search = val
    },

    selectOrd(val){
      this.ord = val
    },

    openDetailModal(val){
        this.modalDetail = val
    },
    openAttendModal(val){
        this.sign = "attend"
        this.modalMessage = val
    },

    openMessageModal(val,sign){
        this.sign = sign
        this.modalMessage = val
    },

    openSnackBar(val, sign){
      console.log("final")
      if(sign=="attend"){
        this.message = "참여되었습니다."
      }else if(sign=="cancel"){
        this.message = "참여가 취소되었습니다."
      }else if(sign=="delete"){
        this.message = "팟이 삭제되었습니다."
      }else if(sign=="not_attend"){
        this.message = "인원이 차서 참여할 수 없어요ㅠㅠ"
      }else if(sign=="dis_attend"){
        this.message = "참여할 수 있는 팟 수를 넘었어요!"
      }

      
      this.snackbar = val
      this.overlay = val
      setTimeout(() => {
        
        this.overlay = false
        this.snackbar = false
      }, 2000)

      this.$router.go();
    }
  },
  mounted: function(){
    this.$nextTick(function(){
      this.$store.dispatch("pot/setUsersPots", this.userinfo.userid)
      this.$store.dispatch("pot/setPotItems")
      this.$store.dispatch("pot/selectPot",[])
    })
  }
}
</script>

<style>

</style>