<template>
  <div>
    <v-container class="px-6 mt-4">
      <feed-search @searchKeyword="searchKeyword"
        @selectOrd="selectOrd"></feed-search>
      <v-card v-if="searchUser && searchUser.length!=0" click:outside="searchKeyword('')">
        <v-col class="mt-5 pl-3">
            <profile-name v-for="(suser, index) in searchUser" :key="index" :user="suser"></profile-name>
        </v-col> 
      </v-card>
      

      <!-- 캐러셀 영역 -->
      <div>
      <v-carousel hide-delimiters height="22vh" data-interval="true" class="mt-7 carouselBorder" :show-arrows="false" cycle interval="4000">
        <v-carousel-item v-for="(info,idx) in recommendList" :key="idx" :src="info.src" @click="recommendClick(info)">
          <!-- 챌린지 일 때 -->
          <div v-if="info.type==0" class="carousel_content">
            <h2>&lt; {{info.challenge.title}} &gt; </h2>
            <div># {{info.challenge.count}}명&nbsp;# {{info.challenge.cycle}}주&nbsp;# {{info.challenge.score}}점 </div> <br>
            <div>클릭 시 챌린지 상세보기</div>
          </div>

          <!-- 레시피 일 때 -->
          <div v-else-if="info.type==1" class="carousel_content blackSquare">
            <h3>인기 레시피를 확인해보세요! </h3>
            <h2># {{info.recipe.title}} </h2> <br>
            <div>클릭 시 레시피 상세보기</div>
          </div>

          <!-- 채식팟 일 때 -->
          <div v-else class="carousel_content blackSquare">
            <div style="font-size:20px"> 음식 뿐만 아니라 가치를 공유하는 </div>
            <div style="font-size:20px">채식팟에 참여해보세요! </div> <br>
            <div>클릭 시 채식팟 이동</div>
          </div>
        </v-carousel-item>
      </v-carousel>

      <!-- 커스텀 모달 영역-->
      <v-dialog v-model="isDetail" max-width="300">
        <v-card>
          <!-- 모달 타이틀 영역 -->
          <v-toolbar dense color="#004627">
            <v-toolbar-title class="modalTitle">{{detailCh.title}}</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon dark @click="isDetail = false">
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
              <br>
            </div>
            <div class="modalContentButtonArea">
              <button v-if="userinfo.challengeId.challengeId === 1" @click="participateClick(detailCh.challengeId)" class="beginBtn">참여하기</button>
              <button v-else-if="detailCh.challengeId === userinfo.challengeId.challengeId" disabled class="myBtn">참여 중</button>
              <button v-else-if="userinfo.challengeId.challengeId!=1 " disabled class="alreadyBtn ">참여 불가</button>
            </div>
          </div>
          </v-container>
        </v-card>
      </v-dialog>
      </div>

      <div v-for="(info, idx) in searchPost" :key="idx" class="mt-4">
        
        <div>
          <!-- post header - 프로필 사진, 유저 닉네임, 카테고리 -->
          <v-row>
            <v-col cols="6" md="1">
              <ProfileName :user="info.postId.userId"></ProfileName>
            </v-col>
            <v-col cols="6" md="1" style="text-align:right">
              <img :src="iconPath(idx)" width=15%>
            </v-col>
          </v-row>

          <!-- post 대표 사진, 내용-->
          <div @click="moveDetail(idx)">
            <img :src="searchPost[idx].filepath" width=100%  style="border-radius: 7px;" class="my-1">
            <div class="contentAndTime">
              <div v-html="contentReplace(info.postId.content)"></div>
              <span>{{ calTime(info.postId.time) }}</span>
            </div>
              
          </div>

          <!-- 좋아요, 댓글 -->
          <!-- 좋아요 눌렀으면 꽉 찬 하트 -->
          <div v-if="isLike(idx)" class="inline mt-1" @click="like(idx)">  
            <v-icon style="color:#20683D">mdi-heart</v-icon> &nbsp;
          </div>
          <!-- 좋아요 안눌렀으면 빈 하트 -->
          <div v-else class="inline mt-1" @click="like(idx)">
            <v-icon style="color:#BABABA">mdi-heart-outline</v-icon> &nbsp;
          </div>
          <span class="color-gray inline">
            {{info.postId.like_count}}  &nbsp;
            <v-icon style="color:#BABABA" @click="moveDetail(idx)">mdi-chat-outline</v-icon>
            {{info.postId.comment_count}}
          </span>
        </div>
        
      </div>
    </v-container>
  </div>
</template>

<script>
import FeedSearch from '@/components/basic/FeedSearch.vue';
import ProfileName from '@/components/basic/ProfileName.vue'
import {mapGetters, mapActions} from "vuex";
import moment from 'moment'


