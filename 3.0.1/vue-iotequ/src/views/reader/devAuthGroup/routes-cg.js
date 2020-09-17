import Layout from '@/layout'
export default {
  path: '/reader/devAuthGroup',
  name: 'devAuthGroup',
  component: Layout,
  meta: {
    authorities: ['/reader/devAuthGroup'],
    title: 'devAuthGroup.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devAuthGroup/list'),
      name: 'DevAuthGroupList',
      meta: {
        title: 'devAuthGroup.route.listTag',
        icon: 'fa fa-user-plus',
        authorities: ['/reader/devAuthGroup/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devAuthGroup/record'),
      name: 'DevAuthGroupForm',
      meta: {
        title: 'devAuthGroup.route.recordTag',
        icon: 'fa fa-user-plus',
        dialog: true,
        authorities: ['/reader/devAuthGroup/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
