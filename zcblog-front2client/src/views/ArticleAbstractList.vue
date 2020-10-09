<template>
  <content-box>
    <div class="article-abstract-container" slot="container">
      <!--文章摘要内容-->
      <div class="article-abstract-item" v-for="article in articleAbstractList" :key="article.id">
        <h1><a href="javascript:void(0);" @click="handleToArticle(article.id)">{{article.title}}</a></h1>
        <div class="article-abstract-info">
          <div class="edit-time-group"><span class="iconfont">&#xe503;</span>{{article.createTime}}</div>
          <span class="left-separator">|</span>
          <div class="update-time-group"><span class="iconfont">&#xe50a;</span>{{article.createTime}}</div>
          <div class="read-num-group"><span class="iconfont">&#xe63f;</span>{{article.readNum}}</div>
          <span class="right-separator">|</span>
          <div class="like-num-group"><span class="iconfont">&#xe504;</span>{{article.likeNum}}</div>
        </div>
        <p>{{article.description}}</p>
      </div>
      <!--页码-->
      <div class="article-abstract-page-container" v-if="checkPage()">
        <iv-page
          class-name="article-abstract-pagination"
          :total="pagination.total"
          :current="pagination.currentPage"
          :pageSize="pagination.pageSize"  >
          @on-change="handleCurrentChange"
        </iv-page>
      </div>
    </div>
  </content-box>
</template>

<script type="text/ecmascript-6">
import ContentBox from 'components/content/ContentBox'

