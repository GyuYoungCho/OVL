<template>
  <v-container>
    <section class="recipeSearch">
      <!-- 레시피 검색 부분 -->
      <RecipeSearchBar @searchKeyword="searchKeyword" @selectOrd="selectOrd" />
      
      <!-- 레시피 목록 -->
      <div v-for="(recipe, idx) in recipes" :key="idx" class="oneRecipe">
        <!-- 검색 조건이랑 맞으면 -->
        <!-- <div v-if="containmentValid(recipe)"> -->
        <div>  
          <ProfileName :user="recipe.userid"></ProfileName>
          <img :src="recipe.filepath" alt="" @click="onImgClick(recipe)" class="recipePic">
          <div class="oneRecipeContent">
            <span>{{ recipe.title }}</span>
            <span class="oneRecipeTime">{{ calTime(recipe) }}</span>
          </div>
          <div>
            <v-icon class="likedHeart" v-if="recipeLikeList.includes(recipe.recipeId)" @click="onHeartIconClick(recipe)">mdi-heart</v-icon>
            <v-icon class="unlikedHeart" v-else @click="onHeartIconClick(recipe)">mdi-heart-outline</v-icon>
            {{ recipe.likecount}}
            
            <v-icon class="chatIcon">mdi-chat-outline</v-icon>
            {{ recipe.commentcount }}
          </div>
        </div>
      </div>
      <infinite-loading @infinite="infiniteHandler" ref="infiniteLoading" spinner="circles">
        <!-- <div slot="spinner"></div> -->
        <div slot="no-more" class="mt-4">
            <v-sheet
              block
              class="pa-5 mx-auto d-flex align-center justify-center"
              rounded="xl"
              color="rgb(224,229,231)"
              style="max-width:680px;"
            >
              <div class="font-weight-medium d-flex flex-column">
                <v-icon large class="blue-grey--text text--lighten-3">mdi-close</v-icon>
                <h4 class="blue-grey--text text--lighten-3">끝</h4>
              </div>
            </v-sheet>
        </div>
        <div slot="no-results" class="mt-4">
            <v-sheet
              block
              class="pa-5 mx-auto d-flex align-center justify-center"
              rounded="xl"
              color="rgb(224,229,231)"
              style="max-width:680px;"
            >
              <div class="font-weight-medium d-flex flex-column">
                <v-icon large class="blue-grey--text text--lighten-3">mdi-close</v-icon>
                <h4 class="blue-grey--text text--lighten-3">불러올 글이 없어</h4>
              </div>
            </v-sheet>
        </div>
        </infinite-loading>

    </section>

  </v-container>
</template>

<script>
import axios from 'axios'
import API from '@/api/index.js'
import recipeAPI from '@/api/recipe.js'
import moment from 'moment'
import { mapActions, mapGetters } from 'vuex'
import ProfileName from '@/components/basic/ProfileName.vue'
import RecipeSearchBar from '@/components/recipe/RecipeSearchBar.vue'
import InfiniteLoading from 'vue-infinite-loading';

export default {
  components: {
    InfiniteLoading,
    ProfileName,
    RecipeSearchBar,
  },
  data: () => ({
    query: "",
    selectedOption: "time",
    searchClicked: false,
    pageNumber: 0,
    pageSize: 3,
  }),
  methods: {
    ...mapActions('recipe', ['fetchRecipes', 'fetchRecipeLikeList', 'fetchRecipeDetail', 'fetchRecipeComments', 
                        'likeRecipe', 'sortRecipes','resetRecipeList', 'modifyRecipeList']),
    
    searchKeyword (query) {
      this.query = query
    },

    selectOrd (option) {
      if (option==="최신순") {
        this.selectedOption = "time"
      } else if (option==="인기순") {
        this.selectedOption = "like_count"
      } else { // 댓글순
        this.selectedOption = "comment_count"
      }
    },


    onImgClick (recipe) {
      this.$router.push({name: 'RecipeDetail', params: {recipeId: recipe.recipeId}})
    },
    async onHeartIconClick (recipe) {
      const data = {
        userId: this.userinfo.userid,
        recipeId: recipe.recipeId,
      }
      await this.likeRecipe(data)
      await this.fetchRecipeLikeList(this.userinfo.userid);
      await this.fetchRecipeDetail(recipe.recipeId);
      await this.modifyPostList(data);
    },
    calTime (recipe) {
      return moment(recipe.time).fromNow()
    },
    // onSearchInput () {
    //   this.searchClicked = false
    // },
    // onSearchBtnClick () {
    //   this.searchClicked = true
    // },  
    // containmentValid (recipe) {
    //   return this.searchClicked ? recipe.title.includes(this.query) || recipe.userid.name.includes(this.query) || recipe.content.includes(this.query) : true
    // },
    infiniteHandler($state) {
      
      var params = new URLSearchParams();
      params.append("size", this.pageSize);
      params.append("page", this.pageNumber);
      params.append("cate", this.selectedOption);
      params.append("keyword", this.query);
      axios.get(API.url + recipeAPI.select_all(),{params})
        .then(res => {
          console.log(res.data.content)
          if (res.data.content.length > 0) {
              this.fetchRecipes(res.data.content)
              this.fetchRecipeLikeList(this.userinfo.userid)
              $state.loaded();
              this.pageNumber++;
          } else {
              $state.complete();
          }
      })
      .catch(err => {
          console.log(err)
          alert('에러');
      })
    }
  },
  computed: {
    ...mapGetters('recipe', ['recipes', 'recipeLikeList' ]),
    ...mapGetters("user", (["userinfo"])),
  },
  watch:{
    query(newVal, oldVal){
      if(newVal==null) this.query =''
      else if(oldVal=='' && newVal !='') this.selectedOption = 'time'

      this.resetRecipeList();
      this.pageNumber = 0;
      this.$refs.infiniteLoading.stateChanger.reset();
    },
    selectedOption : function() {
      this.resetRecipeList();
      this.pageNumber = 0;
      this.$refs.infiniteLoading.stateChanger.reset();
    },
  },
  created () {
    this.resetRecipeList()
    // this.fetchRecipes()
    // this.fetchRecipeLikeList(this.userinfo.userid)
  },

}
</script>

<style>

</style>
