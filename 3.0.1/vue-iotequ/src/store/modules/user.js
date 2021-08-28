// 记录登录用户的基本信息

import { resetRouter, dynaRoutes, constantRoutes } from '@/router'
import { routesWithFullPath, filterAsyncRoutesByApis } from '@/utils/filterRoutes'
import { apiUrl } from '@/utils/requestService'
import { request } from '@/utils/request'
import { getLanguage } from '@/lang'

export function login(data) {
  return request({
    url: '/login/login',
    method: 'post',
    params: data
  }, true)
}

export function getInfo() {
  return request({
    url: '/login/info',
    method: 'get',
    params: { locale : getLanguage() }
  }, true)
}

export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  }, true)
}


const state = {
  name: '',
  id: '',
  newMessages: 0,
  org: '',
  orgName: '',
  avatar: '',
  introduction: '',
  authentication: false,
  routes: [],
  menus: [],
  apis: [],
  roles: [],
  versions: []
}

const mutations = {
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_ID: (state, id) => {
    state.id = id
  },
  SET_ORG: (state, org) => {
    state.org = org
  },
  SET_ORGNAME: (state, orgName) => {
    state.orgName = orgName
  },
  SET_NEWMESSAGES: (state, newMessages) => {
    state.newMessages = newMessages
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_AUTHENTICATION: (state, auth) => {
    state.authentication = auth
  },
  SET_ROUTES: (state, routes) => {
    state.routes = routes
  },
  SET_MENUS: (state, menus) => {
    state.menus = menus
  },
  SET_APIS: (state, apis) => {
    state.apis = apis
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_VERSIONS: (state, versions) => {
    state.versions = versions
  }
}

function filterAsyncMenusByRoute(routes, menus, apis) {
  const res = []
  menus.forEach(menu => {
    const tmp = { ...menu }
    if (tmp.children && tmp.children.length > 0) {
      const subMenus = filterAsyncMenusByRoute(routes, tmp.children, apis)
      if (!subMenus || subMenus.length <= 0) tmp.children = null
      else tmp.children = subMenus
    } else {
      tmp.children = null
      let permission = false
      if (tmp.action) {
        permission = routes.some(r => tmp.action === r.path)
        if (!permission && tmp.jsCmd && apis) {
          permission = apis.some(a => a===tmp.action)
        }
      }
      if (!permission) tmp.action = null
    }
    if ((tmp.children && tmp.children.length > 0) || tmp.action) res.push(tmp)
    // else console.log(menu)
  })
  return res
}
const actions = {
  // user login
  login({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      login(userInfo).then(response => {
        const data = response
        commit('SET_AUTHENTICATION', data.success)
        if (data.success) resolve(data)
        else reject(data)
      }).catch(error => {
        commit('SET_AUTHENTICATION', false)
        reject(error)
      })
    })
  },

  setNewMessages({ commit }, newMessages) {
    commit('SET_NEWMESSAGES', newMessages)
  },
  // get user info , rememeber-me 直接调用，登录
  getInfo({ commit, state }, callByRememberMe) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response
        if (!data || !response.success) {
          commit('SET_AUTHENTICATION', false)
          if (callByRememberMe) resolve(response)
          else reject('Verification failed, please Login again.')
        } else commit('SET_AUTHENTICATION', true)
        let accessRoutes = []
        if (data.apis) {
          commit('SET_APIS', data.apis)
          accessRoutes = filterAsyncRoutesByApis(dynaRoutes, data.apis)
          commit('SET_ROUTES', accessRoutes)
          window.sessionStorage.setItem('user_apis', data.apis) // router 刷新在store实例化之前，通过该参数传递数据
          resetRouter(accessRoutes)
        } else {
          commit('SET_APIS', [])
          commit('SET_ROUTES', [])
          window.sessionStorage.removeItem('user_routes')
          //resetRouter()
        }
        if (data.menus) {
          const routes = routesWithFullPath(accessRoutes.concat(constantRoutes))
          const accessMenus = filterAsyncMenusByRoute(routes, data.menus, data.apis)
          commit('SET_MENUS', accessMenus)
        }
        else commit('SET_MENUS', [])
        if (data.messages) commit('SET_NEWMESSAGES', data.messages)
        else commit('SET_NEWMESSAGES', 0)
        if (data.roles) commit('SET_ROLES', data.roles)
        else commit('SET_ROLES', [])
        commit('SET_NAME', data.user.name)
        commit('SET_ID', data.user.id)
        commit('SET_ORG', data.user.orgCode)
        commit('SET_ORGNAME', data.orgName)
        commit('SET_AVATAR', apiUrl('/res/getUploadImage?id=' + data.user.id + '&path=sysUser&field=icon&name='+data.user.icon))
        commit('SET_INTRODUCTION', data.user.realName)

        if (data.versionInfo) {
          commit('SET_VERSIONS', data.versionInfo)
        }
        else commit('SET_VERSIONS', [])
        const context = require.context('@/extend-src', false, /\/login(\-[a-z0-9]+)*\.js$/)
        context.keys().forEach(key=>{
          const obj = context(key).default
          if (obj) {
            Object.keys(obj).forEach(f=>{
              if (typeof obj[f] == 'function') obj[f]()
            })
          }
        })
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 用户中心改变用户信息
  changeInfo({ commit, state }, user) {
    return new Promise((resolve, reject) => {
      commit('SET_NAME', user.name)
      commit('SET_ID', user.id)
      commit('SET_ORG', user.orgCode)
      commit('SET_ORGNAME', user.orgName)
      commit('SET_AVATAR', apiUrl('/res/getUploadImage?id=' + user.id + '&path=sysUser&field=icon&name='+user.icon))
      commit('SET_INTRODUCTION', user.realName)
      resolve(data)
    })
  },
  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout().then(() => {
        commit('SET_AUTHENTICATION', false)
        commit('SET_MENUS', [])
        commit('SET_APIS', [])
        commit('SET_ROUTES', [])
        commit('SET_NAME', '')
        commit('SET_ORGNAME', '')
        commit('SET_ID', '')
        commit('SET_ORG', '')
        commit('SET_NEWMESSAGES', 0)
        commit('SET_AVATAR', '')
        commit('SET_INTRODUCTION', '')
        commit('SET_ROLES', [])
        window.sessionStorage.removeItem('user_routes')
        resetRouter()

        // reset visited views and cached views
        dispatch('tagsView/delAllViews', null, { root: true })
        const context = require.context('@/extend-src', false, /\/logout(\-[a-z0-9]+)*\.js$/)
        context.keys().forEach(key=>{
          const obj = context(key).default
          if (obj) {
            Object.keys(obj).forEach(f=>{
              if (typeof obj[f] == 'function') obj[f]()
            })
          }
        })
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  }
}
export default {
  namespaced: true,
  state,
  mutations,
  actions
}
