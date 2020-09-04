import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

// https://webpack.js.org/guides/dependency-management/#requirecontext
const modulesFiles = require.context('./modules', true, /\.js$/)

// you do not need `import app from './modules/app'`
// it will auto require all vuex module from modules file
const modules = modulesFiles.keys().reduce((modules, modulePath) => {
  // set './app.js' => 'app'
  const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, '$1')
  const value = modulesFiles(modulePath)
  modules[moduleName] = value.default
  return modules
}, {})

const store = new Vuex.Store({
  modules,
  getters,
  plugins: [
    createPersistedState({ // 该插件使得F5刷新页面后不丢失vuex state数据
      storage: window.sessionStorage,
      reducer(val) {
        return {
        // 只储存state中的app,settings,user,tagsView存储会导致循环引用错误
          app: val.app,
          settings: val.settings,
          user: val.user
        }
      }
    })
  ]
})

export default store
