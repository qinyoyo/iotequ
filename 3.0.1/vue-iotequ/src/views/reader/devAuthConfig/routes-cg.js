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
      props: true,
      meta: {
        title: 'devAuthConfig.title.list',
        authorities: ['/reader/devAuthConfig/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devAuthConfig/record'),
      name: 'AuthConfigForm',
      props: true,
      meta: {
        title: 'devAuthConfig.title.record',
        authorities: ['/reader/devAuthConfig/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
