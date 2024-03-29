<#assign D = "$" />
<#assign J = "#" />

spring:
   datasource:
      max-active : 20
      max-idle : 8
      min-idle : 8
      initial-size : 10
   http: 
      encoding:
         charset: UTF-8
         enabled: true
         force: true      
   mvc:
      static-path-pattern: /res/**
      favicon:
         enabled: false
   resources:
      static-locations: classpath:/static/
   jackson:
      time-zone: GMT+8
server: 
   tomcat:
      uri-encoding: UTF-8   
management:
   endpoints:
       web: 
          exposure:         
            include: '*'
   endpoint: 
      health: 
         show-details: always
      shutdown: 
         enabled: false    
logging: 
   file: log/iotequ.log
   level: 
      root: error
      org.springframework: error
      org.mybatis: error
      top.iotequ: error

mybatis:
   mapper-locations : classpath*:/mybatis/**/*.xml
   configuration: 
      cache-enabled : true
      map-underscore-to-camel-case: true