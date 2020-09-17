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
      meta: {
        title: 'devReaderGroup.route.listTag',
        authorities: ['/reader/devReaderGroup/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devReaderGroup/record'),
      name: 'DevReaderGroupForm',
      meta: {
        title: 'devReaderGroup.route.recordTag',
        dialog: true,
        authorities: ['/reader/devReaderGroup/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
