<template>
<v-container class="mb-4">
  <ModifyModal :modalOpen="isChDelete" title="챌린지 참여 취소" modalContent="참여를 취소하시겠습니까?" type=1
        @modalConfirmBtnClick="challengeEnd" @modalCancelBtnClick="isChDelete = false" />
        <div class="icon mb-4">
            <button class="icon-btn" v-if="!btnActive[0]" @click="selectTypeIcon(0)">
            <img src="@/assets/icon/recipe.png" alt="" class="icon-btn-img"></button>
            <button class="icon-btn" v-else @click="selectTypeIcon(0)">
            <img src="@/assets/icon/recipe_selected.png" alt="" class="icon-btn-img"></button>

            <button class="icon-btn" v-if="!btnActive[1]" @click="selectTypeIcon(1)" >
            <img src="@/assets/icon/cream.png" alt="" class="icon-btn-img"></button>
            <button class="icon-btn" v-else @click="selectTypeIcon(1)">
            <img src="@/assets/icon/cream_selected.png" alt="" class="icon-btn-img"></button>

            <button class="icon-btn" v-if="!btnActive[2]" @click="selectTypeIcon(2)">
            <img src="@/assets/icon/hanger.png" alt="" class="icon-btn-img"></button>
            <button class="icon-btn" v-else @click="selectTypeIcon(2)">
            <img src="@/assets/icon/hanger_selected.png" alt="" class="icon-btn-img"></button>
        </div>
        <div>
                <!-- 컨테이너 1: 카테고리가 딱히 정해지지 않으면 챌린지 리스트 전체를 렌더링 합니다 -->
          <v-container>
            <v-row>
              <v-col v-for="(challenge, idx) in filteredUserChallengeList" :key="idx" cols="4" class="grid-cell">
                <!-- 개별 card 영역 : 카드들이 위의 v-for 태그로 인해 그리드로 들어가게 됩니다 -->
                <v-container v-if="challenge.challengeId === userinfo.challengeId.challengeId" class="card" >
                  <article class="cardContent">
                    <!-- (1) 개별 카드에서의 더미 이미지 영역 -->
                    <div class="cardContentArea" @click="onClickCard(challenge.challengeId )">
                            <img v-if="challenge.type === 2" src="@/assets/image/challenge_recipe.png" class='cardImage'>
                            <img v-else :src="require(`@/assets/image/challenge${challenge.category}.png`)" class='cardImage'>
                    </div>
                    <!-- (2) 제목 -->
                    <div @click="onClickCard(challenge.challengeId )"  class="cardContentArea">
                    <div class="cardContentArea cardContentAreaTitle" >
                      {{challenge.title}}
                    </div >
                      <!-- (3) 해당 챌린지의 기간 영역 -->
                      <div>
                        <v-icon x-small>mdi-run</v-icon>
                        <span v-if="challenge.cycle === 1">
                          매일
                        </span>
                        <span v-else>
                          {{challenge.cycle}}일마다
                        </span>
                      </div>
                      <!-- (4) 해당 챌린지의 경험치 영역 -->
                      <div>
                        <v-icon x-small>mdi-diamond-stone</v-icon>
                        {{challenge.score}}
                      </div>
                      <!-- (5) 참여 인원을 나타내는 영역 -->
                      <div>
                        <v-icon x-small>mdi-account</v-icon>
                        {{challenge.count}}명 참여 중
                      </div>
                      <!-- (6) 참여 기간을 나타내는 영역 -->
                      <div>
                        <v-icon x-small>mdi-calendar-blank</v-icon>
                        {{challenge.period/7}}주
                      </div>
                    </div>
                    <!-- (7) 참여하기 버튼 -> v-if 들로 분기해 줍니다. -->
                    <div class="cardContentArea">
                      <button v-if="challenge.challengeId === userinfo.challengeId.challengeId" @click="isChDeleteClick" class="cancleChallenge">참여 취소</button>
                      <button v-else class="completeChallenge">참여완료</button>
                    </div>
                  </article>
                </v-container>
                <!-- list 가 비활성화 : 참여완료 card-->
                <v-container v-else class="card"  style="opacity: 0.5;">
                  <article class="cardContent">
                    <!-- (1) 개별 카드에서의 더미 이미지 영역 -->
                    <div class="cardContentArea">
                            <img v-if="challenge.type === 2" src="@/assets/image/challenge_recipe.png" class='cardImage'>
                            <img v-else :src="require(`@/assets/image/challenge${challenge.category}.png`)" class='cardImage'>
                    </div>
                    <!-- (2) 제목 -->
                    <div class="cardContentArea cardContentAreaTitle">
                      {{challenge.title}}
                    </div>
                    <!-- (3) 해당 챌린지의 기간 영역 -->
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-run</v-icon>
                      <span v-if="challenge.cycle === 1">
                        매일
                      </span>
                      <span v-else>
                        {{challenge.cycle}}일마다
                      </span>
                    </div>
                    <!-- (4) 해당 챌린지의 경험치 영역 -->
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-diamond-stone</v-icon>
                      {{challenge.score}}
                    </div>
                    <!-- (5) 참여 인원을 나타내는 영역 -->
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-account</v-icon>
                      {{challenge.count}}명 참여 중
                    </div>
                    <!-- (6) 참여 기간을 나타내는 영역 -->
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-calendar-blank</v-icon>
                      {{challenge.period/7}}주
                    </div>
                    <!-- (7) 참여하기 버튼 -> v-if 들로 분기해 줍니다. -->
                    <div class="cardContentArea">
                      <button v-if="challenge.challengeId === userinfo.challengeId.challengeId" @click="isChDeleteClick" class="cancleChallenge">참여 취소</button>
                      <button v-else class="completeChallenge">참여완료</button>
                    </div>
                  </article>
                </v-container>
              </v-col>
            </v-row>
          </v-container>


                                <!-- 커스텀 모달 -->
                <v-dialog v-model="challengedialog" max-width="300" @click:outside="challengedialog = false">
                <v-card>
                    <!-- 모달 타이틀 영역 -->
                    <v-toolbar dense color="#49784B">
                    <v-toolbar-title class="modalTitle">챌린지 상세 내용</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn icon dark @click="challengedialog = false">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                    </v-toolbar>
                    <!-- 모달 컨텐츠 영역 -->
                    <v-container>
                    <div class="modalContent">
                    <div class="mb-3">
                        <div>{{detailCh.content}}</div>
                        <br>
                        <!-- 해당 챌린지의 기간 영역 -->
                        <div>
                          <v-icon dense>mdi-run</v-icon>
                          <span v-if="detailCh.cycle === 1">
                            매일
                          </span>
                          <span v-else>
                            {{detailCh.cycle}}일마다
                          </span>
                        </div>
                        <!-- 해당 챌린지의 경험치 영역 -->
                        <div>
                          <v-icon dense>mdi-diamond-stone</v-icon>
                          {{detailCh.score}}
                        </div>
                        <!-- 참여 인원을 나타내는 영역 -->
                        <div>
                          <v-icon dense>mdi-account</v-icon>
                          {{detailCh.count}}명 참여 중
                        </div>
                        <!-- 참여 기간을 나타내는 영역 -->
                        <div v-if="detailCh.start_date!=undefined">
                          <v-icon dense>mdi-calendar-blank</v-icon>
                          {{ recalibratedDate(detailCh.start_date, 0) }} ~
                            {{ recalibratedDate(detailCh.start_date, detailCh.period) }}  ({{detailCh.period/7}}주)
                        </div>
                    </div>
                    </div>
                    </v-container>
                </v-card>
                </v-dialog>

        </div>
