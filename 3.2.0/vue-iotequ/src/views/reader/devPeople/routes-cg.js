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
      props: true,
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
      props: true,
      meta: {
        title: 'devPeople.title.select',
        authorities: ['/reader/devPeople/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devPeople/record'),
      name: 'DevPeopleForm',
      props: true,
      meta: {
        title: 'devPeople.title.record',
        icon: 'fa fa-user-circle-o',
        authorities: ['/reader/devPeople/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'sample',
      component: () => import('@/views/reader/devPeople/sample'),
      name: 'DevVeinSampleForm',
      props: true,
      meta: {
        title: 'devPeople.title.sample',
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
