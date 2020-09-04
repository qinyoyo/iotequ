import Layout from '@/layout'
export default {
  path: '/attendance/adjust/adAdjust',
  name: 'adAdjust',
  component: Layout,
  meta: {
    authorities: ['/attendance/adjust/adAdjust'],
    title: 'adAdjust.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/attendance/adjust/adAdjust/list'),
      name: 'AdjustList',
      meta: {
        title: 'adAdjust.route.listTag',
        icon: 'fa fa-handshake-o',
        authorities: ['/attendance/adjust/adAdjust/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'approve',
      component: () => import('@/views/attendance/adjust/adAdjust/approve'),
      name: 'AdjustApproveForm',
      meta: {
        title: 'adAdjust.route.approveTag',
        icon: 'fa fa-address-card-o',
        dialog: true,
        authorities: ['/attendance/adjust/adAdjust/f_approve'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/adjust/adAdjust/record'),
      name: 'AdjustRecordForm',
      meta: {
        title: 'adAdjust.route.recordTag',
        icon: 'fa fa-plus-circle',
        dialog: true,
        authorities: ['/attendance/adjust/adAdjust/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
