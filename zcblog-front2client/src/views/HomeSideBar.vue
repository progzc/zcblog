<template>
  <div class="home-side-bar">
<!--    <mobile-side-bar></mobile-side-bar>-->
    <desk-side-bar></desk-side-bar>
  </div>
</template>

<script type="text/ecmascript-6">
import MobileSideBar from 'components/content/Nav/MobileSideBar'
import DeskSideBar from 'components/content/Nav/DeskSideBar'
export default {
  name: 'HomeSideBar',
  components: {
    'mobile-side-bar': MobileSideBar,
    'desk-side-bar': DeskSideBar
  },
  data () {
    return {
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
</style>
