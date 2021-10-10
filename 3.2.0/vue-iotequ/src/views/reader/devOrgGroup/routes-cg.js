import Layout from '@/layout'
export default {
  path: '/reader/devOrgGroup',
  name: 'devOrgGroup',
  component: Layout,
  meta: {
    authorities: ['/reader/devOrgGroup'],
    title: 'devOrgGroup.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devOrgGroup/list'),
      name: 'DevOrgGroupList',
      props: true,
      meta: {
        title: 'devOrgGroup.title.list',
        authorities: ['/reader/devOrgGroup/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devOrgGroup/record'),
      name: 'DevOrgGroupForm',
      props: true,
      meta: {
        title: 'devOrgGroup.title.record',
        dialog: true,
        authorities: ['/reader/devOrgGroup/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
