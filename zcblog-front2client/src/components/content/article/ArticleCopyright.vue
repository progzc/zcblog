<template>
  <div class="article-copyright">
    <p>{{$t('homeNav.articleAuthor')}}：<span>{{$t('homeNav.pseudonym')}}</span></p>
    <p>{{$t('homeNav.articleLink')}}：<span>{{articleLink}}</span></p>
    <p>{{$t('homeNav.artileCopyright')}}：
        <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/" target="_blank">
          <span>{{$t('homeNav.copyrightDeclaration')}}</span>
        </a>
    </p>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  name: 'ArticleCopyright',
  data () {
    return {
      articleLink: ''
    }
  },
  mounted () {
    this.articleLink = window.location.href
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  .article-copyright
    margin 1.5rem auto 1rem auto
    padding 0.5rem 1em
    border-left 3px solid $color-nav
    background-color $color-gradually-gray-101
    p
      height auto
      line-height 1.4rem
      font-weight 600
      color #6DB33F
      a
        color #515A6E
      span
        font-weight 400
        color #515A6E
        overflow-wrap break-word
</style>
