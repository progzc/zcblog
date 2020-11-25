import request from 'network/request'

/**
 * 查询当前用户所创建的角色
 * @returns {AxiosPromise}
 */
export function executeGetRole () {
  return request({
    url: '/admin/sys/role/select',
    method: 'get'
  })
}
