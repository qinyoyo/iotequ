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
      props: true,
      meta: {
        title: 'adShift.title.list',
        authorities: ['/attendance/shift/adShift/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/shift/adShift/record'),
      name: 'AdShiftForm',
      props: true,
      meta: {
        title: 'adShift.title.record',
        icon: 'fa',
        authorities: ['/attendance/shift/adShift/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