</v-container>
</template>

<script>
import axios from 'axios';
import {mapGetters, mapActions} from "vuex";
import API from '@/api/index.js'
import challengeAPI from '@/api/challenge.js'
import moment from 'moment'
import ModifyModal from '@/components/profile/ModifyModal.vue'

export default {
  components:{ModifyModal},
data() {
    return {
      cancleChallenge:false,
      showAll: true,
      categoryNum: '',
      btnActive: {0:false,1:false,2:false},
      detailCh: '',
      challengedialog: false,
      isChDelete: false
    }
},
methods: {
      ...mapActions("challenge", ["fetchUserChallengeList"]),
    recalibratedDate(start_date, period) {
      if (period==0) return start_date.substring(0,10);
      return moment(start_date).add(period-2, 'days').format('YYYY-MM-DD')
    },
    selectTypeIcon(num){
        if(this.btnActive[num] === true){
            this.btnActive[num] = false;
            this.showAll = true;
            this.categoryNum = '';
        }else if(this.btnActive[num] ===false){
            this.btnActive[num] = true;
            this.showAll = false;
            if(num === 0){
              this.categoryNum = '1';
              //console.log("이거 골랐어1")
            }
            else if(num === 1){
              this.categoryNum = '3';
             // console.log("이거 골랐어2")
            }
            if(num === 2){
              this.categoryNum = '2';
              //console.log("이거 골랐어3")
            }
            
            for(var i = 0; i < 3; i++){
              if(i !== num){
                this.btnActive[i] = false;
              }
            }
        }
    },
    isChDeleteClick() {
      this.isChDelete = true;
    },
    challengeEnd(){
      if(this.userinfo.challengeId.challengeId === 1){
        console.log("기본 참여 챌린지 입니다.")
      }else{
        axios({
          method: "get",
          url: API.url + challengeAPI.delete(this.userinfo.userid),
        }).then((res)=>{
          if(res.data === "success"){
            console.log("챌린지 중단")
            this.cancleChallenge = true;
            this.$store.dispatch('user/getUpdateUserInfo', this.userinfo.userid);
          }
        }).catch((err)=>{
          console.log(err)
        })
      }
    },
    onClickCard(challenge_id ) {  
      this.challengedialog = true;
      axios({
          method: "get",
          url: API.url + challengeAPI.search_detail(challenge_id),
        }).then((res)=>{
          if(res.data!== null){
            console.log(res.data.content);
            this.detailCh = res.data;
          }
        }).catch((err)=>{
          console.log(err)
        })

    },
},
computed: {
    ...mapGetters("challenge", ["userchallengeList", "detailContent"]),
    ...mapGetters("user", (["userinfo"])),

      filteredUserChallengeList() {
        if(this.showAll) {
          return this.userchallengeList;
        }else{
          return this.userchallengeList.filter((eachChallenge) => eachChallenge.category === parseInt(this.categoryNum) )
        }
      }
},
created() {

    this.$store.dispatch("post/getPostList", this.userinfo.userid);
    this.$store.dispatch("post/getPostLikeList", this.userinfo.userid);
    // 전체 챌린지 가져오기
    this.$store.dispatch("challenge/fetchUserChallengeList", {challenge_id: this.userinfo.challengeId.challengeId, user_id: this.userinfo.userid});

},
}
</script>
