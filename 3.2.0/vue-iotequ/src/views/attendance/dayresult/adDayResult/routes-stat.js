import Layout from '@/layout'
export default {
  path: '/attendance/dayresult/adDayResult',
  name: 'adDayResultStat',
  component: Layout,
  meta: {
    authorities: ['/attendance/dayresult/adDayResult'],
    title: 'adDayResult.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'statByOrg',
      component: () => import('@/views/attendance/dayresult/adDayResult/statByOrg'),
      name: 'AdStatByOrg',
      meta: {
        title: 'adStat.title.statByOrg',
        authorities: ['/attendance/dayresult/adDayResult/query'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'statRecTime',
      component: () => import('@/views/attendance/dayresult/adDayResult/statRecTime'),
      name: 'AdStatRecTime',
      meta: {
        title: 'adStat.title.statRecTime',
        authorities: ['/attendance/dayresult/adDayResult/query'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
