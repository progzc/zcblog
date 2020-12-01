import request from 'network/request'

/**
 * 查询标签列表
 * @param currentPage 当前页
 * @param pageSize 每页记录数
 * @param keyWord 搜索关键词
 * @returns {*}
 */
export function executeGetTagList (currentPage, pageSize, keyWord) {
  return request({
    url: '/admin/operation/tag/list',
    method: 'get',
    params: {
      currentPage: currentPage,
      pageSize: pageSize,
      keyWord: keyWord
    }
  }, false)
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
    data: ids
  }, false) // 添加时间戳会出错
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
      id: !id ? null : id,
      name: tag.name,
      type: tag.type
    }
  })
}

/**
 * 根据标签类别查询所有标签
 * @param type
 * @returns {AxiosPromise}
 */
export function executeGetTagsByType (type) {
  return request({
    url: '/admin/operation/tag/select',
    method: 'get',
    params: {
      type: type
    }
  })
}
