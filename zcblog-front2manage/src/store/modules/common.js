import {
  UPDATE_DOCUMENT_CLIENT_HEIGHT,
  UPDATE_MAIN_TABS, UPDATE_NAVBAR_LAYOUT_TYPE, UPDATE_SIDEBAR_FOLD
} from 'store/constant/mutation-types'
export default {
  namespaced: true, // 启用命名空间
  state: {
    sidebarFold: false, // 侧边栏折叠状态
    documentClientHeight: 0, // 页面文档可视高度
    mainTabs: [], // 主入口标签页
    navbarLayoutType: 'inverse' // 导航条，布局风格，default（默认）/ inverse(反色)

  },
  mutations: {
    [UPDATE_SIDEBAR_FOLD] (state, sidebarFold) {
      state.navbarLayoutType = sidebarFold
    },
    [UPDATE_DOCUMENT_CLIENT_HEIGHT] (state, height) {
      state.documentClientHeight = height
    },
    [UPDATE_MAIN_TABS] (state, mainTabs) {
      state.mainTabs = mainTabs
    },
    [UPDATE_NAVBAR_LAYOUT_TYPE] (state, layoutType) {
      state.navbarLayoutType = layoutType
    }
  }
}
