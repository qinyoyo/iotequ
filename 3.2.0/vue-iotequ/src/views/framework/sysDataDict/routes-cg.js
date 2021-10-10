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
      props: true,
      meta: {
        title: 'sysDataDict.title.list',
        authorities: ['/framework/sysDataDict/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/framework/sysDataDict/record'),
      name: 'DataDictForm',
      props: true,
      meta: {
        title: 'sysDataDict.title.record',
        authorities: ['/framework/sysDataDict/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
