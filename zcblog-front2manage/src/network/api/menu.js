import request from 'network/request'

/**
 * 查询菜单列表(树形结构)
 * @returns {AxiosPromise}
 */
export function executeGetMenuList () {
  return request({
    url: '/admin/sys/menu/list',
    method: 'get'
  })
}

/**
 * 根据id查询角色信息
 * @param id
 * @returns {AxiosPromise}
 */
export function executeGetRoleInfo (roleId) {
  return request({
    url: `/admin/sys/role/info/${roleId}`,
    method: 'get',
    data: roleId
  })
}
