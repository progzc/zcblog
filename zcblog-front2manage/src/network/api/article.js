import request from 'network/request'

/**
 * 根据id查询文章信息
 * @param articleId
 * @returns {AxiosPromise}
 */
export function executeGetArticleInfo (articleId) {
  return request({
    url: `/admin/article/info/${articleId}`,
    method: 'get'
  })
}

/**
 * 新增或更新文章
 * @param id
 * @param article
 * @returns {AxiosPromise}
 */
export function executeSubmitArticleInfo (article) {
  return request({
    url: `/admin/article/${!article.id ? 'save' : 'update'}`,
    method: `${!article.id ? 'post' : 'put'}`,
    data: article
  })
}

/**
 * 上传文章中的图片
 * @param formData
 * @returns {AxiosPromise}
 */
export function executeImgUpload (formData) {
  return request({
    url: '/admin/oss/resource/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }, false)
}

/**
 * 删除文章中的图片
 * @param url 图片的url
 */
export function executeImgDelete (url) {
  return request({
    url: '/admin/oss/resource/delete',
    method: 'delete',
    params: {
      url: url
    }
  })
}

/**
 * 查询文章列表
 * @param currentPage 当前页码
 * @param pageSize 每页记录数
 * @param keyWord 关键词
 * @returns {AxiosPromise}
 */
export function executeGetArticleList (currentPage, pageSize, keyWord) {
  return request({
    url: '/admin/article/list',
    method: 'get',
    params: {
      currentPage: currentPage,
      pageSize: pageSize,
      keyword: keyWord
    }
  }, false)
}

/**
 * 批量删除文章
 * @param articleIds 文章的id数组
 * @returns {AxiosPromise}
 */
export function executeDeleteArticle (articleIds) {
  return request({
    url: '/admin/article/delete',
    method: 'delete',
    data: articleIds
  }, false)
}

/**
 * 更新文章状态：发布/置顶/推荐/
 * @param data
 * @returns {AxiosPromise}
 */
export function executeUpdateStatus (data) {
  return request({
    url: '/admin/article/update/status',
    method: 'put',
    data: data
  })
}

/**
 * 刷新缓存
 * @returns {AxiosPromise}
 */
export function executeRefresh () {
  return request({
    url: '/admin/article/cache/refresh',
    method: 'delete'
  })
}
