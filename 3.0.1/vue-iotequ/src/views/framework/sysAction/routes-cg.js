import Layout from '@/layout'
export default {
  path: '/framework/sysAction',
  name: 'sysAction',
  component: Layout,
  meta: {
    authorities: ['/framework/sysAction'],
    title: 'sysAction.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/framework/sysAction/list'),
      name: 'ActionList',
      meta: {
        title: 'sysAction.route.listTag',
        authorities: ['/framework/sysAction/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/framework/sysAction/record'),
      name: 'ActionForm',
      meta: {
        title: 'sysAction.route.recordTag',
        dialog: true,
        authorities: ['/framework/sysAction/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
