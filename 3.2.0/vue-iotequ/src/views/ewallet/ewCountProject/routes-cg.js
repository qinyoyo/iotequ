import Layout from '@/layout'
export default {
  path: '/ewallet/ewCountProject',
  name: 'ewCountProject',
  component: Layout,
  meta: {
    authorities: ['/ewallet/ewCountProject'],
    title: 'ewCountProject.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/ewallet/ewCountProject/list'),
      name: 'EwCountProjectList',
      props: true,
      meta: {
        title: 'ewCountProject.title.list',
        authorities: ['/ewallet/ewCountProject/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewCountProject/record'),
      name: 'EwCountProjectForm',
      props: true,
      meta: {
        title: 'ewCountProject.title.record',
        dialog: true,
        authorities: ['/ewallet/ewCountProject/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
