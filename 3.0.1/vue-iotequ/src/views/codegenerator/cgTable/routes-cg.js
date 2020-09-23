import Layout from '@/layout'
export default {
  path: '/codegenerator/cgTable',
  name: 'cgTable',
  component: Layout,
  meta: {
    authorities: ['/codegenerator/cgTable'],
    title: 'cgTable.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/codegenerator/cgTable/list'),
      name: 'CgTableList',
      props: true,
      meta: {
        title: 'cgTable.route.listTag',
        authorities: ['/codegenerator/cgTable/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/codegenerator/cgTable/record'),
      name: 'CgTableForm',
      props: true,
      meta: {
        title: 'cgTable.route.recordTag',
        authorities: ['/codegenerator/cgTable/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
