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
      props: true,
      meta: {
        title: 'adEmployee.title.list',
        authorities: ['/attendance/employee/adEmployee/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'join',
      component: () => import('@/views/attendance/employee/adEmployee/join'),
      name: 'AdEmployeeJoinList',
      props: true,
      meta: {
        title: 'adEmployee.title.join',
        authorities: ['/attendance/employee/adEmployee/list'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/attendance/employee/adEmployee/record'),
      name: 'AdEmployeeForm',
      props: true,
      meta: {
        title: 'adEmployee.title.record',
        authorities: ['/attendance/employee/adEmployee/record'],
        breadcrumb: false,
        tagView: true,
        noCache: false
      }
    }
  ]
}
