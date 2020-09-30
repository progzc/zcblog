<template>
  <div class="main-wrapper">
    <router-view name="sideBar"></router-view>
    <router-view name="content"></router-view>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  name: 'index'
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus">
  .main-wrapper
    background-color $color-page-background
    width 100%
    margin 0 auto
</style>
