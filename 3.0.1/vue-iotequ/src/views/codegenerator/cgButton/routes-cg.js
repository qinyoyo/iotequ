import Layout from '@/layout'
export default {
  path: '/codegenerator/cgButton',
  name: 'cgButton',
  component: Layout,
  meta: {
    authorities: ['/codegenerator/cgButton'],
    title: 'cgButton.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/codegenerator/cgButton/list'),
      name: 'CgButtonList',
      meta: {
        title: 'cgButton.route.listTag',
        authorities: ['/codegenerator/cgButton/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/codegenerator/cgButton/record'),
      name: 'CgButtonForm',
      meta: {
        title: 'cgButton.route.recordTag',
        icon: 'el-icon-mouse',
        authorities: ['/codegenerator/cgButton/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
