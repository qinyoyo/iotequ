import Layout from '@/layout'
export default {
  path: '/codegenerator/cgProject',
  name: 'cgProject',
  component: Layout,
  meta: {
    authorities: ['/codegenerator/cgProject'],
    title: 'cgProject.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/codegenerator/cgProject/list'),
      name: 'CgProjectList',
      props: true,
      meta: {
        title: 'cgProject.title.list',
        authorities: ['/codegenerator/cgProject/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/codegenerator/cgProject/record'),
      name: 'CgProjectForm',
      props: true,
      meta: {
        title: 'cgProject.title.record',
        dialog: true,
        authorities: ['/codegenerator/cgProject/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
