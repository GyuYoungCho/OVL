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
        {{ recipe.ingredient }}
      </div>

      <!-- 과정 탭 -->
      <div class="recipeProcess" v-if="tab==='process'">
        <div v-for="(process, idx) in recipeDetail" :key="idx">
          <hr v-if="idx!==0">  
          <div class="oneProcess">
            <img :src="processSrcPath(recipe, process)" alt="" class="processPic">
            <p class="processContent">{{ process.content }}</p>
          </div>
        </div>
      </div>

      <!-- 댓글 탭 -->
      <div class="recipeComment" v-if="tab==='comment'">
        <div class="commentRegisterForm">
          <v-icon size="10vw" color="black">mdi-subdirectory-arrow-right</v-icon>
          <div class="commentInputForm">
            <input type="text" class="commentInput" placeholder="댓글 입력">
            <button>ad</button>
          </div>
        </div>
        {{ recipeComments }}
        
      </div>

    </div>


    <h2>댓글</h2>
    <p>{{ recipeComments }}</p>
    <input v-model="content" type="text" placeholder="댓글달기">
    <button @click="onRegistCommentBtnClick">댓글 등록</button>

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
  }),
  methods: {
    ...mapActions(['registComment', 'likeRecipe',]),
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

    srcPath(recipe) {
      return "http://localhost:8080/recipe/" + recipe.recipeId+ "/" + recipe.stored_file_path.split('/').reverse()[0];
    },
    processSrcPath(recipe, process) {
      return "http://localhost:8080/recipe/" + recipe.recipeId + "/" + process.filepath.split('/').reverse()[0];
    },
    onRegistCommentBtnClick () {
      this.registComment(this.comment)
      this.content = ""
    }
    
  },
  computed: {
    ...mapGetters(['recipe', 'recipeDetail', 'recipeComments', 'recipeLikeList', ]),
    ...mapGetters("user", ["userinfo",]),
    comment () {
      const data = {
        content: this.content,
        recipeId: this.recipe.recipeId,
        userId: this.userinfo.userid,
      }
      return data
    }
  }
}
</script>

<style>

</style>