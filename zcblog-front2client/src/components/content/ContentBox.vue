<template>
  <div class="content-box">
    <header class="content-header"></header>
    <div class="content-container">
      <slot name="container"></slot>
    </div>
    <div class="content-site-footer">
      <site-footer></site-footer>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import SiteFooter from 'components/content/SiteFooter'
export default {
  name: 'ContentBox',
  components: {
    'site-footer': SiteFooter
  },
  data () {
    return {
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  @import '~common/stylus/index.styl'
  .content-box
    position relative
    left 20%
    background $color-page-background
    width 80%
    min-height 100vh
    .content-header
      display block
      height $header-height-pageContent
    .content-container
      overflow hidden
      margin 0 auto
      width 75%
    .content-site-footer
      display none
      position absolute
      width 300px
      bottom 10px
      left 50%
      margin-left -150px
  @media screen and (max-width: $size-xl)
    .content-box
      left 25%
      width 75%
      .content-container
        width 90%
  @media screen and (max-width: $size-lg)
    .content-box
      left 30%
      width 70%
      .content-container
        width 95%
  @media screen and (max-width: $size-sm)
    .content-box
      position relative
      left 0
      width 100%
      background $color-page-background
      .content-header
        display block
        height 0
      .content-container
        overflow hidden
        margin 0 auto
        width 95%
      .content-site-footer
        display block
</style>
