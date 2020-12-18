export default {
  sysTask: {
    title: {
      list: '调度任务表',
      record: '调度任务',
      code: '调度任务'
    },
    action: {
      runConfirm: '是否手动执行该任务一次？',
      run: '手动运行'
    },
    route: {
      listTag: '调度任务表',
      recordTag: '调度任务'
    },
    field: {
      id: '',
      name: '任务名',
      description: '详细说明',
      sceduleYearsValid: '逗号分隔的调度年份。*表示所有的年',
      sceduleYears: '调度年',
      scheduleMonths: '调度月',
      scheduleDays: '调度日',
      scheduleWeeksValid: '0-6分别表示星期日-星期六',
      scheduleWeeks: '调度星期',
      scheduleHours: '调度时',
      scheduleMinutesValid: '0,10,20,30,40,50,等10分钟为单位',
      scheduleMinutes: '调度分',
      classNameValid: 'sql表示执行sql语句',
      className: '类名',
      mothodNameValid: '方法名或sql语句',
      mothodName: '方法',
      isStatic: '静态方法',
      paramesValid: '传递的参数',
      parames: '参数',
      isRunning: '运行中',
      run: 'Run'
    }
  }
}
