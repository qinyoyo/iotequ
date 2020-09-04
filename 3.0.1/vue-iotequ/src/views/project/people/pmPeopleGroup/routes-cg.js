import Layout from '@/layout'
export default {
  path: '/project/people/pmPeopleGroup',
  name: 'pmPeopleGroup',
  component: Layout,
  meta: {
    authorities: ['/project/people/pmPeopleGroup'],
    title: 'pmPeopleGroup.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/project/people/pmPeopleGroup/list'),
      name: 'PmPeopleGroupList',
      meta: {
        title: 'pmPeopleGroup.route.listTag',
        authorities: ['/project/people/pmPeopleGroup/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/project/people/pmPeopleGroup/record'),
      name: 'PmPeopleGroupForm',
      meta: {
        title: 'pmPeopleGroup.route.recordTag',
        dialog: true,
        authorities: ['/project/people/pmPeopleGroup/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
