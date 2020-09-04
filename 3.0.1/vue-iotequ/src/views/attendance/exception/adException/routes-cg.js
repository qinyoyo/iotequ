import Layout from '@/layout'
export default {
  path: '/attendance/exception/adException',
  name: 'adException',
  component: Layout,
  meta: {
    authorities: ['/attendance/exception/adException'],
    title: 'adException.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/attendance/exception/adException/list'),
      name: 'AdExceptionList',
      meta: {
        title: 'adException.route.listTag',
        authorities: ['/attendance/exception/adException/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/exception/adException/record'),
      name: 'AdExceptionForm',
      meta: {
        title: 'adException.route.recordTag',
        authorities: ['/attendance/exception/adException/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
