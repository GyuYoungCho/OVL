<template>
  <!-- 전체 페이지의 컨테이너 -->
  <v-container>
    <!-- 헤더의 음식, 옷, 화장품 아이콘 영역 -->
    <div class="article mt-4 mb-3">
      <div class="categoryBtn_box">
        <button @click="foodClick" :class="{'articleBtnNotSelected':!foodSelected, 'articleBtnSelected':foodSelected}">음식</button>
        <button @click="clothClick" :class="{'articleBtnNotSelected':!clothSelected, 'articleBtnSelected':clothSelected}">옷</button>
        <button @click="cosmeticClick" :class="{'articleBtnNotSelected':!cosmeticSelected, 'articleBtnSelected':cosmeticSelected}">화장품</button>
      </div>
    </div>

    <!-- 커스텀 모달 -->
    <v-dialog v-model="participation" max-width="300">
      <v-card>
        <!-- 모달 타이틀 영역 -->
        <v-toolbar dense color="#004627">
          <v-toolbar-title class="modalTitle">Challenge</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon dark @click="participation = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
        <!-- 모달 컨텐츠 영역 -->
        <v-container>
        <div class="modalContent">
          <div class="mb-3">
            <span class="modalContentMessage">
              이미 참여한 챌린지가 있습니다! <br>
              참여 취소를 원하시면 내 프로필에서 취소해 주세요!
            </span>
          </div>
          <div class="modalContentButtonArea">
            <button class="modalContentButton" @click="participation = false">확인</button>
          </div>
        </div>
        </v-container>
      </v-card>
    </v-dialog>

    <!-- 컨테이너 1: 카테고리가 딱히 정해지지 않으면 챌린지 리스트 전체를 렌더링 합니다 -->
    <v-container v-if="!nowSelectedCategory.trim()">
      <v-row>
        <v-col v-for="(challenge, idx) in challengeList" :key="idx" cols="4" class="grid-cell">
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
                <button v-if="userinfo.challengeId.challengeId === 1" @click="participateClick(challenge.challengeId)" class="beginParticipation">참여하기</button>
                <button v-else-if="challenge.challengeId === userinfo.challengeId.challengeId" disabled class="myParticipation">참여중</button>
                <button v-else  @click='participation = true' class="alreadyInParticipation">참여하기</button>
              </div>
            </article>
          </v-container>
        </v-col>
      </v-row>
    </v-container>

    <!-- 컨테이너 2: 음식 카테고리인 경우 음식에 해당하는 리스트를 렌더링 합니다. -->
    <v-container v-else-if="nowSelectedCategory==='1'">
      <v-row>
        <v-col v-for="(challenge, idx) in foodChallengeList" :key="idx" cols="4" class="grid-cell">
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
                <button v-if="userinfo.challengeId.challengeId === 1" @click="participateClick(challenge.challengeId)" class="beginParticipation">참여하기</button>
                <button v-else-if="challenge.challengeId === userinfo.challengeId.challengeId" disabled class="myParticipation">참여중</button>
                <button v-else  @click='participation = true' class="alreadyInParticipation">참여하기</button>
              </div>
            </article>
          </v-container>
        </v-col>
      </v-row>
    </v-container>    

    <!-- 컨테이너 3: 옷 카테고리인 경우 음식에 해당하는 리스트를 렌더링 합니다. -->
    <v-container v-else-if="nowSelectedCategory==='2'">
      <v-row>
        <v-col v-for="(challenge, idx) in clothChallengeList" :key="idx" cols="4" class="grid-cell">
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
                <button v-if="userinfo.challengeId.challengeId === 1" @click="participateClick(challenge.challengeId)" class="beginParticipation">참여하기</button>
                <button v-else-if="challenge.challengeId === userinfo.challengeId.challengeId" disabled class="myParticipation">참여중</button>
                <button v-else  @click='participation = true' class="alreadyInParticipation">참여하기</button>
              </div>
            </article>
          </v-container>
        </v-col>
      </v-row>
    </v-container>   

    <!-- 컨테이너 4: 화장품 카테고리인 경우 음식에 해당하는 리스트를 렌더링 합니다. -->
    <v-container v-else>
      <v-row>
        <v-col v-for="(challenge, idx) in cosmeticChallengeList" :key="idx" cols="4" class="grid-cell">
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
                <button v-if="userinfo.challengeId.challengeId === 1" @click="participateClick(challenge.challengeId)" class="beginParticipation">참여하기</button>
                <button v-else-if="challenge.challengeId === userinfo.challengeId.challengeId" disabled class="myParticipation">참여중</button>
                <button v-else  @click='participation = true' class="alreadyInParticipation">참여하기</button>
              </div>
            </article>
          </v-container>
        </v-col>
      </v-row>
    </v-container>  
  </v-container>
