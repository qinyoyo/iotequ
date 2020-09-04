<#assign D = "$" />
<#assign J = "#" />
${J}端口以及主目录路径   
server:
   port : 80
   servlet: 
      context-path : /
      session:
         timeout: PT1H
${J}配置ssl请将 server.port 配置 为 443，然后配置以下参数
${J}   http:
${J}      port : 80       
${J}   ssl:
${J}      key-store: webapp/ssl_www.svein.com.cn.pfx
${J}      key-store-password: xipYs081
${J}      keyStoreType: PKCS12
spring:
${J}数据库连接配置
   datasource:
      url : jdbc:mysql://localhost:3306/iotequ3?characterEncoding=utf8&useSSL=true&allowMultiQueries=true&serverTimezone=Asia/Chongqing
      username : root
      password : root
      driverClassName : com.mysql.cj.jdbc.Driver
      
${J}      url : jdbc:sqlserver://localhost:1433;databaseName=iotequ
${J}      driverClassName : com.microsoft.sqlserver.jdbc.SQLServerDriver
${J}      username : sa
${J}      password : root

${J}      url : jdbc:oracle:thin:@127.0.0.1:1521:iotequ
${J}      driverClassName : oracle.jdbc.driver.OracleDriver
${J}      username : sa
${J}      password : root      

${J}文件上传保存地址，大小限制
   upload-path: web/uploadfiles
   servlet:
      multipart:
         maxFileSize: 100MB
         maxRequestSize: 100MB   

${J}日志输出
logging: 
   file: log/iotequ.log
   level: 
      root: error
      org.springframework: error
      org.mybatis: error
      top.iotequ: error
${J}mybatis输出sql语句
${J}mybatis:
${J}   configuration: 
${J}      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl 
     
<#if gp.modules?? &&  gp.modules?index_of ('codegenerator')  gte 0 >
generator:
   path: D:/iotequ/web/generator/3.0.0
   org: top.iotequ
   project : ewallet
   codepath : E:/J2EE/iotequ/iotequ-3.0.0/
   nosamplecode : true
</#if>
${J}授权，svas配置    
svas:
<#if gp.modules?? &&  gp.modules?index_of ('reader')  gte 0 >
   sn_dev_reader : 9908-AF99-2E68-4C95-6CA1-BEDE-0879-747C
</#if>
<#if gp.modules?? &&  gp.modules?index_of ('attendance')  gte 0 >
   sn_ad_employee : D01D-42AE-B6CB-054A-3773-26E4-4027-A47E
</#if>
<#if gp.modules?? &&  gp.modules?index_of ('project')  gte 0 >
   sn_pm_people_group : 8D00-86B6-3962-5D9D-6C8C-BDC9-1153-7669
   sn_pm_people : 6547-E9D1-0CED-B9FA-3C55-1EE5-D9EA-372B
   sn_pm_project : 8D00-86B6-2E62-4794-6AA7-AAD6-2156-7476
</#if>
<#if gp.modules?? &&  gp.modules?index_of ('svas')  gte 0 >
   sn : B179-1DCB-DBBB-6925-4E16-4385-2478-C113
${J}   matchTestSyncSeconds: 0  ${J} 打开验证测试并设置同步等待时间
   matchTrace: false        ${J} 验证跟踪
   thresh: 380              ${J} 默认阈值
   userNoPrefix : "023"     ${J} userNo 前缀
   userNoMminLength: 10     ${J} userNo 最小长度
   datasource:
      host: localhost
      port: 3306
      user: root
      password: root
      schema: iotequ3

   adjust:
      autoUpdateTemplate: false   ${J} 自动用验证数据更新字典
      templatesMustMatch: false   ${J} 插入模板三个词典必须匹配
      multiMatchedWeight: 0       ${J} 相同模板的多个词典多次匹配增加权重
      maxMatchedWeight: 0         ${J} 最大匹配满足条件增加权重
      maxMatchedCondition: 100    ${J} 最大匹配条件（差值必须大于该值)
${J}   url: https://www.svein.com.cn
${J}   client_id: svas
${J}   client_secret: e10adc3949ba59abbe56e057f20f883e
${J}   scope: api   
</#if>
${J}微信授权登录 
${J}wechat:
${J}   loginUrl : http://www.svein.com.cn/login/wechat
${J}   checkUrl : http://www.svein.com.cn/res/checkopenid
${J}   appid : your appid
${J}   appSecret : your appSecret

${J}使用短消息的模块
${J}sms:
${J}   modules: login,project
   register:
      whilteList:
        ${J}允许注册的手机号码，不输入该参数表示全部允许
        ${J}  sql=输入sql语句，语句中包含一个?参数，表示需要查询的号码，存在结果集则允许，否则不允许。
        ${J}  或者输入正则表达式，匹配则允许
      blackList:
        ${J}禁止注册的手机号码，不输入该参数表示全部允许
        ${J}  sql=输入sql语句，语句中包含一个?参数，表示需要查询的号码，存在结果集则禁止，否则允许。
        ${J}  或者输入正则表达式，匹配则禁止
      defaultSql:
        ${J}设置缺省值参数的sql语句

${J}短信消息配置
${J}aliyunsms:
${J}   accessKeyId : your access KeyId
${J}   secret : your secret
${J}   signName : 智脉科技
${J}   templateCode : SMS_169642214
${J}   msgTemplateCode : SMS_169865398
<#if gp.modules?? && (','+gp.modules)?index_of(',reader') gte 0>
${J}reader配置
config:
   D10port: 87   ${J}D10连接的端口地址
   serverIp: 127.0.0.1
   logger:  DEBUG  ${J}DEBUG,ERROR
   serverurl : http://localhost
   startflag: 1  ${J}启动项目  0:启动所有，1:启动 D10项目， 2:启动锁项目
U53:
   connectUrl: localhost:9000
</#if>






  