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
        <v-menu offset-y class="inline">
          <template v-slot:activator="{ on, attrs }" v-if="recipe.userid.userid===userinfo.userid">
            <v-btn icon v-bind="attrs" v-on="on">
              <v-icon>mdi-dots-horizontal</v-icon>
            </v-btn>
          </template>
          <v-list>
            <!-- 레시피 수정 -->
            <v-list-item @click="onRecipeUpdateClick">
              <v-list-item-title>수정</v-list-item-title>
            </v-list-item>
            <!-- 레시피 삭제 -->
            <v-list-item @click="onRecipeDeleteClick">
              <v-list-item-title>삭제</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
    </div>
    <div>
      {{ recipe.title}}
    </div>
    <!-- 대표 사진 -->
    <img :src="srcPath(recipe)" alt="" class="recipePic">
    
    <div class="recipeContent">
      {{ recipe.content }}
    </div>

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
            <!-- 댓글 작성 폼 -->
            <div class="commentInputForm" v-if="contentMode==='comment'">
              <input type="text" class="commentInput" placeholder="댓글 달기.." v-model="content" @keyup.enter="onRegistCommentBtnClick">
              <button class="commentCreateBtn" @click="onRegistCommentBtnClick">게시</button>
            </div>
            <!-- 답글 작성 폼 -->
            <div class="commentInputForm" v-if="contentMode==='reply'">
              <input type="text" class="commentInput" :placeholder="replyCommentUser + '님에게 답글 달기..'" v-model="content" @keyup.enter="onRegistReplyBtnClick">
              <button class="commentCreateBtn" @click="onRegistReplyBtnClick">게시</button>
            </div>
            <!-- 댓글 수정 폼 -->
            <div class="commentInputForm" v-if="contentMode==='commentModify'">
              <input type="text" class="commentInput" v-model="content" @keyup.enter="onModifyCommentBtnClick">
              <button class="commentCreateBtn" @click="onModifyCommentBtnClick">수정</button>
            </div>
            <!-- 답글 수정 폼 -->
            <div class="commentInputForm" v-if="contentMode==='replyModify'">
              <input type="text" class="commentInput" v-model="content" @keyup.enter="onModifyReplyBtnClick">
              <button class="commentCreateBtn" @click="onModifyReplyBtnClick">수정</button>
            </div>
            <hr> 
          </div>
        </div>
        <div v-for="(recipeComment, idx) in recipeComments" :key="idx" class="oneComment">
          <!-- 댓글 한 개 -->
          <div class="commentUserAndContent">
            <ProfileName :user="recipeComment.userId"></ProfileName>
            <p class="commentContent">{{ recipeComment.content }}</p>
            <v-icon class="likedHeart" v-if="recipeCommentLikeList.includes(recipeComment.recipeCommentId)" @click="onCommentLikeBtnClick(recipeComment)">mdi-heart</v-icon>
            <v-icon class="unlikedHeart" v-else @click="onCommentLikeBtnClick(recipeComment)">mdi-heart-outline</v-icon>
          </div>
          <div class="infoBelowOneComment">
            <div class="infoFirstLine">
              <span class="oneInfo">좋아요{{ recipeComment.like_count }}개</span>
              <span class="oneInfo" @click="onReplyClick(recipeComment)">{{ replyCommentId!==recipeComment.recipeCommentId ? '답글달기' : '답글취소' }}</span>
              <div class="oneInfo">
                <!-- 댓글 수정 -->
                <span v-if="recipeComment.userId.userid===userinfo.userid" @click="onModifyCommentClick(recipeComment)">수정</span> |
                <!-- 댓글 삭제 -->
                <span v-if="recipeComment.userId.userid===userinfo.userid" @click="onDeleteCommentClick(recipeComment.recipeCommentId)">삭제</span>
              </div>
            </div>
            <!-- 답글 -->
            <div class="infoSecondLine">
              <button @click="onShowReplyBtnClick(idx)" v-if="!!recipeComment.reply_count">
                <!-- 답글 펼치고 접는 버튼 -->
                <div v-if="!replyShowIdx.includes(idx)">
                  --답글 {{ recipeComment.reply_count }}개 보기
                </div>
                <div v-else>
                  --답글 접기
                </div>
              </button>
            </div>
          </div>
          <!-- 답글 펼치기 했을 때 보이는 부분 -->
          <div v-show="replyShowIdx.includes(idx)">
            <div v-for="(reply, index) in recipeCommentReply[recipeComment.recipeCommentId]" :key="index" class="oneReply">
              <div class="oneReplyContent">
                <ProfileName :user="reply.userId"></ProfileName>
                <p class="replyContent">{{ reply.content }}</p>
                <v-icon class="likedHeart" @click="onReplyLikeClick(reply)" v-if="recipeReplyLikeList.includes(reply.recipeReplyId)">mdi-heart</v-icon>
                <v-icon class="unlikedHeart" @click="onReplyLikeClick(reply)" v-else>mdi-heart-outline</v-icon>
              </div>
              <div class="infoBelowOneReply">
                <span class="oneInfo">좋아요{{ reply.like_count }}개</span>
                <div class="oneInfo">
                  <!-- 답글 수정 -->
                  <span v-if="reply.userId.userid===userinfo.userid" @click="onModifyReplyClick(reply)">수정</span> |
                  <!-- 답글 삭제 -->
                  <span v-if="reply.userId.userid===userinfo.userid" @click="onDeleteReplyClick(reply)">삭제</span>

                </div>
              </div>
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
    tab: "ingredient",
    commentInputBtn: "",
    content: "",
    contentMode: "comment",
    replyCommentId: "",
    replyCommentUser: "",
    replyShowIdx: [],

    modifyCommentId: -1,
    modifyReplyId: -1,
  }),
  methods: {
    ...mapActions(['registComment', 'likeRecipe', 'deleteRecipe', 'fetchRecipeCommentLikeList', 'likeRecipeComment', 'modifyRecipeComment', 'deleteRecipeComment', 
    'registRecipeCommentReply', 'likeRecipeCommentReply', 'fetchRecipeReplyLikeList', 'modifyRecipeReply', 'deleteRecipeReply',]),
    changeLine (content) {
      return content.replace(/(?:\r\n|\r|\n)/g, '<br />');
    },
    onRecipeLikeBtnClick () {
      const data = {
        userId: this.userinfo.userid,
        recipeId: this.recipe.recipeId,
      }
      this.likeRecipe(data)
    },
    onRecipeUpdateClick () {
      if (confirm('레시피를 수정하시겠습니까?')) {
        this.$router.push({ name: 'RecipeUpdate' })
      }
    },
    onRecipeDeleteClick () {
      if (confirm('레시피를 정말 삭제하시겠습니까?')) {
        this.deleteRecipe(this.recipe.recipeId)
        this.$router.push({ name: 'RecipeSearch' })
      }
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
      if (this.contentMode!=="reply") {
        this.contentMode = "reply"
        this.replyCommentUser = recipeComment.userId.nickname
        this.replyCommentId = recipeComment.recipeCommentId
      } else {
        this.contentMode = "comment"
        this.replyCommentUser = ""
        this.replyCommentId = ""
      }
      this.content = ""
    },
    onModifyCommentClick (recipeComment) {
      if (this.contentMode!=="commentModify") {
        this.contentMode = "commentModify"
        this.content = recipeComment.content
        this.modifyCommentId = recipeComment.recipeCommentId
      } else {
        this.contentMode = "comment"
        this.content = ""
        this.modifyCommentId = -1
      }
    },
    onModifyCommentBtnClick () {
      const data = {
        params: {
          comment_id: this.modifyCommentId,
          content: this.content,
        }
      }
      const recipeId = this.recipe.recipeId
      this.modifyRecipeComment({ data, recipeId })
      this.contentMode = "comment"
      this.content = ""
      this.modifyCommentId = -1
    },
    onDeleteCommentClick (recipeCommentId) {
      const data = {
        recipeId: this.recipe.recipeId,
        recipeCommentId,
      }
      this.deleteRecipeComment(data)
    },
    onReplyLikeClick(reply) {
      const data = {
        recipeCommentId: reply.recipeCommentId.recipeCommentId,
        userId: this.userinfo.userid,
        recipeReplyId: reply.recipeReplyId
      }
      this.likeRecipeCommentReply(data)
    },
    onModifyReplyClick(reply) {
      if (this.contentMode!=="replyModify") {
        this.contentMode = "replyModify"
        this.content = reply.content
        this.modifyReplyId = reply.recipeReplyId
        this.modifyCommentId = reply.recipeCommentId.recipeCommentId
      } else {
        this.contentMode = "comment"
        this.content = ""
        this.modifyReplyId = -1
        this.modifyCommentId = -1
      }
    },
    onModifyReplyBtnClick() {
      const data = {
        params: {
          content: this.content,
          reply_id: this.modifyReplyId
        }
      }
      const recipeCommentId = this.modifyCommentId
      this.modifyRecipeReply({ data, recipeCommentId })
      this.contentMode = "comment"
      this.content = ""
      this.modifyReplyId = -1
      this.modifyCommentId = -1
    },
    onDeleteReplyClick (reply) {
      const data ={
        recipeCommentId: reply.recipeCommentId.recipeCommentId,
        recipeReplyId: reply.recipeReplyId,
        recipeId: this.recipe.recipeId,
      }
      this.deleteRecipeReply(data)
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
      this.registRecipeCommentReply(payload)
      this.content = ""
    },
    onShowReplyBtnClick(idx) {
      const replyShowIdx = this.replyShowIdx
      if (replyShowIdx.includes(idx)) {
        replyShowIdx.splice(replyShowIdx.indexOf(idx), 1)
      } else {
        replyShowIdx.push(idx)
      }
      this.replyShowIdx = replyShowIdx
    },
  },
  computed: {
    ...mapGetters(['recipe', 'recipeDetail', 'recipeComments', 'recipeLikeList', 
    'recipeCommentLikeList', 'recipeCommentReply', 'recipeReplyLikeList',]),
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
    this.fetchRecipeReplyLikeList(this.userinfo.userid)
  }
}
</script>

<style>

</style>