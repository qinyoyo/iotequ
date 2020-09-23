import Layout from '@/layout'
export default {
  path: '/project/version/pmVersionApplication',
  name: 'pmVersionApplication',
  component: Layout,
  meta: {
    authorities: ['/project/version/pmVersionApplication'],
    title: 'pmVersionApplication.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/project/version/pmVersionApplication/list'),
      name: 'PmVersionApplicationList',
      props: true,
      meta: {
        title: 'pmVersionApplication.route.listTag',
        authorities: ['/project/version/pmVersionApplication/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/project/version/pmVersionApplication/record'),
      name: 'PmVersionApplicationForm',
      props: true,
      meta: {
        title: 'pmVersionApplication.route.recordTag',
        icon: 'el-icon-folder-add',
        dialog: true,
        authorities: ['/project/version/pmVersionApplication/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'assess',
      component: () => import('@/views/project/version/pmVersionApplication/assess'),
      name: 'PmVersionFlowAssessForm',
      props: true,
      meta: {
        title: 'pmVersionApplication.title.assess',
        icon: 'fa fa-binoculars',
        dialog: true,
        authorities: ['/project/version/pmVersionApplication/f_assess'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'decision',
      component: () => import('@/views/project/version/pmVersionApplication/assess'),
      name: 'PmVersionFlowDecisionForm',
      props: true,
      meta: {
        title: 'pmVersionApplication.title.decision',
        icon: 'fa fa-gavel',
        dialog: true,
        authorities: ['/project/version/pmVersionApplication/f_decision'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'publish',
      component: () => import('@/views/project/version/pmVersionApplication/assess'),
      name: 'PmVersionFlowPublishForm',
      props: true,
      meta: {
        title: 'pmVersionApplication.title.publish',
        icon: 'fa fa-check',
        dialog: true,
        authorities: ['/project/version/pmVersionApplication/f_publish'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'review',
      component: () => import('@/views/project/version/pmVersionApplication/assess'),
      name: 'PmVersionFlowReviewForm',
      props: true,
      meta: {
        title: 'pmVersionApplication.title.review',
        icon: 'fa fa-search-plus',
        dialog: true,
        authorities: ['/project/version/pmVersionApplication/f_review'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
