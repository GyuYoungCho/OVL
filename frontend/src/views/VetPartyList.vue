<template>
  <div>
    <v-container>
      <section  class="vetparty">
        <!-- 검색바 -->
        <pot-search class="mt-3 mb-3" @searchKeyword="searchKeyword" @selectOrd="selectOrd"></pot-search>
        <!-- 지도 -->
        <map-view class="mt-2" :step="step"></map-view>
         <!-- 내가 만들었거나 참여한 곳 -->
        <div class="mt-5 mb-2 vet-background" v-if="userpots && userpots.length!=0">
          <user-pot-list v-for="(userpot, index) in userpots" :key="index" :userpot="userpot"
          @openDetailModal="openDetailModal"></user-pot-list>
        </div>
        <!-- 다른사람이 만든거 뜨는곳 -->
        <div v-if="searchpots.length==0 && (search!=null && search.length>0)" class="noResult" style="margin-top:25px">
          <img src="@/assets/image/noResult.png" alt="">
          <div class="mt-3">검색 결과가 존재하지 않습니다</div>
        </div>
        <div v-else-if="searchpots.length==0" class="noResult" style="margin-top:25px">
          <img src="@/assets/image/noResult.png" alt="">
          <div class="mt-3">채식팟이 존재하지 않습니다 <br> 팟을 만들어보세요!</div>
        </div>
        <div>
          <all-pot-list v-for="(potitem, index) in searchpots" :key="index" :potitem="potitem"
          @openDetailModal="openDetailModal" @openAttendModal="openAttendModal"></all-pot-list>
          <v-divider class="mt-2"></v-divider>
        </div>
        <!-- 확인메시지용 조그만거 -->
        <confirm-snack :snackbar="snackbar" :text="message"></confirm-snack>
        <!-- 모듈화된 모달 -->
        <vet-party-detail :modalDetail="modalDetail"
            @openMessageModal="openMessageModal" @openDetailModal="openDetailModal"></vet-party-detail>
        <!-- axios 들어있는데 지워야할 모달 -->
        <message-modal :modalMessage="modalMessage" :sign="sign"
                @openMessageModal="openMessageModal" @openSnackBar="openSnackBar"></message-modal>
        <!-- 안내용 심플 팟모달 -->
        <PotSimpleModal :potwarning="potwarning" @warning-cancel="warningCancel"/>
      </section>
      <!-- 뒤에 눌리는거 방지 -->
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
import PotSimpleModal from '@/components/pot/PotSimpleModal.vue';

export default {
  components: { 
    AllPotList, 
    UserPotList, 
    MapView,
    PotSearch,
    ConfirmSnack,
    VetPartyDetail,
    MessageModal,
    PotSimpleModal
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
      btnActive: {0:false,1:false,2:false,3:false,4:false},
      step : '',
      
      overlay : false,
      modalDetail : false,
      modalMessage : false,
      snackbar : false,
      message : "안녕 난 디폴트야" ,
      sign : '',
      potwarning: false,
    }
  },
  computed:{
    ...mapGetters("pot", ['potitems','passpotitems','userpots','selectpot']),
    ...mapGetters("user", ['userinfo']),

    searchpots() {
      const search = this.search
      if (search!=null) search.toLowerCase()
      const allitems = [
        ...this.potitems,
        ...this.passpotitems
      ];
      
      if (!search) return allitems
      if(this.ord==this.order[1]){
        let subitems = allitems.filter(item => {
          const text = item.restaurant_name.toLowerCase()

          return text.indexOf(search) > -1
        })

        return subitems.sort(function(a, b) {
          let x = a.restaurant_name.indexOf(search);
          let y = b.restaurant_name.indexOf(search);
          if (x < y)  return -1;
          if (x > y) return 1;
          return 0;
        });


      }else{
        let subitems = allitems.filter(item => {
          const text = item.userid.nickname.toLowerCase()

          return text.indexOf(search) > -1
        })

        return subitems.sort(function(a, b) {
          let x = a.userid.nickname.indexOf(search);
          let y = b.userid.nickname.indexOf(search);
          if (x < y)  return -1;
          if (x > y) return 1;
          return 0;
        });
      }
    },
  },
  created() {
      // 신고 3번 먹은 유저라면 채식팟 탭 못들어 가야함
      if (this.userinfo.warning >= 3) {
        this.potwarning = true
      }
      
      this.modalDetail = false
      this.modalMessage = false
      this.snackbar = false
      this.overlay = false

      this.updateStore()
  },
  methods:{
    ...mapActions("pot", ['setPotItems',"setUsersPots"]),

    warningCancel() {
      this.potwarning = false
    },
    
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
    // 받은  sign에 따라 다른 snackbar
    openSnackBar(val, sign){
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
      // 현재 페이지 redirect
      this.$router.go();
    },

    selectTypeIcon(num){
      for (let i = 0; i < num; i++) {
        this.btnActive[i] = false
      }
      console.log("j")
      this.step = this.allSteps[4-num]
      
      for (let i = num; i < 5; i++) {
        this.btnActive[i] = true
      }
    },


    async updateStore(){
      await this.$store.dispatch("pot/setUsersPots", this.userinfo.userid)
      await this.$store.dispatch("pot/setPotItems")
      await this.$store.dispatch("pot/selectPot",[])
    }

  },
  
}
</script>

<style>

</style>