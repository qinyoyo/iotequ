import Layout from '@/layout'
export default {
  path: '/ewallet/ewDevice',
  name: 'ewDevice',
  component: Layout,
  meta: {
    authorities: ['/ewallet/ewDevice'],
    title: 'ewDevice.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/ewallet/ewDevice/list'),
      name: 'EwDeviceList',
      meta: {
        title: 'ewDevice.route.listTag',
        authorities: ['/ewallet/ewDevice/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewDevice/record'),
      name: 'EwDeviceForm',
      meta: {
        title: 'ewDevice.route.recordTag',
        dialog: true,
        authorities: ['/ewallet/ewDevice/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
