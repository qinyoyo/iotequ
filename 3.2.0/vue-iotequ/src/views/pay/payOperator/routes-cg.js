import Layout from '@/layout'
export default {
  path: '/pay/payOperator',
  name: 'payOperator',
  component: Layout,
  meta: {
    authorities: ['/pay/payOperator'],
    title: 'payOperator.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/pay/payOperator/list'),
      name: 'PayOperatorList',
      props: true,
      meta: {
        title: 'payOperator.title.list',
        authorities: ['/pay/payOperator/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/pay/payOperator/record'),
      name: 'PayOperatorForm',
      props: true,
      meta: {
        title: 'payOperator.title.record',
        dialog: true,
        authorities: ['/pay/payOperator/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
