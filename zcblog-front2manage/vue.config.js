const path = require('path')
const resolve = dir => path.join(__dirname, dir)
const IS_PROD = ['production', 'prod'].includes(process.env.NODE_ENV)
const IS_DEV = ['dev'].includes(process.env.NODE_ENV)
const StylelintPlugin = require('stylelint-webpack-plugin')

module.exports = {
  publicPath: IS_PROD ? process.env.VUE_APP_PUBLIC_PATH : '/', // 默认'/'，部署应用包时的基本 URL
  outputDir: 'dist', // 默认值,生产环境构建文件的目录
  assetsDir: '', // 默认值,放置生成的静态资源(js、css、img、fonts)的(相对于outputDir的)目录
  lintOnSave: false, // 不会将lint错误输出为编译警告,即有不符合lint语法时，也会编译成功
  runtimeCompiler: false, // 使用runtime-only编译，打包小、效率更高
  productionSourceMap: !IS_PROD, // 生产环境不需要source map时，将其设置为false,可以加速构建
  parallel: require('os').cpus().length > 1, // 默认值,作用于生产构建,在系统的 CPU 有多于一个内核时自动启用
  devServer: {
    open: true, // npm run serve后自动打开页面
    host: '127.0.0.1', // 匹配本机IP地址
    port: 8083, // 开发服务器运行端口号
    https: false, // 不开启https
    hotOnly: true, // 开启热更新
    // 若前端应用和后端API服务器没有运行在同一个主机上，则需要将API请求代理到API服务器
    proxy: {
      // 例如将'http://localhost:8083/api/xxx'代理到'http://localhost:8082/api/xxx'
      '/api': {
        target: 'http://127.0.0.1:8082', // 目标代理接口地址
        secure: false, // 忽略https安全提示(如果是https接口，需要配置这个参数)
        changeOrigin: true, // 本地会虚拟一个服务器接收请求并代发该请求
        ws: true, // 启用websockets
        pathRewrite: { // 重写地址，将前缀 '/api' 转为 '/',相当于此时代理到'http://127.0.0.1:8082/xxx'
          '^/api': '/blog'
        }
      }
    }
  },

  configureWebpack: config => {
    // 配置扩展名
    config.resolve.extensions = ['.js', '.vue', '.json', '.css']
    // 配置开启自动修复检测SCSS、CSS
    const plugins = []
    if (IS_DEV) {
      plugins.push(
        new StylelintPlugin({
          files: ['src/**/*.vue', 'src/**/*.scss'],
          fix: true // 打开自动修复（谨慎使用！注意上面的配置不要加入js或html文件，会发生问题，js文件请手动修复）
        })
      )
    }
    config.plugins = [...config.plugins, ...plugins]
  },

  chainWebpack: config => {
    // 添加别名
    config.resolve.alias
      .set('vue$', 'vue/dist/vue.esm.js')
      .set('@', resolve('src'))
      .set('assets', resolve('src/assets'))
      .set('common', resolve('src/common'))
      .set('components', resolve('src/components'))
      .set('elementUI', resolve('src/elementUI'))
      .set('icons', resolve('src/icons'))
      .set('network', resolve('src/network'))
      .set('router', resolve('src/router'))
      .set('store', resolve('src/store'))
      .set('views', resolve('src/views'))

    // 配置svg组件
    // 1. 让其他的svg loader不要对src/icons/svg进行操作
    config.module.rule('svg').exclude.add(resolve('src/icons/svg')).end()
    // 2. 使用svg-sprite-loader对src/icons/svg下的.svg进行操作
    config.module.rule('icons').test(/\.svg$/)
      .include.add(resolve('src/icons/svg')).end()
      .use('svg-sprite-loader').loader('svg-sprite-loader')
      // 3.定义规则，使用时<svg class="icon"> <use xlink:href="#icon-svg文件名"></use></svg>
      .options({
        symbolId: '[name]'
      }).end()

    // 在每个组件中自动化导入index.scss
    const types = ['vue-modules', 'vue', 'normal-modules', 'normal']
    types.forEach(type => addStyleResource(config.module.rule('scss').oneOf(type)))
  },

  // 配置SCSS全局变量
  css: {
    extract: IS_PROD,
    sourceMap: false,
    loaderOptions: {
      scss: {
        additionalData: '@import "~@/common/scss/theme.scss";'
      }
    }
  }
}

function addStyleResource (rule) {
  rule.use('style-resource')
    .loader('style-resources-loader')
    .options({
      patterns: [
        path.resolve(__dirname, '~@/common/scss/index.scss')
      ]
    })
}
