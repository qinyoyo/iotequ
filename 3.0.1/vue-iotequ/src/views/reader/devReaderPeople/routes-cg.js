import Layout from '@/layout'
export default {
  path: '/reader/devReaderPeople',
  name: 'devReaderPeople',
  component: Layout,
  meta: {
    authorities: ['/reader/devReaderPeople'],
    title: 'devReaderPeople.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devReaderPeople/list'),
      name: 'ReaderPeopleList',
      meta: {
        title: 'devReaderPeople.route.listTag',
        authorities: ['/reader/devReaderPeople/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devReaderPeople/record'),
      name: 'DevReaderPeopleForm',
      meta: {
        title: 'devReaderPeople.route.recordTag',
        authorities: ['/reader/devReaderPeople/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
