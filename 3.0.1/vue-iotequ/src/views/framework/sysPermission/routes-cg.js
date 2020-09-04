import Layout from '@/layout'
export default {
  path: '/framework/sysPermission',
  name: 'sysPermission',
  component: Layout,
  meta: {
    authorities: ['/framework/sysPermission'],
    title: 'sysPermission.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'record',
      component: () => import('@/views/framework/sysPermission/record'),
      name: 'PermissionForm',
      meta: {
        title: 'sysPermission.route.recordTag',
        authorities: ['/framework/sysPermission/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
