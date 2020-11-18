<template>
  <div class="site-wrapper" :class="{ 'site-sidebar--fold': sidebarFold }"
    v-loading.fullscreen.lock="loading"
    element-loading-spinner="el-icon-loading"
    element-loading-text="加载中...">
    <template v-if="!loading">
      <main-navbar></main-navbar>
      <main-sidebar></main-sidebar>
      <div class="site-content__wrapper" :style="{ 'min-height': documentClientHeight + 'px' }">
        <main-content></main-content>
      </div>
    </template>
  </div>
</template>

<script type="text/ecmascript-6">
import MainNavbar from 'views/layout/MainNavbar'
import MainSidebar from 'views/layout/MainSidebar'
import MainContent from 'views/layout/MainContent'

import {
  UPDATE_DOCUMENT_CLIENT_HEIGHT,
  UPDATE_USER_ID,
  UPDATE_USER_NAME
} from 'store/constant/mutation-types'
import { executeGetUserInfo } from 'network/api/user'
import { decryptAES } from 'common/js/utils/encrypt'

export default {
  components: {
    'main-navbar': MainNavbar,
    'main-sidebar': MainSidebar,
    'main-content': MainContent
  },
  data () {
    return {
      loading: true // v-loading属于element-ui中Loading的指令
    }
  },
  computed: {
    sidebarFold: {
      get () { return this.$store.state.common.sidebarFold }
    },
    documentClientHeight: {
      get () { return this.$store.state.common.documentClientHeight },
      set (val) { this.$store.commit(UPDATE_DOCUMENT_CLIENT_HEIGHT, val) }
    },
    userId: {
      get () { return this.$store.state.user.id },
      set (val) { this.$store.commit(UPDATE_USER_ID, val) }
    },
    username: {
      get () { return this.$store.state.user.name },
      set (val) { this.$store.commit(UPDATE_USER_NAME, val) }
    }
  },
  created () {
    this.getUserInfo()
  },
  mounted: function () {
    this.resetDocumentClientHeight()
  },
  methods: {
    // 获取当前管理员信息
    getUserInfo () {
      executeGetUserInfo().then(data => {
        if (data && data.code === 200) {
          this.loading = false
          this.userId = data.user.userId
          this.username = decryptAES(data.user.username)
        }
      })
    },
    // 重置窗口可视高度
    resetDocumentClientHeight () {
      // this.documentClientHeight = document.documentElement.clientHeight
      window.onresize = () => { this.documentClientHeight = document.documentElement.clientHeight }
    }
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>

</style>
