export default {
  adOrg: {
    title: {
      list: '考勤部门信息表',
      record: '部门考勤配置信息',
      code: '考勤部门信息表'
    },
    field: {
      shiftId: '部门排班',
      manageLimitValid: '分钟,人事审批单涉及的时长超过权限值将提交上级部门',
      manageLimit: '审核权限',
      deviationValid: '考勤时间允许的偏差分钟',
      deviation: '允许误差',
      floatLimitValid: '弹性考勤允许的最大浮动分钟数',
      floatLimit: '浮动范围',
      absentLimitValid: '迟到早退超过此分钟数被认为旷工',
      absentLimit: '旷工底限',
      freeWorkLimitValid: '超过此分钟数的自由加班才会被记录',
      freeWorkLimit: '自由加班起限',
      orgCode: '机构代码',
      hrValid: '人事管理员可浏览部门及下属部门的数据',
      hr: '人事',
      managerValid: '部门人事审核管理者,为空时往上级部门传递',
      manager: '考勤审核人'
    }
  }
}
