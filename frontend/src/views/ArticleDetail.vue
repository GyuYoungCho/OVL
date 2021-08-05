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
            <div v-if="isPostLike()" class="inline" @click="postLike">
              <v-icon style="color:#20683D">mdi-heart</v-icon> &nbsp;
            </div>
            <div v-else class="inline" @click="postLike">
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
        <!-- 입력 폼 -->
        <div>
          <v-icon>mdi-subdirectory-arrow-right</v-icon>
          <input type="text" class="pa-1" style="width:80%; font-size:small" :placeholder="holder" v-model="commentInput">
          <button @click="regist"><span style="color:#004627">게시</span></button>
          <hr class="mt-1">
        </div>
        
        <!-- 댓글 가져오기 -->
        <div v-for="(info, idx) in commentList" :key="idx" class="my-1 ">
          <!-- 프로필 사진, 유저 닉네임, 댓글 내용, 하트 -->
          <v-row>
            <v-col cols="10" md="1">
              <img :src="commentUserPath(info)" width=10% style="border-radius: 50%;">
              {{info.postId.userId.nickname}} &nbsp;
              {{info.content}}
            </v-col>
            <v-col cols="2" md="1" style="text-align:right">
              <!-- 하트 확인-->
              <div v-if="isCommentLike(info.postCommentId)" class="inline" @click="commentLike(info.postCommentId)">
                <v-icon style="color:#20683D; font-size:large">mdi-heart</v-icon> &nbsp;
              </div>
              <div v-else class="inline" @click="commentLike(info.postCommentId)">
                <v-icon style="color:#BABABA; font-size:large">mdi-heart-outline</v-icon> &nbsp;
              </div>
            </v-col>
          </v-row>
          
          <!-- 좋아요, 답글 달기 -->
          <div class="ml-5 color-gray smallFont"> 
            좋아요 {{info.like_count}}개 &nbsp;
            <button @click="replyRegist(info)">답글 달기</button>
          </div>

          <!-- 답글 수, 클릭 시 답글 보기 -->
          <div v-if="info.reply_count>0" class="ml-5 color-gray smallFont" @click="replyClick(info)">
            -- {{replyReturn(info)}}
          </div>

          <!-- 답글 가져오기 -->
          <div v-if="isShowReply(info.postCommentId)" class="ml-5">
            <div v-for="(replyInfo, replyIdx) in replyList" :key="replyIdx">
              <!-- 게시글의 모든 답글 중 현재 댓글에 해당하는 답글만 출력해야함 -->
              <div v-if="replyInfo.postCommentId.postCommentId==info.postCommentId">
                <!-- 답글 프로필, 유저 닉네임, 답글 내용, 하트 -->
                <v-row>
                  <v-col cols="10" md="1">
                    <img :src="replyUserPath(replyInfo)" width=10% style="border-radius: 50%;">
                    {{replyInfo.postCommentId.postId.userId.nickname}} &nbsp;
                    {{replyInfo.content}}
                  </v-col>
                  <v-col cols="2" md="1" style="text-align:right">
                    <!-- 하트 확인-->
                    <div v-if="isReplyLike(replyInfo.postReplyId)" class="inline" @click="replyLike(replyInfo.postReplyId)">
                      <v-icon style="color:#20683D; font-size:large">mdi-heart</v-icon> &nbsp;
                    </div>
                    <div v-else class="inline" @click="replyLike(replyInfo.postReplyId)">
                      <v-icon style="color:#BABABA; font-size:large">mdi-heart-outline</v-icon> &nbsp;
                    </div>
                  </v-col>
                </v-row>
                
                <!-- 답글 좋아요 표시 -->
                <div class="ml-6 color-gray smallFont"> 
                  좋아요 {{replyInfo.like_count}}개
                </div>

              </div>
            </div>
          </div>
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
      commentInput:"",
      holder:"댓글 입력..",
      isComment:true, // 댓글 입력? or 답글 입력?
      commentId:0,
      showReply:[], // 답글 보여줄 거?
    }
  },
  methods: {
    replyClick(info) {
      if (this.showReply.includes(info.postCommentId)) { // 답글 이제 숨겨주자!!
        // 답글 보여주는 리스트에서 해당 게시글 아이디 삭제 해주기
        var idx = this.showReply.indexOf(info.postCommentId);
        this.showReply.splice(idx, 1);
      } else this.showReply.push(info.postCommentId); // 답글 보여주자!!
    },
    replyReturn(info) { // 답글 N개 보기 vs 답글 숨기기
      if (this.showReply.includes(info.postCommentId)) return "답글 숨기기";
      else return "답글 "+info.reply_count+"개 보기";
    },
    isShowReply(postCommentId) { // 답글을 보여줘야 할지 안보여줘도 될지 확인
      if (this.showReply.includes(postCommentId)) return true;
      else return false;
    },  
    userPath() { // 프로필 사진 이미지 출력
      if (this.post.userId.stored_file_path==null) {
        return require("@/assets/image/defalutImg.jpg");
      } else {
        return "http://localhost:8080/post/"+this.post.userId.userid+"/"+this.post.userId.stored_file_path.split('/').reverse()[0];
      }
    },
    commentUserPath(info) { // 댓글 프로필 사진 이미지 출력
      if (this.post.userId.stored_file_path==null) {
        return require("@/assets/image/defalutImg.jpg");
      } else {
        return "http://localhost:8080/post/"+info.postId.userId.userid+"/"+info.postId.userId.stored_file_path.split('/').reverse()[0];
      }
    },
    replyUserPath(info) { // 답글 프로필 사진 이미지 출력
      if (this.post.userId.stored_file_path==null) {
        return require("@/assets/image/defalutImg.jpg");
      } else {
        return "http://localhost:8080/post/"+info.postCommentId.postId.userId.userid+"/"+info.postCommentId.postId.userId.stored_file_path.split('/').reverse()[0];
      }
    },
    photoPath(idx){ // 대표 이미지 출력
      return "http://localhost:8080/post/"+this.post.postId+"/"+this.postPhotoList[idx].filepath.split('/').reverse()[0];
    },
    isPostLike() { // 게시글 좋아요 눌렀는지 확인
      if (this.postLikeList.includes(this.post.postId)) return true;
      else return false;
    }, 
    isCommentLike(commentId) { // 댓글 좋아요 눌렀는지 확인
      if(this.commentLikeList.includes(commentId)) return true;
      else return false;
    },
    isReplyLike(replyId) {
      if(this.replyLikeList.includes(replyId)) return true;
      else return false;
    },
    postLike() { // 게시글 좋아요 버튼 눌렀슴
      var payload = {
        "userId" : this.userinfo.userid,
        "postId" : this.post.postId,
        "type" : 2 // 게시글 디테일
      }
      this.$store.dispatch("post/postLike", payload);
    },
    commentLike(commentId) { // 댓글 좋아요 버튼
      var payload = {
        "userId" : this.userinfo.userid,
        "commentId" : commentId,
        "postId" : this.post.postId,
      }
      this.$store.dispatch("postComment/commentLike", payload);
    },
    replyLike(replyId) {
      var payload = {
        "userId" : this.userinfo.userid,
        "replyId" : replyId,
        "postId" : this.post.postId,
      }
      this.$store.dispatch("postReply/replyLike", payload);
    },
    regist() { // 댓글 등록
      console.log("postId : "+this.post.postId);
      if (this.isComment) {
        let payload = {
          "userId" : this.userinfo.userid,
          "postId" : this.post.postId,
          "content" : this.commentInput,
        }
        this.commentInput = "";
        this.$store.dispatch("postComment/commentAdd", payload);
      } else { // 답글
        let payload = {
          "userId" : this.userinfo.userid,
          "commentId" : this.commentId,
          "content" : this.commentInput,
          "postId" : this.post.postId,
        }
        this.commentInput = "";
        this.$store.dispatch("postReply/replyAdd", payload);
        // 댓글 리스트 새로 가져오기 -> 답글 개수 업데이트 위함
        this.$store.dispatch("postComment/getCommentList", this.$route.params.postId); 
      }
      this.$store.dispatch("post/getPost", this.$route.params.postId);
    }, 
    replyRegist(info) { // 답글 달기 버튼 클릭 시
      this.holder = info.postId.userId.nickname+"님에게 답글 달기..";
      this.isComment = false;
      this.commentId = info.postCommentId;
      this.showReply.push(info.postCommentId);
    }
  },
  computed: {
    ...mapState("post", (["postLikeList", "post", "postPhotoList"])),
    ...mapState("user", (["userinfo"])),
    ...mapState("postComment", (["commentList", "commentLikeList"])),
    ...mapState("postReply", (["replyList", "replyLikeList"]))
  },
  created() {
    this.isComment = true;
    // 게시글 가져오기
    this.$store.dispatch("post/getPost", this.$route.params.postId); 
    // 댓글 가져오기
    this.$store.dispatch("postComment/getCommentList", this.$route.params.postId); 
    // 댓글 좋아요 리스트 가져오기
    let payload = {
      "userId" : this.userinfo.userid,
      "postId" : this.$route.params.postId,
    }
    this.$store.dispatch("postComment/getCommentLikeList", payload); 
    // 해당 게시글에 대한 모든 답글 가져오기
    this.$store.dispatch("postReply/getReplyList", this.$route.params.postId); 
    // 답글 좋아요 리스트 가져오기
    this.$store.dispatch("postReply/getReplyLikeList", this.userinfo.userid);
  }
}
</script>

<style>

</style>