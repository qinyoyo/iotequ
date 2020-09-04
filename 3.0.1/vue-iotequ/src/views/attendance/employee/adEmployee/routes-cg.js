import Layout from '@/layout'
export default {
  path: '/attendance/employee/adEmployee',
  name: 'adEmployee',
  component: Layout,
  meta: {
    authorities: ['/attendance/employee/adEmployee'],
    title: 'adEmployee.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/attendance/employee/adEmployee/list'),
      name: 'AdEmployeeList',
      meta: {
        title: 'adEmployee.route.listTag',
        authorities: ['/attendance/employee/adEmployee/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'join',
      component: () => import('@/views/attendance/employee/adEmployee/join'),
      name: 'AdEmployeeJoinList',
      meta: {
        title: 'adEmployee.route.joinTag',
        authorities: ['/attendance/employee/adEmployee/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/employee/adEmployee/record'),
      name: 'AdEmployeeForm',
      meta: {
        title: 'adEmployee.route.recordTag',
        authorities: ['/attendance/employee/adEmployee/record'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }
  ]
}
