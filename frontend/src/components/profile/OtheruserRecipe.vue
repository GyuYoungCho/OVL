<template>
<v-container>
        <div class="mb-4">
          <div class="recipe-slogan text-center">"live vegan, life together"</div>
        </div>
        
            <!-- 사진 배열하기 -->
            <v-container>
              <v-row>
                <v-col v-for="(info, recipe) in myrecipes" :key="recipe" cols="4" class="grid-cell">

                        <!-- recipe 대표 사진, 내용-->
                        <div @click="moveDetail(recipe)" class="postImg">
                            <img :src="info.filepath" width="90px" height="90px"> 
                        </div>

                </v-col>
              </v-row>
            </v-container> 
        </v-container>
</template>

<script>
import axios from 'axios';
import {mapGetters, mapActions} from "vuex";
import API from '@/api/index.js'
import recipeAPI from '@/api/recipe.js'

export default {
  data() {
    return {
      myrecipes: [],
    }
  },
  methods: {
    ...mapActions(['fetchRecipeDetail', 'fetchRecipeComments']),
    moveDetail(recipe) { // 게시글 상세보기
      this.fetchRecipeDetail(recipe.recipeId)
      this.fetchRecipeComments(recipe.recipeId)
      this.$router.push({ name: 'RecipeDetail' })
    },

  },
  computed: {
    ...mapGetters(['recipes']),
    ...mapGetters("user", (["userinfo"])),
  },
  created() {
      axios({
        method: "get",
        url: API.url + recipeAPI.search_myrecipe(this.$route.params.userid),
      }).then((res)=>{
        this.myrecipes = res.data
      }).catch((err)=>{
        console.log(err);
      })
  },
}
</script>
