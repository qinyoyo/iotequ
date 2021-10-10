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
        title: 'sysFlowProcess.title.list',
        authorities: ['/framework/sysFlowProcess/list'],
        breadcrumb: false,
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
        title: 'sysFlowProcess.title.record',
        dialog: true,
        authorities: ['/framework/sysFlowProcess/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
