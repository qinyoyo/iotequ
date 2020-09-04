import Layout from '@/layout'
export default {
  path: '/ewallet/ewTimeProject',
  name: 'ewTimeProject',
  component: Layout,
  meta: {
    authorities: ['/ewallet/ewTimeProject'],
    title: 'ewTimeProject.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/ewallet/ewTimeProject/list'),
      name: 'EwTimeProjectList',
      meta: {
        title: 'ewTimeProject.route.listTag',
        authorities: ['/ewallet/ewTimeProject/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewTimeProject/record'),
      name: 'EwTimeProjectForm',
      meta: {
        title: 'ewTimeProject.route.recordTag',
        dialog: true,
        authorities: ['/ewallet/ewTimeProject/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
