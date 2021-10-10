import Layout from '@/layout'
export default {
  path: '/check-in/ckRegister',
  name: 'ckRegister',
  component: Layout,
  meta: {
    authorities: ['/check-in/ckRegister'],
    title: 'ckRegister.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/check-in/ckRegister/list'),
      name: 'RegisterList',
      props: true,
      meta: {
        title: 'ckRegister.route.listTag',
        authorities: ['/check-in/ckRegister/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/check-in/ckRegister/record'),
      name: 'RegisterForm',
      props: true,
      meta: {
        title: 'ckRegister.route.recordTag',
        dialog: true,
        authorities: ['/check-in/ckRegister/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
