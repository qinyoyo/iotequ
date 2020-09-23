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
        title: 'adData.route.listTag',
        authorities: ['/attendance/data/adData/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
