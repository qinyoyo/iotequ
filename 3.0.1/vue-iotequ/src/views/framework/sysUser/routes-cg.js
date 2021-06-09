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
      props: true,
      meta: {
        title: 'sysUser.title.list',
        authorities: ['/framework/sysUser/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'join',
      component: () => import('@/views/framework/sysUser/join'),
      name: 'UserJoinList',
      props: true,
      meta: {
        title: 'sysUser.title.join',
        authorities: ['/framework/sysUser/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/framework/sysUser/record'),
      name: 'UserForm',
      props: true,
      meta: {
        title: 'sysUser.title.record',
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
      props: true,
      meta: {
        title: 'sysUser.title.register',
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
