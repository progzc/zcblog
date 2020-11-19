<template>
  <el-submenu
    v-if="menu.list && menu.list.length >= 1"
    :index="menu.menuId + ''"
    :popper-class="'site-sidebar--' + sidebarLayoutSkin + '-popper'">
    <template slot="title">
      <svg-icon :name=" menu.icon || ''" class="site-sidebar__menu-icon"></svg-icon>
      <span>{{ menu.name }}</span>
    </template>
    <sub-menu
      v-for="item in menu.list"
      :key="item.menuId"
      :menu="item"
      :dynamicMenuRoutes="dynamicMenuRoutes">
    </sub-menu>
  </el-submenu>
  <el-menu-item v-else :index="menu.menuId + ''" @click="gotoRouteHandle(menu)" v-show="menu.icon">
    <svg-icon :name="menu.icon || ''" class="site-sidebar__menu-icon"></svg-icon>
    <span>{{ menu.name }}</span>
  </el-menu-item>
</template>

<script type="text/ecmascript-6">
import SubMenu from 'components/content/SubMenu'
export default {
  name: 'sub-menu',
  components: {
    'sub-menu': SubMenu
  },
  props: {
    menu: {
      type: Object,
      required: true
    },
    dynamicMenuRoutes: {
      type: Array,
      required: true
    }
  },
  computed: {
    sidebarLayoutSkin: {
      get () { return this.$store.state.common.sidebarLayoutSkin }
    }
  },
  methods: {
    // 通过menuId与动态（菜单）路由进行匹配跳转至指定路由
    gotoRouteHandle (menu) {
      const route = this.dynamicMenuRoutes.filter(item => item.meta.menuId === menu.menuId)
      if (route.length >= 1) {
        this.$router.push({ name: route[0].name })
      }
    }
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>

</style>
