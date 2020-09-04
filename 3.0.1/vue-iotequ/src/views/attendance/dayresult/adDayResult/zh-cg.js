export default {
  adDayResult: {
    title: {
      list: '日考勤汇总',
      code: '日考勤结果'
    },
    action: {
      adjust: '考勤数据校正',
      _exportConfirm: '需选择部门和月份，继续生成报表',
      _export: 'export'
    },
    route: {
      listTag: '日考勤汇总'
    },
    field: {
      id: '',
      orgCode: '部门',
      orgName: '部门名',
      employee: '员工',
      realName: '姓名',
      adDate: '日期',
      shiftName: '班次',
      state: '考勤',
      stateName: '考勤描述',
      times: '次数',
      minutes: '时长',
      workMinutes: '工作时长',
      shiftId: '班次排序',
      employeeNo: '工号'
    }
  }
}
