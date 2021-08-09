import Vue from 'vue'
import Vuex from 'vuex'
import examples from './modules/examples.js'
import user from './modules/user.js'
import post from "./modules/post.js"
import postComment from "./modules/postComment.js";
import postReply from "./modules/postReply.js";
import recipe from "./modules/recipe.js";
import pot from "./modules/pot.js";
import challenge from './modules/challenge.js'
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

export default new Vuex.Store({
  plugins: [createPersistedState()],
  modules: {
    examples,
    user,
    post,
    postComment,
    postReply,
    recipe,
    pot,
    challenge,
  }
})
