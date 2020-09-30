<template>
<div>
  {{$t('homeNav.motto')}}
</div>
</template>

<script type="text/ecmascript-6">
export default {
  name: 'HomeSideBar',
  mounted () {
    this.$i18n.locale = 'zh'
    console.log(this.$i18n.locale)
    this.$Modal.info({ title: '测试', content: '测试iView UI的国际化是否为中文' })
    // console.log(this.$i18n.locale)
    // this.$i18n.locale = 'en'
    // this.$Modal.confirm({ title: '测试', content: '测试iView UI的国际化是否为英文' })
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>

</style>
