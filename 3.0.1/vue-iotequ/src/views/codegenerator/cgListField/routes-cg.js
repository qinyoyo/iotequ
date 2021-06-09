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
      props: true,
      meta: {
        title: 'cgListField.title.list',
        authorities: ['/codegenerator/cgListField/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/codegenerator/cgListField/record'),
      name: 'CgListFieldForm',
      props: true,
      meta: {
        title: 'cgListField.title.record',
        dialog: true,
        authorities: ['/codegenerator/cgListField/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
