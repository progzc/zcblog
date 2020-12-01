<template>
  <div>
    <el-form :model="article" label-width="80px" :rules="rules" ref="articleForm">
      <!--文章标题-->
      <el-form-item label="文章标题" prop="title">
        <el-col :span="12">
          <el-input placeholder="请输入文章标题" v-model="article.title"  clearable></el-input>
        </el-col>
      </el-form-item>
      <!--文章标签-->
      <el-form-item label="文章标签" prop="tagSelectList">
        <el-col :span="8">
          <el-select style="width: 100%" v-model="tagSelectList"
            multiple
            allow-create
            filterable
            default-first-option
            placeholder="请选择文章标签"
            @change="filterTagList">
            <el-option
              v-for="tag in tags"
              :key="tag.id"
              :label="tag.name"
              :value="tag.id">
            </el-option>
          </el-select>
        </el-col>
      </el-form-item>
      <!--文章作者（默认是Clouds）-->
      <el-form-item label="文章作者" prop="author">
        <el-row>
          <el-col :span="4">
            <el-input placeholder="请输入文章作者" v-model="article.author" clearable></el-input>
          </el-col>
        </el-row>
      </el-form-item>
      <!--文章是否发布（默认不发布）-->
      <el-form-item label="是否发布">
        <el-radio-group v-model="article.publish">
          <el-radio :label="true" >发布</el-radio>
          <el-radio :label="false" >不发布</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--文章是否加密（默认不加密）-->
      <el-row>
        <el-col :span="6">
          <el-form-item label="是否加密">
            <el-radio-group v-model="article.needEncrypt" @change="encryptHandle">
              <el-radio :label="true">加密</el-radio>
              <el-radio :label="false" >不加密</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="加密密码" prop="password">
            <el-input placeholder="请输入加密密码" v-model="article.password" :disabled="!article.needEncrypt" type="password" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!--文章是否置顶显示（默认不置顶）-->
      <el-form-item label="是否置顶">
        <el-radio-group v-model="article.top">
          <el-radio :label="true" >置顶</el-radio>
          <el-radio :label="false" >不置顶</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--是否推荐此文章（默认不推荐）-->
      <el-form-item label="是否推荐">
        <el-radio-group v-model="article.recommend">
          <el-radio :label="true" >推荐</el-radio>
          <el-radio :label="false" >不推荐</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--文章概述-->
      <el-form-item label="文章概述" prop="description">
        <el-col :span="12">
          <el-input type="textarea" v-model="article.description" placeholder="请输入文章文章概述" clearable></el-input>
        </el-col>
      </el-form-item>
      <!--文章内容-->
      <!--subfield:是否双栏；code_style：markdown样式；ishljs：是否开启代码高亮；-->
      <!--@imgAdd：添加图片触发的事件；@imgDel：删除图片触发的事件；@change：编辑区发生变化的回调事件-->
      <el-form-item label="文章内容" prop="content">
        <mavon-editor
          ref="md"
          :subfield="true"
          :code_style="code_style"
          :ishljs="true"
          :externalLink="externalLink"
          v-model="article.content"
          @imgAdd="imgAdd"
          @imgDel="imgDel"
          @change="markdownToHtml">
        </mavon-editor>
      </el-form-item>
      <!--保存/重置文章-->
      <el-form-item>
        <el-button type="primary" @click="saveArticle()">保存</el-button>
        <el-button >重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script type="text/ecmascript-6">
import { mavonEditor } from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
// import 'mavon-editor/dist/highlightjs/styles/gruvbox-dark.min.css'
// import 'mavon-editor/dist/highlightjs/styles/monokai-sublime.min.css'
import 'mavon-editor/dist/highlightjs/styles/ir-black.min.css'
import marked from 'marked'

import { executeGetTagsByType } from 'network/api/tag'
import {
  executeGetArticleInfo,
  executeImgUpload,
  executeImgDelete,
  executeSubmitArticleInfo
} from 'network/api/article'
import { decryptAES } from 'common/js/utils/encrypt'

