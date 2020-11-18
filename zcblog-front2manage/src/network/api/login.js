import request from 'network/request'
import { encryptAES } from 'common/js/utils/encrypt'

/**
 * 提交表单
 * @param dataForm
 */
export function executeLogin (dataForm) {
  return request({
    url: '/admin/sys/login',
    method: 'post',
    data: {
      username: encryptAES(dataForm.username),
      password: encryptAES(dataForm.password),
      uuid: dataForm.uuid,
      captcha: dataForm.captcha
    }
  })
}

/**
 * 获取验证码
 * @param uuid
 */
export function executeGetCaptchaPath (uuid) {
  return request({
    url: '/captcha.jpg',
    method: 'get',
    params: {
      uuid
    }
  })
}

/**
 * 退出登录
 * @returns {*}
 */
export function executeLogout () {
  return request({
    url: '/admin/sys/logout',
    method: 'post'
  })
}
