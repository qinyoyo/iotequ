import Layout from '@/layout'
export default {
  path: '/pay/payShop',
  name: 'payShop',
  component: Layout,
  meta: {
    authorities: ['/pay/payShop'],
    title: 'payShop.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/pay/payShop/list'),
      name: 'PayShopList',
      meta: {
        title: 'payShop.route.listTag',
        authorities: ['/pay/payShop/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/pay/payShop/record'),
      name: 'PayShopForm',
      meta: {
        title: 'payShop.route.recordTag',
        dialog: true,
        authorities: ['/pay/payShop/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
