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
        title: 'ewUser.route.listTag',
        authorities: ['/ewallet/ewUser/list'],
        breadcrumb: true,
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
        title: 'ewUser.route.recordTag',
        dialog: true,
        authorities: ['/ewallet/ewUser/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
