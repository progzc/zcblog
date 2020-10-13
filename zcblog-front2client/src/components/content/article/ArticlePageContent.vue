<template>
  <div class="article-page-content" v-html="article.contentFormat">
    {{article.contentFormat}}
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  name: 'ArticlePageContent',
  props: ['article']
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
@import "~common/stylus/article/article.styl"
</style>
