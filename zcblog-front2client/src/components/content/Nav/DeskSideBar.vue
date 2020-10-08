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
        <iv-input search :placeholder="$t('homeNav.searchPlaceholder')" v-model="keyWord" clearable @on-enter="search"></iv-input>
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
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  name: 'DeskSideBar',
  data () {
    return {
      keyWord: ''
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
      padding 24px 10px
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
        margin 1rem 0.4rem
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
</style>
