<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
      <el-form-item label="标签名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="标签名字"></el-input>
      </el-form-item>
      <el-form-item label="所属类别" prop="type">
        <el-select v-model="dataForm.type">
          <el-option
            v-for="type in typeList"
            :key="type.parKey"
            :value="type.parKey"
            :label="type.parDesc"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()" :disabled="confirmButtonDisabled">确定</el-button>
    </span>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
import { executeGetTagById, executePostOrPutTag } from 'network/api/tag'
export default {
  name: 'TagAddOrUpdate',
  data () {
    return {
      visible: false,
      confirmButtonDisabled: false,
      dataForm: {
        type: ''
      },
      dataRule: {
        name: [{ required: true, message: '标签名称不能为空', trigger: 'blur' }],
        type: [{ required: true, message: '所属类别不能为空', trigger: 'blur' }]
      },
      typeList: [
        { parKey: 0, parDesc: '文章' },
        { parKey: 1, parDesc: '相册' }
      ]
    }
  },
  methods: {
    init (id) {
      this.dataForm.id = id || ''
      this.visible = true
      this.confirmButtonDisabled = false
      this.$nextTick(() => {
        this.$refs.dataForm.resetFields()
        if (this.dataForm.id) { // 执行的是修改操作
          executeGetTagById(this.dataForm.id).then(data => {
            if (data && data.code === 200) {
              this.dataForm = data.tag
              this.dataCache = this.dataForm // 缓存值
            }
          })
        } else { // 执行的是新增操作
          this.dataForm = {}
          this.dataCache = this.dataForm // 缓存值
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          executePostOrPutTag(this.dataForm.id, this.dataForm).then(data => {
            this.confirmButtonDisabled = true
            if (data && data.code === 200) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1000,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>

</style>
