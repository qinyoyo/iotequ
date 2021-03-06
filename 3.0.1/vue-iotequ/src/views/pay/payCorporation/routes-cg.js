import Layout from '@/layout'
export default {
  path: '/pay/payCorporation',
  name: 'payCorporation',
  component: Layout,
  meta: {
    authorities: ['/pay/payCorporation'],
    title: 'payCorporation.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/pay/payCorporation/list'),
      name: 'PayCorporationList',
      props: true,
      meta: {
        title: 'payCorporation.title.list',
        authorities: ['/pay/payCorporation/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/pay/payCorporation/record'),
      name: 'PayCorporationForm',
      props: true,
      meta: {
        title: 'payCorporation.title.record',
        dialog: true,
        authorities: ['/pay/payCorporation/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
