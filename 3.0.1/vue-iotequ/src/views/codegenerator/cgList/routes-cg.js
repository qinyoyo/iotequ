import Layout from '@/layout'
export default {
  path: '/codegenerator/cgList',
  name: 'cgList',
  component: Layout,
  meta: {
    authorities: ['/codegenerator/cgList'],
    title: 'cgList.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/codegenerator/cgList/list'),
      name: 'CgListList',
      props: true,
      meta: {
        title: 'cgList.title.list',
        authorities: ['/codegenerator/cgList/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'join',
      component: () => import('@/views/codegenerator/cgList/join'),
      name: 'CgListJoinList',
      props: true,
      meta: {
        title: 'cgList.title.join',
        authorities: ['/codegenerator/cgList/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/codegenerator/cgList/record'),
      name: 'CgListForm',
      props: true,
      meta: {
        title: 'cgList.title.record',
        authorities: ['/codegenerator/cgList/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
