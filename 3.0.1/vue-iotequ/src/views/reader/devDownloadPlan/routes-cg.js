import Layout from '@/layout'
export default {
  path: '/reader/devDownloadPlan',
  name: 'devDownloadPlan',
  component: Layout,
  meta: {
    authorities: ['/reader/devDownloadPlan'],
    title: 'devDownloadPlan.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devDownloadPlan/list'),
      name: 'DownloadPlanList',
      props: true,
      meta: {
        title: 'devDownloadPlan.title.list',
        authorities: ['/reader/devDownloadPlan/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devDownloadPlan/record'),
      name: 'DownloadPlanForm',
      props: true,
      meta: {
        title: 'devDownloadPlan.title.record',
        authorities: ['/reader/devDownloadPlan/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
