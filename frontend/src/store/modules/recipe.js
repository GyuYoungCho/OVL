import axios from 'axios'
import API from '@/api/index.js'
import recipeAPI from '@/api/recipe.js'
import recipeCommentAPI from '@/api/recipe_comment.js'

export default {
  // namspaced: true,
  state: {
    recipe: {},
    // 레시피 과정이 담기기 때문에 리스트 형식, 각 요소는 객체
    recipeDetail: [],
    recipeComments: [],
  },
  getters: {
    recipe (state) {
      return state.recipe
    },
    recipeDetail (state) {
      return state.recipeDetail
    },
    recipeComments (state) {
      return state.recipeComments
    }
  },
  mutations: {
    SET_RECIPE_DETAIL (state, recipeDetail) {
      state.recipeDetail = recipeDetail
      state.recipe = recipeDetail[0].recipeId    },
    SET_RECIPE_COMMENTS (state, recipeComments) {
      state.recipeComments = recipeComments
    }
  },
  actions: {
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