export default {
  data(){
    return{
      search : '',
      ord : 'User',
      order : [
        "User", "Post",
      ],
      recommendList:[],
      isDetail:false,
      detailCh:{},
    }
  },
  components: {
    FeedSearch,
    ProfileName
  },
  methods: {
    ...mapActions('post', ['getPostList', 'getPostLikeList', 'getRecommend']),
    ...mapActions('user', ['getUserList',]),
    ...mapActions("challenge", ["challengeAttend"]),
    recalibratedDate(start_date, period) {
      if (period==0) return start_date.substring(0,10);
      return moment(start_date).add(period-2, 'days').format('YYYY-MM-DD')
    },
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
    recommendClick(info) {
      if (info.type==0) { // 챌린지 클릭 시 
        this.isDetail = true;
        this.detailCh = info.challenge;
      } else if (info.type==1) { // 레시피 클릭 시
        console.log("recipeId : "+info.recipe.recipeId);
        this.$router.push({name: 'RecipeDetail', params: {recipeId: info.recipe.recipeId}});
      } else { // 채식팟 클릭 시 
        this.$router.push({name:'VetPartyList'})
      }
    },
    searchKeyword(val){ // 키워드 받아오기
      this.search = val
    },
    selectOrd(val){ //카테고리 받아오기
      this.ord = val
    },
    contentReplace(content) { // 줄바꿈
      return content.replace(/(?:\r\n|\r|\n)/g, '<br />');
    },
    iconPath(idx) { // 카테고리 이미지 출력
      const category = this.searchPost[idx].postId.category;
      if (category==1) return require("@/assets/image/meal.png");
      else if (category==2) return require("@/assets/image/clothes.png");
      else return require("@/assets/image/cosmetics.png");
    },
    moveDetail(idx) { // 게시글 상세보기
      // this.$router.push({path:"/article_detail/"+this.searchPost[idx].postId.postId});
      this.$router.push({name: "ArticleDetail", params: {postId: this.searchPost[idx].postId.postId}});
    },
    isLike(idx) { // 좋아요 눌렀는지 확인
      if (this.postLikeList.includes(this.searchPost[idx].postId.postId)) return true;
      else return false;
    }, 
    like(idx) { // 좋아요 버튼 눌렀슴
      var payload = {
        "userId" : this.userinfo.userid,
        "postId" : this.searchPost[idx].postId.postId,
        "type": 1 // 뉴스피드
      }
      this.$store.dispatch("post/postLike", payload);
    },
    calTime (time) {
      return moment(time).fromNow()
    },
  },
  computed: {
    ...mapGetters("post", (["postList", "postLikeList", "recommendRe", "recommendCh"])),
    ...mapGetters("user", (["userinfo", "userlist"])),

    searchPost() {
      // 대소문자 구분 x
      const search = this.search.toLowerCase()
      if (this.ord == 'User' || !search) return this.postList
      
      // 포함된 단어 거르기
      let allitems = this.postList.filter(item => {
        const text = item.postId.content.toLowerCase()

        return text.indexOf(search) > -1
      })

      // 포함된 단어 위치 인덱스 기준 정렬
      allitems = allitems.sort(function(a, b) {
          let x = a.postId.content.indexOf(search);
          let y = b.postId.content.indexOf(search);
          if (x < y)  return -1;
          if (x > y) return 1;
          return 0;
      });
      return (allitems.length >5) ? allitems.slice(0,5) : allitems
    },
    searchUser(){ // 위와 같은 원리
      const search = this.search.toLowerCase()
      if(!search || this.ord=='Post') return []
      
      let allitems = this.userlist.filter(item => {
        const text = item.nickname.toLowerCase()
        return text.indexOf(search) > -1
      })
      
      allitems = allitems.sort(function(a, b) {
          let x = a.nickname.indexOf(search);
          let y = b.nickname.indexOf(search);
          if (x < y)  return -1;
          if (x > y) return 1;
          return 0;
      });
       return (allitems.length >3) ? allitems.slice(0,5) : allitems
    },
  },
  created() {
    this.getRecommend() 
    this.getPostList(this.userinfo.userid)
    this.getPostLikeList(this.userinfo.userid)
    this.getUserList()

    // 추천 목록 만들기
    for (var i=0;i<this.recommendCh.length;i++) {
      let src = "";
      if (this.recommendCh[i].category==1 && this.recommendCh[i].type==1) src=require("@/assets/image/challenge1.png")
      else if (this.recommendCh[i].type==2) src=require("@/assets/image/challenge_recipe.png")
      else if (this.recommendCh[i].category==2) src=require("@/assets/image/challenge2.png")
      else src=require("@/assets/image/challenge3.png")
      this.recommendList.push({
        "type":0, // type = 0 -> 챌린지
        "challenge":this.recommendCh[i],
        "src":src
      })
    }

    for (var j=0;j<this.recommendRe.length;j++) {
      this.recommendList.push({
        "type":1, // type = 1 -> 레시피 
        "recipe":this.recommendRe[j],
        "src":this.recommendRe[j].filepath
      })
    }

    this.recommendList.push({
      "type":2, // type = 2 -> 채식팟
      "src":require("@/assets/image/map.png")
    })

    console.log("recommendList : ", this.recommendList);
  }
}
</script>

<style>

</style>