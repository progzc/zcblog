import request from 'network/request'

/**
 * 查询标签列表
 * @param currentPage
 * @param pageSize
 * @param keyWord
 * @returns {*}
 */
export function executeGetTagList (currentPage, pageSize, keyWord) {
  return request({
    url: '/admin/operation/tag/list',
    method: 'get',
    params: {
      page: currentPage,
      limit: pageSize,
      key: keyWord
    }
  })
}

/**
 * 删除标签[列表]
 * @param ids
 * @returns {*}
 */
export function executeDeleteTag (ids) {
  return request({
    url: '/admin/operation/tag/delete',
    method: 'delete',
    data: {
      ids: ids
    }
  })
}

/**
 * 根据标签id查询标签
 * @param id
 * @returns {*}
 */
export function executeGetTagById (id) {
  return request({
    url: `/admin/operation/tag/info/${id}`,
    method: 'get'
  })
}

/**
 * 修改或新增标签
 * @param id
 * @param tag
 * @returns {*}
 */
export function executePostOrPutTag (id, tag) {
  return request({
    url: `/admin/operation/tag/${!id ? 'save' : 'update'}`,
    method: !id ? 'post' : 'put',
    data: {
      tag: tag
    }
  })
}
