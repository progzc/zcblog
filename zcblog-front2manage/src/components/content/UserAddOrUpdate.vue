<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="用户名" prop="username" >
        <el-input v-model="dataForm.username" placeholder="登录帐号" :disabled ="!!dataForm.id"></el-input> <!--修改模式下：用户名不可修改-->
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="dataForm.confirmPassword" type="password" placeholder="确认密码"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="dataForm.phone" placeholder="手机号"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="dataForm.email" placeholder="邮箱"></el-input>
      </el-form-item>
      <el-form-item label="角色" size="mini" prop="roleIdList">
        <el-checkbox-group v-model="dataForm.roleIdList">
          <el-checkbox v-for="role in roleList" :key="role.roleId" :label="role.roleId">{{ role.remark }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="false">禁用</el-radio>
          <el-radio :label="true">正常</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
import { executeGetRole } from 'network/api/role'
import { executeGetUserRoleInfo, executeSubmitUserInfo } from 'network/api/user'
import { psdChar, psdKinds, psdLen, phoneLegal, emailLegal } from 'common/js/utils/validate'
import { decryptAES } from 'common/js/utils/encrypt'

export default {
  name: 'UserAddOrUpdate',
  data () {
    // 校验密码或用户名
    const validatePasswordOrUsername = (rule, value, callback) => {
      if (!psdChar(value)) {
        callback(new Error('不能包含空格和中文字符'))
      } else if (!psdKinds(value)) {
        callback(new Error('至少由数字、字母或特殊符号2种组成'))
      } else if (!psdLen(value)) {
        callback(new Error('长度必须在8~16位'))
      } else {
        callback()
      }
    }
    // 校验确认密码
    const validateConfirmPassword = (rule, value, callback) => {
      if (this.dataForm.password !== value) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    // 校验手机号
    const validatePhone = (rule, value, callback) => {
      if (!phoneLegal(value)) {
        callback(new Error('手机号码格式有误或者不属于可用号码范畴'))
      } else {
        callback()
      }
    }
    // 校验邮箱
    const validateEmail = (rule, value, callback) => {
      if (!emailLegal(value)) {
        callback(new Error('邮箱格式有误'))
      } else {
        callback()
      }
    }
    return {
      visible: false,
      roleList: [],
      dataForm: {
        id: 0,
        username: '',
        password: '', // 不要设置为null或undefined
        confirmPassword: '',
        phone: '',
        email: '',
        roleIdList: [],
        status: true
      },
      dataRule: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
          { validator: validatePasswordOrUsername, trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { validator: validatePasswordOrUsername, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '确认密码不能为空', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          { validator: validatePhone, trigger: 'blur' }
        ],
        email: [
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
          { validator: validateEmail, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.id = id || 0
      executeGetRole().then(data => {
        this.roleList = data && data.code === 200 ? data.list : []
      }).then(() => {
        this.visible = true
        this.$nextTick(() => {
          this.$refs.dataForm.resetFields()
        })
      }).then(() => {
        if (this.dataForm.id) {
          executeGetUserRoleInfo(this.dataForm.id).then(data => {
            if (data && data.code === 200) {
              this.dataForm.username = decryptAES(data.user.username)
              this.dataForm.phone = data.user.phone
              this.dataForm.email = data.user.email
              this.dataForm.roleIdList = data.user.roleIdList
              this.dataForm.status = data.user.status
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          executeSubmitUserInfo(this.dataForm).then(data => {
            if (data && data.code === 200) {
              this.$message({
                message: `${!this.dataForm.id ? '添加' : '更新'}` + '成功',
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
