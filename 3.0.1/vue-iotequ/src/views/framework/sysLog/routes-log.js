import Layout from '@/layout'
export default {
  path: '/framework/sysLog',
  name: 'sysLogStat',
  component: Layout,
  meta: {
    authorities: ['/framework/sysLog'],
    title: 'sysLog.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'loginTop10',
      component: () => import('@/views/framework/sysLog/loginTop10'),
      name: 'SysLoginTop10',
      meta: {
        title: 'sysLog.action.loginTop10',
        authorities: ['/framework/sysLog/query'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
