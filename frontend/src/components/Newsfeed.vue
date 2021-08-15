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
      <!-- <v-carousel hide-delimiters>
        <v-carousel-item
          v-for="(info,i) in adList"
          :key="i"
          :src="info.src"
        ></v-carousel-item>
      </v-carousel> -->

      <div v-for="(info, idx) in searchPost" :key="idx" class="mt-9">
        
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
    }
  },
  components: {
    FeedSearch,
    ProfileName
  },
  methods: {
    ...mapActions('post', ['getPostList', 'getPostLikeList', ]),
    ...mapActions('user', ['getUserList',]),
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
    ...mapGetters("post", (["postList", "postLikeList"])),
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
    this.getPostList(this.userinfo.userid)
    this.getPostLikeList(this.userinfo.userid)
    this.getUserList()
  }
}
</script>

<style>

</style>