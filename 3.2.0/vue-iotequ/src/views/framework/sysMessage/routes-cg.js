import Layout from '@/layout'
export default {
  path: '/framework/sysMessage',
  name: 'sysMessage',
  component: Layout,
  meta: {
    authorities: ['/framework/sysMessage'],
    title: 'sysMessage.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/framework/sysMessage/list'),
      name: 'MessageList',
      props: true,
      meta: {
        title: 'sysMessage.title.list',
        icon: 'fa fa-commenting-o',
        authorities: ['/framework/sysMessage/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/framework/sysMessage/record'),
      name: 'MessageForm',
      props: true,
      meta: {
        title: 'sysMessage.title.record',
        icon: 'fa fa-commenting-o',
        dialog: true,
        authorities: ['/framework/sysMessage/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
