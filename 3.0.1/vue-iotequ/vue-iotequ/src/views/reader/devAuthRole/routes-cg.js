import Layout from '@/layout'
export default {
  path: '/reader/devAuthRole',
  name: 'devAuthRole',
  component: Layout,
  meta: {
    authorities: ['/reader/devAuthRole'],
    title: 'devAuthRole.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devAuthRole/list'),
      name: 'AuthRoleList',
      meta: {
        title: 'devAuthRole.route.listTag',
        authorities: ['/reader/devAuthRole/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
