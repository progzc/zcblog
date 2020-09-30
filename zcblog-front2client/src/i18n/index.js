// 按需导入iview UI国际化语言
import en from 'view-design/dist/locale/en-US'
import zh from 'view-design/dist/locale/zh-CN'

// 导入自定义国际化内容
import enUS from 'i18n/lang/en-US'
import zhCN from 'i18n/lang/zh-CN'

const messages = {
  en: Object.assign(enUS, en),
  zh: Object.assign(zhCN, zh)
}

export default messages
