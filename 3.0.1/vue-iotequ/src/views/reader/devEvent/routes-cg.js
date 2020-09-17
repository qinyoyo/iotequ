import Layout from '@/layout'
export default {
  path: '/reader/devEvent',
  name: 'devEvent',
  component: Layout,
  meta: {
    authorities: ['/reader/devEvent'],
    title: 'devEvent.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/reader/devEvent/list'),
      name: 'DevEventList',
      meta: {
        title: 'devEvent.route.listTag',
        authorities: ['/reader/devEvent/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devEvent/record'),
      name: 'EventForm',
      meta: {
        title: 'devEvent.route.recordTag',
        icon: 'fa fa-user-circle-o',
        authorities: ['/reader/devEvent/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
