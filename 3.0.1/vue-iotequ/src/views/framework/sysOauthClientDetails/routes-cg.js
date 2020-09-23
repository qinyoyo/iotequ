import Layout from '@/layout'
export default {
  path: '/framework/sysOauthClientDetails',
  name: 'sysOauthClientDetails',
  component: Layout,
  meta: {
    authorities: ['/framework/sysOauthClientDetails'],
    title: 'sysOauthClientDetails.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/framework/sysOauthClientDetails/list'),
      name: 'OauthClientDetailsList',
      props: true,
      meta: {
        title: 'sysOauthClientDetails.route.listTag',
        authorities: ['/framework/sysOauthClientDetails/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/framework/sysOauthClientDetails/record'),
      name: 'OauthClientDetailsForm',
      props: true,
      meta: {
        title: 'sysOauthClientDetails.route.recordTag',
        dialog: true,
        authorities: ['/framework/sysOauthClientDetails/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
