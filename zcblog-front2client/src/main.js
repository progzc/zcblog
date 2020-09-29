// vue router相关
import Vue from 'vue'
import App from './App.vue'
import router from './router'
// vue状态管理器
import store from './store'
// 按需引入iView UI组件
import 'view-design/dist/styles/iview.css'
import {
  Row,
  Col,
  Progress,
  Page,
  Icon,
  Affix,
  Modal,
  Message,
  Notice
} from 'view-design'
// 全局注册组件
Vue.component('iv-row', Row)
Vue.component('iv-col', Col)
Vue.component('iv-progress', Progress)
Vue.component('iv-page', Page)
Vue.component('iv-icon', Icon)
Vue.component('iv-affix', Affix)
// 注册全局变量（非响应式）
Vue.prototype.$Modal = Modal // 对话框
Vue.prototype.$Message = Message // 信息提示
Vue.prototype.$Notice = Notice // 通知提醒

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
