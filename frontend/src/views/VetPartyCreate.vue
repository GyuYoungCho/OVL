<template>
  <div>
    <v-container>
    <section  class="vetparty">
    <h4>채식팟 생성</h4>
    <!-- 채식팟 제목 -->
     
      <div>
        <button class="icon-btn" v-if="!btnActive[0]" @click="selectTypeIcon(0)" >
          <img src="@/assets/icon/notmeat.png" alt=""></button>
        <button class="icon-btn" v-else @click="selectTypeIcon(0)">
          <img src="@/assets/icon/meat.png" alt="" ></button>

        <button class="icon-btn" v-if="!btnActive[1]" @click="selectTypeIcon(1)">
          <img src="@/assets/icon/notfish.png" alt=""></button>
        <button class="icon-btn" v-else @click="selectTypeIcon(1)">
          <img src="@/assets/icon/fish.png" alt=""></button>

        <button class="icon-btn" v-if="!btnActive[2]" @click="selectTypeIcon(2)">
          <img src="@/assets/icon/notmilk.png" alt=""></button>
        <button class="icon-btn" v-else @click="selectTypeIcon(2)">
          <img src="@/assets/icon/milk.png" alt=""></button>
        
        <button class="icon-btn" v-if="!btnActive[3]" @click="selectTypeIcon(3)">
          <img src="@/assets/icon/notegg.png" alt="" ></button>
        <button class="icon-btn" v-else @click="selectTypeIcon(3)">
          <img src="@/assets/icon/egg.png" alt="" ></button>
          
        <button class="icon-btn" v-if="!btnActive[4]"  @click="selectTypeIcon(4)">
          <img src="@/assets/icon/notvege.png" alt=""></button>
        <button class="icon-btn" v-else @click="selectTypeIcon(4)">
          <img src="@/assets/icon/vege.png" alt="" ></button>
      </div>

      <div class="categoryBtn_box">
        <button @click="changedRest" :class="{'vetpartyBtnNotSelected':!isRestaurant, 'vetpartyBtnSelected':isRestaurant}">식당</button>
        <button @click="changedAdd" :class="{'vetpartyBtnNotSelected':!isAddress, 'vetpartyBtnSelected':isAddress}">도시락</button>
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
            <v-card-title>
              식당 검색
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
            ></v-text-field>
            </v-card-title>
            
            <restaurant-list :search="search"></restaurant-list>
          </v-card>
    </v-dialog>
    <!-- 주소 넣기 관련 부분 -->
    <div >
       <input type="text" v-model="roadAddress" readonly placeholder="주소"
       style="width:80%;">
       <button class=BtnComp @click="onAddressBtnClick"
       style="width:20%;">검색</button>
    </div>
    <input type="text" v-model="detailAddress" placeholder="상세주소">
    <div>
    
    <input type="date" id="detailAddress" placeholder="날짜" v-model="date" style="width:50%;">
    
    <input type="time" id="detailAddress" placeholder="시간" v-model="times" style="width:50%;">
    </div>
    <!-- 날짜 -->
    <v-dialog>
    <v-date-picker
      v-model="date"
      color="green darken-5"
      elevation="10"
      locale="ko-kr"
    >
    </v-date-picker>
    </v-dialog>
    <!-- 시간 -->
   

    <!-- 인원수 -->
    
    <input type="number" placeholder="인원" v-model="pot.total_people">
      
      <button :disabled="!isValid" @click="onCreateBtnClick" class=BtnComp>생성하기</button>
    
    </section>
    </v-container>
  </div>
</template>


<!-- 주소 찾기 관련 api -->
<!-- 여기에 안 넣으면 shell에서 오류 남(index.html에서도 적고 여기서도 적는 이유) -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
import _ from 'lodash';
import axios from "axios";
import API from '@/api/index.js'
import potAPI from '@/api/pot.js'
import RestaurantList from '@/components/pot/RestaurantList.vue';
import { mapGetters } from 'vuex';
// import RestaurantList from '../components/pot/RestaurantList.vue';


export default {
  components : {
    RestaurantList,
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
      }
  },
  methods: {

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
              this.extraAddress = extraRoadAddr

              this.detailAddress = ''
          }
        }).open()
      }else{
        this.rest_list_modal = true
      }
      
    },

    onCreateBtnClick () {
      
      this.pot.place = this.roadAddress
      this.pot.restaurant_name = this.detailAddress
      
      let date = new Date(this.date + ' 00:00')
      date.setHours(this.times.split(":")[0])
      
      date.setMinutes(this.times.split(":")[1])
      
      this.pot.time = date
      
      axios.post(API.url + potAPI.regist(1), this.pot)
        .then((response) => {
          alert("보냈슴!");
          console.log(response.data);
        })
        .catch((error) => {
          alert("못보냈슴!");
          console.log(error);
        })
      
      this.$router.push({ name: "VetPartyList" })
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
    }

  },
  computed: {
    ...mapGetters("pot", ["rest"]),
    address () {
      return this.roadAddress + this.extraAddress
    },
    isValid () {
      return !!this.pot.title && !!this.pot.content && !!this.roadAddress && !!this.detailAddress && !!this.pot.total_people && 
      !!this.date && !!this.times && !!this.pot.type && !!this.pot.step
    },
  },
  watch:{
      rest : function(newVal, oldVal){
          this.roadAddress = newVal.place
          this.detailAddress = newVal.restaurantName
          this.rest_list_modal = false
          this.isAddress = false
          // this.isRes = false
          oldVal
      },
  }
}
</script>

<style>

</style>