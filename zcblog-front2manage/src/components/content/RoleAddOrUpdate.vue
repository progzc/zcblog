<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="dataForm.roleName" placeholder="角色名称"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="dataForm.remark" placeholder="备注"></el-input>
      </el-form-item>
      <el-form-item size="mini" label="授权">
        <el-tree
          :data="menuList"
          :props="menuListTreeProps"
          node-key="menuId"
          ref="menuListTree"
          :default-expand-all="true"
          show-checkbox>
        </el-tree>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>

</template>

<script type="text/ecmascript-6">
import { executeGetMenuList, executeGetRoleInfo } from 'network/api/menu'
import { executeSubmitRoleInfo } from 'network/api/role'

export default {
  name: 'RoleAddOrUpdate',
  data () {
    return {
      visible: false,
      menuList: [],
      menuListTreeProps: {
        label: 'name',
        children: 'list'
      },
      dataForm: {
        id: 0,
        roleName: '',
        remark: ''
      },
      dataRule: {
        roleName: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' }
        ],
        remark: [
          { required: true, message: '角色备注不能为空', trigger: 'blur' }
        ]
      },
      tempKey: -666666 // 分隔key, 用于解决tree半选中状态
    }
  },
  methods: {
    init (id) {
      this.visible = true
      this.dataForm.id = id || 0
      executeGetMenuList().then(data => {
        this.menuList = data && data.code === 200 ? data.menuList : null
      }).then(() => {
        this.visible = true
        this.$nextTick(() => {
          this.$refs.dataForm.resetFields()
          this.$refs.menuListTree.setCheckedKeys([])
        })
      }).then(() => {
        if (this.dataForm.id) {
          executeGetRoleInfo(this.dataForm.id).then(data => {
            if (data && data.code === 200) {
              this.dataForm.roleName = data.role.roleName
              this.dataForm.remark = data.role.remark
              const idx = data.role.menuIdList.indexOf(this.tempKey)
              if (idx !== -1) {
                data.role.menuIdList.splice(idx, data.role.menuIdList.length - idx)
              }
              this.$refs.menuListTree.setCheckedKeys(data.role.menuIdList)
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          const chooseMenuIdList = [].concat(this.$refs.menuListTree.getCheckedKeys(), [this.tempKey], this.$refs.menuListTree.getHalfCheckedKeys())
          executeSubmitRoleInfo(
            this.dataForm.id,
            this.dataForm.roleName,
            this.dataForm.remark,
            // this.$refs.menuListTree.getCheckedKeys()).then(data => {
            chooseMenuIdList).then(data => {
            if (data && data.code === 200) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
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
