import request from 'network/request'

export function executeGetSysMenuNav () {
  return request({
    url: '/admin/sys/menu/nav',
    method: 'get'
  })
}
