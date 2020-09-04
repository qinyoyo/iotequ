import Layout from '@/layout'
export default {
  path: '/ewallet/ewBill',
  name: 'ewBill',
  component: Layout,
  meta: {
    authorities: ['/ewallet/ewBill'],
    title: 'ewBill.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/ewallet/ewBill/list'),
      name: 'EwBillList',
      meta: {
        title: 'ewBill.route.listTag',
        authorities: ['/ewallet/ewBill/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewBill/record'),
      name: 'EwBillForm',
      meta: {
        title: 'ewBill.route.recordTag',
        dialog: true,
        authorities: ['/ewallet/ewBill/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
