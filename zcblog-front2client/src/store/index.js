import Vue from 'vue'
import Vuex from 'vuex'

import mutations from 'store/mutations'
import actions from 'store/actions'
import getters from 'store/getters'

Vue.use(Vuex)

const state = {
  pv: '',
  uv: ''
}

export default new Vuex.Store({
  state, // 根级别的state
  getters, // 根级别的getters
  mutations, // 根级别的mutations
  actions, // 根级别的actions
  modules: {
  }
})
