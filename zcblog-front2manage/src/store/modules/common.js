import { UPDATE_DOCUMENT_CLIENTHEIGHT } from 'store/constant/mutation-types'
export default {
  namespaced: true, // 启用命名空间
  state: {
    sidebarFold: false, // 侧边栏折叠状态
    documentClientHeight: 0 // 页面文档可视高度

  },
  mutations: {
    [UPDATE_DOCUMENT_CLIENTHEIGHT] (state, height) {
      state.documentClientHeight = height
    }
  }
}
