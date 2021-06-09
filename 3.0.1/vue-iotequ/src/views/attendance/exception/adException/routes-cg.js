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
      props: true,
      meta: {
        title: 'adException.title.list',
        authorities: ['/attendance/exception/adException/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/exception/adException/record'),
      name: 'AdExceptionForm',
      props: true,
      meta: {
        title: 'adException.title.record',
        authorities: ['/attendance/exception/adException/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
