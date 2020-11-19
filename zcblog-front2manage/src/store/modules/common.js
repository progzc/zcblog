import {
  UPDATE_DOCUMENT_CLIENT_HEIGHT,
  UPDATE_MAIN_TABS,
  UPDATE_MAIN_TABS_ACTIVE_NAME,
  UPDATE_MENU_ACTIVE_NAME,
  UPDATE_MENU_LIST,
  UPDATE_NAVBAR_LAYOUT_TYPE,
  UPDATE_SIDEBAR_FOLD,
  UPDATE_SIDEBAR_LAYOUT_SKIN
} from 'store/constant/mutation-types'

export default {
  namespaced: true, // 启用命名空间
  state: {
    sidebarFold: false, // 侧边栏折叠状态
    documentClientHeight: 0, // 页面文档可视高度
    mainTabs: [], // 主入口标签页
    navbarLayoutType: 'inverse', // 导航条，布局风格，default（默认）/ inverse(反色)
    sidebarLayoutSkin: 'light', // 侧边栏，布局皮肤，light（默认浅色） / darK（黑色）
    menuList: [], // 侧边栏，菜单
    menuActiveName: '', // 激活的菜单
    mainTabsActiveName: ''
  },
  mutations: {
    [UPDATE_SIDEBAR_FOLD] (state, sidebarFold) {
      console.log('UPDATE_SIDEBAR_FOLD', sidebarFold)
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
    },
    [UPDATE_SIDEBAR_LAYOUT_SKIN] (state, layoutSkin) {
      state.sidebarLayoutSkin = layoutSkin
    },
    [UPDATE_MENU_LIST] (state, menuList) {
      state.menuList = menuList
    },
    [UPDATE_MENU_ACTIVE_NAME] (state, menuActiveName) {
      state.menuActiveName = menuActiveName
    },
    [UPDATE_MAIN_TABS_ACTIVE_NAME] (state, mainTabsActiveName) {
      state.mainTabsActiveName = mainTabsActiveName
    }
  }
}
