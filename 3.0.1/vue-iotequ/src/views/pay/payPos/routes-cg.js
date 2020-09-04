import Layout from '@/layout'
export default {
  path: '/pay/payPos',
  name: 'payPos',
  component: Layout,
  meta: {
    authorities: ['/pay/payPos'],
    title: 'payPos.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/pay/payPos/list'),
      name: 'PayPosList',
      meta: {
        title: 'payPos.route.listTag',
        authorities: ['/pay/payPos/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/pay/payPos/record'),
      name: 'PayPosForm',
      meta: {
        title: 'payPos.route.recordTag',
        dialog: true,
        authorities: ['/pay/payPos/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
