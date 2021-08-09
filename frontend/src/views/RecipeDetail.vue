<template>
<v-container>
  <section class="recipeDetail">
    <!-- 레시피 작성자 정보 -->
    <div class="header">
      <ProfileName :user="recipe.userid"></ProfileName>
      <div>
        <!-- 좋아요, 댓글수 -->
        <v-icon class="likedHeart" v-if="recipeLikeList.includes(recipe.recipeId)" @click="onRecipeLikeBtnClick">mdi-heart</v-icon>
        <v-icon class="unlikedHeart" v-else @click="onRecipeLikeBtnClick">mdi-heart-outline</v-icon>
        {{ recipe.like_count }}
        <v-icon class="chatIcon">mdi-chat-outline</v-icon>
        {{ recipe.comment_count }}
      </div>
    </div>
    <div>
      {{ recipe.title}}
    </div>
    <!-- 대표 사진 -->
    <img :src="srcPath(recipe)" alt="" class="recipePic">

    <!-- 탭 부분 -->
    <div class="recipeTab">
      <button :class="{'recipeTabBtn': tab==='ingredient', 'unselectedBtn': tab!=='ingredient'}" @click="onRecipeTagBtnClick('ingredient')">재료</button>
      <button :class="{'recipeTabBtn': tab==='process', 'unselectedBtn': tab!=='process'}" @click="onRecipeTagBtnClick('process')">과정</button>
      <button :class="{'recipeTabBtn': tab==='comment', 'unselectedBtn': tab!=='comment'}" @click="onRecipeTagBtnClick('comment')">댓글</button>
    </div>
    <div class="recipeTabContent">
      <!-- 재료 탭 -->
      <div class="recipeIngredient" v-if="tab==='ingredient'">
        <div
          v-html="changeLine(recipe.ingredient)"
        >
        </div>
      </div>

      <!-- 과정 탭 -->
      <div class="recipeProcess" v-if="tab==='process'">
        <div v-for="(process, idx) in recipeDetail" :key="idx">
          <hr v-if="idx!==0">  
          <div class="oneProcess">
            <img :src="processSrcPath(recipe, process)" alt="" class="processPic">
            <p class="processContent" v-html="changeLine(process.content)"></p>
          </div>
        </div>
      </div>

      <!-- 댓글 탭 -->
      <div class="recipeComment" v-if="tab==='comment'">
        <div class="commentRegisterForm">
          <v-icon color="black">mdi-subdirectory-arrow-right</v-icon>
          <div class="lineContainer">
            <div class="commentInputForm" v-if="commentMode">
              <input type="text" class="commentInput" placeholder="댓글 입력" v-model="content">
              <button class="commentCreateBtn" @click="onRegistCommentBtnClick">게시</button>
            </div>
            <div class="commentInputForm" v-else>
              <input type="text" class="commentInput" placeholder="답글 입력" v-model="content">
              <button class="commentCreateBtn" @click="onRegistReplyBtnClick">답글 게시</button>
            </div>
            <hr> 
          </div>
        </div>
        <div v-for="(recipeComment, idx) in recipeComments" :key="idx" class="oneComment">
          <div class="commentUserAndContent">
            <ProfileName :user="recipeComment.userId"></ProfileName>
            <p class="commentContent">{{ recipeComment.content }}</p>
            <v-icon class="likedHeart" v-if="recipeCommentLikeList.includes(recipeComment.recipeCommentId)" @click="onCommentLikeBtnClick(recipeComment)">mdi-heart</v-icon>
            <v-icon class="unlikedHeart" v-else @click="onCommentLikeBtnClick(recipeComment)">mdi-heart-outline</v-icon>
          </div>
          <div class="infoBelowOneComment">
            <div>
              <span class="oneInfo">좋아요{{ recipeComment.like_count }}개</span>
              <span class="oneInfo" @click="onReplyClick(recipeComment)">답글달기</span>
              <span class="oneInfo" v-if="recipeComment.userId.userid===userinfo.userid">수정</span>
              <span class="oneInfo" v-if="recipeComment.userId.userid===userinfo.userid" @click="onDeleteCommentClick(recipeComment.recipeCommentId)">삭제</span>
            </div>
            <!-- 답글 -->
            <div>
              <button @click="onShowReplyBtnClick(idx)">
                --답글 {{ recipeComment.reply_count }}개 보기
              </button>
            </div>
          </div>
          <div v-show="replyShowIdx[idx]">
            <div v-for="(reply, index) in recipeCommentReply[recipeComment.recipeCommentId]" :key="index" class="oneReply">
              <ProfileName :user="reply.userId"></ProfileName>
              <p class="replyContent">{{ reply.content }}</p>
            </div>
          </div>
          
        </div>
        
      </div>

    </div>

  </section>

