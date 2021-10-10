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
      props: true,
      meta: {
        title: 'sysPermission.title.record',
        authorities: ['/framework/sysPermission/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
