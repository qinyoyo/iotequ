import Layout from '@/layout'
export default {
  path: '/attendance/specialshifttime/adSpecialShiftTime',
  name: 'adSpecialShiftTime',
  component: Layout,
  meta: {
    authorities: ['/attendance/specialshifttime/adSpecialShiftTime'],
    title: 'adSpecialShiftTime.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/attendance/specialshifttime/adSpecialShiftTime/list'),
      name: 'AdSpecialShiftTimeList',
      meta: {
        title: 'adSpecialShiftTime.route.listTag',
        authorities: ['/attendance/specialshifttime/adSpecialShiftTime/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/specialshifttime/adSpecialShiftTime/record'),
      name: 'AdSpecialShiftTimeForm',
      meta: {
        title: 'adSpecialShiftTime.route.recordTag',
        authorities: ['/attendance/specialshifttime/adSpecialShiftTime/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
