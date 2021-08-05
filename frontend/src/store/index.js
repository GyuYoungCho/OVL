<<<<<<< HEAD
import Vue from 'vue'
import Vuex from 'vuex'
import examples from './modules/examples.js'
import user from './modules/user.js'
import post from "./modules/post.js"
=======
import Vue from "vue";
import Vuex from "vuex";
import examples from "./modules/examples.js";
import user from "./modules/user.js";
import recipe from "./modules/recipe.js";
import pot from "./modules/pot.js";
>>>>>>> 16fb28da68f21918993b9e021b88a11d7a8f5f37

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    examples,
    user,
<<<<<<< HEAD
    post
  }
})
=======
    recipe,
    pot,
  },
});
>>>>>>> 16fb28da68f21918993b9e021b88a11d7a8f5f37
