export default {
  sysMenu: {
    title: {
      list: '系统菜单表',
      record: '系统菜单',
      code: '系统菜单'
    },
    route: {
      listTag: '系统菜单表',
      recordTag: '系统菜单'
    },
    field: {
      id: '',
      sortNum: '排列顺序',
      nameValid: '请使用非空白字符开始和结束的显示字符串或多语言定义串',
      name: '名称',
      parent: '上级菜单',
      isDivider: '分割线',
      icon: '图标',
      action: '功能地址',
      className: '附加类名',
      dataActionValid: 'json表达式,为js操作指定参数',
      dataAction: '附加参数',
      bigicon: '大图标',
      mobileHidden: '手机隐藏',
      jsCmdValid: '指定一个js操作函数(menu-action-xxx.js中定义)，权限由路由地址决定。为空时进行路由跳转',
      jsCmd: '操作函数'
    }
  }
}
