import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Cookies from 'js-cookie'
import elementEnLocale from 'element-ui/lib/locale/lang/en' // element-ui lang
import elementZhLocale from 'element-ui/lib/locale/lang/zh-CN'// element-ui lang
import enLocale from './en'
import zhLocale from './zh'
import enError from './en-error'
import zhError from './zh-error'

Vue.use(VueI18n)

// 扫描加载模块下的语言定义文件
let zhModuleLocale = {}
const zhContext = require.context('@/views', true, /\/zh\-[a-z0-9]*\.js$/)
zhContext.keys().forEach(key => {
  zhModuleLocale = Object.assign(zhModuleLocale, zhContext(key).default)
})

let enModuleLocale = {}
const enContext = require.context('@/views', true, /\/en\-[a-z0-9]*\.js$/)
enContext.keys().forEach(key => {
  enModuleLocale = Object.assign(enModuleLocale, enContext(key).default)
})

const messages = {
  zh: {
    ...zhLocale,
    ...zhError,
    ...elementZhLocale,
    ...zhModuleLocale
  },
  en: {
    ...enLocale,
    ...enError,
    ...elementEnLocale,
    ...enModuleLocale
  }
}

export function getLanguage() {
  const language = (navigator.language || navigator.browserLanguage).toLowerCase()
  const locales = Object.keys(messages)
  if (!language) return locales[0]
  if (locales.indexOf(language) > -1) return language
  else return locales[0]
}

export function setLanguage(language) {
  Cookies.remove('iotequ_language')
  const locales = Object.keys(messages)
  if (locales.indexOf(language) > -1) {
    i18n.locale = language
    return language
  } else {
    return i18n.locale    
  }
}
export const i18n = new VueI18n({
  // set locale
  // options: en | zh | es
  locale: getLanguage(),
  sync: false,
  silentTranslationWarn: true,
  silentFallbackWarn: true,
  // set locale messages
  messages
})
export default i18n
