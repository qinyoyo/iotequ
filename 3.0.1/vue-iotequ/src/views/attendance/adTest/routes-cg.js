import Layout from '@/layout'
export default {
  path: '/attendance/adTest',
  name: 'adTest',
  component: Layout,
  meta: {
    authorities: ['/attendance/adTest'],
    title: 'adTest.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/attendance/adTest/list'),
      name: 'TestList',
      meta: {
        title: 'adTest.route.listTag',
        authorities: ['/attendance/adTest/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/adTest/record'),
      name: 'TestForm',
      meta: {
        title: 'adTest.route.recordTag',
        dialog: true,
        authorities: ['/attendance/adTest/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
