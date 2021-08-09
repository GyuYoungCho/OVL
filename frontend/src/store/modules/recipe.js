import axios from 'axios'
import API from '@/api/index.js'
import recipeAPI from '@/api/recipe.js'
import recipeCommentAPI from '@/api/recipe_comment.js'

export default {
  // namspaced: true,
  state: {
    recipe: {},
    recipes: [],
    recipeLikeList: [],
    // 레시피 과정이 담기기 때문에 리스트 형식, 각 요소는 객체
    recipeDetail: [],
    recipeComments: [],
  },
  getters: {
    recipe (state) {
      return state.recipe
    },
    recipes (state) {
      return state.recipes
    },
    recipeLikeList (state) {
      return state.recipeLikeList
    },
    recipeDetail (state) {
      return state.recipeDetail
    },
    recipeComments (state) {
      return state.recipeComments
    }
  },
  mutations: {
    SET_RECIPES (state, recipes) {
      state.recipes = recipes
    },
    SORT_RECIPES (state, option) {
      const recipes = state.recipes
      if (option==="new") {
        recipes.sort(function (recipe1, recipe2) {
          const time1 = new Date(recipe1.time)
          const time2 = new Date(recipe2.time)
          return time2 - time1
        })
      } else if (option==="like") {
        recipes.sort(function (recipe1, recipe2) {
          return recipe2.like_count - recipe1.like_count
        })
      } else if (option==="comment") {
        recipes.sort(function (recipe1, recipe2) {
          return recipe2.comment_count - recipe1.comment_count
        })
      }
      state.recipes = recipes
    },
    SET_RECIPE_LIKE_LIST (state, recipeLikeList) {
      state.recipeLikeList = recipeLikeList
    },
    SET_RECIPE_DETAIL (state, recipeDetail) {
      state.recipeDetail = recipeDetail
      state.recipe = recipeDetail[0].recipeId    },
    SET_RECIPE_COMMENTS (state, recipeComments) {
      state.recipeComments = recipeComments
    }
  },
  actions: {
    fetchRecipes ({ commit }) {
      const URL = API.url + recipeAPI.select_all()
      axios.get(URL)
        .then(res => {
          const data = res.data.sort(function(recipe1, recipe2) {
            const time1 = new Date(recipe1.time)
            const time2 = new Date(recipe2.time)
            return time2 - time1
          })
          commit('SET_RECIPES', data)
          this.recipes = data
        })
        .catch(err => console.error(err))
    },

    sortRecipes({ commit }, option) {
      commit('SORT_RECIPES', option)
    },

    fetchRecipeLikeList ({ commit }, userId) {
      const URL = API.url + recipeAPI.like_list(userId)
      axios.get(URL)
        .then(res => {
          console.log(res)
          commit('SET_RECIPE_LIKE_LIST', res.data)
        })
        .catch(err => console.error(err))
    },

    likeRecipe({ dispatch }, { userId, recipeId }) {
      const URL = API.url + recipeAPI.like(userId, recipeId)
      axios.get(URL)
        .then(res => {
          console.log(res)
          dispatch('fetchRecipeLikeList', userId)
          dispatch('fetchRecipes')
          dispatch('fetchRecipeDetail', recipeId)
        })
        .catch(err => console.error(err))
        
    },

    fetchRecipeDetail ({ commit }, recipeId) {
      const URL = API.url + recipeAPI.select_detail(recipeId)
      axios.get(URL)
        .then(res => {
          commit('SET_RECIPE_DETAIL', res.data)
        })
        .catch(err => console.error(err))
    },

    fetchRecipeComments ({ commit }, recipeId) {
      commit
      const URL = API.url + recipeCommentAPI.search_all(recipeId)
      axios.get(URL)
        .then(res => {
          commit('SET_RECIPE_COMMENTS', res.data)
        })
        .catch(err => console.error(err))
    },

    registComment ({ dispatch }, data) {
      console.log(data)
      console.log(data.userId)
      const URL = API.url + recipeCommentAPI.regist()
      console.log(URL)
 
      axios.post(URL, data)
        .then(res => {
          console.log(res)
          dispatch('fetchRecipeComments', data.recipeId)
        })
        .catch(err => console.error(err))
    }
  }
}