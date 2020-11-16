/**
 * 校验URL地址
 * @param s
 * @returns {boolean}
 */
export function isURL (s) {
  return /^http[s]?:\/\/.*/.test(s)
}
