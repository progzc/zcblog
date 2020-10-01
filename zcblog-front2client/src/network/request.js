import axios from 'axios'

function request (config) {
  // 1.创建axios的示例
  const instance = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 1000 * 30, // 最大延时30s
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
