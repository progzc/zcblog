<template>
  <el-dialog title="修改密码" :visible.sync="visible" :append-to-body="true" :close-on-click-modal="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="账号">
        <span>{{ username }}</span>
      </el-form-item>
      <el-form-item label="原密码" prop="password">
        <el-input type="password" v-model="dataForm.password"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input type="password" v-model="dataForm.newPassword"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input type="password" v-model="dataForm.confirmPassword"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取 消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
import { psdChar, psdKinds, psdLen } from 'common/js/utils/validate'
import { UPDATE_MAIN_TABS } from 'store/constant/mutation-types'
import { executeModifyPsd } from 'network/api/user'
import { clearLoginInfo } from 'common/js/utils/login'

export default {
  name: 'UpdatePassword',
  data () {
    const validatePassword = (rule, value, callback) => {
      if (!psdChar(value)) {
        callback(new Error('不能包含空格和中文字符'))
      } else if (!psdKinds(value)) {
        callback(new Error('数字、字母以及特殊符号至少包含2种'))
      } else if (!psdLen(value)) {
        callback(new Error('密码长度必须在8~16位'))
      } else {
        callback()
      }
    }
    const validateConfirmPassword = (rule, value, callback) => {
      if (this.dataForm.newPassword !== value) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      visible: false,
      dataForm: {
        password: '',
        newPassword: '',
        confirmPassword: ''
      },
      dataRule: {
        password: [
          { required: true, message: '原密码不能为空', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '新密码不能为空', trigger: 'blur' },
          { validator: validatePassword, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '确认密码不能为空', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    username: {
      get () { return this.$store.state.user.name }
    },
    mainTabs: {
      get () { return this.$store.state.common.mainTabs },
      set (val) { this.$store.commit(UPDATE_MAIN_TABS, val) }
    }
  },
  methods: {
    init () {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.dataForm.resetFields() // 重置element表单
      })
    },
    dataFormSubmit () {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          executeModifyPsd(this.dataForm.password, this.dataForm.newPassword).then(data => {
            if (data && data.code === 200) {
              this.$message.success({
                message: '操作成功',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$nextTick(() => {
                    this.mainTabs = []
                    clearLoginInfo()
                    this.$router.replace({ name: 'login' })
                  })
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
