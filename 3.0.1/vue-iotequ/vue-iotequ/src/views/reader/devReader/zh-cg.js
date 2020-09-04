export default {
  devReader: {
    title: {
      list: '终端设备表',
      record: '终端设备',
      groupDevReaderName: '设备配置',
      groupDevReaderAlignMethod: '设备参数',
      code: '终端设备表'
    },
    action: {
      queryTime: '查询设备时间',
      setDeviceTime: '设置设备时间',
      resetDeviceConfirm: '复位操作将导致设备短暂离线，确认执行吗',
      resetDevice: '复位',
      data: '设备数据维护',
      deleteAllUsersConfirm: '确定需要清除设备上的所有用户信息',
      deleteAllUsers: '删除设备上的所有用户'
    },
    route: {
      listTag: '终端设备表',
      recordTag: '终端设备'
    },
    field: {
      id: '主键',
      readerNo: '设备编号',
      name: '设备标识名',
      type: '型号',
      readerGroup: '设备组',
      address: '地点',
      connectType: '连接类型',
      ip: 'IP地址',
      devMode: '设备模式',
      firmware: '固件版本',
      isOnline: '在线',
      isTimeSyncValid: '设备时间与服务器同步',
      isTimeSync: '同步',
      alignMethod: '验证方式',
      blacklightTime_0: '10s',
      blacklightTime_1: '30s',
      blacklightTime_2: '60s',
      blacklightTime_3: '90s',
      blacklightTime_4: '常亮',
      blacklightTime: '背光时间',
      voiceprompt: '语言提示',
      menuTime_0: '10s',
      menuTime_1: '30s',
      menuTime_2: '60s',
      menuTime_3: '90s',
      menuTime: '菜单时间',
      wengenform: '韦根格式',
      wengenOutput: '韦根输出',
      wengenOutArea: '韦根输出区位码',
      regfingerOutTime: '指静脉注册超时时长',
      authfingerOutTime: '验证超时时长'
    }
  }
}
