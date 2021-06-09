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
      props: true,
      meta: {
        title: 'cgButton.title.list',
        authorities: ['/codegenerator/cgButton/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/codegenerator/cgButton/record'),
      name: 'CgButtonForm',
      props: true,
      meta: {
        title: 'cgButton.title.record',
        icon: 'el-icon-mouse',
        authorities: ['/codegenerator/cgButton/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