export default {
  name: 'ArticleAbstractList',
  components: {
    'content-box': ContentBox
  },
  data () {
    return {
      pagination: {
        total: 100,
        currentPage: 1,
        pageSize: 10
      },
      articleAbstractList: [
        {
          id: 0,
          title: 'Spring Batch异常处理',
          description: `Spring Batch处理任务过程中如果发生了异常，默认机制是马上停止任务执行，抛出相应异常，
          如果任务还包含未执行的步骤也不会被执行。要改变这个默认规则，我们可以配置异常重试和异常跳过机制。
          异常跳过：遇到异常的时候不希望结束任务，而是跳过这个异常，继续执行；异常重试：遇到异常的时候经过指定次数的重试，
          如果还是失败的话，才会停止任务。除了这两个特性外，本文也会记录一些别的特性。`,
          createTime: '2020-03-12',
          updateTime: '2020-03-12',
          readNum: 10002,
          likeNum: 1004
        },
        {
          id: 1,
          title: 'Spring Batch异常处理',
          description: `Spring Batch处理任务过程中如果发生了异常，默认机制是马上停止任务执行，抛出相应异常，
          如果任务还包含未执行的步骤也不会被执行。要改变这个默认规则，我们可以配置异常重试和异常跳过机制。
          异常跳过：遇到异常的时候不希望结束任务，而是跳过这个异常，继续执行；异常重试：遇到异常的时候经过指定次数的重试，
          如果还是失败的话，才会停止任务。除了这两个特性外，本文也会记录一些别的特性。`,
          createTime: '2020-03-12',
          updateTime: '2020-03-12',
          readNum: 10002,
          likeNum: 1004
        },
        {
          id: 2,
          title: 'Spring Batch异常处理',
          description: `Spring Batch处理任务过程中如果发生了异常，默认机制是马上停止任务执行，抛出相应异常，
          如果任务还包含未执行的步骤也不会被执行。要改变这个默认规则，我们可以配置异常重试和异常跳过机制。
          异常跳过：遇到异常的时候不希望结束任务，而是跳过这个异常，继续执行；异常重试：遇到异常的时候经过指定次数的重试，
          如果还是失败的话，才会停止任务。除了这两个特性外，本文也会记录一些别的特性。`,
          createTime: '2020-03-12',
          updateTime: '2020-03-12',
          readNum: 10002,
          likeNum: 1004
        },
        {
          id: 3,
          title: 'Spring Batch异常处理',
          description: `Spring Batch处理任务过程中如果发生了异常，默认机制是马上停止任务执行，抛出相应异常，
          如果任务还包含未执行的步骤也不会被执行。要改变这个默认规则，我们可以配置异常重试和异常跳过机制。
          异常跳过：遇到异常的时候不希望结束任务，而是跳过这个异常，继续执行；异常重试：遇到异常的时候经过指定次数的重试，
          如果还是失败的话，才会停止任务。除了这两个特性外，本文也会记录一些别的特性。`,
          createTime: '2020-03-12',
          updateTime: '2020-03-12',
          readNum: 10002,
          likeNum: 1004
        },
        {
          id: 4,
          title: 'Spring Batch异常处理',
          description: `Spring Batch处理任务过程中如果发生了异常，默认机制是马上停止任务执行，抛出相应异常，
          如果任务还包含未执行的步骤也不会被执行。要改变这个默认规则，我们可以配置异常重试和异常跳过机制。
          异常跳过：遇到异常的时候不希望结束任务，而是跳过这个异常，继续执行；异常重试：遇到异常的时候经过指定次数的重试，
          如果还是失败的话，才会停止任务。除了这两个特性外，本文也会记录一些别的特性。`,
          createTime: '2020-03-12',
          updateTime: '2020-03-12',
          readNum: 10002,
          likeNum: 1004
        },
        {
          id: 5,
          title: 'Spring Batch异常处理',
          description: `Spring Batch处理任务过程中如果发生了异常，默认机制是马上停止任务执行，抛出相应异常，
          如果任务还包含未执行的步骤也不会被执行。要改变这个默认规则，我们可以配置异常重试和异常跳过机制。
          异常跳过：遇到异常的时候不希望结束任务，而是跳过这个异常，继续执行；异常重试：遇到异常的时候经过指定次数的重试，
          如果还是失败的话，才会停止任务。除了这两个特性外，本文也会记录一些别的特性。`,
          createTime: '2020-03-12',
          updateTime: '2020-03-12',
          readNum: 10002,
          likeNum: 1004
        },
        {
          id: 6,
          title: 'Spring Batch异常处理',
          description: `Spring Batch处理任务过程中如果发生了异常，默认机制是马上停止任务执行，抛出相应异常，
          如果任务还包含未执行的步骤也不会被执行。要改变这个默认规则，我们可以配置异常重试和异常跳过机制。
          异常跳过：遇到异常的时候不希望结束任务，而是跳过这个异常，继续执行；异常重试：遇到异常的时候经过指定次数的重试，
          如果还是失败的话，才会停止任务。除了这两个特性外，本文也会记录一些别的特性。`,
          createTime: '2020-03-12',
          updateTime: '2020-03-12',
          readNum: 10002,
          likeNum: 1004
        },
        {
          id: 7,
          title: 'Spring Batch异常处理',
          description: `Spring Batch处理任务过程中如果发生了异常，默认机制是马上停止任务执行，抛出相应异常，
          如果任务还包含未执行的步骤也不会被执行。要改变这个默认规则，我们可以配置异常重试和异常跳过机制。
          异常跳过：遇到异常的时候不希望结束任务，而是跳过这个异常，继续执行；异常重试：遇到异常的时候经过指定次数的重试，
          如果还是失败的话，才会停止任务。除了这两个特性外，本文也会记录一些别的特性。`,
          createTime: '2020-03-12',
          updateTime: '2020-03-12',
          readNum: 10002,
          likeNum: 1004
        }
      ]
    }
  },
  created () {
    this.findPage()
  },
  methods: {
    checkPage () {
      return this.articleAbstractList.length < this.pagination.pageSize
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
      // 发送Ajax请求，提交分页相关参数
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
  .article-abstract-container
    .article-abstract-item
      display block
      padding 1rem 1rem 1rem 1rem
      /*margin-top 40px*/
      margin 40px 10px 0 10px
      border-radius 6px
      background-color $color-content-background
      &:hover
        -webkit-box-shadow 0 0 10px rgba(0,0,0,.3)
        -moz-box-shadow 0 0 10px rgba(0,0,0,.3)
        box-shadow 0 0 10px rgba(0,0,0,.3)
      &:first-of-type
        margin-top 10px
      &:last-of-type
        margin-bottom $footer-height-pageContent + 10px
      h1>a
        height 1.6rem
        line-height 1.6rem
        text-align left
        font-size 18px
        font-weight 400
        &:hover
          color $color-on-hover
          cursor pointer
      .article-abstract-info
        overflow hidden
        font-weight 400
        opacity 0.6
        &>div
          &:hover
            color $color-on-hover
            cursor pointer
          .iconfont
            margin-right 5px
        .edit-time-group
          float left
          margin 5px 10px 5px 0
        .left-separator
          float left
          margin 5px 0
        .update-time-group
          float left
          margin 5px 0 5px 10px
        .like-num-group
          float right
          margin 5px 10px 5px 0
        .right-separator
          float right
          margin 5px 0
        .read-num-group
          float right
          margin 5px 10px 5px 10px
      p
        font-weight 400
        font-size 14px
        text-indent 1.8em
        line-height 1.8em
    .article-abstract-page-container
      padding 50px 0 80px 0
      height 1.5rem
      line-height 1.5rem
      text-align center
      .article-abstract-pagination
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
  @media screen and (max-width: $size-md)
    .read-num-group, .right-separator, .like-num-group
      display none !important
  @media screen and (max-width: $size-sm)
    .article-abstract-page-container
      margin-bottom 100px
    .article-abstract-item:last-of-type
      margin-bottom $footer-height-pageContent + 150px !important
    >>>.content-container
      width 100% !important
</style>
