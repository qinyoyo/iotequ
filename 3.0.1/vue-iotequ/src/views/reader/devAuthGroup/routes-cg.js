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
      props: true,
      meta: {
        title: 'devAuthGroup.title.list',
        icon: 'fa fa-user-plus',
        authorities: ['/reader/devAuthGroup/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devAuthGroup/record'),
      name: 'DevAuthGroupForm',
      props: true,
      meta: {
        title: 'devAuthGroup.title.record',
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
