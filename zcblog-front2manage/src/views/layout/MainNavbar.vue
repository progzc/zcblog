<template>
  <nav class="site-navbar" :class="'site-navbar--' + navbarLayoutType">
    <div class="site-navbar__header">
      <h1 class="site-navbar__brand" @click="$router.push({ name: 'home' })">
        <svg-icon v-if="!sidebarFold" name="fold" @click.native.stop="sidebarFold = !sidebarFold"></svg-icon>
        <svg-icon v-else name="extend" @click.native.stop="sidebarFold = !sidebarFold"></svg-icon>
        <a class="site-navbar__brand-lg" href="javascript:;">Clouds' Blog</a>
      </h1>
    </div>
    <div class="site-navbar__body clearfix" :class="'site-navbar__body__' + navbarLayoutType">
      <el-dropdown class="site-navbar__user" :show-timeout="0" placement="bottom">
        <span class="el-dropdown-link">
          <img src="~assets/img/avatar.jpg" :alt="username" >{{ username }}
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="submit()">修改密码</el-dropdown-item>
          <el-dropdown-item @click.native="logout()">退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!-- 弹窗, 修改密码 -->
    <update-password v-if="updatePasswordVisible" ref="updatePassword"></update-password>
  </nav>

</template>

<script type="text/ecmascript-6">
import UpdatePassword from 'components/content/UpdatePassword'
import { UPDATE_MAIN_TABS, UPDATE_SIDEBAR_FOLD } from 'store/constant/mutation-types'
import { executeLogout } from 'network/api/login'
import { clearLoginInfo } from 'common/js/utils/login'

export default {
  name: 'MainNavbar',
  components: {
    'update-password': UpdatePassword
  },
  data () {
    return {
      updatePasswordVisible: false,
      show: false
    }
  },
  computed: {
    sidebarFold: {
      get () { return this.$store.state.common.sidebarFold },
      set (val) {
        this.$store.commit(`common/${UPDATE_SIDEBAR_FOLD}`, val)
      }
    },
    mainTabs: {
      get () { return this.$store.state.common.mainTabs },
      set (val) { this.$store.commit(`common/${UPDATE_MAIN_TABS}`, val) }
    },
    navbarLayoutType: {
      get () { return this.$store.state.common.navbarLayoutType }
    },
    username: {
      get () { return this.$store.state.user.name }
    }
  },
  methods: {
    // 修改密码
    submit () {
      this.updatePasswordVisible = true
      this.$nextTick(() => {
        this.$refs.updatePassword.init()
      })
    },
    // 退出系统
    logout () {
      this.$confirm('确定进行[退出操作]?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        executeLogout().then(data => {
          if (data && data.code === 200) {
            clearLoginInfo()
            this.$router.push({ name: 'login' })
          }
        })
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>

</style>
