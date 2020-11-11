import request from 'network/request'

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
 * 获取系统参数
 * @returns {*}
 */
export function executeGetSysParam () {
  return request({
    url: '/admin/sys/param/all',
    method: 'get'
  })
}
