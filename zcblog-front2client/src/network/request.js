import axios from 'axios'

export function request (config) { // 封装网络请求
  // 1.创建axios的示例
  const instance = axios.create({ // 创建网络请求实例（若有不同配置，可以封装多个网络请求实例）
    baseURL: process.env.OPEN_PROXY ? process.env.VUE_APP_API : process.env.BASE_URL,
    timeout: 1000 * 10, // 最大延时10s
    withCredentials: true, // 当前请求为跨域类型时,在请求中携带cookie
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    }
  })

  // 2.1 请求拦截
  instance.interceptors.request.use(config => {
    return config
  }, error => {
    console.log(error)
  })

  // 2.2 响应拦截
  instance.interceptors.response.use(res => {
    return res.data
  }, error => {
    console.log(error)
  })

  // 3.发送真正的网络请求
  return instance(config)
}
