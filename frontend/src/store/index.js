import Vue from 'vue'
import Vuex from 'vuex'
import examples from '@/store/modules/examples.js'
import user from '@/store/modules/user.js'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    examples,
    user,
  }
})
