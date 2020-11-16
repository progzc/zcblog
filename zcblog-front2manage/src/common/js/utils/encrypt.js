import CryptoJS from 'crypto-js'

const key = CryptoJS.enc.Hex.parse('6466326531633534343362323263623965323866373862323937613061666630')
const iv = CryptoJS.enc.Hex.parse('30313233343536373839616263646566')

/**
 * AES加密
 * @returns {string}
 * @param plaintext
 */
export function encryptAES (plaintext) {
  const ecrypted = CryptoJS.AES.encrypt(plaintext, key, {
    iv: iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7
  })
  return ecrypted.ciphertext.toString()
}

/**
 * AES解密
 * @returns {string}
 * @param ciphertext
 */
export function decryptAES (ciphertext) {
  ciphertext = CryptoJS.format.Hex.parse(ciphertext)
  const decrypted = CryptoJS.AES.decrypt(ciphertext, key, {
    iv: iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7
  })
  return CryptoJS.enc.Utf8.stringify(decrypted)
}
