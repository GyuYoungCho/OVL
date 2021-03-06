import Vue from 'vue'
import Vuex from 'vuex'
import examples from './modules/examples.js'
import user from './modules/user.js'
import post from "./modules/post.js"
import postComment from "./modules/postComment.js";
import postReply from "./modules/postReply.js";
import recipe from "./modules/recipe.js";
import pot from "./modules/pot.js";
import follow from "./modules/follow.js";
import challenge from './modules/challenge.js'
import createPersistedState from 'vuex-persistedstate';
import report from './modules/report.js';

Vue.use(Vuex);

export default new Vuex.Store({
  plugins: [createPersistedState(    examples,
    post,
    postComment,
    postReply,
    recipe,
    pot,
    follow,
    challenge,
    report
    )],
  modules: {
    examples,
    user,
    post,
    postComment,
    postReply,
    recipe,
    pot,
    follow,
    challenge,
    report
  }
})
