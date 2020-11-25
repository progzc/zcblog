<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.keyWord" placeholder="输入用户名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('sys:user:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('sys:user:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
      </el-form-item>
    </el-form>
    <el-table :data="dataList" border v-loading="dataListLoading"
              @selection-change="selectionChangeHandle" style="width: 100%;">
      <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column>
      <el-table-column type="index" :index="index => index+1" prop="userId" header-align="center" align="center" width="80" label="编号">
      </el-table-column>
      <el-table-column prop="username" header-align="center" align="center" label="用户名">
      </el-table-column>
      <el-table-column prop="email" header-align="center" align="center" label="邮箱">
      </el-table-column>
      <el-table-column prop="phone" header-align="center" align="center" label="手机号">
      </el-table-column>
      <el-table-column prop="status" header-align="center" align="center" label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === false" size="small" type="danger">禁用</el-tag>
          <el-tag v-else size="small">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" header-align="center" align="center" width="180" label="创建时间">
      </el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('sys:user:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.userId)">修改</el-button>
          <el-button v-if="isAuth('sys:user:delete')" type="text" size="small" @click="deleteHandle(scope.row.userId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="currentPage"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalCount"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <user-add-or-update v-if="addOrUpdateVisible" ref="userAddOrUpdate" @refreshDataList="getDataList"></user-add-or-update>
  </div>
</template>

<script type="text/ecmascript-6">
import UserAddOrUpdate from 'components/content/UserAddOrUpdate'
import { executeGetUserList, executeDeleteUser } from 'network/api/user'
import { decryptAES } from 'common/js/utils/encrypt'

export default {
  name: 'User',
  components: {
    'user-add-or-update': UserAddOrUpdate
  },
  data () {
    return {
      dataForm: {
        keyWord: ''
      },
      dataList: [],
      currentPage: 1,
      pageSize: 10,
      totalCount: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false
    }
  },
  activated () {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      executeGetUserList(this.currentPage, this.pageSize, this.dataForm.keyWord).then(data => {
        if (data && data.code === 200) {
          this.dataList = data.page.list
          // 解密username
          this.dataList.map(e => {
            e.username = (e.username !== null ? decryptAES(e.username) : e.username)
          })
          this.totalCount = data.page.totalCount
        } else {
          this.dataList = []
          this.totalCount = 0
        }
        this.dataListLoading = false
      })
    },
    // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.currentPage = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle (val) {
      this.currentPage = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.userAddOrUpdate.init(id)
      })
    },
    // 删除
    deleteHandle (id) {
      const userIds = id ? [id] : this.dataListSelections.map(item => { return item.userId })
      this.$confirm(`确定对[id=${userIds.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        executeDeleteUser(userIds).then(data => {
          if (data && data.code === 200) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1000,
              onClose: () => {
                this.getDataList()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>

</style>
