import Layout from '@/layout'
export default {
  path: '/codegenerator/cgField',
  name: 'cgField',
  component: Layout,
  meta: {
    authorities: ['/codegenerator/cgField'],
    title: 'cgField.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/codegenerator/cgField/list'),
      name: 'CgFieldList',
      props: true,
      meta: {
        title: 'cgField.title.list',
        authorities: ['/codegenerator/cgField/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/codegenerator/cgField/record'),
      name: 'CgFieldForm',
      props: true,
      meta: {
        title: 'cgField.title.record',
        authorities: ['/codegenerator/cgField/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}