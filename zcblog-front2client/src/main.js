import Vue from 'vue'
import App from './App.vue' // Vue挂载到实例
import router from './router' // Vue路由
import store from './store' // Vue状态管理器
import VueI18n from 'vue-i18n' // 导入vue-i18n
import messages from './i18n' // 引入自定义国际化内容
import 'view-design/dist/styles/iview.css' // 按需引入iView UI的样式
import iViewUI from 'view-design/dist/iview' // 引入iview.js文件，这样才能使用iview.js中的i18n方法实现按需导入的iView组件的国际化

// 按需注册组件
Vue.component('iv-row', iViewUI.Row)
Vue.component('iv-col', iViewUI.Col)
Vue.component('iv-progress', iViewUI.Progress)
Vue.component('iv-page', iViewUI.Page)
Vue.component('iv-icon', iViewUI.Icon)
Vue.component('iv-affix', iViewUI.Affix)
// 注册全局变量（非响应式）
Vue.prototype.$Message = iViewUI.Message // 信息提示
Vue.prototype.$Notice = iViewUI.Notice // 通知提醒
Vue.prototype.$Modal = iViewUI.Modal // 对话框
Vue.prototype.$Notice.config({
  top: 70,
  duration: 3
})

Vue.use(VueI18n) // 全局注册vue-i18n
const i18n = new VueI18n({
  locale: 'en', // 设置国际化语言
  messages // 设置国际化内容
})
iViewUI.i18n((key, value) => i18n.t(key, value))

Vue.config.productionTip = false

new Vue({
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app')