export default {
  name: 'ArticleAddOrUpdate',
  components: {
    'mavon-editor': mavonEditor
  },
  data () {
    const checkTagList = (rule, value, callback) => {
      if (this.article.tagList === null || this.article.tagList.length < 1) {
        callback(new Error('至少选择一种标签'))
      }
    }
    return {
      article: {
        title: '',
        tagList: [],
        author: 'Clouds',
        publish: false,
        needEncrypt: false,
        top: false,
        recommend: false,
        description: '',
        content: '',
        contentFormat: '',
        password: null
      },
      tags: [],
      tagSelectList: [],
      rules: {
        title: { required: true, message: '文章标题不能为空', trigger: 'blur' },
        tagSelectList: { validator: checkTagList, trigger: 'blur' },
        author: { required: true, message: '作者不能为空', trigger: 'blur' },
        description: { required: true, message: '文章概述不能为空', trigger: 'blur' },
        content: { required: true, message: '文章内容不能为空', trigger: 'blur' }
      },
      code_style: 'solarized-dark',
      externalLink: {
        hljs_css: function () {
          return '/highlightjs/styles/ir-black.min.css' // 这是你的代码高亮配色文件路径
        },
        // false表示禁用自动加载，它也可以是个函数，如果它是个函数，那么这个函数应该返回一个可访问的katex的css路径字符串
        katex_css: false
        // 我们没有设置katex_js, hljs_js, hljs_lang, markdown_css, mavon-editor会认为它的值为true，它会默认使用cdnjs相关外链加载
      }
    }
  },
  created () {
    this.init()
  },
  methods: {
    init () {
      executeGetTagsByType(0).then(data => {
        if (data && data.code === 200) {
          this.tags = data.tagList
        }
      }).then(() => {
        const id = this.$route.params.id
        if (id) {
          executeGetArticleInfo(id).then(data => {
            if (data && data.code === 200) {
              if (data.article.password && data.article.needEncrypt === true) {
                data.article.password = decryptAES(data.article.password)
              } else {
                data.article.password = null
              }
              this.article = data.article
              // 转换选中的标签
              this.tagSelectList = this.article.tagList.map(tag => { return tag.id })
            }
          })
        }
      })
    },

    // 当不加密时，清空密码
    encryptHandle (val) {
      if (val === false) {
        this.article.password = null
      }
    },
    // 过滤标签
    filterTagList (selectItems) {
      const temp = []
      selectItems.forEach(item => {
        let isInput = true
        for (let i = 0; i < this.tags.length; i++) {
          const tag = this.tags[i]
          if (tag.id === item) {
            isInput = false
            temp.push({ id: tag.id, name: tag.name, type: 0 })
          }
        }
        if (isInput) {
          temp.push({ name: item, type: 0 })
        }
      })
      this.article.tagList = temp
    },

    // 新增或更新文章
    saveArticle () {
      this.$refs.articleForm.validate(valid => {
        if (valid) {
          executeSubmitArticleInfo(this.article).then(data => {
            if (data && data.code === 200) {
              this.$message.success('保存文章成功')
              // 关闭当前标签
              this.$emit('closeCurrentTabs')
              // 跳转到list
              this.$router.push('article/list')
            } else {
              this.$message.error(data.msg)
            }
          })
        } else {
          return false
        }
      })
    },

    // 文章内容图片上传
    imgAdd (pos, $file) {
      const formData = new FormData()
      formData.append('file', $file)
      executeImgUpload(formData).then(data => {
        if (data && data.code === 200) {
          this.$message.success('图片上传成功')
          this.$refs.md.$img2Url(pos, data.resource.url)
        }
      })
    },
    // 文章内容图片删除
    imgDel (pos) {
      executeImgDelete(pos[0]).then(data => {
        if (data && data.code === 200) {
          this.$message.success('图片删除成功')
        }
      })
    },
    // markdown转换为html
    markdownToHtml (content, render) {
      this.article.contentFormat = marked(content, { breaks: true })
    }
  }
}
</script>

<style lang="scss" type="text/scss" rel="stylesheet/scss" scoped>

</style>
