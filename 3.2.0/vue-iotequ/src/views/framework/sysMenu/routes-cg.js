import Layout from '@/layout'
export default {
  path: '/framework/sysMenu',
  name: 'sysMenu',
  component: Layout,
  meta: {
    authorities: ['/framework/sysMenu'],
    title: 'sysMenu.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/framework/sysMenu/list'),
      name: 'MenuList',
      props: true,
      meta: {
        title: 'sysMenu.title.list',
        authorities: ['/framework/sysMenu/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/framework/sysMenu/record'),
      name: 'MenuForm',
      props: true,
      meta: {
        title: 'sysMenu.title.record',
        authorities: ['/framework/sysMenu/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}