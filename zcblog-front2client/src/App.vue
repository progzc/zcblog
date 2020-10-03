<template>
  <div id="app">
    <router-view/>
  </div>
</template>
<script>
export default {
  name: 'app'
}
</script>
<style lang="stylus" type="text/stylus" rel="stylesheet/stylus">
  @import '~common/stylus/index.styl'
  #app
   font-family $body-font
</style>
