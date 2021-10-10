
import i18n from '@/lang'
const title = window.userSettings.title
export function getPageTitle(key) {
  const hasKey = i18n.te(`${key}`)
  if (hasKey) {
    const pageName = i18n.t(`${key}`)
    return `${pageName} - ${title}`
  }
  return `${title}`
}
export function generateTitle(title) {
  if (!title) return ''
  const vue = window.$vue
  const hasKey = vue.$te(title)
  if (hasKey) {
    const translatedTitle = vue.$t(title)
    return translatedTitle
  }
  let tt=title.split('.')
  return tt[tt.length-1].replace('_',' ')
}
