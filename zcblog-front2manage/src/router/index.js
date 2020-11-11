import Vue from 'vue'
import VueRouter from 'vue-router'

// import request from 'network/request'

Vue.use(VueRouter)

const _import = require('./_import_' + process.env.NODE_ENV) // 智能懒加载：开发环境不采用懒加载，生产环境采用懒加载
// 配置全局路由
const globalRoutes = [
  { path: '/404', component: _import('common/404'), name: '404', meta: { title: '404未找到' } },
  { path: '/login', component: _import('common/login'), name: 'login', meta: { title: '登录' } }
]
// 配置主路由
const mainRoutes = {
  path: '/',
  component: _import('main'),
  name: 'main',
  redirect: { name: 'home' },
  meta: { title: '主入口整体布局' },
  children: [
    { path: '/home', component: _import('common/home'), name: 'home', meta: { title: '首页' } }
  ]

}

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: globalRoutes.concat(mainRoutes) // 合并全局路由和主路由
})

export default router
