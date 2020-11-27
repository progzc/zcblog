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

/**
 * 分页查询当前用户所创建的角色列表
 * @param currentPage 当前页
 * @param pageSize 每页记录数
 * @param keyWord 关键词
 * @returns {AxiosPromise}
 */
export function executeGetRoleList (currentPage, pageSize, keyWord) {
  return request({
    url: '/admin/sys/role/list',
    method: 'get',
    params: {
      currentPage: currentPage,
      pageSize: pageSize,
      keyWord: keyWord
    }
  }, false)
}

/**
 * 删除角色
 * @param ids
 * @returns {AxiosPromise}
 */
export function executeDeleteRole (roleIds) {
  return request({
    url: '/admin/sys/role/delete',
    method: 'delete',
    data: roleIds
  }, false)
}

/**
 * 新增或更新角色
 * @param roleId
 * @param roleName
 * @param remark
 * @param menuIdList
 * @returns {AxiosPromise}
 */
export function executeSubmitRoleInfo (roleId, roleName, remark, menuIdList) {
  return request({
    url: `/admin/sys/role/${!roleId ? 'save' : 'update'}`,
    method: !roleId ? 'post' : 'put',
    data: {
      roleId: roleId || undefined,
      roleName: roleName,
      remark: remark,
      menuIdList: menuIdList
    }
  })
}
