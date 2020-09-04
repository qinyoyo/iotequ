export default {
  sysOauthClientDetails: {
    title: {
      list: 'OAuth2配置表',
      record: 'OAuth2配置',
      code: 'OAuth2客户端配置'
    },
    route: {
      listTag: 'OAuth2配置表',
      recordTag: 'OAuth2配置'
    },
    field: {
      clientId: 'client_id',
      clientSecret: 'client_secret',
      scopeValid: '逗号分隔多个scope',
      scope: 'scope',
      authorizedGrantTypes_0: '密码模式',
      authorizedGrantTypes_1: '授权码模式',
      authorizedGrantTypes_2: '简化模式',
      authorizedGrantTypes_3: '客户端模式',
      authorizedGrantTypes: '认证类型',
      webServerRedirectUriValid: '允许的重定向地址序列，请求中的地址必须完全匹配该序列的一个',
      webServerRedirectUri: 'redirect_url',
      authorities: '权限集',
      accessTokenValidity: 'token有效时间',
      refreshTokenValidity: '刷新时间秒',
      autoapprove_0: '',
      autoapprove_1: '',
      autoapprove: '自动授权',
      expiredDate: '过期时间',
      locked: '锁定',
      enabled: '激活',
      decription: '描述',
      additionalInformationValid: 'json格式字符串',
      additionalInformation: '附加属性(json)',
      resourceIds: '',
      secret: 'secret',
      authUrl: 'OAuth请求url'
    }
  }
}
