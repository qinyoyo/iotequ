import Layout from '@/layout'
export default {
  path: '/project/product/pmProject',
  name: 'pmProject',
  component: Layout,
  meta: {
    authorities: ['/project/product/pmProject'],
    title: 'pmProject.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/project/product/pmProject/list'),
      name: 'PmProjectList',
      props: true,
      meta: {
        title: 'pmProject.route.listTag',
        authorities: ['/project/product/pmProject/list'],
        breadcrumb: true,
        tagView: true,
        noCache: false
      }
    },
    {
      path: 'record',
      component: () => import('@/views/project/product/pmProject/record'),
      name: 'PmProjectForm',
      props: true,
      meta: {
        title: 'pmProject.route.recordTag',
        icon: 'el-icon-folder-add',
        dialog: true,
        authorities: ['/project/product/pmProject/record'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'assess',
      component: () => import('@/views/project/product/pmProject/assess'),
      name: 'PmProjectFlowAssessForm',
      props: true,
      meta: {
        title: 'pmProject.title.assess',
        icon: 'fa fa-binoculars',
        dialog: true,
        authorities: ['/project/product/pmProject/f_assess'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'decision',
      component: () => import('@/views/project/product/pmProject/assess'),
      name: 'PmProjectFlowDecisionForm',
      props: true,
      meta: {
        title: 'pmProject.title.decision',
        icon: 'fa fa-gavel',
        dialog: true,
        authorities: ['/project/product/pmProject/f_decision'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'develop',
      component: () => import('@/views/project/product/pmProject/assess'),
      name: 'PmProjectFlowDevelopForm',
      props: true,
      meta: {
        title: 'pmProject.title.develop',
        icon: 'fa fa-cc',
        dialog: true,
        authorities: ['/project/product/pmProject/f_develop'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'publish',
      component: () => import('@/views/project/product/pmProject/assess'),
      name: 'PmProjectFlowPublishForm',
      props: true,
      meta: {
        title: 'pmProject.title.publish',
        icon: 'fa fa-check',
        dialog: true,
        authorities: ['/project/product/pmProject/f_publish'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'review',
      component: () => import('@/views/project/product/pmProject/assess'),
      name: 'PmProjectFlowReviewForm',
      props: true,
      meta: {
        title: 'pmProject.title.review',
        icon: 'fa fa-search-plus',
        dialog: true,
        authorities: ['/project/product/pmProject/f_review'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    },
    {
      path: 'test',
      component: () => import('@/views/project/product/pmProject/assess'),
      name: 'PmProjectFlowTestForm',
      props: true,
      meta: {
        title: 'pmProject.title.test',
        icon: 'fa fa-user-md',
        dialog: true,
        authorities: ['/project/product/pmProject/f_test'],
        breadcrumb: false,
        tagView: false,
        noCache: true
      }
    }
  ]
}
