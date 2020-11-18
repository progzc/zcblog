import { UPDATE_USER_ID, UPDATE_USER_NAME } from 'store/constant/mutation-types'

export default {
  namespace: true, // 启用命名空间
  state: {
    id: 0,
    name: ''
  },
  mutations: {
    [UPDATE_USER_ID] (state, id) {
      state.id = id
    },
    [UPDATE_USER_NAME] (state, name) {
      state.name = name
    }
  }
}
