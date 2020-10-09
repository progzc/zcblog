<template>
  <content-box>
    <div class="tag-container" slot="container">
      <div class="tag-item" v-for="tag in tagList" :key="tag.id">
        <div class="tag-group">
          <span class="iconfont">&#xe655;</span>{{tag.name}}
        </div>
      </div>
    </div>
  </content-box>
</template>

<script type="text/ecmascript-6">
import ContentBox from 'components/content/ContentBox'
export default {
  name: 'TagList',
  components: {
    'content-box': ContentBox
  },
  data () {
    return {
      tagList: [
        { id: 0, name: 'Ajax' }, { id: 1, name: 'Apache Tiles' }, { id: 2, name: 'Atom' },
        { id: 3, name: 'Bootstrap' }, { id: 4, name: 'CSS' }, { id: 5, name: 'CSS3' },
        { id: 6, name: 'Canvas' }, { id: 7, name: 'Consul' }, { id: 8, name: 'DevOps' },
        { id: 9, name: 'Docker' }, { id: 10, name: 'Druid' }, { id: 11, name: 'Dubbo' },
        { id: 12, name: 'EasyUI' }, { id: 13, name: 'Ehcache' }, { id: 14, name: 'Eureka' },
        { id: 15, name: 'EasyUI' }, { id: 16, name: 'Ehcache' }, { id: 17, name: 'Eureka' },
        { id: 18, name: 'EasyUI' }, { id: 19, name: 'Ehcache' }, { id: 20, name: 'Eureka' },
        { id: 21, name: 'EasyUI' }, { id: 22, name: 'Ehcache' }, { id: 23, name: 'Eureka' },
        { id: 24, name: 'EasyUI' }, { id: 25, name: 'Ehcache' }, { id: 26, name: 'Eureka' },
        { id: 27, name: 'EasyUI' }, { id: 28, name: 'Ehcache' }, { id: 29, name: 'Eureka' },
        { id: 30, name: 'Ajax' }, { id: 31, name: 'Apache Tiles' }, { id: 32, name: 'Atom' },
        { id: 33, name: 'Bootstrap' }, { id: 34, name: 'CSS' }, { id: 35, name: 'CSS3' },
        { id: 36, name: 'Canvas' }, { id: 37, name: 'Consul' }, { id: 38, name: 'DevOps' },
        { id: 39, name: 'Docker' }, { id: 40, name: 'Druid' }, { id: 41, name: 'Dubbo' },
        { id: 42, name: 'EasyUI' }, { id: 43, name: 'Ehcache' }, { id: 44, name: 'Eureka' },
        { id: 45, name: 'EasyUI' }, { id: 46, name: 'Ehcache' }, { id: 47, name: 'Eureka' },
        { id: 48, name: 'EasyUI' }, { id: 49, name: 'Ehcache' }, { id: 50, name: 'Eureka' },
        { id: 51, name: 'EasyUI' }, { id: 52, name: 'Ehcache' }, { id: 53, name: 'Eureka' },
        { id: 54, name: 'EasyUI' }, { id: 55, name: 'Ehcache' }, { id: 56, name: 'Eureka' },
        { id: 57, name: 'EasyUI' }, { id: 58, name: 'Ehcache' }, { id: 59, name: 'Eureka' }
      ]
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  .tag-container
    padding 1.5rem 0 1.5rem 3rem
    border-radius 6px
    background-color $color-content-background
    .tag-item
      display inline-block
      margin 8px 0 8px 0
      width 20%
      .tag-group
        display inline-block
        opacity 0.75
        font-weight 400
        &:hover
          color $color-on-hover
          cursor pointer
        span
          margin-right 5px
          color $color-on-hover
  @media screen and (min-width: $size-xxl)
    .tag-item
      width 10% !important
  @media screen and (max-width: $size-xl)
    .tag-item
      width 25% !important
  @media screen and (max-width: $size-lg)
    .tag-item
      width 33% !important
  @media screen and (max-width: $size-md)
    .tag-item
      width 50% !important
  @media screen and (max-width: $size-sm)
    .tag-container
      margin-bottom 200px
</style>
