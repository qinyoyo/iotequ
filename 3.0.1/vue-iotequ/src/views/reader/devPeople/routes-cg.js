import Layout from '@/layout'
export default {
  path: '/reader/devPeople',
  name: 'devPeople',
  component: Layout,
  meta: {
    authorities: ['/reader/devPeople'],
    title: 'devPeople.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devPeople/list'),
      name: 'DevPeopleList',
      meta: {
        title: 'devPeople.route.listTag',
        authorities: ['/reader/devPeople/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'select',
      component: () => import('@/views/reader/devPeople/select'),
      name: 'DevPeopleSelectList',
      meta: {
        title: 'devPeople.route.selectTag',
        authorities: ['/reader/devPeople/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devPeople/record'),
      name: 'DevPeopleForm',
      meta: {
        title: 'devPeople.route.recordTag',
        icon: 'fa fa-user-circle-o',
        authorities: ['/reader/devPeople/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'sample',
      component: () => import('@/views/reader/devPeople/sample'),
      name: 'DevVeinSampleForm',
      meta: {
        title: 'devPeople.route.sampleTag',
        icon: 'fa fa-eyedropper',
        dialog: true,
        authorities: ['/reader/devPeople/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
