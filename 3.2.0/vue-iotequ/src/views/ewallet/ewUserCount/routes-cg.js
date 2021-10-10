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
      props: true,
      meta: {
        title: 'ewUserCount.title.list',
        authorities: ['/ewallet/ewUserCount/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewUserCount/record'),
      name: 'EwUserCountForm',
      props: true,
      meta: {
        title: 'ewUserCount.title.record',
        dialog: true,
        authorities: ['/ewallet/ewUserCount/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
