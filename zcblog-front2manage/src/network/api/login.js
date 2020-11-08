import request from 'network/request'

/**
 * 提交表单
 * @param dataForm
 */
export function executeLogin (dataForm) {
  return request({
    url: '/admin/sys/login',
    method: 'post',
    data: {
      username: dataForm.userName,
      password: dataForm.password,
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
