<template>
  <div>
    <v-container class="px-7 mt-4">
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
              <div class="postProfile">
                <img :src="userPath(idx)" width=17% style="border-radius: 50%;">
                <div class="ml-1 inline" style="font-size:large">{{info.postId.userId.nickname}}</div>
              </div>
            </v-col>
            <v-col cols="6" md="1" style="text-align:right">
              <img :src="iconPath(idx)" width=15%>
            </v-col>
          </v-row>

          <!-- post 대표 사진, 내용-->
          <div @click="moveDetail(idx)">
             <img :src="postPath(idx)" width=100%  style="border-radius: 7px;" class="my-1">  
             {{info.postId.content}}
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
import {mapState} from "vuex";

export default {
  data() {
    return {

    }
  },
  methods: {
    postPath(idx){ // 대표 이미지 출력
      return "http://localhost:8080/post/"+this.postList[idx].postId.postId+"/"+this.postList[idx].filepath.split('/').reverse()[0];
    },
    iconPath(idx) { // 카테고리 이미지 출력
      var category = this.postList[idx].postId.categori;
      if (category==1) return require("@/assets/image/meal.png");
      else if (category==2) return require("@/assets/image/clothes.png");
      else return require("@/assets/image/cosmetics.png");
    },
    userPath(idx) { // 프로필 사진 이미지 출력
      if (this.postList[idx].postId.userId.stored_file_path==null) {
        return require("@/assets/image/defalutImg.jpg");
      } else {
        return "http://localhost:8080/post/"+this.postList[idx].postId.userId.userid+"/"+this.postList[idx].postId.userId.stored_file_path.split('/').reverse()[0];
      }
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
    }
  },
  computed: {
    ...mapState("post", (["postList", "postLikeList"])),
    ...mapState("user", (["userinfo"])),
  },
  created() {
    this.$store.dispatch("post/getPostList", this.userinfo.userid);
    this.$store.dispatch("post/getPostLikeList", this.userinfo.userid);
  }
}
</script>

<style>

</style>