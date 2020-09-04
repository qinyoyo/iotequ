import Layout from '@/layout'
export default {
  path: '/codegenerator/cgForm',
  name: 'cgForm',
  component: Layout,
  meta: {
    authorities: ['/codegenerator/cgForm'],
    title: 'cgForm.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/codegenerator/cgForm/list'),
      name: 'CgFormList',
      meta: {
        title: 'cgForm.route.listTag',
        authorities: ['/codegenerator/cgForm/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/codegenerator/cgForm/record'),
      name: 'CgFormForm',
      meta: {
        title: 'cgForm.route.recordTag',
        authorities: ['/codegenerator/cgForm/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
