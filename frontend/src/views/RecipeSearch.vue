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
        <input type="text" placeholder="검색" v-model="query" class="searchBar" @input="onSearchInput" @keyup.enter="onSearchBtnClick">
        <!-- 검색 아이콘은 아이패드 프로, 갤럭시 폴드에서 위치 깨짐, absolute라 어쩔 수 없나... -->
        <v-btn icon absolute right @click="onSearchBtnClick"><v-icon color="white" dark>mdi-magnify</v-icon></v-btn>        
      </div>

      <!-- 레시피 목록 -->
      <div v-for="(recipe, idx) in recipes" :key="idx" class="oneRecipe">
        <!-- 검색 조건이랑 맞으면 -->
        <div v-if="containmentValid(recipe)">
          <ProfileName :user="recipe.userid"></ProfileName>
          <img :src="recipe.filepath" alt="" @click="onImgClick(recipe)" class="recipePic">
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
    searchClicked: false,
  }),
  methods: {
    ...mapActions('recipe', ['fetchRecipes', 'fetchRecipeLikeList', 'fetchRecipeDetail', 'fetchRecipeComments', 'likeRecipe', 'sortRecipes',]),
    
    onImgClick (recipe) {
      this.$router.push({name: 'RecipeDetail', params: {recipeId: recipe.recipeId}})
    },
    onHeartIconClick (recipe) {
      const data = {
        userId: this.userinfo.userid,
        recipeId: recipe.recipeId,
      }
      this.likeRecipe(data)
    },
    calTime (recipe) {
      return moment(recipe.time).fromNow()
    },
    onSearchInput () {
      this.searchClicked = false
    },
    onSearchBtnClick () {
      this.searchClicked = true
    },  
    containmentValid (recipe) {
      return this.searchClicked ? recipe.title.includes(this.query) || recipe.userid.name.includes(this.query) || recipe.content.includes(this.query) : true
    }
    
  },
  computed: {
    ...mapGetters('recipe', ['recipes', 'recipeLikeList', ]),
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