</template>

<script>
import { mapActions, mapGetters} from 'vuex'

export default {
  data () {
    return {
      // css 토글용 변수들
      foodSelected: false,
      clothSelected: false,
      cosmeticSelected: false,
      // 현재 선택된 카테고리가 무엇인지 가리키는 변수
      nowSelectedCategory: '',
      // 커스텀 모달 온오프 위한 변수
      participation: false,
    }
  },
  methods: {
    // 스토어에 있는 actions 들
    ...mapActions("challenge", ["fetchChallengeList", "challengeAttend"]),

    // 이 컴포넌트 안에서의 메서드들
    foodClick () {
      // css 토글용 변수
      // 전부 보기로 돌아가는 버튼이 없는 관계로, 한번더 클릭시 해제되면서 전체 리스트가 보이게끔 함.
      // food 가 1번인데, 이미 1번 카테고리로 설정 돼 있는 경우라면 아까 눌렀던 거니까? 버튼도 해제하고 카테고리도 초기화
        if (this.nowSelectedCategory === '1') {
          this.foodSelected = false
          this.nowSelectedCategory = ''
        } else {
          // 만약 초기화된 상태 or 옷이나 화장품이 클릭된 상태였다면? 버튼 색 바꾸고 카테고리도 음식으로!
          this.foodSelected = true
          this.clothSelected = false
          this.cosmeticSelected = false
          this.nowSelectedCategory = '1'
        } 
      },
    clothClick () {
        if (this.nowSelectedCategory === '2') {
          this.clothSelected = false
          this.nowSelectedCategory = ''
        } else {
          this.foodSelected = false
          this.clothSelected = true
          this.cosmeticSelected = false
          this.nowSelectedCategory = '2'
        }
      },
    cosmeticClick () {
        if (this.nowSelectedCategory === '3') {
          this.cosmeticSelected = false
          this.nowSelectedCategory = ''
        } else {
          this.foodSelected = false
          this.clothSelected = false
          this.cosmeticSelected = true
          this.nowSelectedCategory = '3'
        }
      },
    // 참여하기 버튼을 누르는 경우
    participateClick (challenge_id) {
      if (this.userinfo.challengeId.challengeId === 1) {
        // 만약 유저가 챌린지에 참여하고 있지 않은 경우라면?
          const payload = {
              user_id: this.userinfo.userid,
              challenge_id
            }
          this.challengeAttend(payload)
        } 
      },
  },
  computed: {
    // 일단 store.state 에서 4개의 리스트 (챌린지 종류에 따른 배열) 을 가져 옵니다. <위에서 v-for 로 풀어주는 용도>
    ...mapGetters("challenge", ["challengeList", "foodChallengeList", "clothChallengeList", "cosmeticChallengeList"]),
    // userinfo.challengeId.challengeId 가 1 이라면 아직 아무런 챌린지도 하고 있지 않은 상태고, 나머지라면 어떤 챌린지에 참여중인 상태입니다.
    ...mapGetters("user", ["userinfo",])
  },

  created () {
    // 1. 컴포넌트가 렌더링 되면 일단 스토어의 전체 챌린지 리스트를 뷰엑스에 업데이트 합니다.
    this.fetchChallengeList()
  },
}
</script>

<style>

</style>