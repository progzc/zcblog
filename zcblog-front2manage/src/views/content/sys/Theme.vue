<template>
  <el-form>
    <h2>布局设置</h2>
    <el-form-item label="导航条类型">
      <el-radio-group v-model="navbarLayoutType">
        <el-radio label="default" border>default</el-radio>
        <el-radio label="inverse" border>inverse</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="侧边栏皮肤">
      <el-radio-group v-model="sidebarLayoutSkin">
        <el-radio label="light" border>light</el-radio>
        <el-radio label="dark" border>dark</el-radio>
      </el-radio-group>
    </el-form-item>
  </el-form>
</template>

<script type="text/ecmascript-6">
import { UPDATE_NAVBAR_LAYOUT_TYPE, UPDATE_SIDEBAR_LAYOUT_SKIN } from 'store/constant/mutation-types'

export default {
  name: 'Theme',
  computed: {
    navbarLayoutType: {
      get () { return this.$store.state.common.navbarLayoutType },
      set (val) { this.$store.commit(`common/${UPDATE_NAVBAR_LAYOUT_TYPE}`, val) }
    },
    sidebarLayoutSkin: {
      get () { return this.$store.state.common.sidebarLayoutSkin },
      set (val) { this.$store.commit(`common/${UPDATE_SIDEBAR_LAYOUT_SKIN}`, val) }
    }
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>

</style>
