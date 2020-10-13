/**
 * 字符串转换成日期
 */
export function str2Date (str) {
  str = str.replace(/-/g, '/')
  return new Date(str)
}

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

/**
 * 为DOM文档的h1~h6标题生成id属性
 * @param srcToc: 需要动态生成id属性的DOM文档
 */
export function makeIds (srcToc) {
  var headings = srcToc.querySelectorAll('h1, h2, h3, h4, h5, h6')
  var headingMap = {}
  Array.prototype.forEach.call(headings, function (heading) {
    var id = heading.id ? heading.id : heading.textContent.trim().toLowerCase()
      .split(' ').join('-').replace(/[!@#$%^&*():]/ig, '').replace(/\//ig, '-')
    headingMap[id] = !isNaN(headingMap[id]) ? ++headingMap[id] : 0
    if (headingMap[id]) {
      heading.id = id + '-' + headingMap[id]
    } else {
      heading.id = id
    }
  })
}

/**
 * 映射过滤器
 */
export const mixin = {
  filters: {
    // 用于映射标签颜色
    mapTagColor: function (id) {
      return mapTagColor(id)
    }
  }
}
