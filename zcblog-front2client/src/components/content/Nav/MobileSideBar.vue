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
    background-color $color-header-mobile
    .header-inner
      overflow hidden
      width auto
      margin 0 auto 20px auto
      padding-top 10px
      .site-brand-wrapper
        padding 0 10px
        font normal 700 1.4rem $body-font
        text-align center
        line-height 2.2rem
        height 2.2rem
        color white
        .site-nav-toggle
          float left
          padding 0 5px
          &:hover
            cursor pointer
        .site-title
          font-weight 400
    .site-nav
      border-top 1px solid $color-border
      color white
      ul li
        margin 5px 0 5px 0
        padding 5px 15px
        list-style none
        line-height 24
        font normal 400 16px $body-font
        &:first-of-type
          margin-top 0
          padding-top 8px
        &>a
          text-decoration none
          color white
          span
            margin-right 10px
        &:hover
          color $color-on-hover
          cursor pointer
          background-color $color-gradually-gray-11
      .menu-item-search
        margin-bottom 0
        padding-bottom 7px
        span
          position relative
          top 2px
          margin-right 10px
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
          margin 1px 0
          .mobile-switch span
            position absolute
            top 2px
            font-size 14px
</style>
