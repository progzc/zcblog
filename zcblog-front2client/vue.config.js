const path = require('path')
const resolve = dir => path.join(__dirname, dir)
const IS_PROD = ['production', 'prod'].includes(process.env.NODE_ENV)
const addStylusResource = rule => {
  rule
    .use('style-resouce')
    .loader('style-resources-loader')
    .options({
      patterns: [resolve('src/common/stylus/theme.styl')]
    })
}

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
    host: 'localhost', // 匹配本机IP地址
    port: 8080, // 开发服务器运行端口号
    https: false, // 不开启https
    hotOnly: true, // 开启热更新
    // 若前端应用和后端API服务器没有运行在同一个主机上，则需要将API请求代理到API服务器
    proxy: {
      // 例如将'http://localhost:8080/api/xxx'代理到'https://localhost:8082/api/xxx'
      '/api': {
        target: 'http://localhost:8082', // 目标代理接口地址
        secure: false, // 忽略https安全提示(如果是https接口，需要配置这个参数)
        changeOrigin: true, // 本地会虚拟一个服务器接收请求并代发该请求
        ws: true, // 启用websockets
        pathRewrite: { // 重写地址，将前缀 '/api' 转为 '/',相当于此时代理到'http://localhost:8082/xxx'
          '^/api': '/'
        }
      }
    }
  },
  // 配置扩展名
  configureWebpack: {
    resolve: {
      extensions: ['.js', '.vue', '.json']
    }
  },

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
      .set('i18n', resolve('src/i18n'))
      .set('store', resolve('src/store'))
    // 为 stylus 提供全局变量
    const types = ['vue-modules', 'vue', 'normal-modules', 'normal']
    types.forEach(type =>
      addStylusResource(config.module.rule('stylus').oneOf(type))
    )
  }
}
