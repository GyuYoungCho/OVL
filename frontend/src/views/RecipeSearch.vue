<template>
  <v-container>
    <section class="recipeSearch">
      <!-- 레시피 검색 부분 -->
      <div class="recipeSearchHeader">
        <select class="sortSelect" v-model="selectedOption" @change="sortRecipes(selectedOption)">
          <option value="time" selected>최신순</option>
          <option value="likecount">인기순</option>
          <option value="commentcount">댓글순</option>
        </select>
        <input type="text" placeholder="검색" v-model="query" class="searchBar" @input="onSearchInput" @keyup.enter="onSearchBtnClick">
        <!-- 검색 아이콘은 아이패드 프로, 갤럭시 폴드에서 위치 깨짐, absolute라 어쩔 수 없나... -->
        <v-btn icon absolute right @click="onSearchBtnClick"><v-icon color="white" dark>mdi-magnify</v-icon></v-btn>        
      </div>

      <!-- 레시피 목록 -->
      <div v-for="(recipe, idx) in feedDatas" :key="idx" class="oneRecipe">
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
import InfiniteLoading from 'vue-infinite-loading';

export default {
  components: {
    InfiniteLoading,
    ProfileName
  },
  data: () => ({
    query: "",
    selectedOption: "time",
    searchClicked: false,
    pageNumber: 0,
    pageSize: 2,
    feedDatas: [],
  }),
  methods: {
    ...mapActions('recipe', ['fetchRecipes', 'fetchRecipeLikeList', 'fetchRecipeDetail', 'fetchRecipeComments', 'likeRecipe', 'sortRecipes','ResetRecipes']),
    
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
    },
    infiniteHandler($state) {
      
      var params = new URLSearchParams();
      params.append("size", this.pageSize);
      params.append("page", this.pageNumber);
      params.append("cate", this.selectedOption);
      params.append("keyword", this.query);
      axios.get(API.url + recipeAPI.select_all(),{params})
        .then(res => {
          if (res.data.content.length > 0) {
              this.feedDatas = this.feedDatas.concat(res.data.content);
              this.fetchRecipes(this.feedDatas)
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
    ...mapGetters('recipe', ['recipes', 'recipeLikeList', ]),
    ...mapGetters("user", (["userinfo"])),
  },
  watch:{
    query(newVal, oldVal){
      if(newVal==null) this.query =''
      else if(oldVal=='' && newVal !='') this.selectedOption = 'time'

      this.feedDatas = [];
      this.pageNumber = 0;
      this.$refs.infiniteLoading.stateChanger.reset();
    },
    selectedOption : function() {
      this.feedDatas = [];
      this.pageNumber = 0;
      this.$refs.infiniteLoading.stateChanger.reset();
    },
  }
}
</script>

<style>

</style>