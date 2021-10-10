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
      props: true,
      meta: {
        title: 'ewTimeProject.title.list',
        authorities: ['/ewallet/ewTimeProject/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewTimeProject/record'),
      name: 'EwTimeProjectForm',
      props: true,
      meta: {
        title: 'ewTimeProject.title.record',
        dialog: true,
        authorities: ['/ewallet/ewTimeProject/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
