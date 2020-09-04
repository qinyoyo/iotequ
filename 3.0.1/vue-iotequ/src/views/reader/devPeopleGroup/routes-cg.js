import Layout from '@/layout'
export default {
  path: '/reader/devPeopleGroup',
  name: 'devPeopleGroup',
  component: Layout,
  meta: {
    authorities: ['/reader/devPeopleGroup'],
    title: 'devPeopleGroup.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devPeopleGroup/list'),
      name: 'DevPeopleGroupList',
      meta: {
        title: 'devPeopleGroup.route.listTag',
        authorities: ['/reader/devPeopleGroup/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devPeopleGroup/record'),
      name: 'DevPeopleGroupForm',
      meta: {
        title: 'devPeopleGroup.route.recordTag',
        dialog: true,
        authorities: ['/reader/devPeopleGroup/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
