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
      props: true,
      meta: {
        title: 'pmPeople.title.list',
        authorities: ['/project/people/pmPeople/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/project/people/pmPeople/record'),
      name: 'PmPeopleForm',
      props: true,
      meta: {
        title: 'pmPeople.title.record',
        dialog: true,
        authorities: ['/project/people/pmPeople/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
