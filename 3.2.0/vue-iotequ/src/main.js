
import "core-js/stable" // 兼容ie
import "regenerator-runtime/runtime" // 兼容ie

import Vue from 'vue'

import './init'

import 'core-js'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import splitPane from 'vue-splitpane'

import './styles/element-variables.scss'

import '@/styles/index.scss' // global css
import 'font-awesome/css/font-awesome.css' // 兼容 fa 图标
import App from './App'
import i18n from './lang' // internationalization,在store之前初始化
import store from './store' // 在 router之前初始化

import router from './router'

import './icons' // icon
import './permission' // permission control
import './utils/error-log' // error log

import * as filters from './filters' // global filters

import { methodsStrategy } from './mixin-merge-strategies'
import { addBodyNode } from '@/utils/dom'

import { isMobile } from '@/utils/cg'

import packageConfig from '../package.json'
window.packageName = packageConfig.name
window.packageVersion = packageConfig.version

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */
/*
import { mockXHR } from '../mock'
if (process.env.NODE_ENV === 'production') {
  mockXHR()
}
*/

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

Vue.component('split-pane', splitPane)  // 分栏插件

import nutui from '@/components/nutui'
Vue.use(nutui)

import iotequ from '@/components/Iotequ'
Vue.use(iotequ)

Vue.config.optionMergeStrategies.methods = methodsStrategy // mixin 方法优先

// cgform调整指令
import cgFormAdjust from '@/directive/iotequ/form-adjust'
Vue.directive('cg-form-adjust', cgFormAdjust)

// 调整input属性指令，如readonly,type等
import setInput from '@/directive/iotequ/set-input'
Vue.directive('set-input', setInput)

// 表格回车切换焦点
import tableEnterTab from '@/directive/iotequ/table-enter-tab'
Vue.directive('table-enter-tab', tableEnterTab)

// 多语言显示input内容
import i18nView from '@/directive/iotequ/i18n-view'
Vue.directive('i18n-view', i18nView)

// register global utility filters
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

window.$vue = new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})

String.prototype.trim = function(){  
  return this.replace(/(^\s+)|(\s+$)/g,"")
} 
String.prototype.local = function() {
  if (!this) return ''
  const tt = this.split(' ')
  const rr = []
  tt.forEach(t=>{
    t=t.trim()
    if (t) {
      if (window.$vue.$te(t)) {
        const r = window.$vue.$t(t)
        if (typeof r === 'string') rr.push(r)
        else rr.push(t)
      }
      else rr.push(t)
    }
  })
  return rr.join(' ')
}

store.dispatch('app/toggleDevice', isMobile() ? 'mobile' : 'desktop')

var eventBus = new Vue()

Object.defineProperties(Vue.prototype, {
  $eventBus: {
    get: function() {
      return eventBus
    }
  }
})

Vue.prototype.$dialog = function(component, options) {
  const domId = 'dialog_' + new Date().getTime()
  addBodyNode(domId)
  const open = function(com) {
    const Constructor = Vue.extend(com)
    new Constructor({
      el: '#' + domId,
      router,
      store,
      i18n,
      propsData: {
        ...options,
        dialogClass: domId
      },
      created() {
        const _this = this
        this.$on('close',function() {
          document.querySelectorAll('.'+domId).forEach(e=>e.remove())
          if (_this && _this.$el && _this.$el.parentNode) { 
            const $d = _this.$el.parentNode.querySelector('#' + domId + ' + .el-dialog__wrapper')
            if ($d) $d.remove()
          }
          _this && _this.$el && _this.$el.remove()
        })
      },
      mounted() {
        this.$el.id = domId
      }
    })
  }
  if (typeof component == 'object' && typeof component.default == 'function') {
    component.default().then(com=>{
      open(com.default)
    })
  } else {
    open(component)
  }
}

const initialized_context = require.context('@/extend-src', false, /\/initialized(\-[a-z0-9]+)*\.js$/)
initialized_context.keys().forEach(key=>{
  const obj = initialized_context(key).default
  if (obj) {
    Object.keys(obj).forEach(f=>{
      if (typeof obj[f] == 'function') obj[f]()
    })
  }
})

try { // cookie remember-me 不可读，自动检查一次
    store.dispatch('user/getInfo', true) // 解决刷新页面后丢失route的问题,remember-me 自动登录
} catch (error) {
    console.log('检查remember-me失败：' + error)
}
