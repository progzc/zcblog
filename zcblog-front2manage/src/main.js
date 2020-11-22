import Vue from 'vue'
import App from '@/App.vue'
import router from '@/router'
import store from '@/store'
import VueCookie from 'vue-cookie' // 导入vue-cookie插件

import 'common/scss/index.scss' // 导入全局样式文件src/common/scss/index.scss
import 'icons' // 导入src/icons/index.js
import 'elementUI' // 按需引入element-ui中的vue组件
import 'elementUI/theme/elementUITheme' // 引入element-ui自定义主题色
import cloneDeep from 'lodash/cloneDeep' // 引入lodash/cloneDeep.js
import { isAuth } from 'common/js/utils/validate'

Vue.use(VueCookie)
Vue.config.productionTip = false

// 全局注册
Vue.prototype.isAuth = isAuth // 权限方法

window.SITE_CONFIG = {}
window.SITE_CONFIG.storeState = cloneDeep(store.state) // 保存整站vuex到本地（储存初始状态）

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
