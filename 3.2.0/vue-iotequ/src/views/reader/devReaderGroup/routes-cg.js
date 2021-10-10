import Layout from '@/layout'
export default {
  path: '/reader/devReaderGroup',
  name: 'devReaderGroup',
  component: Layout,
  meta: {
    authorities: ['/reader/devReaderGroup'],
    title: 'devReaderGroup.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devReaderGroup/list'),
      name: 'DevReaderGroupList',
      props: true,
      meta: {
        title: 'devReaderGroup.title.list',
        authorities: ['/reader/devReaderGroup/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devReaderGroup/record'),
      name: 'DevReaderGroupForm',
      props: true,
      meta: {
        title: 'devReaderGroup.title.record',
        dialog: true,
        authorities: ['/reader/devReaderGroup/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
