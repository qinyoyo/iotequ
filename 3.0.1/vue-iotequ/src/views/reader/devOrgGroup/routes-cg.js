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
      meta: {
        title: 'devOrgGroup.route.listTag',
        authorities: ['/reader/devOrgGroup/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devOrgGroup/record'),
      name: 'DevOrgGroupForm',
      meta: {
        title: 'devOrgGroup.route.recordTag',
        dialog: true,
        authorities: ['/reader/devOrgGroup/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