</v-container>
</template>

<script>
import ProfileName from '@/components/basic/ProfileName.vue'
import { mapActions, mapGetters } from 'vuex'

export default {
  components: {
    ProfileName,
  },
  data: () => ({
    content: "",
    tab: "ingredient",
    commentInputBtn: "",
    commentMode: true,
    replyCommentId: "",
    replyShowIdx: {},
  }),
  methods: {
    ...mapActions(['registComment', 'likeRecipe', 'fetchRecipeCommentLikeList', 'likeRecipeComment', 'deleteRecipeComment', 'registCommentReply',]),
    changeLine (c) {
      return c.replace(/(?:\r\n|\r|\n)/g, '<br />');
    },
    onRecipeLikeBtnClick () {
      const data = {
        userId: this.userinfo.userid,
        recipeId: this.recipe.recipeId,
      }
      this.likeRecipe(data)
    },
    onRecipeTagBtnClick (option) {
      this.tab = option
    },
    onCommentLikeBtnClick (recipeComment) {
      const data = {
        userId: this.userinfo.userid,
        recipeId: this.recipe.recipeId,
        recipeCommentId: recipeComment.recipeCommentId
      }
      this.likeRecipeComment(data)
    },
    onReplyClick (recipeComment) {
      this.commentMode = !this.commentMode
      this.replyCommentId = recipeComment.recipeCommentId
    },
    onDeleteCommentClick (recipeCommentId) {
      const data = {
        recipeId: this.recipe.recipeId,
        recipeCommentId,
      }
      this.deleteRecipeComment(data)
    },

    srcPath(recipe) {
      return "http://localhost:8080/recipe/" + recipe.recipeId+ "/" + recipe.stored_file_path.split('/').reverse()[0];
    },
    processSrcPath(recipe, process) {
      return "http://localhost:8080/recipe/" + recipe.recipeId + "/" + process.filepath.split('/').reverse()[0];
    },
    onRegistCommentBtnClick () {
      this.registComment(this.comment)
      this.content = ""
    },
    onRegistReplyBtnClick () {
      console.log(this.replyCommentId)
      const data = {
        params:{
          "content": this.content,
          "commentId": this.replyCommentId,
          "userId": this.userinfo.userid,
        }
      }
      const payload = {
        data,
        recipeId: this.recipe.recipeId
      }
      this.registCommentReply(payload)
      this.content = ""
    },
    onShowReplyBtnClick(idx) {
      this.replyShowIdx[idx] = !this.replyShowIdx[idx]
      this.replyShowIdx[idx] = !!this.replyShowIdx[idx]
    },
    isShowingReply (idx) {
      return this.replyShowIdx[idx]
    }
    
  },
  computed: {
    ...mapGetters(['recipe', 'recipeDetail', 'recipeComments', 'recipeLikeList', 'recipeCommentLikeList', 'recipeCommentReply',]),
    ...mapGetters("user", ["userinfo",]),
    comment () {
      const data = {
        params: {
          "content": this.content,
          "recipeId": this.recipe.recipeId,
          "userId": this.userinfo.userid,
        }
      }
      return data
    }
  },
  created () {
    this.fetchRecipeCommentLikeList(this.userinfo.userid)
  }
}
</script>

<style>

</style>