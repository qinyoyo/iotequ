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
      props: true,
      meta: {
        title: 'devPeopleGroup.title.list',
        authorities: ['/reader/devPeopleGroup/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devPeopleGroup/record'),
      name: 'DevPeopleGroupForm',
      props: true,
      meta: {
        title: 'devPeopleGroup.title.record',
        dialog: true,
        authorities: ['/reader/devPeopleGroup/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
