<template>
  <v-container>
    <section class="recipeSearch">
      <!-- 레시피 검색 부분 -->
      <div class="recipeSearchHeader">
        <select class="sortSelect" v-model="selectedOption" @change="sortRecipes(selectedOption)">
          <option value="new" selected>최신순</option>
          <option value="like">인기순</option>
          <option value="comment">댓글순</option>
        </select>
        <input type="text" placeholder="검색" v-model="query" class="searchBar">
      </div>


      <div v-for="(recipe, idx) in recipes" :key="idx" class="oneRecipe">
        
        <div v-if="containmentValid(recipe)">
          <ProfileName :user="recipe.userid"></ProfileName>
          <img :src="srcPath(recipe)" alt="" @click="onImgClick(recipe)" class="recipePic">
          <div class="oneRecipeContent">
            <span>{{ recipe.title }}</span>
            <span class="oneRecipeTime">{{ calTime(recipe) }}</span>
          </div>
          <div>
            <v-icon class="likedHeart" v-if="recipeLikeList.includes(recipe.recipeId)" @click="onHeartIconClick(recipe)">mdi-heart</v-icon>
            <v-icon class="unlikedHeart" v-else @click="onHeartIconClick(recipe)">mdi-heart-outline</v-icon>
            {{ recipe.like_count}}
            
            <v-icon class="chatIcon">mdi-chat-outline</v-icon>
            {{ recipe.comment_count }}
          </div>
        </div>
      </div>

    </section>

  </v-container>
</template>

<script>
import moment from 'moment'
import { mapActions, mapGetters } from 'vuex'
import ProfileName from '@/components/basic/ProfileName.vue'

export default {
  components: {
    ProfileName
  },
  data: () => ({
    query: "",
    selectedOption: "",
  }),
  methods: {
    ...mapActions(['fetchRecipes', 'fetchRecipeLikeList', 'fetchRecipeDetail', 'fetchRecipeComments', 'likeRecipe', 'sortRecipes',]),
    
    onImgClick (recipe) {
      this.fetchRecipeDetail(recipe.recipeId)
      this.fetchRecipeComments(recipe.recipeId)
      this.$router.push({ name: 'RecipeDetail' })
    },
    onHeartIconClick (recipe) {
      const data = {
        userId: this.userinfo.userid,
        recipeId: recipe.recipeId,
      }
      this.likeRecipe(data)
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
  computed: {
    ...mapGetters(['recipes', 'recipeLikeList', ]),
    ...mapGetters("user", (["userinfo"])),
  },

  created () {
    this.fetchRecipes()
    this.fetchRecipeLikeList(this.userinfo.userid)
  },
}
</script>

<style>

</style>