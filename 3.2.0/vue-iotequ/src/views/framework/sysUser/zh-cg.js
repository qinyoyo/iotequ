export default {
  sysUser: {
    title: {
      list: '用户表',
      join: '用户表',
      record: '用户',
      groupUserIcon: '基本信息',
      groupUserMobilePhone: '联系信息',
      groupUserRoleList: '状态信息',
      register: '新用户注册',
      code: '用户'
    },
    action: {
      changePassword: '修改密码',
      resetPasswordConfirm: '确实需要将用户密码复位吗',
      resetPassword: '复位密码'
    },
    field: {
      id: 'uuid主键',
      icon: '头像',
      nameValid: '非空白字符开始和结束的字符串',
      name: '用户名',
      realName: '真实名',
      sex: '性别',
      birthDate: '生日',
      regTime: '注册时间',
      mobilePhone: '手机号码',
      email: '邮箱',
      wechatOpenid: '微信openId',
      orgCode: '部门',
      orgPrivilegeValid: '可以浏览该部门及其子部门的数据',
      orgPrivilege: '部门权限',
      roleListValid: '只有超级管理员才能赋权',
      roleList: '角色序列',
      locked: '被锁定',
      state: '激活',
      idType: '证件类型',
      idNumber: '证件号码',
      expiredTime: '账号过期时间',
      passwordExpiredTime: '密码过期时间',
      password: '密码',
      passwordErrorTimes: '',
      htmlNoteValid: '<p class="text-warning">只有具有超级管理员角色权限才能添加和修改权限</p>',
      htmlNote: '提示'
    }
  }
}
