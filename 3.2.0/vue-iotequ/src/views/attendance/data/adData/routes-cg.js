import Layout from '@/layout'
export default {
  path: '/attendance/data/adData',
  name: 'adData',
  component: Layout,
  meta: {
    authorities: ['/attendance/data/adData'],
    title: 'adData.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/attendance/data/adData/list'),
      name: 'AdDataList',
      props: true,
      meta: {
        title: 'adData.title.list',
        authorities: ['/attendance/data/adData/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
