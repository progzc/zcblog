<template>
  <div id="vcomment"></div>
</template>

<script type="text/ecmascript-6">
import Valine from 'valine'
export default {
  name: 'ValineCommon',
  mounted () {
    this.createValine()
  },
  methods: {
    createValine () {
      Valine({
        el: '#vcomment',
        appId: process.env.VUE_APP_Valine_APPID,
        appKey: process.env.VUE_APP_Valine_APPKEY,
        avatar: 'retro',
        visitor: false,
        // 当前文章页路径，用于区分不同的文章页，以保证正确读取该文章页下的评论列表
        path: window.location.pathname,
        recordIP: true,
        lang: this.$i18n.t('homeNav.valineLang')
      })
    }
  },
  watch: {
    '$route' (to, from) {
      if (to.path !== from.path) {
        setTimeout(() => {
          // 重新刷新valine
          this.createValine()
        }, 300)
      }
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>

</style>
