import Layout from '@/layout'
export default {
  path: '/pay/payLogin',
  name: 'payLogin',
  component: Layout,
  meta: {
    authorities: ['/pay/payLogin'],
    title: 'payLogin.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/pay/payLogin/list'),
      name: 'PayLoginList',
      props: true,
      meta: {
        title: 'payLogin.route.listTag',
        authorities: ['/pay/payLogin/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/pay/payLogin/record'),
      name: 'PayLoginForm',
      props: true,
      meta: {
        title: 'payLogin.route.recordTag',
        dialog: true,
        authorities: ['/pay/payLogin/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
