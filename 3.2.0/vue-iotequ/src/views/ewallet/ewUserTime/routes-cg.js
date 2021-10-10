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
      props: true,
      meta: {
        title: 'ewUserTime.title.list',
        authorities: ['/ewallet/ewUserTime/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewUserTime/record'),
      name: 'EwUserTimeForm',
      props: true,
      meta: {
        title: 'ewUserTime.title.record',
        dialog: true,
        authorities: ['/ewallet/ewUserTime/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
