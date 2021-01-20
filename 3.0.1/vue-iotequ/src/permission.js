// route 控制流程，由 main.js 加载
import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { routeMatchList } from '@/utils/filterRoutes'
import { request } from './utils/request'
import { getPageTitle } from './utils/i18n'

function gotoShortUrl(url,next) {
  request({
    url: url,
    method: 'get'
  }, true).then(res=>{
    if (res.success && res.url) next({ path: res.url })
    else next({ path: '/' })
  })
  .catch(err=>{
    next({ path: '/' })
  })
}
NProgress.configure({ showSpinner: false }) // NProgress Configuration
const whiteList = ['/', '/index.html','/register','/profile','/dashboard', '/error/*', '/login/*', '/redirect/*'] // route whitelist
router.beforeEach(async(to, from, next) => {
  NProgress.start()
  document.title = getPageTitle(to.meta.title)
  if (to.path.indexOf('\/m\/')==0) {
    gotoShortUrl(to.path,next)
    NProgress.done()
  }
  else if (routeMatchList(to.path, whiteList)) {
    next()
    NProgress.done()
  } else if (store.getters.authentication) { // logged in
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      next()
      NProgress.done()
    }
  } else { // not login, need login
    if (to.path === '/login') {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
    NProgress.done()
  }
})

router.afterEach(() => {
  NProgress.done()
})
