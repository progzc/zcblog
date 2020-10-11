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
  switch (id % 4) {
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

export const mixin = {
  filters: {
    // 用于映射标签颜色
    mapTagColor: function (id) {
      return mapTagColor(id)
    }
  }
}
