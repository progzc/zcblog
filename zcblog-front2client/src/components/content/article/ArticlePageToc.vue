<template>
  <div class="article-page-toc"></div>
</template>

<script type="text/ecmascript-6">
export default {
  name: 'ArticlePageToc'
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus">
  .article-page-toc
    overflow-y auto
    max-height 90vh
    padding 0.6rem
    margin 0.4rem
    border-radius 6px
    background-color $color-content-background
    font-weight 400
    .toc-list
      position relative
      margin 0
      padding-left 10px
      list-style none
      .toc-list-item
        &>a.toc-link
          display inline-block
          overflow hidden
          white-space nowrap
          text-overflow ellipsis
          width 100%
          height 18px
          line-height 18px
    .toc-link
      color black
      height 100%
    .is-active-link
      font-weight 400
      color $color-nav
    .is-collapsible
      overflow hidden
      padding-left 10px
      max-height 1000px
      transition all 300ms ease-in-out
    .is-collapsed
      max-height 0
    .is-position-fixed
      position fixed !important
      top 0
</style>
