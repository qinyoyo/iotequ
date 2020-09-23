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
        title: 'cgList.route.listTag',
        authorities: ['/codegenerator/cgList/list'],
        breadcrumb: true,
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
        title: 'cgList.route.joinTag',
        authorities: ['/codegenerator/cgList/list'],
        breadcrumb: true,
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
        title: 'cgList.route.recordTag',
        authorities: ['/codegenerator/cgList/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
