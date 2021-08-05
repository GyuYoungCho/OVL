import Vue from 'vue'
import Vuex from 'vuex'
import examples from './modules/examples.js'
import user from './modules/user.js'
import post from "./modules/post.js"

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    examples,
    user,
    post
  }
})
