<template>
  <div class="content-box">
    <header class="content-header"></header>
    <!--文章内容-->
    <div class="content-container">
      <slot name="content"></slot>
    </div>
    <!--评论内容-->
    <div class="common-container">
      <slot name="common"></slot>
    </div>
    <div class="content-site-footer">
      <site-footer></site-footer>
    </div>
    <transition name="slide-fade">
      <div class="scroll-progress-bar" @click="scroll2Top" v-show="show">
        {{scrollPercent}}
      </div>
    </transition>
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
      show: false,
      scrollPercent: 0
    }
  },
  mounted () {
    window.addEventListener('scroll', this.windowScroll)
  },
  methods: {
    windowScroll () {
      // 滚动条距离顶部距离
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
      // 窗口高度
      const windowHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight
      // 滚动条内容总高度
      const scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight
      this.scrollPercent = Math.round(scrollTop / (scrollHeight - windowHeight) * 100)
      if (this.scrollPercent === 0) {
        this.show = false
      } else {
        this.show = true
      }
    },
    scroll2Top () {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      })
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
    .content-container,.common-container
      overflow hidden
      margin 0 auto
      width 75%
    .content-site-footer
      display none
    .scroll-progress-bar
      display inline-block
      position fixed
      padding-top 1.5px
      bottom 30px
      right 30px
      text-align center
      color white
      font-weight normal
      width 1.7rem
      height 1.7rem
      line-height 1.7rem
      border-radius 50%
      background $color-gradually-gray-31
      &:hover
        cursor pointer
        background $color-on-hover
  .slide-fade-enter-active ,.slide-fade-leave-active
    transition all .2s ease
  .slide-fade-leave-to ,.slide-fade-enter
    transform translateY(70px)
  @media screen and (max-width: $size-xxl)
    .content-box
      left 20%
      width 80%
  @media screen and (max-width: $size-xl)
    .content-box
      left 25%
      width 75%
      .content-container,.common-container
        width 90%
      .scroll-progress-bar
        display none
  @media screen and (max-width: $size-lg)
    .content-box
      left 30%
      width 70%
      .content-container,.common-container
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
      .content-container,.common-container
        overflow hidden
        margin 0 auto
        width 95%
      .content-site-footer
        display block
    >>>.side-bar-footer
      background-color $color-footer-mobile
      padding 15px 0 10px 0
      color white
</style>
