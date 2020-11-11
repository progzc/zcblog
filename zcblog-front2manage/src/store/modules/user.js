import { UPDATE_ID, UPDATE_NAME } from 'store/constant/mutation-types'

export default {
  namespace: true, // 启用命名空间
  state: {
    id: 0,
    name: ''
  },
  mutations: {
    [UPDATE_ID] (state, id) {
      state.id = id
    },
    [UPDATE_NAME] (state, name) {
      state.name = name
    }
  }
}
