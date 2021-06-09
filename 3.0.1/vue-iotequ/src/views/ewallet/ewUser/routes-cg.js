import Layout from '@/layout'
export default {
  path: '/ewallet/ewUser',
  name: 'ewUser',
  component: Layout,
  meta: {
    authorities: ['/ewallet/ewUser'],
    title: 'ewUser.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/ewallet/ewUser/list'),
      name: 'EwUserList',
      props: true,
      meta: {
        title: 'ewUser.title.list',
        authorities: ['/ewallet/ewUser/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/ewallet/ewUser/record'),
      name: 'EwUserForm',
      props: true,
      meta: {
        title: 'ewUser.title.record',
        dialog: true,
        authorities: ['/ewallet/ewUser/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
