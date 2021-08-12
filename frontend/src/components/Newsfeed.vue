<template>
  <div>
    
    <v-container class="px-6 mt-4">
      <feed-search @searchKeyword="searchKeyword"
        @selectOrd="selectOrd"></feed-search>
      <!-- 캐러셀 영역 -->
      <!-- <v-carousel hide-delimiters>
        <v-carousel-item
          v-for="(info,i) in adList"
          :key="i"
          :src="info.src"
        ></v-carousel-item>
      </v-carousel> -->

      <div v-for="(info, idx) in postList" :key="idx" class="mt-9">
        
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
            <img :src="postList[idx].filepath" width=100%  style="border-radius: 7px;" class="my-1">
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
import {mapState} from "vuex";
import moment from 'moment'


export default {
  data(){
    return{
      search : '',
      ord : '',
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
      var category = this.postList[idx].postId.category;
      if (category==1) return require("@/assets/image/meal.png");
      else if (category==2) return require("@/assets/image/clothes.png");
      else return require("@/assets/image/cosmetics.png");
    },
    moveDetail(idx) { // 게시글 상세보기
      this.$router.push({path:"/article_detail/"+this.postList[idx].postId.postId});
    },
    isLike(idx) { // 좋아요 눌렀는지 확인
      if (this.postLikeList.includes(this.postList[idx].postId.postId)) return true;
      else return false;
    }, 
    like(idx) { // 좋아요 버튼 눌렀슴
      var payload = {
        "userId" : this.userinfo.userid,
        "postId" : this.postList[idx].postId.postId,
        "type": 1 // 뉴스피드
      }
      this.$store.dispatch("post/postLike", payload);
    },
    calTime (time) {
      return moment(time).fromNow()
    },
  },
  computed: {
    ...mapState("post", (["postList", "postLikeList"])),
    ...mapState("user", (["userinfo","userlist"])),

    searchPost() {
      const search = this.search.toLowerCase()
      const allitems = this.postList
      
      if (!search) return allitems
      if(this.ord==this.order[1]){
        return allitems.filter(item => {
          const text = item.restaurant_name.toLowerCase()

          return text.indexOf(search) > -1
        })
      }else{
        return allitems.filter(item => {
          const text = item.userid.nickname.toLowerCase()

          return text.indexOf(search) > -1
        })
      }
    },
    searchUser(){
      const search = this.search.toLowerCase()
      if(this.ord=='Post' || search) return []
      const allitems = this.userlist
      return allitems.filter(item => {
        const text = item.restaurant_name.toLowerCase()

        return text.indexOf(search) > -1
      })
    },
  },
  created() {
    this.$store.dispatch("user/getUserList");
    this.$store.dispatch("post/getPostList", this.userinfo.userid);
    this.$store.dispatch("post/getPostLikeList", this.userinfo.userid);
  }
}
</script>

<style>

</style>