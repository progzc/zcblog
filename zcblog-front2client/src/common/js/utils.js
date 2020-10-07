// 字符串转换成日期
export function str2Date (str) {
  str = str.replace(/-/g, '/')
  return new Date(str)
}
