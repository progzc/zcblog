import Vue from 'vue'
import Vuex from 'vuex'

import mutations from 'store/mutations'
import actions from 'store/actions'
import getters from 'store/getters'

Vue.use(Vuex)

const state = {
}

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions,
  modules: {
  },
  strict: process.env.NODE_ENV !== 'production' // 不要在生产环境下启用严格模式
})
