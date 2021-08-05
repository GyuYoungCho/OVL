<template>
  <div>
    <h1>Recipe Detail 페이지입니다.</h1>
    <h2>대표사진</h2>
    <img :src="srcPath(recipe)" alt="">
    <p>title : {{ recipe.title }}</p>
    <p>author : {{ recipe.userid.nickname }}</p>

    <h2>재료</h2>
    <p>{{ recipe.ingredient }}</p>

    <h2>과정</h2>
    <div v-for="(process, idx) in recipeDetail" :key="idx">
      <img :src="processSrcPath(recipe, process)" alt="">
      <p>내용 : {{ process.content }}</p>
      
    </div>

    <h2>댓글</h2>
    <p>{{ recipeComments }}</p>
    <input v-model="content" type="text" placeholder="댓글달기">
    <button @click="onRegistCommentBtnClick">댓글 등록</button>

  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'

export default {
  data: () => ({
    content: "",
  }),
  methods: {
    ...mapActions(['registComment', ]),
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
    ...mapGetters(['recipe', 'recipeDetail', 'recipeComments',]),
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