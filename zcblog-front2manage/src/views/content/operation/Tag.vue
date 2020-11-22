<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.keyWord" placeholder="标签名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('operation:tag:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('operation:tag:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
      </el-form-item>
    </el-form>
    <el-table :data="dataList" border v-loading="dataListLoading"
              @selection-change="selectionChangeHandle" style="width: 100%;">
      <el-table-column type="selection" header-align="center" align="center" width="50">
      </el-table-column>
      <el-table-column type="index" :index="index => index+1" prop="id" header-align="center" align="center" label="编号" width="80">
      </el-table-column>
      <el-table-column prop="name" header-align="center" align="center" label="标签名称">
      </el-table-column>
      <el-table-column prop="type" header-align="center" align="center" label="所属类别">
        <template slot-scope="scope">
          {{scope.row.type === 0 ? "文章" : "相册"}}
        </template>
      </el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <el-button :disabled="!isAuth('operation:tag:update')"
                     type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改
          </el-button>
          <el-button :disabled="!isAuth('operation:tag:delete')"
                     type="text" size="small" @click="deleteHandle(scope.row.id)">删除
          </el-button>
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
    <tag-add-or-update v-if="addOrUpdateVisible" ref="tagAddOrUpdate" @refreshDataList="getDataList"></tag-add-or-update>
  </div>
</template>

<script>
import TagAddOrUpdate from 'components/content/TagAddOrUpdate'
import { executeDeleteTag, executeGetTagList } from 'network/api/tag'
export default {
  name: 'Tag',
  components: {
    'tag-add-or-update': TagAddOrUpdate
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
      executeGetTagList(this.currentPage, this.pageSize, this.dataForm.keyWord).then(data => {
        if (data && data.code === 200) {
          this.dataList = data.page.list
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
        this.$refs.tagAddOrUpdate.init(id)
      })
    },
    // 批量删除
    deleteHandle (id) {
      const ids = id ? [id] : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm(`确定对这${ids.length}条数据进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        executeDeleteTag(ids).then(data => {
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
      })
    }
  }
}
</script>
