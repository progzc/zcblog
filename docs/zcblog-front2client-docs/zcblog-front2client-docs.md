# 0 技术方案

主体技术方案：**VueCli4**（webpack）+ **Vue**  + **Vuex** + **Vue Router** + **ECMAScript**（JavaScript）+ **npm** (Node.js) + **CSS/Stylus**

第三方插件：[mavon-editor](https://github.com/topics/mavon-editor)（实现文档的编辑）、[tocbot](https://github.com/tscanlin/tocbot)（制作博客文档目录）、[highlight.js](https://github.com/highlightjs/highlight.js)（实现代码高亮显示）、**Valine/Github**（评论系统）

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

## 6.1 全局引入

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