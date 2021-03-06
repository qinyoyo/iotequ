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
      props: true,
      meta: {
        title: 'payShop.title.list',
        authorities: ['/pay/payShop/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/pay/payShop/record'),
      name: 'PayShopForm',
      props: true,
      meta: {
        title: 'payShop.title.record',
        dialog: true,
        authorities: ['/pay/payShop/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
