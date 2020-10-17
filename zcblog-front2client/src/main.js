import Vue from 'vue'
import App from './App.vue' // Vue挂载到实例
import router from './router' // Vue路由
import store from './store' // Vue状态管理器
import VueI18n from 'vue-i18n' // 导入vue-i18n
import messages from './i18n' // 引入自定义国际化内容
import 'view-design/dist/styles/iview.css' // 按需引入iView UI的样式
import iViewUI from 'view-design/dist/iview' // 引入iview.js文件，这样才能使用iview.js中的i18n方法实现按需导入的iView组件的国际化

// 若使用 import hljs from 'highlight.js'会引入所有的语言，导致性能降低
import hljs from 'highlight.js/lib/core' // 引入highlight.js核心包
import javascript from 'highlight.js/lib/languages/javascript' // 支持javascript语法高亮
import java from 'highlight.js/lib/languages/java' // 支持java语法高亮
import css from 'highlight.js/lib/languages/css' // 支持css语法高亮x
import xml from 'highlight.js/lib/languages/xml' // 支持xml语法高亮
import 'highlight.js/styles/monokai-sublime.css' // 引入样式文件
hljs.registerLanguage('javascript', javascript)
hljs.registerLanguage('java', java)
hljs.registerLanguage('css', css)
hljs.registerLanguage('xml', xml)
// 可以在Vue组件中通过<highlightjs language='javascript' code="var x = 5;" />对代码进行语法高亮
Vue.use(hljs.vuePlugin) // 非必须，也可以使用Vue.directive自定义配置highlightjs指令
Vue.prototype.$hljs = hljs

// 按需注册组件
Vue.component('iv-row', iViewUI.Row)
Vue.component('iv-col', iViewUI.Col)
Vue.component('iv-progress', iViewUI.Progress)
Vue.component('iv-page', iViewUI.Page)
Vue.component('iv-icon', iViewUI.Icon)
Vue.component('iv-affix', iViewUI.Affix)
Vue.component('iv-input', iViewUI.Input)
Vue.component('iv-switch', iViewUI.Switch)
Vue.component('iv-tag', iViewUI.Tag)
// 注册全局变量（非响应式）
Vue.prototype.$Message = iViewUI.Message // 信息提示
Vue.prototype.$Notice = iViewUI.Notice // 通知提醒
Vue.prototype.$Modal = iViewUI.Modal // 对话框
Vue.prototype.$Notice.config({ // 配置通知项
  top: 70,
  duration: 3
})

Vue.use(VueI18n) // 全局注册vue-i18n
const i18n = new VueI18n({
  locale: localStorage.getItem('language') || 'en', // 设置国际化语言
  messages // 设置国际化内容
})
Vue.prototype.i18n = i18n // 为将路由中的title国际化，配置全局i18n
iViewUI.i18n((key, value) => i18n.t(key, value))

Vue.config.productionTip = false

new Vue({
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app')
