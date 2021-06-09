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
      props: true,
      meta: {
        title: 'ewDevice.title.list',
        authorities: ['/ewallet/ewDevice/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewDevice/record'),
      name: 'EwDeviceForm',
      props: true,
      meta: {
        title: 'ewDevice.title.record',
        dialog: true,
        authorities: ['/ewallet/ewDevice/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
