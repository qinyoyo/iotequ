import variables from '@/styles/element-variables.scss'

const state = {
  theme: variables.theme,
  title: window.userSettings.title,
  footer: window.userSettings.footer,
  server: window.userSettings.server,
  baseUrl: window.userSettings.baseUrl,
  showSettings: window.userSettings.showSettings,
  tagsView: window.userSettings.tagsView,
  fixedHeader: window.userSettings.fixedHeader,
  sidebarLogo: window.userSettings.sidebarLogo
}

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

