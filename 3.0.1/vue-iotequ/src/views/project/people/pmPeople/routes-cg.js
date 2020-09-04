import Layout from '@/layout'
export default {
  path: '/project/people/pmPeople',
  name: 'pmPeople',
  component: Layout,
  meta: {
    authorities: ['/project/people/pmPeople'],
    title: 'pmPeople.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/project/people/pmPeople/list'),
      name: 'PmPeopleList',
      meta: {
        title: 'pmPeople.route.listTag',
        authorities: ['/project/people/pmPeople/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/project/people/pmPeople/record'),
      name: 'PmPeopleForm',
      meta: {
        title: 'pmPeople.route.recordTag',
        dialog: true,
        authorities: ['/project/people/pmPeople/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
