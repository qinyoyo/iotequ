import Layout from '@/layout'
export default {
  path: '/reader/devAuthConfig',
  name: 'devAuthConfig',
  component: Layout,
  meta: {
    authorities: ['/reader/devAuthConfig'],
    title: 'devAuthConfig.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devAuthConfig/list'),
      name: 'AuthConfigList',
      meta: {
        title: 'devAuthConfig.route.listTag',
        authorities: ['/reader/devAuthConfig/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devAuthConfig/record'),
      name: 'AuthConfigForm',
      meta: {
        title: 'devAuthConfig.route.recordTag',
        authorities: ['/reader/devAuthConfig/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
