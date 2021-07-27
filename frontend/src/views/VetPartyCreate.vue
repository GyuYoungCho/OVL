<template>
  <div>
    <h1>채식팟 생성</h1>
    <!-- 채식팟 제목 -->
    <div>
      채식팟 제목 : <input type="text" v-model="partyTitle">
    </div>

    <!-- 주소 넣기 관련 부분 -->
    <div>
      <p> 채식팟 주소 : <button @click="onAddressBtnClick">주소 찾기</button></p>
      <p> 도로명 주소 : {{ roadAddress }}</p>
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
      <select v-model="partyAmPm">
        <option value="">
          --선택--
        </option>
        <option value="AM">
          오전
        </option>
        <option value="PM">
          오후
        </option>
      </select>
      <select v-model="partyTime">
          <option value="">--시간--</option>
          <option :value="time" v-for="time in times" :key="time">
            {{ time }}시
          </option>
      </select>
      <select v-model="partyMinute">
          <option value="">--분--</option>
          <option :value="minute" v-for="minute in minutes" :key="minute">
            {{ minute }}분
          </option>
      </select>
    </div>

    <!-- 인원수 -->
    <div>
      <input type="number" v-model="numPeople">명
    </div>

    <!-- 비건종류 -->

    <!-- 식당 / 도시락 --> 
    <div>
      <select name="" id="" v-model="howToEat">
        <option value="">--옵션--</option>
        <option value="식당">식당</option>
        <option value="도시락">도시락</option>
      </select>
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

export default {
  data: () => ({
    date:  (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    times: _.range(1, 13),
    minutes: _.range(1, 60),
    partyTitle: '',
    partyAmPm: '',
    partyTime: 0,
    partyMinute: 0,
    numPeople: 0,
    howToEat: '',
    roadAddress: '',
    detailAddress: '',
    extraAddress: '',
  }),
  methods: {
    onChange () {
      console.log('hi')
    },

    // 주소 넣는 팝업창 생성
    onAddressBtnClick () {
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
        }
      }).open()
    },

    onCreateBtnClick () {
      this.$router.push({ name: "VetPartyList" })
    }
  },
  computed: {
    address () {
      return this.roadAddress + this.extraAddress + this.detailAddress
    },
    isValid () {
      return !!this.partyTitle && !!this.address && !!this.numPeople && !!this.partyAmPm && !!this.partyTime && !!this.partyMinute && !!this.howToEat
    }
  }
}
</script>

<style>

</style>