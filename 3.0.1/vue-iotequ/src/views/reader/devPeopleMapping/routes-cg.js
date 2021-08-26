import Layout from '@/layout'
export default {
  path: '/reader/devPeopleMapping',
  name: 'devPeopleMapping',
  component: Layout,
  meta: {
    authorities: ['/reader/devPeopleMapping'],
    title: 'devPeopleMapping.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devPeopleMapping/list'),
      name: 'PeopleMappingList',
      props: true,
      meta: {
        title: 'devPeopleMapping.title.list',
        authorities: ['/reader/devPeopleMapping/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devPeopleMapping/record'),
      name: 'PeopleMappingForm',
      props: true,
      meta: {
        title: 'devPeopleMapping.title.record',
        authorities: ['/reader/devPeopleMapping/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
