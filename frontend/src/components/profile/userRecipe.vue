<template>
<v-container>
        <div class="px-7 mt-4">
        </div>
            <!-- 사진 배열하기 -->
            <section  class="postArea">
                <v-row>
                    <div v-for="(info, recipe) in myrecipes" :key="recipe" cols="4" class="grid-cell">
                        
                        <!-- post 대표 사진, 내용-->
                        <div @click="moveDetail(idx)" class="box">
                            <img :src="info.filepath" width=100%  style="border-radius: 7px;">  

                        <div @click="moveDetail(idx)" class="box">
                            <img :src="info.filepath" width=100%  style="border-radius: 7px;">  

                        </div>
                    </div>
                </v-row>
            </section>    
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
    ...mapActions(['myrecipes','fetchRecipeDetail', 'fetchRecipeComments', ]),
    srcPath(recipe){
      return "http://localhost:8080/recipe/" + recipe.recipeId+ "/" + recipe.stored_file_path.split('/').reverse()[0];
    },
    moveDetail(recipe) { // 게시글 상세보기
      this.fetchRecipeDetail(recipe.recipeId)
      this.fetchRecipeComments(recipe.recipeId)
      this.$router.push({ name: 'RecipeDetail' })
    },

  },
  computed: {
    ...mapGetters(['recipes', 'myrecipes', ]),
    ...mapGetters("user", (["userinfo"])),
  },
  created() {
      this.myrecipes(this.userinfo.userid)
  },
}
</script>
