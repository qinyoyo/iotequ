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
      props: true,
      meta: {
        title: 'pmPeopleGroup.title.list',
        authorities: ['/project/people/pmPeopleGroup/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/project/people/pmPeopleGroup/record'),
      name: 'PmPeopleGroupForm',
      props: true,
      meta: {
        title: 'pmPeopleGroup.title.record',
        dialog: true,
        authorities: ['/project/people/pmPeopleGroup/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
