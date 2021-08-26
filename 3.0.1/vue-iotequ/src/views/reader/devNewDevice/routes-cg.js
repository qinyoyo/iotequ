import Layout from '@/layout'
export default {
  path: '/reader/devNewDevice',
  name: 'devNewDevice',
  component: Layout,
  meta: {
    authorities: ['/reader/devNewDevice'],
    title: 'devNewDevice.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devNewDevice/list'),
      name: 'NewDeviceList',
      props: true,
      meta: {
        title: 'devNewDevice.route.listTag',
        authorities: ['/reader/devNewDevice/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
