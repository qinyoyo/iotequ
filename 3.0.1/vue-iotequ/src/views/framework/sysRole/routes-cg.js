import Layout from '@/layout'
export default {
  path: '/framework/sysRole',
  name: 'sysRole',
  component: Layout,
  meta: {
    authorities: ['/framework/sysRole'],
    title: 'sysRole.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/framework/sysRole/list'),
      name: 'RoleList',
      meta: {
        title: 'sysRole.route.listTag',
        authorities: ['/framework/sysRole/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/framework/sysRole/record'),
      name: 'RoleForm',
      meta: {
        title: 'sysRole.route.recordTag',
        dialog: true,
        authorities: ['/framework/sysRole/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
