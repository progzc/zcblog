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

/**
 * 是否具有权限
 * @param key
 * @returns {boolean}
 */
export function isAuth (key) {
  return JSON.parse(sessionStorage.getItem('perms') || '[]').indexOf(key) !== -1 || false
}

/**
 * 校验手机号码是否合法
 * 可用号段主要有(不包括上网卡)：130~139、150~153，155~159，180~189、170~171、176~178
 * @param s
 */
export function phoneLegal (s) {
  // 校验手机号，号段主要有(不包括上网卡)：130~139、150~153，155~159，180~189、170~171、176~178
  return /^((13[0-9])|(17[0-1,6-8])|(15[^4,\\D])|(18[0-9]))\d{8}$/.test(s)
}

/**
 * 校验邮箱格式是否合法
 * 1.@之前必须有内容且只能是字母（大小写）、数字、下划线(_)、减号（-）、点（.）
 * 2.@和最后一个.之间必须有内容且只能是字母（大小写）、数字、点（.）、减号（-），且两个点不能紧挨着
 * 3.最后一个.之后必须有内容且内容只能是字母（大小写）、数字且长度为大于等于2个字符，小于等于6个字符
 * @param s
 */
export function emailLegal (s) {
  return /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(s)
}
