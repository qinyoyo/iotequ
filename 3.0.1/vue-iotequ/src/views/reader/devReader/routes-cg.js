import Layout from '@/layout'
export default {
  path: '/reader/devReader',
  name: 'devReader',
  component: Layout,
  meta: {
    authorities: ['/reader/devReader'],
    title: 'devReader.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devReader/list'),
      name: 'DevReaderList',
      props: true,
      meta: {
        title: 'devReader.title.list',
        authorities: ['/reader/devReader/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devReader/record'),
      name: 'DevReaderForm',
      props: true,
      meta: {
        title: 'devReader.title.record',
        dialog: true,
        authorities: ['/reader/devReader/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
