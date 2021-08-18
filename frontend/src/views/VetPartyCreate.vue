<template>
  <div>
    <v-container>
    <section  class="vetparty">
      <content-confirm :modalOpen="modalOpen" :modalContent="modalContent"
        @changeNext="changeNext" />

    <div class="vetpartyHeaderArea">

      <h4 v-if="type==0">채식팟 생성</h4>
      <h4 v-else>채식팟 수정</h4>
        
        <div>
          <button class="icon-btn" v-if="!btnActive[0]" @click="selectTypeIcon(0)" >
            <img class="vetpartyVeganImg" src="@/assets/icon/notmeat.png" alt=""></button>
          <button class="icon-btn" v-else @click="selectTypeIcon(0)">
            <img class="vetpartyVeganImg" src="@/assets/icon/meat.png" alt="" ></button>

          <button class="icon-btn" v-if="!btnActive[1]" @click="selectTypeIcon(1)">
            <img class="vetpartyVeganImg" src="@/assets/icon/notfish.png" alt=""></button>
          <button class="icon-btn" v-else @click="selectTypeIcon(1)">
            <img class="vetpartyVeganImg" src="@/assets/icon/fish.png" alt=""></button>

          <button class="icon-btn" v-if="!btnActive[2]" @click="selectTypeIcon(2)">
            <img class="vetpartyVeganImg" src="@/assets/icon/notmilk.png" alt=""></button>
          <button class="icon-btn" v-else @click="selectTypeIcon(2)">
            <img class="vetpartyVeganImg" src="@/assets/icon/milk.png" alt=""></button>
          
          <button class="icon-btn" v-if="!btnActive[3]" @click="selectTypeIcon(3)">
            <img class="vetpartyVeganImg" src="@/assets/icon/notegg.png" alt="" ></button>
          <button class="icon-btn" v-else @click="selectTypeIcon(3)">
            <img class="vetpartyVeganImg" src="@/assets/icon/egg.png" alt="" ></button>
            
          <button class="icon-btn" v-if="!btnActive[4]"  @click="selectTypeIcon(4)">
            <img class="vetpartyVeganImg" src="@/assets/icon/notvege.png" alt=""></button>
          <button class="icon-btn" v-else @click="selectTypeIcon(4)">
            <img class="vetpartyVeganImg" src="@/assets/icon/vege.png" alt="" ></button>
        </div>
    </div>

      <div class="categoryBtn_box">
        <div></div>
        <div></div>
        <button @click="changedRest" :class="{'vetpartyBtnNotSelected':!isRestaurant, 'vetpartyBtnSelected':isRestaurant}">식당</button>
        <button @click="changedAdd" :class="{'vetpartyBtnNotSelected':!isAddress, 'vetpartyBtnSelected':isAddress}">도시락</button>
        <div></div>
        <div></div>
      </div>


      <input type="text" v-model="pot.title" placeholder="제목">
    
      <textarea v-model="pot.content" placeholder="내용"></textarea>
        

    <!--식당 선택 관련-->
    <v-dialog
          v-model="rest_list_modal"
          hide-overlay
          transition="dialog-bottom-transition"
          max-width="500px"
          scrollable
        >
      <v-card>
        <v-toolbar color="#49784B" dark dense>
          <v-toolbar-title>식당 검색</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon dark @click="cancelAddres">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
          <v-card-title>
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
            ></v-text-field>
          </v-card-title>
        <!-- 레스토랑 리스트 -->
        <restaurant-list :search="search"></restaurant-list>
      </v-card>
    </v-dialog>
    <!-- 주소 넣기 관련 부분 -->
    <div style="display:flex">
       <input type="text" v-model="roadAddress" readonly placeholder="주소" style="width:76%;">
       <v-spacer></v-spacer>
       <button class=BtnComp @click="onAddressBtnClick" style="width:20%;">검색</button>
    </div>
    <input type="text" v-model="detailAddress" placeholder="상세주소">
    <div style="display:flex">
      <input type="date" id="detailAddress" placeholder="날짜" v-model="date" style="width:48%;">
      <v-spacer></v-spacer>
      <input type="time" id="detailAddress" placeholder="시간" v-model="times" style="width:48%;">
    </div>
   

    <!-- 인원수 -->
    <input type="number" placeholder="인원" v-model="pot.total_people"  min="1" max="10">
      
      <button v-if="type==0" :disabled="!isValid" @click="onCreateBtnClick" class=BtnComp>채식팟 등록</button>
      <button v-else :disabled="!isValid" @click="onCreateBtnClick" class=BtnComp>수정</button>
      <v-overlay :value="overlay"></v-overlay>
    </section>
    
    <confirm-snack :snackbar="snack" :text="message"></confirm-snack>
    </v-container>
  </div>
