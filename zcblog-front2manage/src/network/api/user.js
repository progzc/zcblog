import request from 'network/request'
import { encryptAES } from 'common/js/utils/encrypt'

/**
 * 获取当前管理员信息
 * @returns {*}
 */
export function executeGetUserInfo () {
  return request({
    url: '/admin/sys/user/info',
    method: 'get'
  })
}

/**
 * 修改密码
 * @param password 原密码
 * @param newPassword 新密码
 * @returns {AxiosPromise}
 */
export function executeModifyPsd (password, newPassword) {
  return request({
    url: '/admin/sys/user/password',
    method: 'put',
    data: {
      password: encryptAES(password),
      newPassword: encryptAES(newPassword)
    }
  })
}

// /**
//  * 获取系统参数
//  * @returns {*}
//  */
// export function executeGetSysParam () {
//   return request({
//     url: '/admin/sys/param/all',
//     method: 'get'
//   })
// }
