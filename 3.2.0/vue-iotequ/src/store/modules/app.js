import Cookies from 'js-cookie'
import { request } from '@/utils/request'
import { getLanguage, setLanguage } from '@/lang'
import { addClass, removeClass } from '@/utils'

const state = {
  sidebar: {
    opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
    withoutAnimation: false
  },
  device: 'desktop',
  language: getLanguage(),
  size: Cookies.get('size') || 'medium'
}

export function changeLanguage(locale) {
  return request({
    url: '/res/language',
    method: 'get',
    params: { locale }
  }, true)
}

const mutations = {
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0)
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
    if (device==='mobile') {
      addClass(document.body, 'mobile')
      window.$vue.$store.dispatch('settings/changeSetting', {
        key: 'tagsView',
        value: false
      })
      window.$vue.$store.dispatch('settings/changeSetting', {
        key: 'fixedHeader',
        value: false
      })
    } else {
      removeClass(document.body, 'mobile')
      window.$vue.$store.dispatch('settings/changeSetting', {
        key: 'tagsView',
        value: true
      })      
    }
  },
  SET_LANGUAGE: (state, language) => {
    state.language = setLanguage(language)
  },
  SET_SIZE: (state, size) => {
    state.size = size
    Cookies.set('size', size)
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({ commit }, device) {
    commit('TOGGLE_DEVICE', device)
  },
  setLanguage({ commit }, locale) {
    commit('SET_LANGUAGE', locale)
    changeLanguage(locale)
  },
  setSize({ commit }, size) {
    commit('SET_SIZE', size)
  }
}
export default {
  namespaced: true,
  state,
  mutations,
  actions
}
