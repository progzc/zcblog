import Vue from 'vue'
import axios from 'axios'
import router from '@/router'

import { clearLoginInfo } from 'common/js/utils/login'
import merge from 'lodash/merge'
import qs from 'qs'

/**
 * 封装axios请求
 * @param config http请求配置
 * @param openDefaultParams 默认添加时间戳
 * @param contentType 数据类型默认为json
 * @returns {AxiosPromise}
 */
export default function request (config, openDefaultParams = true, contentType = 'json') { // 封装网络请求
  // 1. 创建axios的示例
  const instance = axios.create({ // 创建网络请求实例（若有不同配置，可以封装多个网络请求实例）
    baseURL: process.env.VUE_APP_API,
    timeout: 1000 * 10, // 最大延时10s
    withCredentials: true, // 当前请求为跨域类型时,在请求中携带cookie
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    }
  })

  // 2.1 请求拦截
  instance.interceptors.request.use(config => {
    const timestamp = { // 对每次请求生成当前时间戳（作用是清楚缓存）
      t: new Date().getTime()
    }
    if (config.params) { // 若存在请求参数，请求参数处理添加时间戳，并json化
      config.params = openDefaultParams ? merge(timestamp, config.params) : config.params
    }
    if (config.data) { // 若存在请求体，请求体添加时间戳，并json化
      config.data = openDefaultParams ? merge(timestamp, config.data) : config.data
      config.data = contentType === 'json' ? JSON.stringify(config.data) : qs.stringify(config.data)
    }
    if (!config.params && !config.data) { // 若请求参数和请求体均不存在，则给请求参数添加时间戳
      config.params = openDefaultParams ? merge(timestamp, config.params) : config.params
    }

    config.headers.token = Vue.cookie.get('token') // 请求头带上token
    return config
  }, error => {
    // console.log(error)
    return Promise.reject(error)
  })

  // 2.2 响应拦截
  instance.interceptors.response.use(res => {
    if (res.data && res.data.code === 403) { // 403: token失效返回登录页面
      clearLoginInfo()
      router.push({ name: 'login' })
    }
    return res.data
  }, error => {
    // console.log(error)
    return Promise.reject(error)
  })

  // 3. 发送真正的网络请求
  return instance(config)
}
