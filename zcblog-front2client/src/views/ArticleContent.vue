<template>
  <content-box>
    <div class="article-container" slot="container">
      <p class="article-title">{{article.title}}</p>
    </div>
  </content-box>
</template>

<script type="text/ecmascript-6">
import ContentBox from 'components/content/ContentBox'
export default {
  name: 'ArticleContent',
  components: {
    'content-box': ContentBox
  },
  data () {
    return {
      article: {
        title: 'CI/CD实践笔记'
      }
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  @import '~common/stylus/index.styl'
  .article-container
    display block
    padding 1rem 1rem 1rem 1rem
    margin-bottom $footer-height-pageContent
    border-radius 6px
    background-color $color-content-background
</style>
