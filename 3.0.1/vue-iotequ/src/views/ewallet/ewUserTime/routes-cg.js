import Layout from '@/layout'
export default {
  path: '/ewallet/ewUserTime',
  name: 'ewUserTime',
  component: Layout,
  meta: {
    authorities: ['/ewallet/ewUserTime'],
    title: 'ewUserTime.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/ewallet/ewUserTime/list'),
      name: 'EwUserTimeList',
      meta: {
        title: 'ewUserTime.route.listTag',
        authorities: ['/ewallet/ewUserTime/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewUserTime/record'),
      name: 'EwUserTimeForm',
      meta: {
        title: 'ewUserTime.route.recordTag',
        dialog: true,
        authorities: ['/ewallet/ewUserTime/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
