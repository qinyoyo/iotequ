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
      props: true,
      meta: {
        title: 'cgForm.title.list',
        authorities: ['/codegenerator/cgForm/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/codegenerator/cgForm/record'),
      name: 'CgFormForm',
      props: true,
      meta: {
        title: 'cgForm.title.record',
        authorities: ['/codegenerator/cgForm/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}