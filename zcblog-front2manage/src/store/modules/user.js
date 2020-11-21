import {
  UPDATE_ARTICLE_NUM,
  UPDATE_COMMENT_NUM, UPDATE_LIKE_NUM,
  UPDATE_USER_ID,
  UPDATE_USER_NAME, UPDATE_VISITOR_NUM
} from 'store/constant/mutation-types'

export default {
  namespace: true, // 启用命名空间
  state: {
    id: 0,
    name: '',
    visitorNum: 10555,
    commentNum: 81212,
    likeNum: 9280,
    articleNum: 13600
  },
  mutations: {
    [UPDATE_USER_ID] (state, id) {
      state.id = id
    },
    [UPDATE_USER_NAME] (state, name) {
      state.name = name
    },
    [UPDATE_VISITOR_NUM] (state, visitorNum) {
      state.visitorNum = visitorNum
    },
    [UPDATE_COMMENT_NUM] (state, commentNum) {
      state.commentNum = commentNum
    },
    [UPDATE_LIKE_NUM] (state, likeNum) {
      state.likeNum = likeNum
    },
    [UPDATE_ARTICLE_NUM] (state, articleNum) {
      state.articleNum = articleNum
    }
  }
}
