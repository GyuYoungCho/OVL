<template>
<v-container class="mb-4">
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
          <v-container v-if="showAll">
            <v-row>
              <v-col v-for="(challenge, idx) in userchallengeList" :key="idx" cols="4" class="grid-cell">
                <!-- 개별 card 영역 : 카드들이 위의 v-for 태그로 인해 그리드로 들어가게 됩니다 -->
                <v-container class="card">
                  <article class="cardContent">
                    <!-- (1) 개별 카드에서의 더미 이미지 영역 -->
                    <div class="cardContentArea">
                      <img src="@/assets/image/challenge_dummy.jpg" class='cardImage'>
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
                      {{challenge.count}}명 참여중
                    </div>
                    <!-- (6) 참여 기간을 나타내는 영역 -->
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-calendar-blank</v-icon>
                      ({{challenge.period/7}}주)
                    </div>
                    <!-- (7) 참여하기 버튼 -> v-if 들로 분기해 줍니다. -->
                    <div class="cardContentArea">
                      <button v-if="challenge.challengeId === userinfo.challengeId.challengeId" @click="challengeEnd()" class="cancleChallenge">참여취소</button>
                      <button v-else class="completeChallenge">참여완료</button>
                    </div>
                  </article>
                </v-container>
              </v-col>
            </v-row>
          </v-container>

          <!-- 컨테이너 2: 음식 카테고리인 경우 음식에 해당하는 리스트를 렌더링 합니다. -->
          <v-container v-else-if="btnActive[0]">
            <v-row>
              <v-col v-for="(challenge, idx) in userfoodChallengeList" :key="idx" cols="4" class="grid-cell">
                <v-container class="card">
                  <article class="cardContent">
                    <div class="cardContentArea">
                      <img src="@/assets/image/challenge_dummy.jpg" class='cardImage'>
                    </div>
                    <div class="cardContentArea cardContentAreaTitle">
                      {{challenge.title}}
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-run</v-icon>
                      <span v-if="challenge.cycle === 1">
                        매일
                      </span>
                      <span v-else>
                        {{challenge.cycle}}일마다
                      </span>
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-diamond-stone</v-icon>
                      {{challenge.score}}
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-account</v-icon>
                      {{challenge.count}}명 참여중
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-calendar-blank</v-icon>
                      ({{challenge.period/7}}주)
                    </div>
                    <div class="cardContentArea">
                      <button v-if="challenge.challengeId === userinfo.challengeId.challengeId" @click="challengeEnd()"  class="cancleChallenge">참여중</button>
                      <button v-else class="card completeChallenge" >참여종료</button>
                      <!-- <button v-if="challenge.challengeId === userinfo.challengeId.challengeId" @click="participateClick(challenge.challengeId)" class="myParticipation">참여중</button>
                      <button v-else-if="userinfo.challengeId.challengeId === 1" @click="participateClick(challenge.challengeId)" class="beginParticipation">참여하기</button>
                      <button v-else @click="participateClick(challenge.challengeId)" class="alreadyInParticipation">참여하기</button> -->
                    </div>
                  </article>
                </v-container>
              </v-col>
            </v-row>
          </v-container>    

          <!-- 컨테이너 3: 옷 카테고리인 경우 음식에 해당하는 리스트를 렌더링 합니다. -->
          <v-container v-else-if="btnActive[2]">
            <v-row>
              <v-col v-for="(challenge, idx) in userclothChallengeList" :key="idx" cols="4" class="grid-cell">
                <v-container class="card">
                  <article class="cardContent">
                    <div class="cardContentArea">
                      <img src="@/assets/image/challenge_dummy.jpg" class='cardImage'>
                    </div>
                    <div class="cardContentArea cardContentAreaTitle">
                      {{challenge.title}}
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-run</v-icon>
                      <span v-if="challenge.cycle === 1">
                        매일
                      </span>
                      <span v-else>
                        {{challenge.cycle}}일마다
                      </span>
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-diamond-stone</v-icon>
                      {{challenge.score}}
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-account</v-icon>
                      {{challenge.count}}명 참여중
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-calendar-blank</v-icon>
                      ({{challenge.period/7}}주)
                    </div>
                    <div class="cardContentArea">
                      <button v-if="challenge.challengeId === userinfo.challengeId.challengeId" @click="challengeEnd(challenge.challengeId)"  class="cancleChallenge">참여취소</button>
                      <button v-else class="completeChallenge" >참여완료</button>
                    </div>
                  </article>
                </v-container>
              </v-col>
            </v-row>
          </v-container>   

          <!-- 컨테이너 4: 화장품 카테고리인 경우 음식에 해당하는 리스트를 렌더링 합니다. -->
          <v-container v-else-if="btnActive[1]">
            <v-row>
              <v-col v-for="(challenge, idx) in usercosmeticChallengeList" :key="idx" cols="4" class="grid-cell">
                <v-container class="card">
                  <article class="cardContent">
                    <div class="cardContentArea">
                      <img src="@/assets/image/challenge_dummy.jpg" class='cardImage'>
                    </div>
                    <div class="cardContentArea cardContentAreaTitle">
                      {{challenge.title}}
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-run</v-icon>
                      <span v-if="challenge.cycle === 1">
                        매일
                      </span>
                      <span v-else>
                        {{challenge.cycle}}일마다
                      </span>
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-diamond-stone</v-icon>
                      {{challenge.score}}
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-account</v-icon>
                      {{challenge.count}}명 참여중
                    </div>
                    <div class="cardContentArea">
                      <v-icon x-small>mdi-calendar-blank</v-icon>
                      ({{challenge.period/7}}주)
                    </div>
                    <div class="cardContentArea">
                      <button v-if="challenge.challengeId === userinfo.challengeId.challengeId"  @click="challengeEnd(challenge.challengeId)" class="cancleChallenge" >참여취소</button>
                      <button v-else class="completeChallenge" >참여완료</button>
                    </div>
                  </article>
                </v-container>
              </v-col>
            </v-row>
          </v-container> 

        </div>
</v-container>
</template>

<script>
import axios from 'axios';
import {mapGetters, mapState, mapActions} from "vuex";
import API from '@/api/index.js'
import challengeAPI from '@/api/challenge.js'

export default {
data() {
    return {
      cancleChallenge:false,
      showAll: true,
      btnActive: {0:false,1:false,2:false},
    }
},
methods: {
    moveDetail(idx) { // 게시글 상세보기
    this.$router.push({path:"/article_detail/"+this.postList[idx].postId.postId});
    },
      ...mapActions("challenge", ["fetchUserChallengeList", "challengeAttend"]),
    selectTypeIcon(num){
        if(this.btnActive[num] === true){
            this.btnActive[num] = false;
            this.showAll = true;
            // console.log(num)
            // console.log("btn",this.btnActive)
            // console.log("show",this.showAll)
        }else if(this.btnActive[num] ===false){
            this.btnActive[num] = true;
            this.showAll = false;
            for(var i = 0; i < 3; i++){
              if(i !== num){
                this.btnActive[i] = false;
              }
            }
        }
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
},
computed: {
    ...mapGetters("challenge", ["userchallengeList", "userfoodChallengeList", "userclothChallengeList", "usercosmeticChallengeList"]),
    ...mapState("user", (["userinfo"])),
},
created() {
    this.$store.dispatch("post/getPostList", this.userinfo.userid);
    this.$store.dispatch("post/getPostLikeList", this.userinfo.userid);

    this.$store.dispatch("challenge/fetchUserChallengeList", {challenge_id: this.userinfo.challengeId.challengeId, user_id: this.userinfo.userid});
},
}
</script>
