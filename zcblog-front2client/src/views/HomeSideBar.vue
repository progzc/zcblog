<template>
  <div class="home-side-bar">
<!--    <mobile-side-bar></mobile-side-bar>-->
<!--    <desk-side-bar></desk-side-bar>-->
<!--    <tag-list></tag-list>-->
<!--    <article-abstract-list></article-abstract-list>-->
    <Tag-time-line></Tag-time-line>
  </div>
</template>

<script type="text/ecmascript-6">
// import MobileSideBar from 'components/content/Nav/MobileSideBar'
// import DeskSideBar from 'components/content/Nav/DeskSideBar'
// import TagList from './TagList'
// import ArticleAbstractList from './ArticleAbstractList'
import TagTimeLine from 'views/TagTimeLine'
export default {
  name: 'HomeSideBar',
  components: {
    // 'mobile-side-bar': MobileSideBar,
    // 'desk-side-bar': DeskSideBar
    // 'tag-list': TagList,
    // 'article-abstract-list': ArticleAbstractList,
    'Tag-time-line': TagTimeLine
  },
  data () {
    return {
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
</style>
