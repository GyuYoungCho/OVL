<template>
  <div class="vetparty">
    <h4>채식팟 생성</h4>
    <!-- 채식팟 제목 -->
    <div>
      <input type="text" v-model="pot.title" placeholder="제목">
    </div>
    <div>
      <textarea v-model="pot.content" placeholder="내용"></textarea>
    </div>

     <!-- 식당 / 도시락 --> 
    <div>
      <v-select name="" id="" v-model="pot.type"
        :items="eatType" placeholder="어떻게 먹을까용"
        @input="changedStep"
        dense solo>
        
      </v-select>
    </div>

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
            
            <restaurant-list></restaurant-list>
          </v-card>
    </v-dialog>
    <!-- 주소 넣기 관련 부분 -->
    <div >
      <p> 도로명 주소 : <input type="text" v-model="roadAddress" placeholder="제목"></p><button class=BtnComp @click="onAddressBtnClick">검색</button>
      <p> 참고 : {{ extraAddress }}</p>
      <p> 상세 주소 : <input type="text" id="detailAddress" placeholder="상세주소" v-model="detailAddress"></p>
    </div>

    <!-- 날짜 -->
    <v-date-picker
      v-model="date"
      color="green darken-5"
      elevation="10"
      locale="ko-kr"
    >


    </v-date-picker>

    <!-- 시간 -->
    <!-- 뷰티파이 time picker 안 이뻐보여서 일단 순수 html로 -->
    <div>
      <v-row>
        <v-col>
      
          <v-select name="" id="" v-model="partyAmPm" 
            :items="timePart" placeholder="-시간-"
            dense solo>
          </v-select>
      </v-col>
      <v-col>
        <v-select name="" id="" v-model="partyHour" 
          :items="times" placeholder="-시-"
          dense solo>
        </v-select>
      </v-col>
      <v-col> 시</v-col>
      <v-col>
        <v-select name="" id="" v-model="partyMinute" 
          :items="minutes" placeholder="-분-"
          dense solo>
        </v-select>
      </v-col>
      <v-col> 분</v-col>
      </v-row>
    </div>

    <!-- 인원수 -->
    <div>
      <input type="number" placeholder=0 v-model="pot.total_people">명
    </div>

    <!-- 비건종류 -->
    <div>
      <v-select name="" id="" v-model="pot.step"
        :items="allSteps" placeholder="어떤 거까지 되시나용"
        dense solo>
      </v-select>
    </div>
   

    <!-- 생성버튼 -->
    <div>
      <v-btn
        :disabled="!isValid"
        @click="onCreateBtnClick"
      >
        생성하기
      </v-btn>
    </div>
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
  data: () => ({
    date:  (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    times: _.range(1, 13),
    minutes: _.range(1, 60),
    pot : {},
    
    partyAmPm: '',
    partyHour: 0,
    partyMinute: 0,

    roadAddress: '',
    detailAddress: '',
    extraAddress: '',
    isAddress: false,
    isRestaurant: false,
    rest_list_modal : false,

    search: '',

    allSteps: [
      "과일채소", "유제품1","유제품all","생선","고기"
    ],
    eatType: [
      "식당", "도시락"
    ],
    timePart: [
      "오전", "오후"
    ],
  }),
  methods: {

    // 주소 넣는 팝업창 생성
    onAddressBtnClick () {
      if(this.isAddress){
        new daum.Postcode({
          oncomplete: (data) => {
              console.log(data)
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
      

      let formData = new FormData()
      this.pot.place = this.roadAddress
      this.pot.restaurant_name = this.detailAddress
      
      let date = new Date(this.date + ' 00:00')
      
      date.setHours(this.partyHour)
      if(this.partyAmPm=="PM"){
        date.setHours(date.getHours() + 12)
      }
      date.setMinutes(this.partyMinute)
      
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
    changedStep(type){
      
      if(type==this.eatType[0]) {
        this.isRestaurant = true;
        this.isAddress = false
      }
      else if(type==this.eatType[1]) {
        this.isAddress = true;
        this.isRestaurant = false;
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
      !!this.partyAmPm && !!this.partyHour && !!this.partyMinute && !!this.pot.type &&
      !!this.pot.step
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