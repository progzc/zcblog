<template>
  <div class="home-side-bar">
    <mobile-side-bar id="mobile-side-bar"></mobile-side-bar>
    <div id="side-bar-footer">
      <div class="side-bar-choose">
        <span class="toc-nav-choose" :class="{'selected': show}" @click="tocNavClick">
          {{$t('homeNav.content')}}
        </span>
        <span class="home-nav-choose" :class="{'selected': !show}" @click="homeNavClick">
          {{$t('homeNav.sitePreview')}}
        </span>
      </div>
      <div class="side-bar-home-nav" v-show="!show">
        <desk-side-bar id="desk-side-bar"></desk-side-bar>
        <site-footer id="site-footer"></site-footer>
      </div>
      <div class="side-bar-toc-nav" v-show="show">
        <!--在这里自动生成目录-->
        <div class="content-toc" id="content-toc" ref="content-toc">
          这里是自动生成的目录abc
        </div>
      </div>
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
      show: true
    }
  },
  methods: {
    tocNavClick () {
      if (this.show !== true) {
        this.show = true
      }
    },
    homeNavClick () {
      if (this.show !== false) {
        this.show = false
      }
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
      .side-bar-choose
        margin-top 20px
        text-align center
        height 2rem
        line-height 2rem
        font-weight 400
        font-size 15px
        span
          display inline-block
          padding 0 2px
          &:hover
            cursor pointer
        span:first-of-type
          margin-right 13px
        .selected
          border-bottom 2.5px solid $color-nav
          color $color-nav
      .side-bar-home-nav
        #desk-side-bar
          >>>.side-bar-sticky
            padding-top 10px !important
        #site-footer
          position absolute
          left 50%
          width 100%
          margin-left -50%
          bottom 10px
      .side-bar-toc-nav
        padding 10px 10px
        .content-toc
          padding 0.6rem
          margin 0.4rem
          border-radius 6px
          background-color $color-content-background
          font-weight 400
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
