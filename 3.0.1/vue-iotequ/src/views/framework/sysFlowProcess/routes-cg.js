import Layout from '@/layout'
export default {
  path: '/framework/sysFlowProcess',
  name: 'sysFlowProcess',
  component: Layout,
  meta: {
    authorities: ['/framework/sysFlowProcess'],
    title: 'sysFlowProcess.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/framework/sysFlowProcess/list'),
      name: 'FlowProcessList',
      props: true,
      meta: {
        title: 'sysFlowProcess.route.listTag',
        authorities: ['/framework/sysFlowProcess/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/framework/sysFlowProcess/record'),
      name: 'FlowProcessForm',
      props: true,
      meta: {
        title: 'sysFlowProcess.route.recordTag',
        dialog: true,
        authorities: ['/framework/sysFlowProcess/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
