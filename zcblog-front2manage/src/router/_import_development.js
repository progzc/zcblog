// 开发环境不采用懒加载，生产环境采用懒加载
module.exports = file => require('@/views/' + file + '.vue').default
