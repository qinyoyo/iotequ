import Layout from '@/layout'
export default {
  path: '/framework/sysUser',
  name: 'sysUser',
  component: Layout,
  meta: {
    authorities: ['/framework/sysUser'],
    title: 'sysUser.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/framework/sysUser/list'),
      name: 'UserList',
      meta: {
        title: 'sysUser.route.listTag',
        authorities: ['/framework/sysUser/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'join',
      component: () => import('@/views/framework/sysUser/join'),
      name: 'UserJoinList',
      meta: {
        title: 'sysUser.route.joinTag',
        authorities: ['/framework/sysUser/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/framework/sysUser/record'),
      name: 'UserForm',
      meta: {
        title: 'sysUser.route.recordTag',
        dialog: true,
        authorities: ['/framework/sysUser/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'register',
      component: () => import('@/views/framework/sysUser/register'),
      name: 'RegisterForm',
      meta: {
        title: 'sysUser.route.registerTag',
        icon: 'fa fa-user-plus',
        dialog: true,
        authorities: ['/framework/sysUser/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
