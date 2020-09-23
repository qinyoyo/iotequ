import Layout from '@/layout'
export default {
  path: '/codegenerator/cgFormField',
  name: 'cgFormField',
  component: Layout,
  meta: {
    authorities: ['/codegenerator/cgFormField'],
    title: 'cgFormField.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/codegenerator/cgFormField/list'),
      name: 'CgFormFieldList',
      props: true,
      meta: {
        title: 'cgFormField.route.listTag',
        authorities: ['/codegenerator/cgFormField/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/codegenerator/cgFormField/record'),
      name: 'CgFormFieldForm',
      props: true,
      meta: {
        title: 'cgFormField.route.recordTag',
        authorities: ['/codegenerator/cgFormField/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
