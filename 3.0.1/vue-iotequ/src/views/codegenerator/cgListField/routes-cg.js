import Layout from '@/layout'
export default {
  path: '/codegenerator/cgListField',
  name: 'cgListField',
  component: Layout,
  meta: {
    authorities: ['/codegenerator/cgListField'],
    title: 'cgListField.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/codegenerator/cgListField/list'),
      name: 'CgListFieldList',
      meta: {
        title: 'cgListField.route.listTag',
        authorities: ['/codegenerator/cgListField/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/codegenerator/cgListField/record'),
      name: 'CgListFieldForm',
      meta: {
        title: 'cgListField.route.recordTag',
        dialog: true,
        authorities: ['/codegenerator/cgListField/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
