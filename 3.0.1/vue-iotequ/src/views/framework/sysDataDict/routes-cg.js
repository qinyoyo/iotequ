import Layout from '@/layout'
export default {
  path: '/framework/sysDataDict',
  name: 'sysDataDict',
  component: Layout,
  meta: {
    authorities: ['/framework/sysDataDict'],
    title: 'sysDataDict.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/framework/sysDataDict/list'),
      name: 'DataDictList',
      meta: {
        title: 'sysDataDict.route.listTag',
        authorities: ['/framework/sysDataDict/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/framework/sysDataDict/record'),
      name: 'DataDictForm',
      meta: {
        title: 'sysDataDict.route.recordTag',
        authorities: ['/framework/sysDataDict/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
