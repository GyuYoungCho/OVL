<template>
  <div>
    <v-container class="px-7 mt-4">
      <!-- 게시글 답글 삭제 모달 -->
      <ArticleDetailModal :modalOpen="deleteModal" modalContent="정말로 삭제하시겠습니까?" :post="post"
      @modalConfirmBtnClick="onDeleteReplyModalClick" v-if="deletingReply" @modalCancelBtnClick="deleteModal = false" />
      <!-- 게시글 댓글 삭제 모달 -->
      <ArticleDetailModal :modalOpen="deleteModal" modalContent="정말로 삭제하시겠습니까?" :post="post"
      @modalConfirmBtnClick="onDeleteCommentModalClick" v-else-if="deletingComment" @modalCancelBtnClick="deleteModal = false" />
      <!-- 게시글 삭제 모달 -->
      <ArticleDetailModal :modalOpen="deleteModal" modalContent="정말로 삭제하시겠습니까?" :post="post"
      @modalConfirmBtnClick="onDeleteConfirmClick" v-else @modalCancelBtnClick="deleteModal = false" />
      

      <!-- 게시글 수정 모달 -->
      <ArticleDetailModal :modalOpen="updateModal" modalContent="정말로 수정하시겠습니까?" :post="post"
      @modalConfirmBtnClick="onUpdateConfirmClick" @modalCancelBtnClick="updateModal = false" />
      
      <!-- 게시글 시작 -->
      <div>
        <!-- post header - 프로필 사진, 유저 닉네임, 좋아요, 댓글 -->
        <v-row>
          <v-col cols="6" md="1">
            <ProfileName :user="post.userId"></ProfileName>
          </v-col>
          <!-- 게시글 좋아요, 댓글 -->
          <v-col cols="6" md="1" style="text-align:right">
            <div v-if="postLikeList.includes(post.postId)" class="inline" @click="postLike">
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

            <!-- 게시글 수정, 삭제 버튼 -->
            <v-menu offset-y class="inline" v-if="isUser()">
              <template v-slot:activator="{ on, attrs }">
                <v-btn icon v-bind="attrs" v-on="on">
                  &nbsp; <v-icon>mdi-dots-horizontal</v-icon>
                </v-btn>
              </template>
              <v-list>
                <!-- 게시글 수정 -->
                <v-list-item @click="onPostUpdateClick">
                  <v-list-item-title>수정</v-list-item-title>
                </v-list-item>
                <!-- 게시글 삭제 -->
                <v-list-item @click="onPostDeleteClick">
                  <v-list-item-title>삭제</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>
          </v-col>

        </v-row>

        <!-- 사진 캐러셀 -->
        <v-carousel class="carouselBorder my-1" hide-delimiters height="30vh" style="border-radius: 8px;">
          <v-carousel-item
            v-for="(info,idx) in postPhotoList" :key="idx" :src="postPhotoList[idx].filepath"
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
          <input type="text" class="pa-1" style="width:80%; font-size:small" :placeholder="holder" v-model="commentInput" @keyup.enter="regist">
          <button @click="regist"><span style="color:#004627">{{inputBtn}}</span></button>
          <hr class="mt-1">
        </div>
        
        <!-- 댓글 가져오기 -->
        <div v-for="(info, idx) in commentList" :key="idx" class="my-1 ">
          <!-- 댓글 프로필 사진, 유저 닉네임, 댓글 내용, 하트 -->
          <v-row>
            <v-col cols="10" md="1">
              
              <img :src="info.userId.filepath" width=10% style="border-radius: 50%;">
              {{info.userId.nickname}} &nbsp;
              {{info.content}}
            </v-col>
            <v-col cols="2" md="1" style="text-align:right">
              <!-- 댓글 하트 확인-->
              <div v-if="commentLikeList.includes(info.postCommentId)" class="inline" @click="commentLike(info.postCommentId)">
                <v-icon style="color:#20683D; font-size:large">mdi-heart</v-icon> &nbsp;
              </div>
              <div v-else class="inline" @click="commentLike(info.postCommentId)">
                <v-icon style="color:#BABABA; font-size:large">mdi-heart-outline</v-icon> &nbsp;
              </div>
            </v-col>
          </v-row>
          
          <!-- 댓글 좋아요, 답글 달기 -->
          <div class="ml-5 color-gray smallFont"> 
            좋아요 {{info.like_count}}개 &nbsp;
            
            <div @click="replyRegist(info)" class="inline">
              <div class="inline" v-if="replyBtnClickId==info.postCommentId">답글 취소</div> 
              <div class="inline" v-else>답글 달기</div>
              &nbsp;
            </div>
            <div class="inline" v-if="isCommentUser(info)">
              <button @click="commentModify(info)">수정</button> | 
              <button @click="onDeleteCommentClick(info.postCommentId)">삭제</button>
            </div>
          </div>

          <!-- 답글 수, 클릭 시 답글 보기 -->
          <div v-if="info.reply_count>0" class="ml-5 color-gray smallFont" @click="replyClick(info)">
            -- {{replyReturn(info)}}
          </div>

          <!-- 답글 가져오기 -->
          <div v-if="showReply.includes(info.postCommentId)" class="ml-5">
            <div v-for="(replyInfo, replyIdx) in replyList" :key="replyIdx">
              <!-- 게시글의 모든 답글 중 현재 댓글에 해당하는 답글만 출력해야함 -->
              <div v-if="replyInfo.postCommentId.postCommentId==info.postCommentId">
                <!-- 답글 프로필, 유저 닉네임, 답글 내용, 하트 -->
                <v-row>
                  <v-col cols="10" md="1">
                    <img :src="replyInfo.postCommentId.userId.filepath" width=10% style="border-radius: 50%;">
                    {{replyInfo.userId.nickname}} &nbsp;
                    {{replyInfo.content}}
                  </v-col>
                  <v-col cols="2" md="1" style="text-align:right">
                    <!-- 답글 하트 확인-->
                    <div v-if="replyLikeList.includes(replyInfo.postReplyId)" class="inline" @click="replyLike(replyInfo.postReplyId)">
                      <v-icon style="color:#20683D; font-size:large">mdi-heart</v-icon> &nbsp;
                    </div>
                    <div v-else class="inline" @click="replyLike(replyInfo.postReplyId)">
                      <v-icon style="color:#BABABA; font-size:large">mdi-heart-outline</v-icon> &nbsp;
                    </div>
                  </v-col>
                </v-row>
                
                <!-- 답글 좋아요 표시 -->
                <div class="ml-6 color-gray smallFont"> 
                  좋아요 {{replyInfo.like_count}}개 &nbsp;
                  <div class="inline" v-if="isReplyUser(replyInfo)">
                    <button @click="replyModify(replyInfo)">수정</button> | 
                    <button @click="deleteModalPopup(replyInfo)">삭제</button>
                  </div>
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
import ProfileName from '@/components/basic/ProfileName.vue'
import ArticleDetailModal from '@/components/article/ArticleDetailModal.vue'
import {mapGetters, mapActions} from "vuex";
export default {
  components: {
    ProfileName, ArticleDetailModal,
  },
  data() {
    return {
      commentInput:"", // 댓글 입력 창
      holder:"댓글 입력..", // 댓글 입력 창 placeholder
      isComment:true, // 댓글 입력? or 답글 입력?
      commentId:0, // 댓글 아이디
      replyId:0, // 답글 아이디
      showReply:[], // 답글 보여줄 거?
      items: [
        { title: '수정'},
        { title: '삭제'},
      ],
      inputBtn:"게시", // 댓글 입력 창 게시 or 수정
      replyBtnClickId:0,

      // 모달 관련 변수
      deleteModal: false,
      deletingComment: false,
      deletingCommentId: 0,
      deletingReply: false,
      deletingReplyId: 0,

      updateModal: false,
    }
  },
  methods: {
    ...mapActions('post', ['getPost',]),
    ...mapActions('postComment', ['getCommentList', 'getCommentLikeList']),
    ...mapActions('postReply', ['getReplyList', 'getReplyLikeList',]),
    onPostUpdateClick () {
      this.updateModal = true
    },
    onPostDeleteClick () {
      this.deleteModal = true
    },
    commentModify(info) { // 댓글 수정 
      this.isComment = true;
      this.inputBtn = "수정";
      this.commentInput = info.content;
      this.commentId = info.postCommentId;
    },
    replyModify(replyInfo) { // 답글 수정
      this.isComment = false;
      this.inputBtn = "수정";
      this.commentInput = replyInfo.content;
      this.replyId = replyInfo.postReplyId;
    },
    deleteModalPopup(replyInfo) {
      this.deletingReply = true
      this.deleteModal = true
      this.deletingCommentId = replyInfo.postCommentId.postCommentId
      this.deletingReplyId = replyInfo.postReplyId
    },
    onDeleteCommentClick (postCommentId) {
      this.deletingComment = true
      this.deleteModal = true
      this.deletingCommentId = postCommentId
    },
    onDeleteCommentModalClick() { // 댓글 삭제
      let paylaod = {
        "commentId" : this.deletingCommentId,
        "postId" : this.post.postId
      }
      this.deletingComment = false
      this.deletingCommentId = 0
      this.deleteModal = false
      this.$store.dispatch("postComment/commentDelete", paylaod);
    },
    onDeleteReplyModalClick() { // 답글 삭제
      let paylaod = {
        "replyId" : this.deletingReplyId,
        "postId" : this.post.postId
      }
      this.deleteModal = false
      this.deletingReply = false
      this.deletingCommentId = 0
      this.deletingReplyId = 0
      this.$store.dispatch("postReply/replyDelete", paylaod);
    },
    isUser() { // 유저가 맞다면 수정, 삭제 버튼 생기기
      if (this.post.userId.userid == this.userinfo.userid) return true;
      else return false;
    },
    isCommentUser(info) { // 댓글 쓴 유저가 맞다면 수정, 삭제 버튼 생기기
      if (info.userId.userid == this.userinfo.userid) return true;
      else return false;
    },
    isReplyUser(replyInfo) { // 답글 쓴 유저가 맞다면 수정, 삭제 버튼 생기기
      if (replyInfo.userId.userid == this.userinfo.userid) return true;
      else return false;
    },
    onUpdateConfirmClick() { // 게시글 수정
      this.$router.push({path:"/article_create/"+this.post.postId});
    },
    onDeleteConfirmClick() { // 게시글 삭제
      var payload = {
        "userId" : this.userinfo.userid,
        "postId" : this.post.postId
      }
      var move = this.$store.dispatch("post/postDelete", payload);
      if (move) this.$router.push({path:"/"});
    },
    replyClick(info) { // 답글 보기 or 숨기기
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
    regist() { // 댓글, 답글 등록
      if (this.isComment) { // 댓글
        
        let registPayload = {
          "userId" : this.userinfo.userid,
          "postId" : this.post.postId,
          "content" : this.commentInput,
        };
        let modifyPayload = {
          "commentId" : this.commentId,
          "postId" : this.post.postId,
          "content" : this.commentInput,
        }
        this.commentInput = "";
        if (this.inputBtn=="게시") this.$store.dispatch("postComment/commentAdd", registPayload); // 댓글 등록일 때
        else {
          this.inputBtn = "게시";
          this.$store.dispatch("postComment/commentModify", modifyPayload); // 댓글 수정일 때
        }
      } else { // 답글
        let registpayload = {
          "userId" : this.userinfo.userid,
          "commentId" : this.commentId,
          "content" : this.commentInput,
          "postId" : this.post.postId,
        };
        let modifyPayload = {
          "replyId" : this.replyId,
          "postId" : this.post.postId,
          "content" : this.commentInput,
        }
        this.commentInput = "";
        
        if (this.inputBtn=="게시") this.$store.dispatch("postReply/replyAdd", registpayload);
        else {
          this.inputBtn = "게시";
          this.$store.dispatch("postReply/replyModify", modifyPayload);
        }
      }
    }, 
    replyRegist(info) { // 답글 달기 버튼 클릭 시
      if (this.isComment) { // 댓글 달기로 되어있을 때 답글 다는 걸로 변경
        this.holder = info.userId.nickname+"님에게 답글 달기..";
        this.replyBtnClickId = info.postCommentId;
        this.isComment = false;
        this.commentId = info.postCommentId;
        this.showReply.push(info.postCommentId);
      } else { // 답글 달기로 되어있을 때 댓글 다는 걸로 변경
        this.holder = "댓글 달기..";
        this.replyBtnClickId = 0;
        this.isComment = true;
        var idx = this.showReply.indexOf(this.commentId);
        this.showReply.splice(idx, 1);
      } 
      
    }
  },
  computed: {
    ...mapGetters("post", (["postLikeList", "post", "postPhotoList"])),
    ...mapGetters("user", (["userinfo"])),
    ...mapGetters("postComment", (["commentList", "commentLikeList"])),
    ...mapGetters("postReply", (["replyList", "replyLikeList"]))
  },
  created() {
    const postId = this.$route.params.postId
    const userId = this.userinfo.userid
    // 게시글 가져오기
    this.getPost(postId)
    // 댓글 가져오기
    this.getCommentList(postId)
    // 댓글 좋아요 리스트 가져오기
    this.getCommentLikeList({userId, postId})
    // 해당 게시글에 대한 모든 답글 가져오기
    this.getReplyList(postId)
    // 답글 좋아요 리스트 가져오기
    this.getReplyLikeList(userId)
  }
}
</script>

<style>

</style>