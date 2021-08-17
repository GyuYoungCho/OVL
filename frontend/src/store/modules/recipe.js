import axios from "axios";
import API from "@/api/index.js";
import recipeAPI from "@/api/recipe.js";
import recipeCommentAPI from "@/api/recipe_comment.js";
import recipeReplyAPI from "@/api/recipe_reply.js";

export default {
  namespaced: true,
  state: {
    recipe: {},
    recipes: [],
    // 내가 좋아요 누른 레시피
    recipeLikeList: [],
    // 레시피 과정이 담기기 때문에 리스트 형식, 각 요소는 객체
    recipeDetail: [],
    recipeComments: [],
    // 내가 좋아요 누른 레시피 댓글
    recipeCommentLikeList: [],
    // 댓글이 key, 대댓글이 value인 객체
    recipeCommentReply: {},
    // 내가 좋아요 누른 대댓글
    recipeReplyLikeList: [],
    //내 레시피들
    myrecipes: [],
  },
  getters: {
    recipe(state) {
      return state.recipe;
    },
    recipes(state) {
      return state.recipes;
    },
    recipeLikeList(state) {
      return state.recipeLikeList;
    },
    recipeDetail(state) {
      return state.recipeDetail;
    },
    recipeComments(state) {
      return state.recipeComments;
    },
    recipeCommentLikeList(state) {
      return state.recipeCommentLikeList;
    },
    recipeCommentReply(state) {
      return state.recipeCommentReply;
    },
    recipeReplyLikeList(state) {
      return state.recipeReplyLikeList;
    },
    myrecipes(state) {
      return state.myrecipes;
    },
  },
  mutations: {
    SET_RECIPES(state, payload) {
      state.recipes = state.recipes.concat(payload);
    },
    SORT_RECIPES(state, option) {
      const recipes = state.recipes;
      if (option === "new") {
        recipes.sort(function(recipe1, recipe2) {
          const time1 = new Date(recipe1.time);
          const time2 = new Date(recipe2.time);
          return time2 - time1;
        });
      } else if (option === "like") {
        recipes.sort(function(recipe1, recipe2) {
          return recipe2.likecount - recipe1.likecount;
        });
      } else if (option === "comment") {
        recipes.sort(function(recipe1, recipe2) {
          return recipe2.commentcount - recipe1.commentcount;
        });
      }
      state.recipes = recipes;
    },
    SET_RECIPE_LIKE_LIST(state, recipeLikeList) {
      state.recipeLikeList = recipeLikeList;
    },
    SET_RECIPE_DETAIL(state, recipeDetail) {
      state.recipeDetail = recipeDetail;
      state.recipe = recipeDetail[0].recipeId;
      console.log(state.recipe);
    },
    SET_RECIPE_COMMENTS(state, recipeComments) {
      state.recipeComments = recipeComments;
    },
    SET_RECIPE_COMMENT_LIKE_LIST(state, recipeCommentLikeList) {
      state.recipeCommentLikeList = recipeCommentLikeList;
    },
    SET_RECIPE_COMMENT_REPLY(state, { recipeCommentId, recipeCommentReply }) {
      state.recipeCommentReply[recipeCommentId] = recipeCommentReply;
    },
    SET_RECIPE_REPLY_LIKE_LIST(state, recipeReplyLikeList) {
      state.recipeReplyLikeList = recipeReplyLikeList;
    },
    SET_USER_RECIPES(state, myrecipes) {
      state.myrecipes = myrecipes;
    },
    MODIFY_RECIPE_LIST(state, payload) {
      state.recipes[payload].likecount = state.recipe.likecount;
    },
    RESET_RECIPE_LIST(state) {
      state.recipes = [];
    },
  },
  actions: {
    fetchRecipes({ commit }, payload) {
      // const URL = API.url + recipeAPI.select_all()
      // axios.get(URL)
      //   .then(res => {
      //     const data = res.data.sort(function(recipe1, recipe2) {
      //       const time1 = new Date(recipe1.time)
      //       const time2 = new Date(recipe2.time)
      //       return time2 - time1
      //     })
      commit("SET_RECIPES", payload);
      //   this.recipes = data
      // })
      // .catch(err => console.error(err))
    },
    sortRecipes({ commit }, option) {
      commit("SORT_RECIPES", option);
    },
    async fetchRecipeLikeList({ commit }, userId) {
      const URL = API.url + recipeAPI.like_list(userId);
      await axios
        .get(URL)
        .then((res) => {
          commit("SET_RECIPE_LIKE_LIST", res.data);
        })
        .catch((err) => console.error(err));
    },
    async likeRecipe(store, { userId, recipeId }) {
      const URL = API.url + recipeAPI.like(userId, recipeId);
      await axios
        .get(URL)
        .then(() => {})
        .catch((err) => console.error(err));
    },
    async fetchRecipeDetail({ commit }, recipeId) {
      const URL = API.url + recipeAPI.select_detail(recipeId);
      await axios
        .get(URL)
        .then((res) => {
          commit("SET_RECIPE_DETAIL", res.data);
        })
        .catch((err) => console.error(err));
    },
    deleteRecipe({ dispatch }, recipeId) {
      const URL = API.url + recipeAPI.delete(recipeId);
      axios
        .delete(URL)
        .then(() => {
          dispatch("fetchRecipes");
        })
        .catch((err) => console.error(err));
    },
    fetchRecipeComments({ commit }, recipeId) {
      const URL = API.url + recipeCommentAPI.search_all(recipeId);
      axios
        .get(URL)
        .then((res) => {
          commit("SET_RECIPE_COMMENTS", res.data);
        })
        .catch((err) => console.error(err));
    },
    fetchRecipeCommentLikeList({ commit }, userId) {
      const URL = API.url + recipeCommentAPI.like_list(userId);
      axios
        .get(URL)
        .then((res) => {
          commit("SET_RECIPE_COMMENT_LIKE_LIST", res.data);
        })
        .catch((err) => console.error(err));
    },
    registComment({ dispatch }, data) {
      const URL = API.url + recipeCommentAPI.regist();
      axios
        .post(URL, null, data)
        .then(() => {
          dispatch("fetchRecipeComments", data.params.recipeId);
          dispatch("fetchRecipeDetail", data.params.recipeId);
        })
        .catch((err) => console.error(err));
    },
    likeRecipeComment({ dispatch }, { userId, recipeId, recipeCommentId }) {
      const URL = API.url + recipeCommentAPI.like(userId, recipeCommentId);
      axios
        .get(URL)
        .then(() => {
          dispatch("fetchRecipeCommentLikeList", userId);
          dispatch("fetchRecipeComments", recipeId);
        })
        .catch((err) => console.error(err));
    },
    modifyRecipeComment({ dispatch }, { data, recipeId }) {
      const URL = API.url + recipeCommentAPI.modify();
      axios
        .put(URL, null, data)
        .then(() => {
          dispatch("fetchRecipeComments", recipeId);
        })
        .catch((err) => console.error(err));
    },
    deleteRecipeComment({ dispatch }, { recipeId, recipeCommentId }) {
      const URL = API.url + recipeCommentAPI.delete(recipeCommentId);
      axios
        .delete(URL)
        .then(() => {
          dispatch("fetchRecipeComments", recipeId);
          dispatch("fetchRecipeDetail", recipeId);
        })
        .catch((err) => console.error(err));
    },
    registRecipeCommentReply({ commit, dispatch }, { data, recipeId }) {
      const URL = API.url + recipeReplyAPI.regist();
      axios.post(URL, null, data).then(() => {
        const finalData = { recipeCommentId: data.params.commentId };
        axios
          .get(API.url + recipeReplyAPI.select_all(data.params.commentId))
          .then((res) => {
            finalData.recipeCommentReply = res.data;
            commit("SET_RECIPE_COMMENT_REPLY", finalData);
            dispatch("fetchRecipeComments", recipeId);
          })
          .catch((err) => console.error(err));
      });
    },
    fetchRecipeCommentReply({ commit }, recipeCommentId) {
      const URL = API.url + recipeReplyAPI.select_all(recipeCommentId);
      axios
        .get(URL)
        .then((res) => {
          const finalData = {
            recipeCommentId,
            recipeCommentReply: res.data,
          };
          commit("SET_RECIPE_COMMENT_REPLY", finalData);
        })
        .catch((err) => console.error(err));
    },
    likeRecipeCommentReply({ dispatch }, { recipeCommentId, userId, recipeReplyId }) {
      const URL = API.url + recipeReplyAPI.like(userId, recipeReplyId);
      axios
        .get(URL)
        .then(() => {
          dispatch("fetchRecipeCommentReply", recipeCommentId);
          dispatch("fetchRecipeReplyLikeList", userId);
        })
        .catch((err) => console.error(err));
    },
    fetchRecipeReplyLikeList({ commit }, userId) {
      const URL = API.url + recipeReplyAPI.like_list(userId);
      axios
        .get(URL)
        .then((res) => {
          commit("SET_RECIPE_REPLY_LIKE_LIST", res.data);
        })
        .catch((err) => console.error(err));
    },
    modifyRecipeReply({ dispatch }, { data, recipeCommentId }) {
      const URL = API.url + recipeReplyAPI.modify();
      axios
        .put(URL, null, data)
        .then(() => {
          dispatch("fetchRecipeCommentReply", recipeCommentId);
        })
        .catch((err) => console.error(err));
    },
    deleteRecipeReply({ dispatch }, { recipeCommentId, recipeReplyId, recipeId }) {
      const URL = API.url + recipeReplyAPI.delete(recipeReplyId);
      axios
        .delete(URL)
        .then(() => {
          dispatch("fetchRecipeCommentReply", recipeCommentId);
          dispatch("fetchRecipeComments", recipeId);
        })
        .catch((err) => console.error(err));
    },
    //이름이 겹치면 안된다!!! 알겠니?
    search_myrecipe({ commit }, userId) {
      const URL = API.url + recipeAPI.search_myrecipe(userId);
      axios
        .get(URL)
        .then((res) => {
          commit("SET_USER_RECIPES", res.data);
        })
        .catch((err) => console.error(err));
    },

    resetRecipeList(store) {
      store.commit("RESET_RECIPE_LIST");
    },
    modifyRecipeList(store, payload) {
      store.commit("MODIFY_RECIPE_LIST", payload);
    },
  },
};
