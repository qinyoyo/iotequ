import Layout from '@/layout'
export default {
  path: '/attendance/shifttime/adShiftTime',
  name: 'adShiftTime',
  component: Layout,
  meta: {
    authorities: ['/attendance/shifttime/adShiftTime'],
    title: 'adShiftTime.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/attendance/shifttime/adShiftTime/list'),
      name: 'AdShiftTimeList',
      props: true,
      meta: {
        title: 'adShiftTime.title.list',
        authorities: ['/attendance/shifttime/adShiftTime/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/shifttime/adShiftTime/record'),
      name: 'AdShiftTimeForm',
      props: true,
      meta: {
        title: 'adShiftTime.title.record',
        authorities: ['/attendance/shifttime/adShiftTime/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
