<template>
  <div class="article-reward">
    <p>{{$t('homeNav.reward')}}<span class="iconfont">&#xe614;</span></p>
    <div class="reward-icon" ><span class="iconfont" @click="show=!show">&#xe506;</span></div>
    <div class="reward" v-show="show">
      <div class="zfb-bk"><img class="zfb-jpg" src="~assets/img/reward-zfb.jpg"></div>
      <div class="wx-bk"><img class="wx-jpg" src="~assets/img/reward-wx.jpg"></div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  name: 'ArticleReward',
  data () {
    return {
      show: false
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  .article-reward
    margin 20px auto
    text-align center
    font-weight 400
    p
      line-height 1.4rem
      height 1.4rem
      font-size 0.9rem
      .iconfont
        margin-left 5px
        font-size 1.2rem
        color $color-nav
    .reward-icon
      margin 10px 0
      span
        position relative
        display inline-block
        height 46px
        width 46px
        line-height 46px
        font-size 23px
        border-radius 50%
        background #FFD5BE
        color #fe5f55
        border none
        box-shadow 0 4px 8px 0 rgba(255,213,190,.4)
        &:after // 添加遮罩层
          content ''
          display none
          position absolute
          top 0
          left 0
          width 100%
          height 100%
          border-radius 50%
          background-color rgba(255,255,255,0.3)
        &:hover
          cursor pointer
        &:hover:after
          display block
    .reward
      div
        display inline-block
        margin 15px 0 0 0
        height 150px
        line-height 150px
        img
          height 100%
      .zfb-bk
        padding 15px
        margin-right 15px
        background-color #1678FF // 支付宝蓝
      .wx-bk
        padding 15px
        margin-left 15px
        background-color #22AB39 // 微信绿
  @media screen and (max-width: $size-sm)
    .reward div
      height 130px !important
      line-height 130px !important
    .zfb-bk
      margin-right 10px !important
    .wx-bk
      margin-left 10px !important
</style>
