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
      props: true,
      meta: {
        title: 'devEvent.title.list',
        authorities: ['/reader/devEvent/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/reader/devEvent/record'),
      name: 'EventForm',
      props: true,
      meta: {
        title: 'devEvent.title.record',
        icon: 'fa fa-user-circle-o',
        authorities: ['/reader/devEvent/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
