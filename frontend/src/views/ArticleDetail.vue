<template>
  <div>
    <v-container class="px-7 mt-4">
      <!-- 게시글 시작 -->
      <div>
        <!-- post header - 프로필 사진, 유저 닉네임, 좋아요, 댓글 -->
        <v-row>

          <v-col cols="6" md="1">
            <div class="postProfile">
              <img :src="userPath()" width=17% style="border-radius: 50%;">
              <div class="ml-1 inline" style="font-size:large">{{post.userId.nickname}}</div>
            </div>
          </v-col>

          <v-col cols="6" md="1" style="text-align:right">
            <div v-if="isLike()" class="inline" @click="like">
              <v-icon style="color:#20683D">mdi-heart</v-icon> &nbsp;
            </div>
            <div v-else class="inline" @click="like">
              <v-icon style="color:#BABABA">mdi-heart-outline</v-icon> &nbsp;
            </div>
            <span class="color-gray inline">
              {{post.like_count}} &nbsp;
              <v-icon style="color:#BABABA">mdi-chat-outline</v-icon>
              {{post.comment_count}}
            </span>
          </v-col>

        </v-row>

        <!-- 사진 캐러셀 -->
        <v-carousel class="carouselBorder my-1" hide-delimiters height="30vh" style="border-radius: 8px;">
          <v-carousel-item
            v-for="(info,idx) in postPhotoList" :key="idx" :src="photoPath(idx)"
          ></v-carousel-item>
        </v-carousel>

        <!-- 내용 -->
        <div class="my-2">{{post.content}}</div>
      </div>
      <!-- 게시글 끝 -->

      <!-- 댓글 시작 -->
      <div class="comment pa-2">  
        <div>
          <v-icon>mdi-subdirectory-arrow-right</v-icon>
          <input type="text" style="width:80%;" placeholder="댓글 입력..">
          <button><span style="color:#004627">게시</span></button>
          <hr class="mt-1">

        </div>
        
      </div>
      <!-- 댓글 끝 -->
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
    userPath() { // 프로필 사진 이미지 출력
      if (this.post.userId.stored_file_path==null) {
        return require("@/assets/image/defalutImg.jpg");
      } else {
        return "http://localhost:8080/post/"+this.post.userId.userid+"/"+this.post.userId.stored_file_path.split('/').reverse()[0];
      }
    },
    photoPath(idx){ // 대표 이미지 출력
      return "http://localhost:8080/post/"+this.post.postId+"/"+this.postPhotoList[idx].filepath.split('/').reverse()[0];
    },
    isLike() { // 좋아요 눌렀는지 확인
      if (this.postLikeList.includes(this.post.postId)) return true;
      else return false;
    }, 
    like() { // 좋아요 버튼 눌렀슴
      var payload = {
        "userId" : this.userinfo.userid,
        "postId" : this.post.postId,
        "type" : 2 // 게시글 디테일
      }
      this.$store.dispatch("post/postLike", payload);
    }
  },
  computed: {
    ...mapState("post", (["postLikeList", "post", "postPhotoList"])),
    ...mapState("user", (["userinfo"])),
  },
  created() {
    this.$store.dispatch("post/getPost", this.$route.params.postId);
  }
}
</script>

<style>

</style>