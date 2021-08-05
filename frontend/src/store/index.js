import Vue from 'vue'
import Vuex from 'vuex'
import examples from './modules/examples.js'
import user from './modules/user.js'
import post from "./modules/post.js"
import recipe from "./modules/recipe.js";
import pot from "./modules/pot.js";

Vue.use(Vuex);

export default new Vuex.Store({
  // plugins: [createPersistedState()],
  modules: {
    examples,
    user,
    post,
    recipe,
    pot,
  }
})
