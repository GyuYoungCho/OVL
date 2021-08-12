<template>
<v-container>
        <div class="px-7 mt-4">
        </div>
            <!-- 사진 배열하기 -->
            <v-container>
              <v-row>
                <v-col v-for="(info, recipe) in myrecipes" :key="recipe" cols="4" class="grid-cell">

                        <!-- recipe 대표 사진, 내용-->
                        <div @click="moveDetail(recipe)" class="box">
                            <img :src="info.filepath" width=100% > 
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
    ...mapActions(['search_myrecipe','fetchRecipeDetail', 'fetchRecipeComments']),
    moveDetail(recipe) { // 게시글 상세보기
      this.fetchRecipeDetail(recipe.recipeId)
      this.fetchRecipeComments(recipe.recipeId)
      this.$router.push({ name: 'RecipeDetail' })
    },

  },
  computed: {
    ...mapGetters(['recipes', 'myrecipes']),
    ...mapGetters("user", (["userinfo"])),
  },
  created() {
      this.search_myrecipe(this.userinfo.userid)
  },
}
</script>
