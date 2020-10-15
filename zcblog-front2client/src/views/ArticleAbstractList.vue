<template>
  <content-box>
    <div class="article-abstract-container" slot="content">
      <!--文章摘要内容-->
      <div class="article-abstract-item" v-for="article in articleAbstractList" :key="article.id">
        <h1><a href="javascript:void(0);" @click="handleToArticle(article.id)">
          {{article.title}}</a><span class="toTop iconfont" v-if="article.top===1">[{{$t('homeNav.toTop')}}]</span>
        </h1>
        <article-info :article="article"></article-info>
        <p>{{article.description}}
          <span class="article-link" @click="handleToArticle(article.id)">
            {{$t('homeNav.seeMore')}}
          </span>
        </p>
        <tag-wall :tagList="article.tagList"></tag-wall>
      </div>
      <!--页码-->
      <div class="article-abstract-page-container" v-if="checkPage()">
        <iv-page
          class-name="article-abstract-pagination"
          :total="pagination.total"
          :current="pagination.currentPage"
          :pageSize="pagination.pageSize" >
          @on-change="handleCurrentChange"
        </iv-page>
      </div>
    </div>
  </content-box>
</template>

<script type="text/ecmascript-6">
import ContentBox from 'components/content/ContentBox'
import TagWall from 'components/content/TagWall'
import ArticleInfo from 'components/content/ArticleInfo'

export default {
  name: 'ArticleAbstractList',
  components: {
    'content-box': ContentBox,
    'tag-wall': TagWall,
    'article-info': ArticleInfo
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
          top: 1,
          tagList: [{ id: 0, name: 'Java' },
            { id: 1, name: 'RabbitMQ' },
            { id: 2, name: 'ElasticSearch' },
            { id: 3, name: 'Vue' }],
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
          top: 1,
          tagList: [{ id: 0, name: 'Linux' },
            { id: 1, name: 'SpringBoot' },
            { id: 2, name: 'SpringCloud' },
            { id: 3, name: 'Nuxt.js' }],
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
      return this.articleAbstractList.length > this.pagination.pageSize
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
      this.$router.push({ path: '/article/java' })
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  @import '~common/stylus/index.styl'
  .article-abstract-container
    .article-abstract-item
      display block
      padding 1rem 1.2rem 1rem 1.2rem
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
        margin-bottom $footer-height-pageContent
      h1>a
        height 1.6rem
        line-height 1.6rem
        text-align left
        font-size 20px
        font-weight 400
        &:hover
          color $color-on-hover
          cursor pointer
          border-bottom 1px solid $color-on-hover
      h1>span
        margin-left 5px
        color $color-nav
        line-height 1.8rem
        height 1.8rem
        font-size 1.2rem
        font-weight 500
      p
        font-weight 400
        font-size 14px
        text-indent 1.6rem
        line-height 1.6rem
        text-align justify
        .article-link
          color $color-on-hover
          &:hover
            border-bottom  1px solid $color-on-hover
            cursor pointer
          &:after
            content '\e517'
            margin-left 0
            font-family: "iconfont" !important;
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
  >>>.content-header //解决添加阴影导致的盒子下沉的效果(若不下沉,盒子上边阴影又无法显现)
    height $header-height-pageContent - 10px !important
  @media screen and (max-width: $size-sm)
    >>>.content-container
      width 100% !important
    >>>.content-header
      height 0 !important
    .article-abstract-item:first-of-type
      margin-top 0 !important // 解决移动端上边距不一致问题（根源均在于为添加上边阴影导致的问题）
</style>
