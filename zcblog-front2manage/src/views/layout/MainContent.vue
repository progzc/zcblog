<template>
  <main class="site-content" :class="{ 'site-content--tabs': $route.meta.isTab }">
    <!-- 主入口标签页 -->
    <el-tabs
      v-if="$route.meta.isTab"
      v-model="mainTabsActiveName"
      :closable="true"
      @tab-click="selectedTabHandle"
      @tab-remove="removeTabHandle">
      <el-dropdown class="site-tabs__tools" :show-timeout="0">
        <i class="el-icon-arrow-down el-icon--right"></i>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="tabsCloseCurrentHandle">关闭当前标签页</el-dropdown-item>
          <el-dropdown-item @click.native="tabsCloseOtherHandle">关闭其它标签页</el-dropdown-item>
          <el-dropdown-item @click.native="tabsCloseAllHandle">关闭全部标签页</el-dropdown-item>
          <el-dropdown-item @click.native="tabsRefreshCurrentHandle">刷新当前标签页</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <el-tab-pane
        v-for="item in mainTabs"
        :key="item.name"
        :label="item.title"
        :name="item.name">
        <el-card :body-style="siteContentViewHeight" >
          <iframe
            v-if="item.type === 'iframe'"
            :src="item.iframeUrl"
            width="100%" height="100%" frameborder="0" scrolling="yes">
          </iframe>
          <keep-alive v-else>
            <router-view v-if="item.name === mainTabsActiveName" @closeCurrentTabs="tabsCloseCurrentHandle" />
          </keep-alive>
        </el-card>
      </el-tab-pane>
    </el-tabs>
    <!-- 主入口标签页 -->
    <el-card v-else :body-style="siteContentViewHeight">
      <keep-alive>
        <router-view />
      </keep-alive>
    </el-card>
  </main>
</template>

<script type="text/ecmascript-6">
import { isURL } from 'common/js/utils/validate'
import {
  UPDATE_MAIN_TABS,
  UPDATE_MAIN_TABS_ACTIVE_NAME,
  UPDATE_MENU_ACTIVE_NAME
} from 'store/constant/mutation-types'

export default {
  name: 'MainContent',
  computed: {
    documentClientHeight: {
      get () { return this.$store.state.common.documentClientHeight }
    },
    menuActiveName: {
      get () { return this.$store.state.common.menuActiveName },
      set (val) { this.$store.commit(`common/${UPDATE_MENU_ACTIVE_NAME}`, val) }
    },
    mainTabs: {
      get () { return this.$store.state.common.mainTabs },
      set (val) { this.$store.commit(`common/${UPDATE_MAIN_TABS}`, val) }
    },
    mainTabsActiveName: {
      get () { return this.$store.state.common.mainTabsActiveName },
      set (val) { this.$store.commit(`common/${UPDATE_MAIN_TABS_ACTIVE_NAME}`, val) }
    },
    siteContentViewHeight () {
      let height = this.documentClientHeight - 50 - 30 - 2
      if (this.$route.meta.isTab) {
        height -= 40
        return isURL(this.$route.meta.iframeUrl) ? { height: height + 'px' } : { minHeight: height + 'px' }
      }
      return { minHeight: height + 'px' }
    }
  },
  methods: {
    // tabs, 选中tab
    selectedTabHandle (tab) {
      tab = this.mainTabs.filter(item => item.name === tab.name)
      if (tab.length >= 1) {
        this.$router.push({ name: tab[0].name })
      }
    },
    // tabs, 删除tab
    removeTabHandle (tabName) {
      this.mainTabs = this.mainTabs.filter(item => item.name !== tabName)
      if (this.mainTabs.length >= 1) {
        // 当前选中tab被删除
        if (tabName === this.mainTabsActiveName) {
          this.$router.push({ name: this.mainTabs[this.mainTabs.length - 1].name }, () => {
            this.mainTabsActiveName = this.$route.name
          })
        }
      } else {
        this.menuActiveName = ''
        this.$router.push({ name: 'home' })
      }
    },
    // tabs, 关闭当前
    tabsCloseCurrentHandle () {
      this.removeTabHandle(this.mainTabsActiveName)
    },
    // tabs, 关闭其它
    tabsCloseOtherHandle () {
      this.mainTabs = this.mainTabs.filter(item => item.name === this.mainTabsActiveName)
    },
    // tabs, 关闭全部
    tabsCloseAllHandle () {
      this.mainTabs = []
      this.menuActiveName = ''
      this.$router.push({ name: 'home' })
    },
    // tabs, 刷新当前
    tabsRefreshCurrentHandle () {
      const tempTabName = this.mainTabsActiveName
      this.removeTabHandle(tempTabName)
      this.$nextTick(() => {
        this.$router.push({ name: tempTabName })
      })
    }
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>
 /* .site-content {
    position: relative;
    padding: 15px;

    &--tabs {
      padding: 55px 0 0;
    }
    > .el-tabs {
      > .el-tabs__header {
        position: fixed;
        top: 50px;
        left: 230px;
        right: 0;
        z-index: 930;
        padding: 0 55px 0 15px;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .12), 0 0 6px 0 rgba(0, 0, 0, .04);
        background-color: #fff;
        > .el-tabs__nav-wrap {
          margin-bottom: 0;
          &:after {
            display: none;
          }
        }
      }
      > .el-tabs__content {
        padding: 0 15px 15px;
        > .site-tabs__tools {
          position: fixed;
          top: 50px;
          right: 0;
          z-index: 931;
          height: 40px;
          padding: 0 12px;
          font-size: 16px;
          line-height: 40px;
          background-color: $content--background-color;
          cursor: pointer;
          .el-icon--right {
            margin-left: 0;
          }
        }
      }
    }
  }*/
</style>
