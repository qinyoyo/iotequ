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
      props: true,
      meta: {
        title: 'ewBill.title.list',
        authorities: ['/ewallet/ewBill/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewBill/record'),
      name: 'EwBillForm',
      props: true,
      meta: {
        title: 'ewBill.title.record',
        dialog: true,
        authorities: ['/ewallet/ewBill/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
