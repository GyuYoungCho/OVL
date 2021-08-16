<template>
  <v-container class="px-6 mt-5 searchHeader borderRadius">
      <feed-search @searchKeyword="searchKeyword"
        @selectOrd="selectOrd"></feed-search>
      <v-card v-if="searchUser && searchUser.length!=0" click:outside="searchKeyword('')">
        <v-col class="mt-5 pl-3">
            <profile-name v-for="(suser, index) in searchUser" :key="index" :user="suser"></profile-name>
        </v-col> 
      </v-card>

      <div v-if="search.length==0" class="noResult">
          <img src="@/assets/image/search.png" alt="">
          <div class="mt-3">닉네임, 게시글을 검색할 수 있습니다!</div>
      </div>

      <div v-if="ordCheck">
        <div v-if="searchPost.length==0 && search.length>0" class="noResult">
            <img src="@/assets/image/noResult.png" alt="">
            <div class="mt-3">검색 결과가 존재하지 않습니다</div>
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
      </div>
  </v-container>
</template>

<script>
import FeedSearch from '@/components/basic/FeedSearch.vue';
import ProfileName from '@/components/basic/ProfileName.vue'
import { mapActions, mapGetters } from 'vuex'
import moment from 'moment'

export default {
  components: {
    FeedSearch,
    ProfileName
  },
  data () {
    return {
        search : '',
        ord : '닉네임',
        order : [
            "닉네임", "게시글",
        ],
    }
  },
  methods: {
    ...mapActions('post', ['getPostAll', 'getPostLikeList', 'getRecommend']),
    ...mapActions('user', ['getUserList',]),
    recalibratedDate(start_date, period) {
      if (period==0) return start_date.substring(0,10);
      return moment(start_date).add(period-2, 'days').format('YYYY-MM-DD')
    },
    ordCheck() {
        if (this.ord=="게시글") return true;
        else return false;
    },
    contentReplace(content) { // 줄바꿈
      return content.replace(/(?:\r\n|\r|\n)/g, '<br />');
    },
    searchKeyword(val){ // 키워드 받아오기
      this.search = val
    },
    selectOrd(val){ //카테고리 받아오기
      this.ord = val
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
    ...mapGetters("user", (["userinfo", "userlist"])),
    ...mapGetters("post", (["postAll", "postLikeList"])),
    searchPost() {
      // 대소문자 구분 x
      const search = this.search.toLowerCase()
      if (this.ord == '닉네임' || search=="") return []
      
      // 포함된 단어 거르기
      let allitems = this.postAll.filter(item => {
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
      
      if(!search || this.ord=='게시글') return []
      
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
    this.getPostAll()
    this.getPostLikeList(this.userinfo.userid)
    this.getUserList()
 }
}
</script>

<style>

</style>