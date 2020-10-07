<template>
  <div class="article-timeline">
    <header class="article-timeline-header"></header>
    <div class="article-timeline-container">
      <time-line :itemList="articleTimelineList" :temp="temp" @linkToArticle="handleToArticle"></time-line>
    </div>
    <div class="article-timeline-page-container" v-if="checkPage()">
      <iv-page
        class-name="article-timeline-pagination"
        :total="pagination.total"
        :current="pagination.currentPage"
        :pageSize="pagination.pageSize"  >
        @on-change="handleCurrentChange"
      </iv-page>
    </div>
    <footer class="article-timeline-footer"></footer>
  </div>
</template>

<script type="text/ecmascript-6">
import HashMap from 'common/js/HashMap'
import TimeLine from 'components/content/TimeLine'

export default {
  name: 'ArticleTimeLine',
  components: {
    'time-line': TimeLine
  },
  data () {
    return {
      temp: undefined,
      pagination: {
        total: 100,
        currentPage: 1,
        pageSize: 45
      },
      articleTimelineList: [// 降序排出
        { id: 0, createdTime: '2020-05-17', articleTitle: 'Java数字签名算法' },
        { id: 1, createdTime: '2020-05-15', articleTitle: 'Java非对称加密算法RSA' },
        { id: 2, createdTime: '2020-05-06', articleTitle: 'Java对称加密算法' },
        { id: 3, createdTime: '2020-04-28', articleTitle: 'Java消息摘要算法' },
        { id: 4, createdTime: '2020-04-21', articleTitle: 'Java Base64算法' },
        { id: 5, createdTime: '2020-04-01', articleTitle: 'Java设计模式学习' },
        { id: 6, createdTime: '2020-03-29', articleTitle: 'Kafka API使用' },
        { id: 7, createdTime: '2020-03-28', articleTitle: 'Kafka消费者' },
        { id: 8, createdTime: '2020-03-26', articleTitle: 'Kafka生产者' },
        { id: 9, createdTime: '2020-03-21', articleTitle: 'Java数字签名算法' },
        { id: 10, createdTime: '2020-03-20', articleTitle: 'Java非对称加密算法RSA' },
        { id: 11, createdTime: '2020-03-17', articleTitle: 'Java数字签名算法' },
        { id: 12, createdTime: '2020-03-15', articleTitle: 'Java非对称加密算法RSA' },
        { id: 13, createdTime: '2020-03-06', articleTitle: 'Java对称加密算法' },
        { id: 14, createdTime: '2020-02-21', articleTitle: 'Java Base64算法' },
        { id: 15, createdTime: '2019-10-01', articleTitle: 'Java设计模式学习' },
        { id: 16, createdTime: '2019-09-29', articleTitle: 'Kafka API使用' },
        { id: 17, createdTime: '2019-08-28', articleTitle: 'Kafka消费者' },
        { id: 18, createdTime: '2019-08-26', articleTitle: 'Kafka生产者' },
        { id: 19, createdTime: '2019-07-17', articleTitle: 'Java数字签名算法' },
        { id: 20, createdTime: '2019-05-15', articleTitle: 'Java非对称加密算法RSA' },
        { id: 21, createdTime: '2019-05-14', articleTitle: 'Java非对称加密算法RSA' },
        { id: 22, createdTime: '2019-05-06', articleTitle: 'Java对称加密算法' },
        { id: 23, createdTime: '2019-04-28', articleTitle: 'Java消息摘要算法' },
        { id: 24, createdTime: '2019-04-21', articleTitle: 'Java Base64算法' },
        { id: 25, createdTime: '2019-04-01', articleTitle: 'Java设计模式学习' },
        { id: 26, createdTime: '2019-03-29', articleTitle: 'Kafka API使用' },
        { id: 27, createdTime: '2019-03-28', articleTitle: 'Kafka消费者' },
        { id: 28, createdTime: '2019-03-26', articleTitle: 'Kafka生产者' },
        { id: 29, createdTime: '2018-05-17', articleTitle: 'Java数字签名算法' },
        { id: 30, createdTime: '2018-05-15', articleTitle: 'Java非对称加密算法RSA' },
        { id: 31, createdTime: '2018-05-15', articleTitle: 'Java非对称加密算法RSA' },
        { id: 32, createdTime: '2018-05-06', articleTitle: 'Java对称加密算法' },
        { id: 33, createdTime: '2018-04-28', articleTitle: 'Java消息摘要算法' },
        { id: 34, createdTime: '2018-04-21', articleTitle: 'Java Base64算法' },
        { id: 35, createdTime: '2018-04-01', articleTitle: 'Java设计模式学习' },
        { id: 36, createdTime: '2018-03-29', articleTitle: 'Kafka API使用' },
        { id: 37, createdTime: '2018-03-28', articleTitle: 'Kafka消费者' },
        { id: 38, createdTime: '2018-03-26', articleTitle: 'Kafka生产者' },
        { id: 39, createdTime: '2018-03-17', articleTitle: 'Java数字签名算法' },
        { id: 40, createdTime: '2018-03-15', articleTitle: 'Java非对称加密算法RSA' },
        { id: 41, createdTime: '2018-03-14', articleTitle: 'Java非对称加密算法RSA' },
        { id: 42, createdTime: '2018-03-06', articleTitle: 'Java对称加密算法' },
        { id: 43, createdTime: '2018-02-28', articleTitle: 'Java消息摘要算法' },
        { id: 44, createdTime: '2018-02-21', articleTitle: 'Java Base64算法' },
        { id: 45, createdTime: '2018-02-01', articleTitle: 'Java设计模式学习' },
        { id: 46, createdTime: '2018-01-29', articleTitle: 'Kafka API使用' },
        { id: 47, createdTime: '2018-01-28', articleTitle: 'Kafka消费者' },
        { id: 48, createdTime: '2018-01-26', articleTitle: 'Kafka生产者' },
        { id: 49, createdTime: '2018-01-25', articleTitle: 'Java数字签名算法' },
        { id: 50, createdTime: '2018-01-24', articleTitle: 'Java非对称加密算法RSA' }
      ]
    }
  },
  created () {
    this.temp = new HashMap()
    this.findPage()
  },
  methods: {
    checkPage () {
      return this.articleTimelineList.length > this.pagination.pageSize
    },
    handleCurrentChange (currentPage) {
      this.pagination.currentPage = currentPage
      this.findPage()
    },
    findPage () {
      var param = { // 封装查询条件
        currentPage: this.pagination.currentPage,
        pageSize: this.pagination.pageSize
      }
      // 发送Ajax请求，提交分页相关参数，在赋值之前先清空temp
      console.log(param)
    },
    handleToArticle (id) {
      console.log('跳转到文章' + id)
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  @import '~common/stylus/index.styl'
  .article-timeline
    .article-timeline-header
      display block
      height $header-height-pageContent
    .article-timeline-container
      overflow hidden
      padding 1rem 1rem 1rem 1rem
      margin 0 auto
      width 75%
      border-radius 6px
      background-color $color-content-background
    .article-timeline-page-container
      padding 50px 0 50px 0
      height 1.5rem
      line-height 1.5rem
      text-align center
      .article-timeline-pagination
        >>>.ivu-page-item
          width 20px !important
          min-width 20px !important
          border none !important
          border-radius 0 !important
          background none !important
        >>>.ivu-page-item-active
          border none !important
          background-color $color-on-hover !important
        >>>a
          margin 0 !important
          color #515a6e
          font-family $body-font !important
        >>>.ivu-page-prev
          border none !important
          border-radius 0 !important
          background none !important
        >>>.ivu-page-next
          border none !important
          border-radius 0 !important
          background none !important
    .article-timeline-footer
      display block
      height $footer-height-pageContent
</style>
