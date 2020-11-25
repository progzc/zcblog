import request from 'network/request'
import { encryptAES } from 'common/js/utils/encrypt'

/**
 * 获取当前登录用户信息
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

/**
 * 查询用户列表
 * @param currentPage 当前页
 * @param pageSize 每页记录数
 * @param keyWord 搜索关键词
 * @returns {AxiosPromise}
 */
export function executeGetUserList (currentPage, pageSize, keyWord) {
  return request({
    url: '/admin/sys/user/list',
    method: 'get',
    params: {
      currentPage: currentPage,
      pageSize: pageSize,
      keyWord: keyWord
    }
  }, false) // 添加时间戳会导致缓存"失效"
}

/**
 * 删除用户
 * @param userIds 用户id数组
 * @returns {AxiosPromise}
 */
export function executeDeleteUser (userIds) {
  return request({
    url: '/admin/sys/user/delete',
    method: 'delete',
    data: userIds
  }, false) // 添加时间戳会出错
}

/**
 * 根据用户id获取用户信息
 * @param userId
 * @returns {AxiosPromise}
 */
export function executeGetUserRoleInfo (userId) {
  return request({
    url: `/admin/sys/user/info/${userId}`,
    method: 'get'
  })
}

/**
 * 新增或修改用户信息
 * @param userId
 * @param dataForm
 * @returns {AxiosPromise}
 */
export function executeSubmitUserInfo (dataForm) {
  return request({
    url: `/admin/sys/user/${!dataForm.id ? 'save' : 'update'}`,
    method: `${!dataForm.id ? 'post' : 'put'}`,
    data: {
      userId: dataForm.id || undefined,
      username: encryptAES(dataForm.username),
      password: encryptAES(dataForm.password),
      phone: dataForm.phone,
      email: dataForm.email,
      status: dataForm.status,
      roleIdList: dataForm.roleIdList
    }
  })
}
