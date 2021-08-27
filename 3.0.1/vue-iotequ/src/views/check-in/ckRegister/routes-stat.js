import Layout from '@/layout'
export default {
  path: '/check-in/ckRegister',
  name: 'ckRegisterStat',
  component: Layout,
  meta: {
    authorities: ['/check-in/ckRegister'],
    title: 'ckRegister.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'amountByDay',
      component: () => import('@/views/check-in/ckRegister/amountByDay'),
      name: 'CkAmountByDay',
      meta: {
        title: 'ckStat.title.amountByDay',
        authorities: ['/check-in/ckRegister/query'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'amountByMonth',
      component: () => import('@/views/check-in/ckRegister/amountByMonth'),
      name: 'CkAmountByMonth',
      meta: {
        title: 'ckStat.title.amountByMonth',
        authorities: ['/check-in/ckRegister/query'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'amountByAge',
      component: () => import('@/views/check-in/ckRegister/amountByAge'),
      name: 'CkAmountByAge',
      meta: {
        title: 'ckStat.title.amountByAge',
        authorities: ['/check-in/ckRegister/query'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'amountByAgeMonth',
      component: () => import('@/views/check-in/ckRegister/amountByAgeMonth'),
      name: 'CkAmountByAgeMonth',
      meta: {
        title: 'ckStat.title.amountByAgeMonth',
        authorities: ['/check-in/ckRegister/query'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'distributionByAge',
      component: () => import('@/views/check-in/ckRegister/distributionByAge'),
      name: 'CkDistributionByAge',
      meta: {
        title: 'ckStat.title.distributionByAge',
        authorities: ['/check-in/ckRegister/query'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'timeByDay',
      component: () => import('@/views/check-in/ckRegister/timeByDay'),
      name: 'CkTimeByDay',
      meta: {
        title: 'ckStat.title.timeByDay',
        authorities: ['/check-in/ckRegister/query'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'amountByArea',
      component: () => import('@/views/check-in/ckRegister/amountByArea'),
      name: 'CkAmountByArea',
      meta: {
        title: 'ckStat.title.amountByArea',
        authorities: ['/check-in/ckRegister/query'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    }               
  ]
}
