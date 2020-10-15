# 0 技术方案

> 主体技术方案：**VueCli4**（webpack）+ **Vue**  + **Vuex** + **Vue Router** + **ECMAScript**（JavaScript）+ **npm**（Node.js）+ **CSS/Stylus** + **Vue I18n**（国际化）+ **iView UI**（UI组件，有栅格模式，类似于Bootstrap，利于实现响应式布局）+ **v-viewer**（实现图片预览和简单的编辑）

> 第三方插件：[mavon-editor](https://github.com/topics/mavon-editor)（实现文档转html）、[tocbot](https://github.com/tscanlin/tocbot)（制作博客文档目录）、[highlight.js](https://github.com/highlightjs/highlight.js)（实现代码高亮显示）、**Valine/Github**（评论系统）

快速删除node_modules文件夹：`rimraf node_module`

# 1 字体的设置

## 1.1 字体的引入语法

```css
@font-face {
    font-family: <webFontName>;
    src: <source> [<format>][,<source> [<format>]]*;
    [font-weight: <weight>];
    [font-style: <style>];
}
```

## 1.2 引入字体和字体图标

```css
//引入"Josefin Sans"字体
@font-face
  font-family: 'Josefin Sans'
  src: url('../fonts/font/JosefinSans/JosefinSans-Bold-2.ttf')
  font-weight: bold, 700
  font-style: normal
@font-face
  font-family: 'Josefin Sans'
  src: url('../fonts/font/JosefinSans/JosefinSans-BoldItalic-3.ttf')
  font-weight: bold, 700
  font-style: italic, oblique
@font-face
  font-family: 'Josefin Sans'
  src: url('../fonts/font/JosefinSans/JosefinSans-Italic-4.ttf')
  font-weight: normal, 400
  font-style: italic, oblique
@font-face
  font-family: 'Josefin Sans'
  src: url('../fonts/font/JosefinSans/JosefinSans-Light-5.ttf')
  font-weight: 200
  font-style: normal
@font-face
  font-family: 'Josefin Sans'
  src: url('../fonts/font/JosefinSans/JosefinSans-LightItalic-6.ttf')
  font-weight: 200
  font-style: italic, oblique
@font-face
  font-family: 'Josefin Sans'
  src: url('../fonts/font/JosefinSans/JosefinSans-Regular-7.ttf')
  font-weight: normal, 400
  font-style: normal
@font-face
  font-family: 'Josefin Sans'
  src: url('../fonts/font/JosefinSans/JosefinSans-SemiBold-8.ttf')
  font-weight: 600
  font-style: normal
@font-face
  font-family: 'Josefin Sans'
  src: url('../fonts/font/JosefinSans/JosefinSans-SemiBoldItalic-9.ttf')
  font-weight: 600
  font-style: italic, oblique
@font-face
  font-family: 'Josefin Sans'
  src: url('../fonts/font/JosefinSans/JosefinSans-Thin-10.ttf')
  font-weight: 100
  font-style: nomal
@font-face
  font-family: 'Josefin Sans'
  src: url('../fonts/font/JosefinSans/JosefinSans-ThinItalic-11.ttf')
  font-weight: 100
  font-style: italic, oblique

//引入inconsolata.otf字体
@font-face
  font-family: inconsolata
  src: url('../fonts/font/inconsolata/Inconsolata.otf')
  font-weight: normal
  font-style: normal
```

## 1.3 字体的设置

> 字体回退 (font fallback) 设置为西文→中文，并建议将不同平台的分组按照 OS X→Windows→Linux 的顺序设置。

```stylus
body, html, pre
  line-height: 1
  font-weight: 200
  font-family: "Josefin Sans",Inconsolata,"Helvetica Neue","PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif
```

# 2 代码风格限制

## 2.1 代码编辑风格

> 代码编辑风格在`.editorconfig`文件中进行编辑。

```json
[*.{js,jsx,ts,tsx,vue}]
indent_style = space
indent_size = 2
end_of_line = lf
charset = utf-8
trim_trailing_whitespace = true
insert_final_newline = true

[*.md]
trim_trailing_whitespace = false

[Makefile]
indent_style = tab
```

## 2.2 JavaScript语法检测

> JavaScript语法检测在`.eslintrc.js`文件中进行配置；同时在IDEA中可以启用自动修复功能。
>
> > `.eslintrc.js`文件中的配置如下：
```javascript
module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: [
    'plugin:vue/essential',
    '@vue/standard'
  ],
  parserOptions: {
    parser: 'babel-eslint'
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off'
  }
}
```

>  关于`JSLint`和`ESLint`的区别：
>
>  - `JSLint`
>1. 优点：已由Douglas Crockford配置好，开箱即用。
>    2. 缺点：配置选项有限、规范太严格、扩展性差、无法根据错误定位到规则。
>  - `ESLint`
>    1. 优点：默认规则继承了JSLint和JSHint、可配置`警告`和`错误`等级、可自定义规则、支持ES6。
>    2. 缺点：需要自定义配置（配置比较繁琐）、运行效率慢。
>  >`IDEA`中的设置如下（本项目使用`ESLint`语法检测js文件）：

![image-20200926085426294](zcblog-front2client-docs.assets/image-20200926085426294.png)

## 2.3 Stylus和CSS语法检测

### 2.3.1 自定义vue模板

> 引入stylus时，使用`<style lang="stylus" type="text/stylus" rel="stylesheet/stylus"></style>`
>
> > 可在`IDEA`中自定义生成模板：

![image-20200926100624516](zcblog-front2client-docs.assets/image-20200926100624516.png)

### 2.3.2 开启Stylus、CSS语法检测

>步骤：
>
>- 第一步：安装插件`npm install --save-dev stylelint stylelint-plugin-stylus stylelint-config-standard`；
>
>- 第二步：在`package.json`的scripts中增加如下代码：
>
>```json
>"scripts": {
>   "lint:style": "stylelint src/*.{vue,css,styl} --custom-syntax stylelint-plugin-stylus/custom-syntax"
>}
>```
>- 第三步：在`stylelint.config.js`文件中配置规则，开启stylelint检测css、stylus语法：
>
> ```javascript
> // 开启stylelint检测css、stylus语法
> module.exports = {
>   ignoreFiles: ['**/*.js'],
>   extends: ['stylelint-config-standard', 'stylelint-plugin-stylus/recommended'],
>   rules: {
>     // override/add rules settings here, such as:
>     // "stylus/declaration-colon": "never"
>     'no-empty-source': null, // 允许空源
>     'font-family-no-duplicate-names': null, // 允许使用重复的字体名称
>     'value-list-comma-space-after': 'always', // 在值列表的逗号之后要求有一个空格
>     'selector-pseudo-element-colon-notation': 'double', // 指定伪元素使用双冒号
>     'declaration-colon-space-before': 'never', // 在冒号之前禁止有空白
>     'declaration-colon-space-after': 'always', // 在冒号之后要求有一个空格
>     'color-hex-length': 'long', // 指定十六进制颜色不使用缩写
>     'value-keyword-case': 'lower', // 指定关键字的值采用小写
>     'media-feature-name-no-unknown': null, // 允许使用未知的media特性名称
>     'media-query-list-comma-newline-before': 'never-multi-line', // 在多行媒体查询列表的逗号之前禁止有空白
>     'media-query-list-comma-space-after': 'always', // 在媒体查询的逗号之后要求有一个空格
>     'rule-empty-line-before': null // 在规则之前并非必须有一空行
>   }
> }
> ```
>- 第四步：运行`npm run lint:style`对vue中的stylus代码进行检查。

> **注意事项**：
>
> - `stylelint.config.js`中的rules配置请查看：[Stylelint规则用户指南](https://cloud.tencent.com/developer/section/1489630)
>
> - 开启Scss、CSS语法检测请查看：[配置Stylelint检测Scss、CSS语法](https://staven630.github.io/vue-cli4-config/#stylelint)
> - 此时若开启IDEA的Stylus Linter插件的自动检测功能，会导致JSLint enable，最后会导致js文件报错，感觉是一个Bug!

![image-20200926133323223](zcblog-front2client-docs.assets/image-20200926133323223.png)

### 2.3.3 @import和import

@import和import的区别：
>- 在js中引入stylus、css文件使用import；
>- 在css、stylus中引入其他css、stylus文件使用@import；
>- 在vue的`<style></style>`中引入其他css、stylus文件使用@import。

# 3 Vue Cli4配置

> **注意事项**：Vue脚手架的具体配置请参考 [Vue-Cli4-config](https://staven630.github.io/vue-cli4-config/#globalstylus)、[Vue-Cli3-config](https://github.com/staven630/vue-cli4-config/tree/vue-cli3)、[Vue-Cli官网](https://cli.vuejs.org/zh/config/)。

## 3.1 配置项目构建

> 在`vue.config.js`中配置项目构建：
>

```javascript
const IS_PROD = ['production', 'prod'].includes(process.env.NODE_ENV)

module.exports = {
  publicPath: IS_PROD ? process.env.VUE_APP_PUBLIC_PATH : './', // 默认'/'，部署应用包时的基本 URL
  outputDir: 'dist', // 默认值,生产环境构建文件的目录
  assetsDir: '', // 默认值,放置生成的静态资源(js、css、img、fonts)的(相对于outputDir的)目录
  lintOnSave: false, // 不会将lint错误输出为编译警告,即有不符合lint语法时，也会编译成功
  runtimeCompiler: false, // 使用runtime-only编译，打包小、效率更高
  productionSourceMap: !IS_PROD, // 生产环境不需要source map时，将其设置为false,可以加速构建
  parallel: require('os').cpus().length > 1, // 默认值,作用于生产构建,在系统的 CPU 有多于一个内核时自动启用
}
```

## 3.2 配置跨域

在`vue.config.js`中配置跨域：

```javascript
module.exports = {
  devServer: {
    open: true, // npm run serve后自动打开页面
    host: 'localhost', // 匹配本机IP地址
    port: 8080, // 开发服务器运行端口号
    https: false, // 不开启https
    hotOnly: true, // 开启热更新
    // 若前端应用和后端API服务器没有运行在同一个主机上，则需要将API请求代理到API服务器
    proxy: {
      // 例如将'https://localhost:8080/api/xxx'代理到'https://localhost:8082/api/xxx'
      '/api': {
		target: 'http://localhost:8082', // 目标代理接口地址
        secure: false, // 忽略https安全提示(如果是https接口，需要配置这个参数)
        changeOrigin: true, // 本地会虚拟一个服务器接收请求并代发该请求
        ws: true, // 启用websockets
        pathRewrite: { // 重写地址，将前缀 '/api' 转为 '/',相当于此时代理到'https://localhost:8082/xxx'
          '^/api': '/'
        }
      }
    }
  }
}

```

## 3.3 配置扩展名

> 在`vue.config.js`中配置扩展名：
>

```javascript
const path = require('path')
const resolve = dir => path.join(__dirname, dir)

module.exports = {
  // 配置扩展名
  configureWebpack: {
    resolve: {
      extensions: ['.js', '.vue', '.json'] // 默认配置
    }
  }
}
```

## 3.4 配置别名

> 在`vue.config.js`中配置别名：
>

```javascript
const path = require('path')
const resolve = dir => path.join(__dirname, dir)

module.exports = {
  chainWebpack: config => {
    // 添加别名
    config.resolve.alias
      .set('vue$', 'vue/dist/vue.esm.js')
      .set('@', resolve('src'))
      .set('assets', resolve('src/assets'))
      .set('common', resolve('src/common'))
      .set('components', resolve('src/components'))
      .set('network', resolve('src/network'))
      .set('views', resolve('src/views'))
  }
}
```

> **别名的使用**：
>
> - 在`<style></style>`和`<template></template>`里使用别名时，需要在别名前面加上~，这样就会告知加载器这是一个模块，而不是绝对路径；
> - 在css、styl文件中引入的时候，使用别名时前也需要加~；
> - 在`<script></script>`或者js里引入的时候，不需要加 ~，直接用别名就行。

## 3.5 配置stylus全局变量 

> 安装style-resources-loader：`npm i -D style-resources-loader`（等价于`npm install --dev style-resources-loader`）
>
> 在`vue.config.js`中配置全局变量
>

```javascript
const path = require("path")
const resolve = dir => path.resolve(__dirname, dir)
const addStylusResource = rule => {
  rule
    .use('style-resouce')
    .loader('style-resources-loader')
    .options({
      patterns: [resolve('src/common/stylus/theme.styl')]
    })
}

module.exports = {
  chainWebpack: config => {
    // 为 stylus 提供全局变量
    const types = ['vue-modules', 'vue', 'normal-modules', 'normal']
    types.forEach(type =>
      addStylusResource(config.module.rule('stylus').oneOf(type))
    )
  }
}
```

## 3.6 package.json

在`package.json`中主要对项目运行配置及依赖包进行管理：

```json
{
  "name": "zcblog-front2client",
  "version": "0.1.0",
  "private": true,
  "scripts": {
    "serve": "vue-cli-service serve",
    "build": "vue-cli-service build",
    "crm": "vue-cli-service build --mode crm",
    "lint": "vue-cli-service lint",
    "lint:style": "stylelint src/**/*.{vue,css,styl} --custom-syntax stylelint-plugin-stylus/custom-syntax"
  },
  "dependencies": {
    "core-js": "^3.6.5",
    "vue": "^2.6.11",
    "vue-router": "^3.2.0",
    "vuex": "^3.4.0"
  },
  "devDependencies": {
    "@vue/cli-plugin-babel": "~4.5.0",
    "@vue/cli-plugin-eslint": "~4.5.0",
    "@vue/cli-plugin-router": "~4.5.0",
    "@vue/cli-plugin-vuex": "~4.5.0",
    "@vue/cli-service": "~4.5.0",
    "@vue/eslint-config-standard": "^5.1.2",
    "babel-eslint": "^10.1.0",
    "eslint": "^6.7.2",
    "eslint-plugin-import": "^2.20.2",
    "eslint-plugin-node": "^11.1.0",
    "eslint-plugin-promise": "^4.2.1",
    "eslint-plugin-standard": "^4.0.0",
    "eslint-plugin-vue": "^6.2.2",
    "style-resources-loader": "^1.3.3",
    "stylelint": "^13.7.1",
    "stylelint-config-standard": "^20.0.0",
    "stylelint-plugin-stylus": "^0.9.0",
    "stylus": "^0.54.7",
    "stylus-loader": "^3.0.2",
    "vue-template-compiler": "^2.6.11"
  }
}
```

## 3.7 开发环境配置

​		开发环境配置一般有开发环境（development）、测试环境（test）、预发服务器环境（crm）、生产环境（production）；其中基于Vue的前端项目有部分公司有test环境，有大部分是没有test环境的；预发服务器环境和生产环境基本一致。

​		还有项目的开发环境是这样配置的：.env（配置基本环境）、.env.development（配置开发环境）、.env.test（配置测试环境）、.env.production（配置生产环境）。

### 3.7.1 本地开发环境配置

本地开发环境在`.env`文件中配置：

```json
// serve 默认的本地开发环境配置
NODE_ENV = "development"
BASE_URL = "./"
// VUE_APP_PUBLIC_PATH = "./"
// VUE_APP_API = "https://test.zcblog.com/api"
```

### 3.7.2 预发环境配置

预发环境在`.env.crm`中配置：

```json
// 自定义 build 环境配置（预发服务器）
NODE_ENV = "production"
// BASE_URL = "https://crm.zcblog.com/"
// VUE_APP_PUBLIC_PATH = "https://crm.oss.com/zcblog"
// VUE_APP_API = "https://crm.zcblog.com/api"

// ACCESS_KEY_ID = "xxxxxxxxxxxxx"
// ACCESS_KEY_SECRET = "xxxxxxxxxxxxx"
// REGION = "oss-cn-hangzhou"
// BUCKET = "zcblog-crm"
// PREFIX = "zc-blog
```

### 3.7.3 生产环境配置

生产环境在`.env.production`中配置：

```json
// build 默认的环境配置（正式服务器）
NODE_ENV = "production"
// BASE_URL = "https://prod.zcblog.com/"
// VUE_APP_PUBLIC_PATH = "https://prod.oss.com/zcblog"
// VUE_APP_API = "https://prod.zcblog.com/api"

// ACCESS_KEY_ID = "xxxxxxxxxxxxx"
// ACCESS_KEY_SECRET = "xxxxxxxxxxxxx"
// REGION = "oss-cn-hangzhou"
// BUCKET = "zcblog-prod"
// PREFIX = "zc-blog"
```

# 4 基本开发思路构建

## 4.1 系统基本结构

下图为博客前台系统的开发思路，对应于`route/index.js`

![image-20200929204030386](zcblog-front2client-docs.assets/image-20200929204030386.png)

## 4.2 数据表的结构

### 4.2.1 article/tag/article_tag

​		文章表/标签表/文章表与标签表的中间表的构建：

![image-20200929204644788](zcblog-front2client-docs.assets/image-20200929204644788.png)

# 5 项目目录



# 6 引入iview UI

## 6.1 全量引入

- 第1步：使用npm安装iview

  ```json
  npm install view-design --save
  ```

- 第2步：在`mian.js`中进行如下配置：

  ```javascript
  import Vue from 'vue';
  import VueRouter from 'vue-router';
  import App from 'components/app.vue';
  import Routers from './router.js';
  
  import ViewUI from 'view-design';
  import 'view-design/dist/styles/iview.css';
  
  Vue.use(VueRouter);
  Vue.use(ViewUI);
  
  // The routing configuration
  const RouterConfig = {
      routes: Routers
  };
  const router = new VueRouter(RouterConfig);
  
  new Vue({
      el: '#app',
      router: router,
      render: h => h(App)
  });
  ```

## 6.2 按需引入（推荐）

​		借助插件`babel-plugin-import`可以实现按需加载组件，**减少文件体积**。

- 第1步：使用npm安装iview

  ```json
  npm install view-design --save
  ```

- 第2步：安装`babel-plugin-import`插件

  ```json
  npm install babel-plugin-import --save-dev
  ```

- 第3步：在`babel.config.js`文件中配置插件

  ```javascript
  plugins: [['import', {
    libraryName: 'view-design',
    libraryDirectory: 'src/components'
  }]]
  ```

- 第4步：按需引入组件（注意：需要引入`iview.css`文件）

  ```javascript
  import Vue from 'vue';
  import VueRouter from 'vue-router';
  import App from 'components/app.vue';
  import Routers from './router.js';
  // 按需引入iview
  import { Button, Table } from 'view-design';
  import 'view-design/dist/styles/iview.css';
  
  Vue.use(VueRouter);
  // 全局注册iview(例)
  Vue.component('Button', Button);
  Vue.component('Table', Table);
  
  // The routing configuration
  const RouterConfig = {
      routes: Routers
  };
  const router = new VueRouter(RouterConfig);
  
  new Vue({
      el: '#app',
      router: router,
      render: h => h(App)
  });
  ```

## 6.3 Vuex与Vue.prototype

**Vuex与Vue.prototype的区别：**

- Vuex和Vue.prototype.xxx都可以用来定义全局变量，注册完后，分别使用$store.xxx、$xxx来访问。
- Vuex管理的变量是响应式，若被修改，会被重新渲染到页面。
- Vue.prototype注册的全局变量只能手动修改，不是响应式的，不会被重新渲染。

# 7 国际化（前端UI界面）

## 7.1 iView和vue-i18n的国际化

### 7.1.1 iView全量引入

- 第1步：根据官网给出的iView全局引入的国际化方法，在`mian.js`中进行如下配置：

  ```javascript
  // 可以兼容 vue-i18n@6.x+
  import Vue from 'vue'
  import App from './App.vue' // Vue挂载到实例
  import router from './router' // Vue路由
  import store from './store' // Vue状态管理器
  import VueI18n from 'vue-i18n' // 导入vue-i18n
  import messages from './i18n' // 引入自定义国际化内容
  import 'view-design/dist/styles/iview.css' // 按需引入iView UI的样式
  import iViewUI from 'view-design' // 全局引入
  
  Vue.use(VueI18n)
  Vue.use(iViewUI) // 全局注册iView组件
  Vue.locale = () => {} // 旨在解决兼容性问题，但是测试后发现并不能解决问题
  
  Vue.prototype.$Modal = iViewUI.Modal // 使用Model来在组件中测试iView的国际化问题是否已经成功
  
  // Create VueI18n instance with options
  const i18n = new VueI18n({
      locale: 'en',  // set locale
      messages  // set locale messages
  })
  new Vue({
    router,
    store,
    i18n,
    render: h => h(App)
  }).$mount('#app')
  ```

- 第2步：在组件中测试iView的国际化问题是否已经成功：

  ```javascript
  export default {
    name: 'HomeSideBar',
    mounted () {
      this.$Modal.info({ title: '测试', content: '测试iView UI的国际化是否已经成功' })
    }
  }
  ```

  发现曝出如下错误，说明官方的指导文件是错误的。

![image-20200930171418314](zcblog-front2client-docs.assets/image-20200930171418314.png)

- 第3步：进行如下改动

  ```javascript
  // 将下面这句注释
  //Vue.locale = () => {};// 旨在解决兼容性问题，但是测试后发现并不能解决问题
  // 替换成这句
  Vue.use(iViewUI, {
    i18n: (key, value) => i18n.t(key, value)
  })
  ```

  同样进行测试，发现iView UI全量引入的国际化问题已经成功：

  ![image-20200930172436408](zcblog-front2client-docs.assets/image-20200930172436408.png)

### 7.1.2 iView按需引入的国际化

- 官网只给出了单独实现iView组件按需引入的国际化问题，对于结合vue-i18n的按需引入iView实现国际化并未给出具体解决方案，试验了几种方法找到了最终解决办法方案。

```javascript
// 可以兼容 vue-i18n@6.x+
import Vue from 'vue'
import App from './App.vue' // Vue挂载到实例
import router from './router' // Vue路由
import store from './store' // Vue状态管理器
import VueI18n from 'vue-i18n' // 导入vue-i18n
import messages from './i18n' // 引入自定义国际化内容
import 'view-design/dist/styles/iview.css' // 按需引入iView UI的样式
// 引入iview.js文件，这样才能使用iview.js中的i18n方法实现按需导入的iView组件的国际化
import iViewUI from 'view-design/dist/iview'

Vue.use(VueI18n)
// Vue.use(iViewUI) // 全局注册iView组件
// Vue.locale = () => {} // 旨在解决兼容性问题，但是测试后发现并不能解决问题

Vue.component('iv-row', iViewUI.Row) // 按需注册组件
Vue.prototype.$Modal = iViewUI.Modal // 使用Model来在组件中测试iView的国际化问题是否已经成功
iViewUI.i18n((key, value) => i18n.t(key, value)) // 解决按需注册iView组件与vue-i18n引起的兼容性问题

// Create VueI18n instance with options
const i18n = new VueI18n({
  locale: 'en', // 设置国际化语言
  messages // 设置国际化内容
})
new Vue({
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app')
```

> **将上面的三个重点地方单独标注出来**：

```js
// 按这种方法可以引入iview.js文件
import iViewUI from 'view-design/dist/iview'; // 千万不要使用import iViewUI from 'view-design'这种方式
// 按需注册组件使用下面的方法
Vue.component('iv-row', iViewUI.Row)
// 解决兼容性问题
iViewUI.i18n((key, value) => i18n.t(key, value))
```

### 7.1.3 注意事项

#### 7.1.3.1 一点细节

> 在iView从按需引入转换到全量引入时，不要忘记调整`babel.config.js`中的内容（否则按需引入的国际化也会出错）、重启项目生效：

```js
module.exports = {
  presets: [
    '@vue/cli-plugin-babel/preset'
  ],
  plugins: [['import', { // iView按需引入时的配置
    libraryName: 'view-design',
    libraryDirectory: 'src/components'
  }]]
  // plugins: [] // iView全量引入时的配置
}
```

#### 7.1.3.2 关于箭头函数

​		在使用this.$Modal.info时遇到了一些麻烦，注意应该在挂载的vue组件下使用，而在普通函数下使用会显示this.$Modal是undefined类型，这里的关键是this的含义。（具体普通函数和箭头函数下的this的指向推荐阅读文章：[普通函数和箭头函数](https://segmentfault.com/a/1190000015480642)）

## 7.2 自定义国际化内容

使用vue-i18n来完成自定义国际化的内容，具体做法如下：

- 第1步：自定义项目的国际化内容。

  `zh-CN.js`中定义中文的国际化内容：

  ```js
  module.exports = {
    homeNav: {
      home: '主页',
      tags: '标签',
      timeline: '时光轴',
      pseudonym: '云 岫',
      motto: '凡是过往, 皆为序章',
      searchPlaceholder: '搜索关键词',
      pageView: '浏览量',
      uniqueVisitor: '访客数',
      gallery: '相册'
    }
  }
  ```

  `en-US.js`中定义英文的国际化内容：

  ```js
  module.exports = {
    homeNav: {
      home: 'Home',
      tags: 'Tags',
      timeline: 'Timeline',
      pseudonym: 'Clouds',
      motto: 'Where of what\'s past is prologue',
      searchPlaceholder: 'Search keywords...',
      pageView: 'PV',
      uniqueVisitor: 'UV',
      gallery: 'Gallery'
    }
  }
  ```

- 第2步：与iView中的国际化内容进行合并。

  在`i18n/index.js`中合并自定义的国际化内容与iView的国际化内容：

  ```js
  // 按需导入iView UI国际化语言
  import en from 'view-design/dist/locale/en-US'
  import zh from 'view-design/dist/locale/zh-CN'
  
  // 导入自定义国际化内容
  import enUS from 'i18n/lang/en-US'
  import zhCN from 'i18n/lang/zh-CN'
  
  const messages = {
    en: Object.assign(enUS, en),
    zh: Object.assign(zhCN, zh)
  }
  export default messages
  ```

- 第3步：在`main.js`中进行配置，参见7.1.1或7.1.2

## 7.3 国际化的使用

### 7.3.1 内容引用

> 使用``{{$t('可国际化内容')}}``进行引用，具体例子：`{{$t('homeNav.motto')}}`。

> **input标签的placeholder属性的国际化：**:placeholder="$t('homeNav.searchPlaceholder')"。

### 7.3.2 语言切换

> 全局切换（不常用）：在挂载的vue组件中使用`this.$i18n.locale = 'zh'`可将语言切换成中文。（千万注意this的指向）

> **针对不同用户切换，需要解决两个问题：**

- 页面刷新后，通过按钮切换的语言还原成了最初的语言，无法保存。

- 同一个浏览器中新增页面时必须"记住"选择的语言。

  以上问题可以使用cookie的思想来解决：
  `main.js`中如下配置：

  ```javascript
  const i18n = new VueI18n({
    locale: localStorage.getItem('language') || 'en', // 设置国际化语言
    messages // 设置国际化内容
  })
  ```

  切换语言时进行如下配置：

  ```vue
  methods: {
      changeLanguage (flag) {
        if (flag === false) {
          this.$i18n.locale = 'en'
          localStorage.setItem('language', 'en')
        } else {
          this.$i18n.locale = 'zh'
          localStorage.setItem('language', 'zh')
        }
      }
    }
  ```

  相关博客文章参考：**[vue项目使用vue-i18n和iView切换多语言](https://www.cnblogs.com/zhengshize/p/10002870.html)**、[使用 vue-i18n 切换中英文](https://www.cnblogs.com/rogerwu/p/7744476.html)

### 7.3.3 关于**localStorage**

- localStorage主要用来作为本地存储，解决cookie存储空间不足的问题（cookie中每条cookie的存储空间为4k），而localStorage中一般浏览器支持的是5M大小；

- 支持IE8以上；

- 目前所有的浏览器中都会把localStorage的值类型限定为string类型；

- 在浏览器的隐私模式下面是不可读取；不能被爬虫抓取到；

- 本质上是对字符串的读取，如果存储内容多的话会消耗内存空间，会导致页面变卡。

​        相关博客文章参考：[localStorage使用总结](https://www.cnblogs.com/st-leslie/p/5617130.html)

### 7.3.4 网页title的国际化

> 解决办法：

- 网页title随页面的变化可以在路由守卫中进行动态设置。

- 网页title的国际化可以使用i18n的t()方法进行设置。

> 具体步骤如下：

- 第1步：在`main.js`中全局注册i18n

  ```js
  Vue.use(VueI18n) // 全局注册vue-i18n
  const i18n = new VueI18n({
    locale: localStorage.getItem('language') || 'en', // 设置国际化语言
    messages // 设置国际化内容
  })
  Vue.prototype.i18n = i18n // 为将路由中的title国际化，配置全局i18n
  iViewUI.i18n((key, value) => i18n.t(key, value))
  ```

- 第2步：在`en-US.js`、`zh-CN.js`中配置国际化项

  ```javascript
  // en-US.js
  module.exports = {
    metaTitle: {
      index: 'Home | Clouds'
    }
  }
  // zh-CN.js
  module.exports = {
    metaTitle: {
      index: '首页 | 云岫'
    }
  }
  ```

- 第3步：在`routes.js`中配置页面title

  ```javascript
  export default [
    {
      path: '/',
      component: Index,
      children: [
        {
          path: '/',
          name: 'index',
          components: {
            sideBar: HomeSideBar,
            content: ArticleAbstractList
          },
          meta: {
            title: 'metaTitle.index'
          }
        }
      ]
    }
  ]
  ```

  

- 第4步：在`router/index.js`中利用路由守卫动态更新title

  ```java
  router.beforeEach((to, from, next) => {
    LoadingBar.start()
    window.scrollTo(0, 0)
    if (to.meta.title) {
      document.title = Vue.prototype.i18n.t(to.meta.title) // 这里是配置的关键
    }
    next()
  })
  ```


# 8 网络请求封装

## 8.1 注意事项

### 8.1.1 字符串拼接

> ES6中进行字符串拼接使用${变量}，注意拼接时要使用反单引号（即\`），示例如下：

```javascript
示例一：
var a = 1;
console.log(`a的值是：${a}`); // I love ${a}, because he is handsome.a的值是：1
示例二：
let a='Karry Wang';
let str=`I love ${a}, because he is handsome.`;
//注意：这行代码是用返单号引起来的
alert(str); // I love Karry Wang, because he is handsome.
```

### 8.1.2 Terminal的git配置

在IDEA的Terminal窗口使用git将本地文件推送到Github时，每次推送都要输入账号和密码，比较繁琐，解决方法为：

在该项目的.git文件夹，修改config文件：

```json
[core]
	repositoryformatversion = 0
	filemode = false
	bare = false
	logallrefupdates = true
	symlinks = false
	ignorecase = true
[remote "origin"]
	url = https://name:password@github.com/progzc/zcblog.git //在name位置配置账户名，在password位置配置密码即可
	fetch = +refs/heads/*:refs/remotes/origin/*
[branch "master"]
	remote = origin
	merge = refs/heads/master
```

### 8.1.3 XSS和CSRF攻击

#### 8.1.3.1 XSS攻击

**XSS（Cross Site Scripting）**：跨站脚本攻击，恶意攻击者往 Web 页面里插入恶意 Script 代码，当用户浏览该页之时，嵌入其中 Web 里面的 Script 代码会被执行，从而达到恶意攻击用户的目的。主要类型有**Reflected XSS**、**Stored XSS**、**DOM-based or local XSS**

**XSS**防御的方法：输入过滤、输出转义、使用 HttpOnly Cookie。

> 本项目解决XSS攻击的方法：后端采用**Jsoup**过滤恶意脚本。

#### 8.1.3.2 CSRF攻击

**CSRF（Cross-site request forgery）**：跨站请求伪造，伪造请求，冒充用户在站内的正常操作。

**CSRF防御的方法**：验证 HTTP Referer 字段、在请求地址中添加 token 并验证、在 HTTP 头中自定义属性并验证。

### 8.1.4 前后端加密

#### 8.1.4.1 加密和解密

**对称加密算法**：加密和解密用到的密钥是相同的，这种加密方式加密速度非常快，适合经常发送数据的场合；缺点是密钥的传输比较麻烦。

**非对称加密算法**：加密和解密用的密钥是不同的，这种加密方式是用数学上的难解问题构造的，通常加密解密的速度比较慢，适合偶尔发送数据的场合。优点是密钥传输方便。常见的非对称加密算法为RSA、ECC和EIGamal。

**实际使用**：一般是通过RSA加密AES的密钥，传输到接收方，接收方解密得到AES密钥，然后发送方和接收方用AES密钥来通信。

#### 8.1.4.2 前后台交互加密方式

CSDN上有一篇文章写得很好，[前后台交互加密方式](https://blog.csdn.net/qq_42039281/article/details/89680210)；值得一看（包括登录机制、加密方法）。

### 8.1.5 crypto-js

`crypto-js`是前端项目中一般用来加密的库（可以对请求参数），本项目博客前台未采用加密，博客管理前台采用cookie来保证跨域安全性。

## 8.2 封装axios

为了降低代码的耦合度，可以根据axios封装自己的网络请求。

在`network/request.js`下进行封装：

```javascript
import axios from 'axios'

function request (config) { // 封装网络请求
  // 1.创建axios的示例
  const instance = axios.create({ // 创建网络请求示例
    baseURL: process.env.OPEN_PROXY ? process.env.VUE_APP_API : process.env.BASE_URL,
    timeout: 1000 * 10, // 最大延时10s
    withCredentials: true, // 当前请求为跨域类型时,在请求中携带cookie
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    }
  })

  // 2.1 请求拦截
  instance.interceptors.request.use(config => {
    return config
  }, error => {
    console.log(error)
  })

  // 2.2 响应拦截
  instance.interceptors.response.use(res => {
    return res.data
  }, error => {
    console.log(error)
  })

  // 3.发送真正的网络请求
  return instance(config)
}
```

# 9 vue相关技巧

## 9.1 深入理解.sync修饰符

> 传统的子组件像父组件通信的方法是在子组件中使用$emit向父组件发射事件传递信息。

子组件`son.vue`：

```vue
<template>
    <div>
        <input @input="onInput" :value="value"/>
    </div>
</template>

<script>
export default {
    props: {
        value: {
            type: String
        }
    },
    methods: {
        onInput(e) {
            this.$emit("valueChanged", e.target.value)
        }
    }
}
</script>
```

父组件`father.vue`：

```vue
<template>
    <son :value="myValue" @valueChanged="e => myValue = e"></son>
</template>

<script>
    import son from './son.vue';
    export default {
        components: {
            son,
        },
        data() {
            return {
                myValue: 1234,
            }
        },
    }
</script>
```

> 采用.sync修饰符可以简化这一过程，具体方法是：

1. 组件内触发的事件名称以"update:myPropName"命名，相应的上述sun组件改为 update:value。
2. 父组件v-bind:value加上.sync修饰符，即v-bind:value.sync，这样父组件就不用再手动绑定@update:value事件了。

子组件`son.vue`：

```vue
methods: {
    onInput(e) {
        this.$emit("update:value", e.target.value)
    }
}
```

父组件`father.vue`：

```vue
<son :value.sync="myValue"></son>
```

## 9.2 穿透组件

### 9.2.1 穿透iView组件

> 在使用iView组件时，如果想更改iView某一个组件的样式，可以在CSS/Stylus中采用穿透iView组件的方法来改变iView的自定义样式，方法如下：

1. 通过浏览器查询到所要更改的样式的选择器（Chrome浏览器是按下F12）：

   ![image-20201005092129871](zcblog-front2client-docs.assets/image-20201005092129871.png)

2.  根据查询到的选择器自定义样式（必须要在选择器前增加>>>），通过样式的层叠性更改默认样式：

   ```stylus
   >>>.ive-page-item
         background-color red
   ```

3. 若自定义样式未生效，应重点检查样式的权重（终极方法是在样式后面添加`!important`将权重提高到最优先级）

   ```stylus
   >>>.ive-page-item
         background-color red !important
   ```

### 9.2.2 穿透自定义组件

> `>>>`除了可以穿透UI框架组件的样式，还可以穿透自定义组件的样式（即使在vue的样式中定义了scoped属性），利用这一特性，可以使自定义组件在不同组件间使用时的呈现出不一致的样式，这在某些情况下可能会很有用。

例如：`ArticleAbstractList.vue`需要在屏幕尺寸小于576px时，其引入的`content-box.vue`组件的.content-container的宽度为100%，而在其他组件中需要在屏幕尺寸小于576px时，其引入的`content-box.vue`组件的.content-container的宽度为95%，可以在`ArticleAbstractList.vue`采用如下配置：

```stylus
@media screen and (max-width: $size-sm)
  .article-abstract-page-container
    margin-bottom 100px
  .article-abstract-item:last-of-type
    margin-bottom $footer-height-pageContent + 150px !important
  >>>.content-container // 穿透自定义组件
   width 100% !important
```

## 9.3 时间线的实现

### 9.3.1 时间线的背景

> 在使用时间线展示文章列表时，需要按照年份（从今往前排列）分类展示文章写作情况（包括文章创建时间、文章标题），并按照分页展示。

![image-20201005172740546](zcblog-front2client-docs.assets/image-20201005172740546.png)

### 9.3.2 时间线的实现思路

> 思路：

1. 先将页面需要的数据用ajax查询出的后台结果封装到一个对象articleListByDate中。
   - articleListByDate对象包含创建日期（例`createTime："2020-10-01"`）、文章标题（例`articleTitle:"Java数字签名算法"`）、文章总数（例`total`）；
   - 从数据库中查询到的数据要按照降序排列**（第一排序：日期；第二排序：文章标题按照字典顺序排列）**；
   - 由于文章可能太多，要求进行分页展示，每页展示45条数据。

2. 实现算法：

   - 第1步：在工具类中，实现一个快速查询当前日期是否存在的方法；

     - 创建一个js的dateMap集合（实现javad中类似HashMap的功能）
     - 实现DateMap集合的快速查询功能

   - 第2步：进行第一页的展示（**使用v-for循环**）；

     - 从数据库中取出前45条数据，放到articleListByDate集合中；

     - 创建一个dateMap集合，将articleListByDate集合中的第一条数据取出，将其createTime属性取出；

     - 判断dateMap集合是否为空，若为空，则直接将create属性放置到dateMap集合中，若不为空，则将dateMap集合置为空后再将create属性放置到dateMap集合中；

     - 创建第1个日期（年）标签，展示第1条数据（月-日 文章标题）标签；

     - 判断第2条数据中的日期在dateMap集合中是否存在：

       - 若存在，则直接将第2条数据（月-日 文章标题）标签（**使用v-if使（年）标签隐藏**，不要使用v-show标签）；

       - 若不存在，则先将第2条数据的create属性放置到dateMap集合中，然后创建第2个日期（年）标签，展示第2条数据（月-日 文章标题）标签；

     - ......循环判断，直到第一页的45条数据已经处理完。
     - 最后，清空dateMap集合

    - 第3步：更新当前页的页码，进行第2页的数据展示。
   
   - 第4步：相关样式的编辑。

## 9.4 使用v-if/v-show控制分页

> 背景：在页面设计中经常需要进行分页，为了提高用户体验，当页面显示项小于单页允许项的数量时，需要隐藏分页条，反之需要显示分页条。

解决方法：若需要频繁切换显示与隐藏，使用v-show；反之使用v-if。

```vue
<template>
    <div class="tag-timeline-page-container" v-if="checkPage()">
      <iv-page
        class-name="tag-timeline-pagination"
        :total="pagination.total"
        :current="pagination.currentPage"
        :pageSize="pagination.pageSize"  >
        @on-change="handleCurrentChange"
      </iv-page>
    </div>
</template>
<script type="text/ecmascript-6">
import HashMap from 'common/js/HashMap'
import TimeLine from 'components/content/TimeLine'

export default {
  methods: {
    checkPage () {
      return this.tagTimelineList.articleTimelineList.length > this.pagination.pageSize
    }
  }
}
</script>
```

> 注意事项：`v-if="checkPage()"`写成`v-if="checkPage"`不会生效。

## 9.5 监听滚动位置

> 为了提高用户体验，需要监听`content.vue`中内容的滚动条位置，实时向用户输出滚动条位置，需要注意以下细节：

- 滚动进度条提供置顶功能，且要平滑过渡；
- 当滚动条位于顶端时，隐藏滚动进度条；
- 没有滚动条未出现时，隐藏滚动进度条；
- 小屏幕需要隐藏滚动进度条。
- 考虑浏览器的兼容性问题

> 具体实现（参见`content.vue`中的代码），关键内容如下：

```vue
<template>
  <div class="content-box">
    <transition name="slide-fade"> // 添加隐藏动画效果
      <div class="scroll-progress-bar" @click="scroll2Top" v-show="show">
        {{scrollPercent}}
      </div>
    </transition>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  name: 'ContentBox',
  data () {
    return {
      show: false, // 没有滚动条未出现时，隐藏滚动进度条
      scrollPercent: 0 // 没有滚动条未出现时，隐藏滚动进度条
    }
  },
  mounted () {
    window.addEventListener('scroll', this.windowScroll) // 监听浏览器滚动事件
  },
  methods: {
    windowScroll () { // 浏览器滚动事件反馈
      // 滚动条距离顶部距离（考虑浏览器兼容性问题）
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
      // 窗口高度（考虑浏览器兼容性问题）
      const windowHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight
      // 滚动条内容总高度（考虑浏览器兼容性问题）
      const scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight
      this.scrollPercent = Math.round(scrollTop / (scrollHeight - windowHeight) * 100)
      if (this.scrollPercent === 0) { // 当滚动条位于顶端时，隐藏滚动进度条
        this.show = false
      } else {
        this.show = true
      }
    },
    scroll2Top () { // 滚动进度条提供置顶功能，且要平滑过渡
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      })
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  @import '~common/stylus/index.styl'
  .content-box
    position relative
    left 20%
    background $color-page-background
    width 80%
    min-height 100vh
    .scroll-progress-bar
      display inline-block
      position fixed
      padding-top 1.5px
      bottom 30px
      right 30px
      text-align center
      color white
      font-weight normal
      width 1.7rem
      height 1.7rem
      line-height 1.7rem
      border-radius 50%
      background $color-gradually-gray-31
      &:hover
        cursor pointer
        background $color-on-hover
  .slide-fade-enter-active ,.slide-fade-leave-active // 显示效果动画
    transition all .2s ease
  .slide-fade-leave-to ,.slide-fade-enter // 隐藏效果动画
    transform translateY(70px)
  @media screen and (max-width: $size-xl)
    .content-box
      left 25%
      width 75%
      .content-container
        width 90%
      .scroll-progress-bar // 小屏幕不显示滚动进度条
        display none 
</style>
```

## 9.6 Vue-Cli4中路由刷新报错

> 问题：vue-cli3中路由刷新报错：`Uncaught SyntaxError: Unexpected token <`
>
> 解决办法：publicPath 不要写成相对路径'./' 要写成绝对路径 '/'

## 9.7 vue项目在IE11中报错(暂未解决)

> 问题：vue项目在IE11中报错`SCRIPT1002: 语法错误`、`chunk-vendors.js (1391,3)`
>
> 解决办法：babel-loder不能把高版本webpack-dev-server的ES6转换成ES5，需要将webpack-dev-server版本降低到2.6.1。
>
> 操作步骤（**本机上未试验成功？**）：
>
> - npm uninstall webpack-dev-server
> - npm install webpack-dev-server@2.6.1 --save-dev

## 9.8 混入mixins

`mixins` 选项接收一个混入对象的数组。这些混入对象可以像正常的实例对象一样包含实例选项，这些选项将会被合并到最终的选项中。本项目通过在混入中定义过滤器，可以对数据做一些过滤操作（使用函数可以实现同样的功能，但搭配管道符的过滤操作语义化更佳）。下面举个例子：

- 在`utils.js`中定义混入的对象：

```javascript
/**
 * 映射tag颜色
 */
export function mapTagColor (id) {
  switch (id % 7) {
    case 0:
      return '#FF5722'
    case 1:
      return '#4CAF50'
    case 2:
      return '#2196F3'
    case 3:
      return '#9C27B0'
    case 4:
      return '#00BCD4'
    case 5:
      return '#FFC107'
    case 6:
      return '#795548'
  }
}
// 在混入对象中定义过滤器
export const mixin = {
  filters: {
    // 用于映射标签颜色
    mapTagColor: function (id) {
      return mapTagColor(id)
    }
  }
}
```

- 在`ArticleAbstractList.vue`中引入混入对象，对数据进行过滤操作

```vue
<template>
  <content-box>
	<div class="article-abstract-tag" v-if="article.tagList">
      <span class="iconfont">&#xe611;</span> // 使用过滤器对数据进行操作
      <iv-tag class="article-abstract-tag-item" :color="tag.id | mapTagColor" v-for="tag in article.tagList" :key="tag.id">
        <span class="article-abstract-tag-click" @click="handleToTag(tag.id)">{{tag.name}}</span>
      </iv-tag>
    </div>
  </content-box>
</template>

<script type="text/ecmascript-6">
import ContentBox from 'components/content/ContentBox'
import { mixin } from 'common/js/utils'

export default {
  name: 'ArticleAbstractList',
  components: {
    'content-box': ContentBox
  },
  data () {
    return {
      pagination: {
        total: 100,
        currentPage: 1,
        pageSize: 10
      },
      articleAbstractList:[]
    }
  },
  mixins: [mixin] // 引入混入对象
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
</style>
```

# 10 代码语法高亮

## 10.1 安装highlight.js

> 主要有三种安装方式：直接引入、CDN、npm安装。

- 第一种：直接引入本地文件，需要先下载[highlight](https://highlightjs.org/download/)到本地。

  ```html
  // 在public/index.html文件中引入
  <link rel="stylesheet" href="/path/to/styles/default.css">
  <script src="/path/to/highlight.min.js"></script>
  <script>hljs.initHighlightingOnLoad();</script>
  ```

- 第二种：采用CDN引入本地文件。具体又分为两种方式：cdnjs和jsdelivr。

  - cdnjs方式（以go语言高亮为例）：

    ```html
    <link rel="stylesheet"
          href="//cdnjs.cloudflare.com/ajax/libs/highlight.js/10.1.2/styles/default.min.css">
    <script src="//cdnjs.cloudflare.com/ajax/libs/highlight.js/10.1.2/highlight.min.js"></script>
    <!-- and it's easy to individually load additional languages -->
    <script charset="UTF-8"
     src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.1.2/languages/go.min.js"></script>
    ```

  - jsdelivr方式：

    ```html
    <link rel="stylesheet"
          href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.1.2/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.1.2/build/highlight.min.js"></script>
    ```

- 第三种：采用npm进行安装（本项目采用这种方式）

  安装方法：`npm install highlight.js`

## 10.2 highlight.js简介

> 使用highlight.js对代码块进行语法高亮时，需要将代码块包裹在`<pre><code>`中，highlight.js会对`<pre><code>`中的代码进行语法高亮。

- 对html进行语法高亮（方法一）：

  ```html
  <pre><code class="html">...</code></pre>
  ```

- 对html进行语法高亮（方法二）

  ```html
  <pre><code class="language-html">...</code></pre>
  ```

- 将代码当作纯文本，不进行语法高亮

  ```html
  <pre><code class="plaintext">...</code></pre>
  ```

- 移除语法高亮效果

  ```html
  <pre><code class="nohighlight">...</code></pre>
  ```

## 10.3 引入highlight.js

> 使用npm安装的`highlight.js`有两种引入方式：全量引入和按需引入。全量引入支持对所有编程语法进行语法高亮，按需引入只支持对指定的编程语言进行语法高亮。按需引入的性能更好。

### 10.3.1 全量引入

在`main.js`中进行全量引入：

```javascript
import hljs from 'highlight.js' // 全量引入highlight.js,会包含所有支持的编程语言
import 'highlight.js/styles/monokai-sublime.css' // 引入样式文件

// 可以在Vue组件中通过<highlightjs language='javascript' code="var x = 5;" />对代码进行语法高亮
Vue.use(hljs.vuePlugin) // 非必须，也可以使用Vue.directive自定义配置highlightjs指令
Vue.prototype.$hljs = hljs // 注册全局变量
```

### 10.3.2 按需引入

在`main.js`中进行按需引入（本项目采用）：

```javascript
import hljs from 'highlight.js/lib/core' // 引入highlight.js核心包
import javascript from 'highlight.js/lib/languages/javascript' // 支持javascript语法高亮
import java from 'highlight.js/lib/languages/java' // 支持java语法高亮
import css from 'highlight.js/lib/languages/css' // 支持css语法高亮
import xml from 'highlight.js/lib/languages/xml' // 支持xml语法高亮
import 'highlight.js/styles/monokai-sublime.css' // 引入样式文件

hljs.registerLanguage('javascript', javascript) // 注册java语言
hljs.registerLanguage('java', java) // 注册javascript语言
hljs.registerLanguage('css', css) // 注册css语言
hljs.registerLanguage('xml', xml) // 注册xml语言

// 可以在Vue组件中通过<highlightjs language='javascript' code="var x = 5;" />对代码进行语法高亮
Vue.use(hljs.vuePlugin) // 非必须，也可以使用Vue.directive自定义配置highlightjs指令
Vue.prototype.$hljs = hljs // 注册全局变量
```

## 10.4 生成html代码

> 本项目采用`mavon-editor`编辑器生成mark-down文件，相关操作在博客后台界面系统（zcblog-front2manage）中进行操作。highlight.js不能识别原生mark-down文件，需要对原生mark-down文件转换成highlight.js可以识别的html文件（**使用marked.js进行转换**）。

关键代码如下：

```javascript
// article-add-or-update.vue中
mavonChangeHandle (context, render) {
   this.article.contentFormat = marked(context, {breaks: true}) // 该方法会自动为h1~h6生成id属性
}

// marked.js中的关键部分
function marked(src, opt, callback){
    // 该方法是marked插件(marked.js)中的方法
    // 安装marked插件：npm install marked
}
```

## 10.5 高亮显示代码/行号

在`ArticleContent.vue`中高亮显示代码：

```javascript
methods: {
  highlightCode () {
    const srcToc = document.querySelector('#src-toc')
    const blocks = srcToc.querySelectorAll('pre code')
    blocks.forEach((block) => {
      this.$hljs.highlightBlock(block)
      // 去前后空格并添加行号
      block.innerHTML = '<ul><li>' + block.innerHTML.replace(/(^\s*)|(\s*$)/g, '').replace(/\n/g, '\n</li><li>') + '\n</li></ul>'
    })
  }
}
```

在`article.styl`中编辑样式（给代码添加行号）：

```stylus
  // 给代码框添加间隙
  >>>code
    margin 0 10px
  // 给代码框添加样式
  >>>.hljs ul
    list-style decimal
    margin 0 0 0 40px!important
    padding 0
  >>>.hljs li
    list-style decimal-leading-zero
    border-left 1px solid $color-border !important
    padding 2px 5px !important
    margin 0 !important
    line-height 14px
    width 100%
    box-sizing border-box
  >>>.hljs li:nth-of-type(even)
    background-color rgba(255,255,255,.015)
    color inherit
```

**展示效果（具体内容请无视，只注意显示效果即可）：**

![image-20201016005453280](zcblog-front2client-docs.assets/image-20201016005453280.png)

# 11 目录锚点制作