import Layout from '@/layout'
export default {
  path: '/check-in/ckSignIn',
  name: 'ckSignIn',
  component: Layout,
  meta: {
    authorities: ['/check-in/ckSignIn'],
    title: 'ckSignIn.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/check-in/ckSignIn/list'),
      name: 'SignInList',
      props: true,
      meta: {
        title: 'ckSignIn.route.listTag',
        authorities: ['/check-in/ckSignIn/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
