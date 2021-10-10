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

// 扫描加载模块下的语言定义文件，自定义的覆盖cg生成的 2021/6/7
let zhCgModuleLocale = {}
let zhModuleLocale = {}
const zhContext = require.context('@/views', true, /\/zh\-[a-z0-9]*\.js$/)
zhContext.keys().forEach(key => {
  let pos = key.indexOf('zh-cg.js')
  if (pos>=0) 
    zhCgModuleLocale = Object.assign(zhCgModuleLocale, zhContext(key).default)
  else
    zhModuleLocale = Object.assign(zhModuleLocale, zhContext(key).default)
})

let enCgModuleLocale = {}
let enModuleLocale = {}
const enContext = require.context('@/views', true, /\/en\-[a-z0-9]*\.js$/)
enContext.keys().forEach(key => {
  let pos = key.indexOf('en-cg.js')
  if (pos>=0) 
    enCgModuleLocale = Object.assign(enCgModuleLocale, enContext(key).default)
  else
    enModuleLocale = Object.assign(enModuleLocale, enContext(key).default)
})

const messages = {
  zh: {
    ...zhLocale,
    ...zhError,
    ...elementZhLocale,
    ...zhCgModuleLocale,
    ...zhModuleLocale
  },
  en: {
    ...enLocale,
    ...enError,
    ...elementEnLocale,
    ...enCgModuleLocale,
    ...enModuleLocale
  }
}

export function localeText(text) {
  if (window.userSettings && window.userSettings.disableMLText) return text
  if (typeof text == 'string') {
    let ss = text.split('\|')
    if (ss.length<=1) return text
    else if (i18n.locale=='en') return ss[1]
    else return ss[0]
  } else return ''
}
export function setLocaleText(text, loc) {
  if (window.userSettings && window.userSettings.disableMLText) return loc
  if (typeof text == 'string') {
    let ss = text.split('\|')
    if (ss.length<=1) {
      if (i18n.locale=='en') return text+'|'+loc
      else return loc
    } else {
      if (i18n.locale=='en') return ss[0] + '|' +loc
      else return loc+'|' + ss[1]
    }
  } else return loc
}
export function getLanguage() {
  const cookLng = Cookies.get('iotequ_language')
  if (cookLng) return cookLng
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
    Cookies.set('iotequ_language',language)
    return language
  } else {
    Cookies.set('iotequ_language',i18n.locale)
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