</template>

<!-- 주소 찾기 관련 api -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
import axios from "axios";
import API from '@/api/index.js'
import potAPI from '@/api/pot.js'
import RestaurantList from '@/components/pot/RestaurantList.vue';
import ConfirmSnack from '@/components/basic/ConfirmSnack.vue';
import { mapGetters, mapActions } from 'vuex';
import ContentConfirm from '@/components/basic/ContentConfirm.vue';

export default {
  components : {
    RestaurantList,
    ConfirmSnack,
    ContentConfirm,
  },
  data() {
      return {
        date:  (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
        pot : {
          type : "식당",
          step : "과일채소",
        },
        times : '',
        roadAddress: '',
        detailAddress: '',

        isAddress: false,
        isRestaurant: true,
        rest_list_modal : false,

        search: '',

        allSteps: [
          "과일채소", "계란","유제품","생선","고기"
        ],
        
        btnActive: {0:false,1:false,2:false,3:false,4:true},

        message : "",
        snack : false,
        overlay : false,

        type : 0,

        confirms : false,
        modalOpen:false,
        modalContent:'저장되지 않은 작업이 있습니다! 정말 나갈까요?'
      }
  },
  computed: {
    ...mapGetters("pot", ["rest", "selectpot","userpots"]),
    ...mapGetters("user", ['userinfo']),
    
    isValid () {
      return !!this.pot.title && !!this.pot.content && !!this.roadAddress && !!this.detailAddress && !!this.pot.total_people && 
      !!this.date && !!this.times && !!this.pot.type && !!this.pot.step
    },
  },
  created(){

    this.updateStore()

    this.search = ''
    this.snack = false
    this.overlay = false
    this.rest_list_modal = false

    if (this.$route.params.type !=0) {
      this.type = 1
      this.roadAddress = this.selectpot.place
      this.detailAddress = this.selectpot.restaurant_name
      this.pot.title = this.selectpot.title
      this.pot.content = this.selectpot.content
      this.pot.total_people = this.selectpot.total_people
      this.pot.potid = this.selectpot.potid
      this.pot.type = this.selectpot.type
      this.pot.time = this.selectpot.time
      this.date = new Date(this.selectpot.time).toISOString().substr(0, 10)
      this.times = new Date(this.selectpot.time).toISOString().slice(11, 16)
      if(this.pot.type="식당"){
        this.isRestaurant = true
        this.isAddress = false
      }else{
        this.isRestaurant = false
        this.isAddress = true
      }

      for(let i=0;i<5;i++){
        if(this.allSteps[i]==this.pot.type){
          selectTypeIcon(i)
          break
        }
      }

      this.message = "내 팟이 수정되었어요!"
    }else{
      this.message = "팟을 만드셨네요! 다른 팟도 찾아볼까요?"
    }

    
  },
  methods: {
    
    ...mapActions("pot", ["setPotItems","setUsersPots"]),
    // 이동 함수    
    goList(){
      this.$router.push({ name: "VetPartyList" })
    },

    // 주소 넣는 팝업창 생성
    onAddressBtnClick () {
      if(this.isAddress){
        new daum.Postcode({
          oncomplete: (data) => {
              
              var extraRoadAddr = ''; // 참고 항목 변수
              this.roadAddress = data.roadAddress

              // 법정동명이 있을 경우 추가한다. (법정리는 제외)
              // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
              if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                  extraRoadAddr += data.bname;
              }
              // 건물명이 있고, 공동주택일 경우 추가한다.
              if(data.buildingName !== '' && data.apartment === 'Y'){
                  extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
              }
              // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
              if(extraRoadAddr !== ''){
                  extraRoadAddr = ' (' + extraRoadAddr + ')';
              }
              this.detailAddress = ''
          }
        }).open()
      }else{
        this.rest_list_modal = true
      }
      
    },

    //데이터 받은거 보내기
    onCreateBtnClick () {
      
      this.pot.place = this.roadAddress
      this.pot.restaurant_name = this.detailAddress
      
      let date = new Date(this.date + ' 00:00')
      date.setHours(this.times.split(":")[0])
      
      date.setMinutes(this.times.split(":")[1])
      
      this.pot.time = date

      if(this.type ==0){
        axios.post(API.url + potAPI.regist(this.userinfo.userid), this.pot)
          .then((res) => {
            res
            this.snack = true
            this.overlay = true
          })
          .catch((error) => {
            console.log(error);
          })
        }else{
        
        axios.put(API.url + potAPI.modify(),this.pot)
          .then((res) => {
            if (res.data === "success") {
               this.snack = true
                this.overlay = true
            }
          })
          .catch((error) => {
            alert("수정 ㄴ");
            console.log(error);
          })
    
      }
      this.setUsersPots(this.userinfo.userid)
      this.setPotItems()

      setTimeout(() => {
        this.snack = false
        this.overlay = false
        this.goList()
      }, 2000)
    },

    changedRest(){
      this.isRestaurant = true;
      this.isAddress = false
      if(this.pot.type="도시락"){
        this.roadAddress = ''
        this.detailAddress = ''
      }
      this.pot.type="식당"
    },

    changedAdd(){
      this.isRestaurant = false;
      this.isAddress = true;
      if(this.pot.type="도시락"){
        this.roadAddress = ''
        this.detailAddress = ''
      }
      this.pot.type="도시락"
    },

    selectTypeIcon(num){
      for (var i = 0; i < num; i++) {
        this.btnActive[i] = false
      }
      this.pot.step = this.allSteps[4-num]
      
      for (var i = num; i < 5; i++) {
        this.btnActive[i] = true
      }
    },
    cancelAddres(){
      this.rest_list_modal = false
    },
    openModal(){
      this.modalOpen = true
      return this.confirms
    },
    changeNext(val){
      this.confirms = val
      return k
    },

    async updateStore(){
          
        await this.$store.dispatch("pot/setUsersPots", this.userinfo.userid)
        await this.$store.dispatch("pot/setPotItems")
        await this.$store.dispatch("pot/selectPot",[])
      
    }
  },
  
  watch:{
      rest : function(newVal, oldVal){
          this.roadAddress = newVal.place
          this.detailAddress = newVal.restaurantName
          this.rest_list_modal = false
          this.isAddress = false
          
          oldVal
      },
  },
  mounted(){
    if(this.userpots.length >=3 && this.$route.params.type ==0){
      this.message = "이미 3개 참여 중이라 생성할 수 없어요!"
      this.snack = true
      this.overlay = true
      
      setTimeout(() => {
        this.goList()
      }, 2000)
    }
  },
  // async beforeRouteLeave(to, from, next) {
  //   const a = await this.openModal()
  //   if (this.confirms) {
  //       next();
  //   } else {
  //       next(false);
  //   }
    
  // },
}
</script>

<style>

</style>