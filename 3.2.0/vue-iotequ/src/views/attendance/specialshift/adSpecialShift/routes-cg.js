import Layout from '@/layout'
export default {
  path: '/attendance/specialshift/adSpecialShift',
  name: 'adSpecialShift',
  component: Layout,
  meta: {
    authorities: ['/attendance/specialshift/adSpecialShift'],
    title: 'adSpecialShift.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/attendance/specialshift/adSpecialShift/list'),
      name: 'AdSpecialShiftList',
      props: true,
      meta: {
        title: 'adSpecialShift.title.list',
        authorities: ['/attendance/specialshift/adSpecialShift/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/specialshift/adSpecialShift/record'),
      name: 'AdSpecialShiftForm',
      props: true,
      meta: {
        title: 'adSpecialShift.title.record',
        authorities: ['/attendance/specialshift/adSpecialShift/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
