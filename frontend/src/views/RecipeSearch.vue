<template>
  <v-container>
    <section class="recipeSearch">
      <!-- 레시피 검색 부분 -->
      <RecipeSearchBar @searchKeyword="searchKeyword" @selectOrd="selectOrd" />

      <div v-if="recipes.length==0 && query.length!=0" class="noResult">
        <img src="@/assets/image/noResult.png" alt="">
        <div class="mt-3">검색 결과가 존재하지 않습니다</div>
      </div>
      <div v-else-if="recipes.length==0" class="noResult">
        <img src="@/assets/image/noResult.png" alt="">
        <div class="mt-3">레시피가 존재하지 않습니다 <br> 레시피를 새로 등록해보세요!</div>
      </div>
      
      <!-- 레시피 목록 -->
      <div v-for="(recipe, idx) in recipes" :key="idx" class="oneRecipe">
        <div>  
          <ProfileName :user="recipe.userid"></ProfileName>
          <img :src="recipe.filepath" alt="" @click="onImgClick(recipe)" class="recipePic">
          <div class="oneRecipeContent">
            <span>{{ recipe.title }}</span>
          </div>
          <div>
            <v-row>
              <v-col cols="6" md="1">
                <v-icon class="likedHeart" v-if="recipeLikeList.includes(recipe.recipeId)" @click="onHeartIconClick(recipe,idx)">mdi-heart</v-icon>
                <v-icon class="unlikedHeart" v-else @click="onHeartIconClick(recipe,idx)">mdi-heart-outline</v-icon>
                {{ recipe.likecount}}
                
                <v-icon class="chatIcon">mdi-chat-outline</v-icon>
                {{ recipe.commentcount }}
              </v-col>
              <v-col cols="6" md="1" style="text-align:right">
                <div>{{ calTime(recipe.time) }}</div>
              </v-col>
            </v-row>
            
          </div>
        </div>
      </div>
      <infinite-loading @infinite="infiniteHandler" ref="infiniteLoading" spinner="circles">
        <!-- <div slot="spinner"></div> -->
        <div slot="no-more" class="mt-4">
          <hr>
          <div class="mt-2">레시피 끝</div>
        </div>
        <div slot="no-results" class="mt-4">
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
    async onHeartIconClick (recipe , idx) {
      const data = {
        userId: this.userinfo.userid,
        recipeId: recipe.recipeId,
      }
      
      await this.likeRecipe(data)
      await this.fetchRecipeLikeList(this.userinfo.userid);
      
      await this.fetchRecipeDetail(recipe.recipeId);
      this.modifyRecipeList(idx);
    },
    calTime (recipe) {
      return moment(recipe.time).fromNow()
    },
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
  },

}
</script>

<style>

</style>
