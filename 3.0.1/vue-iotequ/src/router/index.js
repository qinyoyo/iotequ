import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import { filterAsyncRoutesByApis } from '@/utils/filterRoutes'
/**
 * Note: sub-menu only appear when route children.length >= 1
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set and same as the component's name!!!)
 * meta : {
    authorities: ['/codegenerator/cgTable/record',...]    the server authorities need
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: false               if set true, the page will no be cached
    tagView: true                if set true, the page will no be add into tags-view
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/m/*',
    component: Layout
  },
  {
    path: '/icon',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/common-views/icons/index'),
        name: 'Icons',
        meta: { title: 'icons', icon: 'icon', tagView: true, noCache: false }
      }
    ]
  },
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/common-views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/common-views/login/index'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/common-views/extend-views/CgUserRegister'),
    hidden: true
  },
  {
    path: '/profile',
    component: () => import('@/views/common-views/extend-views/CgUserProfile'),
    hidden: true,
    meta: {
      dialog: true
    }
  },
  {
    path: '/changePassword',
    component: () => import('@/views/common-views/extend-views/CgUserChgPass'),
    hidden: true,
    meta: {
      dialog: true
    }
  },
  {
    path: '/error',
    component: Layout,
    redirect: 'noRedirect',
    name: 'ErrorPages',
    hidden: true,
    meta: {
      title: 'errorPages',
      breadcrumb: false,
      icon: '404'
    },
    children: [
      {
        path: '404',
        component: () => import('@/views/common-views/error-page/404'),
        name: 'Page404',
        meta: { title: 'page404', noCache: true, breadcrumb: false }
      }
    ]
  },
  {
    path: '/about',
    component: () => import('@/views/common-views/about/index'),
    hidden: true,
    name: 'About',
    meta: { title: 'About', affix: true }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    hidden: true,
    name: 'ROOT',
    children: [
      {
        path: 'home',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'system.layout.dashboard', icon: 'dashboard', affix: true }
      }
    ]
  }
]

export const route404 = { path: '*', redirect: '/error/404', hidden: true }

export const dynaRoutes = []
const context = require.context('@', true, /\/routes\-[a-z0-9]*\.js$/)
context.keys().forEach(key=>{
  dynaRoutes.push(context(key).default)
})
/*
const moduleRoutes = context.keys().map(context) // auto import all routes.js
moduleRoutes.forEach((r) => {
  const v = r.__esModule && r.default ? r.default : r
  if (v && v.path && v.name) dynaRoutes.push(v)
})
*/
const apis = window.sessionStorage.getItem('user_apis')
const accessRoutes = apis ? filterAsyncRoutesByApis(dynaRoutes, apis) : []
const usedRoutes = constantRoutes.concat(accessRoutes)
usedRoutes.push(route404)

const router = new Router({
  mode: window.userSettings && window.userSettings.insideSpring ? 'hash' : 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: usedRoutes
})
const createRouter = () => new Router({
  mode: window.userSettings && window.userSettings.insideSpring ? 'hash' : 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter(routes) {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
  if (routes && routes.length > 0) router.addRoutes(routes)
  router.addRoutes([route404])
}

export function addRoutes(routes) {
  router.addRoutes(routes)
}

export default router

