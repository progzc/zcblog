<template>
  <div class="article-tag-wall" v-if="tagList">
    <span class="iconfont">&#xe611;</span>
    <iv-tag class="article-tag-item" v-for="tag in tagList" :key="tag.id" :color="tag.id | mapTagColor" >
      <span class="article-tag-click" @click="handleToTag(tag.id)">{{tag.name}}</span>
    </iv-tag>
  </div>
</template>

<script type="text/ecmascript-6">
import { mixin } from 'common/js/utils'
export default {
  name: 'TagWall',
  props: ['tagList'],
  mixins: [mixin],
  methods: {
    handleToTag (id) {
      console.log('跳转到标签时间轴' + id)
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  .article-tag-wall
    position relative
    margin 5px 0
    .iconfont
      position absolute
      top 3.5px
      margin-left 5px
      font-size 1.2rem
    .article-tag-item
      margin 5px
      color white
      font-weight 400
      &:hover
        cursor pointer
    .article-tag-item:first-of-type
      margin-left 32px
</style>
