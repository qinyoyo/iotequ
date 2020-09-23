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
        title: 'adSpecialShift.route.listTag',
        authorities: ['/attendance/specialshift/adSpecialShift/list'],
        breadcrumb: true,
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
        title: 'adSpecialShift.route.recordTag',
        authorities: ['/attendance/specialshift/adSpecialShift/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
