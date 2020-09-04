import Layout from '@/layout'
export default {
  path: '/ewallet/ewUserCount',
  name: 'ewUserCount',
  component: Layout,
  meta: {
    authorities: ['/ewallet/ewUserCount'],
    title: 'ewUserCount.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/ewallet/ewUserCount/list'),
      name: 'EwUserCountList',
      meta: {
        title: 'ewUserCount.route.listTag',
        authorities: ['/ewallet/ewUserCount/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewUserCount/record'),
      name: 'EwUserCountForm',
      meta: {
        title: 'ewUserCount.route.recordTag',
        dialog: true,
        authorities: ['/ewallet/ewUserCount/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
