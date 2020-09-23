import Layout from '@/layout'
export default {
  path: '/framework/sysOrg',
  name: 'sysOrg',
  component: Layout,
  meta: {
    authorities: ['/framework/sysOrg'],
    title: 'sysOrg.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/framework/sysOrg/list'),
      name: 'OrgList',
      props: true,
      meta: {
        title: 'sysOrg.route.listTag',
        authorities: ['/framework/sysOrg/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/framework/sysOrg/record'),
      name: 'OrgForm',
      props: true,
      meta: {
        title: 'sysOrg.route.recordTag',
        dialog: true,
        authorities: ['/framework/sysOrg/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
