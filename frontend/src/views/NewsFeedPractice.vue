<template>
  <div>
      <v-container class="px-6 mt-4">
        <feed-search @searchKeyword="searchKeyword"
        @selectOrd="selectOrd"></feed-search>
      <!-- <v-card v-if="searchUser && searchUser.length!=0" click:outside="searchKeyword('')">
        <v-col class="mt-5 pl-3">
            <profile-name v-for="(suser, index) in searchUser" :key="index" :user="suser"></profile-name>
        </v-col> 
      </v-card> -->
        <div class="filtering d-flex justify-center">
        <v-btn-toggle mandatory dense borderless rounded>
            <v-btn small color="primary" :ripple="false" text>ALL</v-btn>
            <v-btn small color="primary" :ripple="false" text>ME</v-btn>
            <v-btn small color="primary" :ripple="false" text>FOLLOW</v-btn>
        </v-btn-toggle>
        </div>

        <div v-for="(info, idx) in feedDatas" :key="idx" class="mt-9">
        
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
            <img :src="feedDatas[idx].filepath" width=100%  style="border-radius: 7px;" class="my-1">
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


      <!-- 무한 스크롤 컨트롤 영역-->
        <infinite-loading @infinite="infiniteHandler" ref="infiniteLoading" spinner="circles">
            
        <!-- 더 이상 없는 경우 -->
        <div slot="no-more" class="mt-4">
            <v-sheet
              block
              class="pa-5 mx-auto d-flex align-center justify-center"
              rounded="xl"
              color="rgb(224,229,231)"
              style="max-width:680px;">
              <div class="font-weight-medium d-flex flex-column">
                <v-icon large class="blue-grey--text text--lighten-3">mdi-close</v-icon>
                <h4 class="blue-grey--text text--lighten-3">끝</h4>
              </div>
            </v-sheet>
        </div>
        <div slot="no-results" class="mt-4">
            <v-sheet
              block
              class="pa-5 mx-auto d-flex align-center justify-center"
              rounded="xl"
              color="rgb(224,229,231)"
              style="max-width:680px;">
              <div class="font-weight-medium d-flex flex-column">
                <v-icon large class="blue-grey--text text--lighten-3">mdi-close</v-icon>
                <h4 class="blue-grey--text text--lighten-3">불러올 글이 없어</h4>
              </div>
            </v-sheet>
        </div>
        </infinite-loading>
      </v-container>
  </div>
</template>

<script>
import InfiniteLoading from 'vue-infinite-loading';
import { mapGetters, mapActions} from 'vuex';
import axios from "axios";
import API from "@/api/index.js";
import feedAPI from '@/api/feed.js'
import FeedSearch from '@/components/basic/FeedSearch.vue';
import ProfileName from '@/components/basic/ProfileName.vue'
import moment from 'moment'


export default {
  components: {
    InfiniteLoading,
    FeedSearch,
    ProfileName,
  },
  data(){
    return{
      feedDatas: [],
      pageNumber: 0,
      pageSize: 2,
    }
  },
  created() {
    this.getPostList(this.userinfo.userid)
    this.getPostLikeList(this.userinfo.userid)
    this.getUserList()
  },
  computed:{
    ...mapGetters("post", (["postList", "postLikeList"])),
    ...mapGetters("user", (["userinfo", "userlist"])),

    // searchPost() {
    //   // 대소문자 구분 x
    //   const search = this.search.toLowerCase()
    //   if (this.ord == 'User' || !search) return this.postList
      
    //   // 포함된 단어 거르기
    //   let allitems = this.postList.filter(item => {
    //     const text = item.postId.content.toLowerCase()

    //     return text.indexOf(search) > -1
    //   })

    //   // 포함된 단어 위치 인덱스 기준 정렬
    //   allitems = allitems.sort(function(a, b) {
    //       let x = a.postId.content.indexOf(search);
    //       let y = b.postId.content.indexOf(search);
    //       if (x < y)  return -1;
    //       if (x > y) return 1;
    //       return 0;
    //   });
    //   return (allitems.length >5) ? allitems.slice(0,5) : allitems
    // },
    // searchUser(){ // 위와 같은 원리
    //   const search = this.search.toLowerCase()
    //   if(!search || this.ord=='Post') return []
      
    //   let allitems = this.userlist.filter(item => {
    //     const text = item.nickname.toLowerCase()
    //     return text.indexOf(search) > -1
    //   })
      
    //   allitems = allitems.sort(function(a, b) {
    //       let x = a.nickname.indexOf(search);
    //       let y = b.nickname.indexOf(search);
    //       if (x < y)  return -1;
    //       if (x > y) return 1;
    //       return 0;
    //   });
    //    return (allitems.length >3) ? allitems.slice(0,5) : allitems
    // },
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
      const category = this.feedDatas[idx].category;
      if (category==1) return require("@/assets/image/meal.png");
      else if (category==2) return require("@/assets/image/clothes.png");
      else return require("@/assets/image/cosmetics.png");
    },
    moveDetail(idx) { // 게시글 상세보기
      // this.$router.push({path:"/article_detail/"+this.searchPost[idx].postId.postId});
      this.$router.push({name: "ArticleDetail", params: {postId: this.feedDatas[idx].postId}});
    },
    isLike(idx) { // 좋아요 눌렀는지 확인
      if (this.postLikeList.includes(this.feedDatas[idx].postId)) return true;
      else return false;
    }, 
    like(idx) { // 좋아요 버튼 눌렀슴
      var payload = {
        "userId" : this.userinfo.userid,
        "postId" : this.feedDatas[idx].postId,
        "type": 1 // 뉴스피드
      }
      this.$store.dispatch("post/postLike", payload);
    },
    calTime (time) {
      return moment(time).fromNow()
    },
    infiniteHandler($state) {
      
      var params = new URLSearchParams();
      params.append("size", this.pageSize);
      params.append("page", this.pageNumber);
      params.append("userId", this.userinfo.userid);
      console.log(params)
      axios.get(
          API.url + feedAPI.feedmain(),
          {params})
      .then(res => {
        console.log(res.data)
            if (res.data.content.length > 0) {
                this.feedDatas = this.feedDatas.concat(res.data.content);
                $state.loaded();
                this.pageNumber++;
              } else {
                $state.complete();
              }
      })
      .catch(err => {
          console.log(err)
          alert('에러');
          
      })
    }
  },
}
</script>

<style>

</style>