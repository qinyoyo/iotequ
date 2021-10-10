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
        title: 'payLogin.title.list',
        authorities: ['/pay/payLogin/list'],
        breadcrumb: false,
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
        title: 'payLogin.title.record',
        dialog: true,
        authorities: ['/pay/payLogin/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
