import Layout from '@/layout'
export default {
  path: '/attendance/shift/adShift',
  name: 'adShift',
  component: Layout,
  meta: {
    authorities: ['/attendance/shift/adShift'],
    title: 'adShift.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/attendance/shift/adShift/list'),
      name: 'AdShiftList',
      meta: {
        title: 'adShift.route.listTag',
        authorities: ['/attendance/shift/adShift/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/shift/adShift/record'),
      name: 'AdShiftForm',
      meta: {
        title: 'adShift.route.recordTag',
        icon: 'fa',
        authorities: ['/attendance/shift/adShift/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
