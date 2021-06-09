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
      props: true,
      meta: {
        title: 'adSpecialShiftTime.title.list',
        authorities: ['/attendance/specialshifttime/adSpecialShiftTime/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/specialshifttime/adSpecialShiftTime/record'),
      name: 'AdSpecialShiftTimeForm',
      props: true,
      meta: {
        title: 'adSpecialShiftTime.title.record',
        authorities: ['/attendance/specialshifttime/adSpecialShiftTime/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
