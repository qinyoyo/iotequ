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
      props: true,
      meta: {
        title: 'payPos.title.list',
        authorities: ['/pay/payPos/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/pay/payPos/record'),
      name: 'PayPosForm',
      props: true,
      meta: {
        title: 'payPos.title.record',
        dialog: true,
        authorities: ['/pay/payPos/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
