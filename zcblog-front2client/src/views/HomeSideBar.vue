<template>
  <div class="home-side-bar">
    <mobile-side-bar id="mobile-side-bar"></mobile-side-bar>
    <div id="side-bar-footer">
      <desk-side-bar id="desk-side-bar"></desk-side-bar>
      <site-footer id="site-footer"></site-footer>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import MobileSideBar from 'components/content/Nav/MobileSideBar'
import DeskSideBar from 'components/content/Nav/DeskSideBar'
import SiteFooter from 'components/content/SiteFooter'

export default {
  name: 'HomeSideBar',
  components: {
    'mobile-side-bar': MobileSideBar,
    'desk-side-bar': DeskSideBar,
    'site-footer': SiteFooter
  },
  data () {
    return {
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  .home-side-bar
    #mobile-side-bar
      display none
    #side-bar-footer
      display block
      position fixed
      left 0
      top 0
      bottom 0
      background $color-page-background
      width 20%
      border-right 1px solid $color-border
      #site-footer
        position absolute
        left 50%
        width 100%
        margin-left -50%
        bottom 0
  @media screen and (max-width: $size-xxl )
    #side-bar-footer
      width 20% !important
  @media screen and (max-width: $size-xl )
    #side-bar-footer
      width 25% !important
  @media screen and (max-width: $size-lg )
    #side-bar-footer
      width 30% !important
  @media screen and (max-width: $size-sm)
    #side-bar-footer
      display none !important
    #mobile-side-bar
      display block !important
</style>
