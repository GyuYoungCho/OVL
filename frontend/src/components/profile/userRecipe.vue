<template>
<v-container>
        <div class="mb-4">
          <div class="recipe-slogan text-center">"live vegan, life together"</div>
        </div>
            <!-- 사진 배열하기 -->
            <v-container>
              <v-row>
                <v-col v-for="(recipe, idx) in myrecipes" :key="idx" cols="4" class="grid-cell">

                        <!-- recipe 대표 사진, 내용-->
                        <div @click="moveDetail(recipe)" class="postImg">
                            <img :src="recipe.filepath" width="90px" height="90px"> 
                        </div>

                </v-col>
              </v-row>
            </v-container> 
        </v-container>
</template>

<script>
import {mapGetters, mapActions} from "vuex";

export default {
  data() {
    return {
    }
  },
  methods: {
    ...mapActions('recipe', ['search_myrecipe','fetchRecipeDetail', 'fetchRecipeComments']),

    moveDetail(recipe) { // 게시글 상세보기
      console.log("레시피 상세보기 : ", recipe)
      this.fetchRecipeDetail(recipe.recipeId)
      this.fetchRecipeComments(recipe.recipeId)
      this.$router.push({ name: 'RecipeDetail' , params: {recipeId: recipe.recipeId}});
    },

  },
  computed: {
    ...mapGetters('recipe',['myrecipes']),
    ...mapGetters("user", (["userinfo"])),
  },
  created() {
      this.search_myrecipe(this.userinfo.userid)
  },
}
</script>
