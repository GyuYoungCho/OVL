<template>
  <div>
    <h1>레시피들이 보여질 공간</h1>
    <!-- 레시피 검색 부분 -->
    <input type="text" placeholder="검색" v-model="query">

    <div v-for="(recipe, idx) in recipes" :key="idx">
      <div v-if="containmentValid(recipe)">
        ------------
        <p>제목 : {{ recipe.title }}</p>
        <p>작성자 : {{ recipe.userid.name }}</p>
        <img :src="srcPath(recipe)" alt="" @click="onImgClick(recipe)">
        <p>내용 : {{ recipe.content }}</p>
        <p>시간 : {{ calTime(recipe) }}</p>
        
        -------------

      </div>
      
    </div>

  </div>
</template>

<script>
import axios from 'axios'
import API from '@/api/index.js'
import recipeAPI from '@/api/recipe.js'
import moment from 'moment'
import { mapActions } from 'vuex'

export default {
  data: () => ({
    recipes: [],
    query: "",
  }),
  methods: {
    ...mapActions(['fetchRecipeDetail', 'fetchRecipeComments',]),
    fetchRecipes () {
      const URL = API.url + recipeAPI.select_all()
      axios.get(URL)
        .then(res => {
          const data = res.data.sort(function(recipe1, recipe2) {
            const time1 = new Date(recipe1.time)
            const time2 = new Date(recipe2.time)
            return time2 - time1
          })
          this.recipes = data
        })
        .catch(err => console.error(err))
    },
    onImgClick (recipe) {
      this.fetchRecipeDetail(recipe.recipeId)
      this.fetchRecipeComments(recipe.recipeId)
      this.$router.push({ name: 'RecipeDetail' })
    },
    srcPath(recipe){
      return "http://localhost:8080/recipe/" + recipe.recipeId+ "/" + recipe.stored_file_path.split('/').reverse()[0];
    },
    calTime (recipe) {
      return moment(recipe.time).fromNow()
    },
    containmentValid (recipe) {
      return recipe.title.includes(this.query) || recipe.userid.name.includes(this.query) || recipe.content.includes(this.query)
    }
    
  },

  created () {
    this.fetchRecipes()
  },
}
</script>

<style>

</style>