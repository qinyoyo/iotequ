import Layout from '@/layout'
export default {
  path: '/attendance/dayresult/adDayResult',
  name: 'adDayResult',
  component: Layout,
  meta: {
    authorities: ['/attendance/dayresult/adDayResult'],
    title: 'adDayResult.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/attendance/dayresult/adDayResult/list'),
      name: 'AdDayResultList',
      props: true,
      meta: {
        title: 'adDayResult.route.listTag',
        authorities: ['/attendance/dayresult/adDayResult/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
