import Layout from '@/layout'
export default {
  path: '/reader/devData',
  name: 'devData',
  component: Layout,
  meta: {
    authorities: ['/reader/devData'],
    title: 'devData.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'record',
      component: () => import('@/views/reader/devData/record'),
      name: 'DevDataForm',
      props: true,
      meta: {
        title: 'devData.title.record',
        icon: 'fa fa-skyatlas',
        dialog: true,
        authorities: ['/reader/devData/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
