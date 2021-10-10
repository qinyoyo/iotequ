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
      props: true,
      meta: {
        title: 'devAuthRole.title.list',
        authorities: ['/reader/devAuthRole/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
