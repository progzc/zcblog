<template>
  <div class="desk-side-bar">
    <!--侧边栏主要内容-->
    <div class="side-bar-sticky">
      <div class="author-header">
        <div class="author-avatar">
          <img src="~assets/img/blogImage.jpg">
        </div>
        <div class="author-pseudonym">{{$t('homeNav.pseudonym')}}</div>
        <div class="author-motto">{{$t('homeNav.motto')}}</div>
      </div>
      <div class="blog-search">
        <iv-input search :placeholder="searchPlaceholder" v-model="keyWord" clearable @on-enter="search"></iv-input>
      </div>
      <div class="side-bar-nav-menu">
        <div class="menu-item-home">
          <div class="menu-item-choose"><span class="iconfont">&#xe501;</span>{{$t('homeNav.home')}}</div>
        </div>
        <div class="menu-item-gallery">
          <div class="menu-item-choose"><span class="iconfont">&#xe891;</span>{{$t('homeNav.gallery')}}</div>
        </div>
        <div class="menu-item-mix">
          <div class="menu-item-timeline"><span class="iconfont">&#xe7e3;</span>{{$t('homeNav.timeline')}}</div>
          <span class="separator"></span>
          <div class="menu-item-tags"><span class="iconfont">&#xe613;</span>{{$t('homeNav.tags')}}</div>
        </div>
      </div>
    </div>
    <!--侧边栏页脚-->
    <div class="side-bar-footer">
      <div class="links">
        <a href="https://github.com/progzc/zcblog" target="_blank"><span class="iconfont github">&#xe64a;</span></a>
        <a href="https://gitee.com/progzc" target="_blank"><span class="iconfont gitee">&#xe6d0;</span></a>
        <a href="#" target="_blank"><span class="iconfont admin">&#xe6ed;</span></a>
        <a href="#" target="_blank"><span class="iconfont docs">&#xe6f7;</span></a>
        <a href="http://qhnmn5y5g.hn-bkt.clouddn.com/personal-wx.jpg" target="_blank"><span class="iconfont wx">&#xe601;</span></a>
      </div>
      <div class="copyRight">
        {{$t('copyright.desc')}}<span>&copy;</span>2020-2022 by {{$t('homeNav.pseudonym')}}
      </div>
      <div class="site-static-info">
        <span class="pv">{{$t('homeNav.pageView')}}:{{pv}}</span>
        <span class="uv">{{$t('homeNav.uniqueVisitor')}}:{{uv}}</span>
      </div>
    </div>
  </div>

</template>

<script type="text/ecmascript-6">
export default {
  name: 'DeskSideBar',
  data () {
    return {
      searchPlaceholder: this.$t('homeNav.searchPlaceholder'),
      keyWord: '',
      uv: '2185',
      pv: '759'
    }
  },
  mounted () {
  },
  methods: {
    search () {
      if (this.keyWord === '' || this.keyWord.length < 1) {
        this.$Message.warning({
          content: this.$t('warningMessage.nullForSearch')
        })
      } else {
        this.$Message.success({
          content: this.$t('successMessage.successForSearch')
        })
      }
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  .desk-side-bar
    position relative
    width 100%
    .side-bar-sticky
      padding 20px 10px
      text-align center
      .author-header
        padding 1rem
        margin 0.4rem
        border-radius 6px
        background-color $color-content-background
        .author-avatar
          display block
          text-align center
          img
            display inline-block
            vertical-align top
            padding 4px
            width 100%
            max-width 100px
            border-radius 50%
            border 1px solid $color-border
        .author-pseudonym
          margin 0.8rem 0 0 0
          line-height 1.8rem
          height 1.8rem
          white-space nowrap
          font-size 1.3rem !important
          font-weight 400
          color $color-font-content
        .author-motto
          margin  0.5rem 0
          font-weight 300
          color $color-font-blog-desc
      .blog-search
        padding 0.8rem 0
        margin 0.4rem
        >>>.ivu-input // 穿透进iViewUI组件更改样式
          border-color  $color-on-hover
      .side-bar-nav-menu
        padding 0.5rem 0
        margin 3rem 0.8em 0.8rem 0.8rem
        font-weight normal
        .menu-item-choose
          display inline-block
          &:hover
            color $color-on-hover
            cursor pointer
        span
          font-size 1.05rem
          margin 0 5px
        .menu-item-mix
          margin 4rem 0 1rem 0
          .menu-item-timeline
            display inline-block
            &:hover
              color $color-on-hover
              cursor pointer
          .separator
            margin 0 6px 0 10px
            border-right 1px solid $color-gradually-gray-71
          .menu-item-tags
            display inline-block
            &:hover
              color $color-on-hover
              cursor pointer
    .side-bar-footer
      .links
        text-align center
        a
          color inherit
          span
            font-size 1.6rem
            margin 0 10px
          .github
            &:hover
              color #098DE6
              cursor pointer
          .gitee
            &:hover
              color #C71D23
              cursor pointer
          .admin
            margin-left 5px
            margin-right 5px
            &:hover
              color #409EFF
              cursor pointer
          .docs
            &:hover
              color #41B883
              cursor pointer
          .wx
            margin-left 5px
            &:hover
              color #1AAD19
              cursor pointer
      .copyRight
        margin 5px 0
        text-align center
      .site-static-info
        margin 5px 0
        text-align center
        .pv
          margin-right 15px
        .uv
          margin-left 15px
</style>
