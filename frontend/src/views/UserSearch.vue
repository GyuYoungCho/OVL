<template>
  <v-container class="px-6 searchHeader borderRadius">
      <feed-search @searchKeyword="searchKeyword"
        @selectOrd="selectOrd"></feed-search>
      <v-card v-if="userlist && userlist.length!=0" click:outside="searchKeyword('')">
        <v-col class="mt-5 pl-3">
            <profile-name v-for="(suser, index) in userlist" :key="index" :user="suser"></profile-name>
        </v-col> 
      </v-card>

      <div v-if="search.length==0" class="noResult">
          <img src="@/assets/image/search.png" alt="">
          <div class="mt-3">닉네임, 게시글을 검색할 수 있습니다!</div>
      </div>
      <div v-else-if="(ord=='닉네임' &&  userlist.length==0) || (ord=='게시글' && postAll.length==0)" class="noResult">
          <img src="@/assets/image/noResult.png" alt="">
          <div class="mt-3">검색 결과가 존재하지 않습니다</div>
      </div>


      <div v-if="ordCheck">
        <div v-for="(info, idx) in postAll" :key="idx" class="mt-4">
            
            <div>
            <!-- post header - 프로필 사진, 유저 닉네임, 카테고리 -->
            <v-row>
                <v-col cols="6" md="1">
                <ProfileName :user="info.postId.userId"></ProfileName>
                </v-col>
                <v-col cols="6" md="1" class="mt-2" style="text-align:right">
                <img :src="iconPath(idx)" width=15%>
                </v-col>
            </v-row>

            <!-- post 대표 사진, 내용-->
            <div @click="moveDetail(idx)">
                <img :src="postAll[idx].filepath" width=100%  style="border-radius: 7px;" class="my-1">
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

    
      <infinite-loading @infinite="infiniteHandler" ref="infiniteLoading" 
      spinner="none">
        <div slot="spinner"></div>
        <div slot="no-more" class="mt-4">
            <!-- <div class="noResult">
                <img src="@/assets/image/noResult.png" alt="">
                <div class="mt-3">불러올 목록이 없어요</div>
            </div> -->
          </div>
          <div slot="no-results" class="mt-4">
            <!-- <div v-if="postAll.length==0 && search.length>0" class="noResult">
                <img src="@/assets/image/noResult.png" alt="">
                <div class="mt-3">검색 결과가 존재하지 않습니다</div>
            </div> -->
          </div>
        </infinite-loading>
   

  </v-container>
</template>

<script>
import FeedSearch from '@/components/basic/FeedSearch.vue';
import ProfileName from '@/components/basic/ProfileName.vue'
import InfiniteLoading from 'vue-infinite-loading';
import { mapActions, mapGetters } from 'vuex'
import moment from 'moment'
import axios from "axios";
import API from "@/api/index.js";
import feedAPI from '@/api/feed.js'

export default {
  components: {
    FeedSearch,
    ProfileName,
    InfiniteLoading,
  },
  data () {
    return {
        search : '',
        ord : '닉네임',
        order : [
            "닉네임", "게시글",
        ],
        pageNumber: 0,
        pageSize: 3,
    }
  },
  methods: {
    ...mapActions('post', ['resetPostAll', 'getPost', 'getPostAll', 'getPostLikeList',
                          'getRecommend','modifyPostAll', 'postLike']),
    ...mapActions('user', ['resetUserList','getUserList']),
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
      const category = this.postAll[idx].postId.category;
      if (category==1) return require("@/assets/image/meal.png");
      else if (category==2) return require("@/assets/image/clothes.png");
      else return require("@/assets/image/cosmetics.png");
    },
    moveDetail(idx) { // 게시글 상세보기
      // this.$router.push({path:"/article_detail/"+this.searchPost[idx].postId.postId});
      this.$router.push({name: "ArticleDetail", params: {postId: this.postAll[idx].postId.postId}});
    },
    isLike(idx) { // 좋아요 눌렀는지 확인
      if (this.postLikeList.includes(this.postAll[idx].postId.postId)) return true;
      else return false;
    }, 
    async like(idx) { // 좋아요 버튼 눌렀슴
      var payload = {
        "userId" : this.userinfo.userid,
        "postId" : this.postAll[idx].postId.postId,
        "type": 1, // 뉴스피드
        "idx" : idx
      }
      await this.postLike(payload);
      await this.getPostLikeList(this.userinfo.userid);
      await this.getPost(this.postAll[idx].postId.postId);
      await this.modifyPostAll(payload);
    },
    calTime (time) {
      return moment(time).fromNow()
    },
    infiniteHandler($state) {
      
        if(this.ord == '닉네임' && this.search!=''){
          this.pageSize = 10
          let params = new URLSearchParams();
          params.append("size", this.pageSize);
          params.append("page", this.pageNumber);
          params.append("keyword", this.search);
          axios.get(API.url + feedAPI.select_alluser(),{params})
            .then(res => {
              
              if (res.data.content.length > 0) {
                  this.getUserList(res.data.content)
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
        }else if(this.ord=='게시글' && this.search!=''){
          this.pageSize = 5
          let params = new URLSearchParams();
          params.append("size", this.pageSize);
          params.append("page", this.pageNumber);
          params.append("keyword", this.search);
          axios.get(API.url + feedAPI.postsearch(),{params})
            .then(res => {
              
              if (res.data.content.length > 0) {
                  this.getPostAll(res.data.content)
                  this.getPostLikeList(this.userinfo.userid)
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
      }
    
 },
 computed: {
    ...mapGetters("user", (["userinfo", "userlist"])),
    ...mapGetters("post", (["postAll", "postLikeList"])),
    
 },
 watch:{
    search(newVal,oldVal) {
      oldVal
      if(this.ord=="게시글") this.resetPostAll();
      else this.resetUserList();
      this.pageNumber = 0;
      if(newVal!='') this.$refs.infiniteLoading.stateChanger.reset();
    },
    ord : function() {
      this.resetUserList();
      this.resetPostAll();
      this.pageNumber = 0;
      // this.$refs.infiniteLoading.stateChanger.reset();
    },
  },
 created() {
    this.resetUserList()
    this.resetPostAll()
 }
}
</script>

<style>

</style>