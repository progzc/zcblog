import Vue from 'vue'
import VueRouter from 'vue-router'

import routes from './routes'

import { LoadingBar } from 'view-design'

Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  base: process.env.BASE_URL,
  routes
})

LoadingBar.config({ // 配置进度条
  color: '#19be6b',
  failedColor: '#ff9900',
  height: 2
})

router.beforeEach((to, from, next) => { // 动态更新页面title
  LoadingBar.start()
  window.scrollTo(0, 0)
  if (to.meta.title) {
    document.title = Vue.prototype.i18n.t(to.meta.title)
  }
  next()
})

router.afterEach((to, from, next) => {
  LoadingBar.finish()
  window.scrollTo(0, 0)
})
export default router
