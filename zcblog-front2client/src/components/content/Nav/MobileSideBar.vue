<template>
  <div class="mobile-side-bar">
    <div class="header-inner">
      <!--移动端横幅Banner-->
      <div class="site-brand-wrapper">
        <div class="site-nav-toggle" @click="show=!show"><span class="iconfont">&#xe660;</span></div>
        <div class="site-title"><span class="blog-pseudonym">{{$t('homeNav.pseudonym')}}</span></div>
      </div>
      <!--移动端菜单栏-->
      <div class="site-nav" v-show="show">
        <ul class="menu">
          <li class="menu-item-home">
            <a href="/"><span class="iconfont">&#xe501;</span>{{$t('homeNav.home')}}</a>
          </li>
          <li class="menu-item-tags">
            <a href="/tags"><span class="iconfont">&#xe613;</span>{{$t('homeNav.tags')}}</a>
          </li>
          <li class="menu-item-timeline">
            <a href="/timeline"><span class="iconfont">&#xe7e3;</span>{{$t('homeNav.timeline')}}</a>
          </li>
          <li class="menu-item-gallery">
            <a href="#"><span class="iconfont">&#xe891;</span>{{$t('homeNav.gallery')}}</a>
          </li>
          <li class="menu-item-search">
            <span class="iconfont">&#xe505;</span>
            <input class="mobile-search" :placeholder="$t('homeNav.searchPlaceholder')" v-model="keyWord" />
            <div class="site-language">
              <iv-switch class="mobile-switch" size="large" @on-change="changeLanguage" true-color="forestgreen">
                <span slot="open">{{$t('language.chinese')}}</span>
                <span slot="close">{{$t('language.english')}}</span>
              </iv-switch>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  name: 'MobileSideBar',
  data () {
    return {
      show: false,
      keyWord: ''
    }
  },
  methods: {
    changeLanguage (flag) {
      if (flag === false) {
        this.$i18n.locale = 'en'
        localStorage.setItem('language', 'en')
      } else {
        this.$i18n.locale = 'zh'
        localStorage.setItem('language', 'zh')
      }
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  .mobile-side-bar
    background-color $color-page-background
    .header-inner
      overflow hidden
      width auto
      margin 0 auto
      margin-bottom 10px
      padding 10px
      .site-brand-wrapper
        font normal 700 1.4rem $body-font
        text-align center
        line-height 2.2rem
        height 2.2rem
        color black
        .site-nav-toggle
          float left
          padding 0 5px
          &:hover
            cursor pointer
        .site-title
          font-weight 400
          border-bottom 1px solid $color-border
    .site-nav
      border-top 1px solid $color-border
      ul li
        margin 5px 0
        padding 5px 0
        list-style none
        line-height 24
        font normal 400 16px $body-font
        &>a
          text-decoration none
          color black
        &:hover
          color $color-on-hover
          cursor pointer
          background-color $color-gradually-gray-91
        span
          margin 0 5px
          font-size 18px
        .mobile-search
          margin 0
          outline none
          width 65%
          height 24px
          border none
          border-bottom 1px solid $color-border
          background-color $color-page-background
          &:hover
            border-bottom 1px solid $color-border-hover
        .site-language
          position relative
          float right
          margin 2px 0
          .mobile-switch span
            position absolute
            top 2px
            font-size 14px
</style>
