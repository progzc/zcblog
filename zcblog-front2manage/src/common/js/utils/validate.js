/**
 * 校验URL地址
 * @param s
 * @returns {boolean}
 */
export function isURL (s) {
  return /^http[s]?:\/\/.*/.test(s)
}

/**
 * 校验是否包含空格和中文字符
 * 空格包含换页符/换行符/回车符/制表符/垂直制表符/DOS终止符
 * @param s
 */
export function psdChar (s) {
  return /^[^\s\u4e00-\u9fa5]+$/.test(s)
}

/**
 * 校验数字/字母以及特殊符号至少包含2种（在不包含空格和中文字符的基础上）
 * @param s
 */
export function psdKinds (s) {
  return /(?!^[0-9]+$)(?!^[A-Za-z]+$)(?!^[`~!@#$%^&*()\-_+={}\[\]|;:\\"'<>,.?/]+$)(?!^[^\x21-\x7e]+$)^.+$/.test(s)
}

/**
 * 校验密码的字符串长度在8~16位
 * @param s
 */
export function psdLen (s) {
  return /^.{8,16}$/.test(s)
}
