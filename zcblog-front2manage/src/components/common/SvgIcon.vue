<template>
  <svg class="svg-icon"
       aria-hidden="true">
    <use :xlink:href="iconName" />
  </svg>
</template>

<script type="text/ecmascript-6">
export default {
  name: 'SvgIcon',
  props: {
    name: {
      type: String,
      required: true
    }
  },
  computed: {
    iconName () {
      return `#icon-${this.name}`
    }
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>
  .svg-icon {
    width: 1em;
    height: 1em;
    vertical-align: -0.15em;
    fill: currentColor;
    overflow: hidden;
  }
</style>
