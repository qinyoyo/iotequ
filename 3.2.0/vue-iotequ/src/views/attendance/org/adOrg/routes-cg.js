import Layout from '@/layout'
export default {
  path: '/attendance/org/adOrg',
  name: 'adOrg',
  component: Layout,
  meta: {
    authorities: ['/attendance/org/adOrg'],
    title: 'adOrg.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/attendance/org/adOrg/list'),
      name: 'AdOrgList',
      props: true,
      meta: {
        title: 'adOrg.title.list',
        authorities: ['/attendance/org/adOrg/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/org/adOrg/record'),
      name: 'AdOrgForm',
      props: true,
      meta: {
        title: 'adOrg.title.record',
        dialog: true,
        authorities: ['/attendance/org/adOrg/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
