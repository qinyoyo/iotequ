
use iotequ3;

truncate table sys_flow_process;
truncate table sys_log;
truncate table sys_message;
truncate table sys_org;
truncate table sys_role;
truncate table sys_task;
truncate table sys_user;
truncate table oauth_client_details;

insert into iotequ3.sys_org(org_code,name,parent,phone,fax,address,role_list) select org_code,name,parent,phone,fax,address,role_list from iotequ.sys_org;
insert into iotequ3.sys_role(id,code,name,note) select id,code,name,note from iotequ.sys_role;
insert into iotequ3.sys_user(id,name,password,real_name,sex,mobile_phone,employee_no,org_code,email,wechat_openid,reg_time,locked,state,role_list,expired_time,password_expired_time,icon,birth_date,id_type,id_number,password_error_times,org_privilege) select id,name,password,real_name,sex,mobile_phone,employee_no,org_code,email,wechat_openid,reg_time,locked,state,role_list,expired_time,password_expired_time,icon,birth_date,id_type,id_number,password_error_times,org_privilege from iotequ.sys_user;
insert into iotequ3.sys_task(id,name,description,scedule_years,schedule_months,schedule_days,schedule_hours,schedule_minutes,schedule_weeks,class_name,mothod_name,is_static,parames,is_running) select id,name,description,scedule_years,schedule_months,schedule_days,schedule_hours,schedule_minutes,schedule_weeks,class_name,mothod_name,is_static,parames,is_running from iotequ.sys_task;
insert into iotequ3.oauth_client_details(client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove,expired_date,locked,enabled,decription) select client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove,expired_date,locked,enabled,decription from iotequ.oauth_client_details;
insert into iotequ3.sys_message(id,read_time,title,content,url,receiver,receiver_name,sender,sender_name,create_time,event_id) select id,read_time,title,content,url,receiver,receiver_name,sender,sender_name,create_time,event_id from iotequ.sys_message;
insert into iotequ3.sys_flow_process(id,flow_id,state0,state_name0,state1,state_name1,operator,next_operator,operation,selection,time,note) select id,flow_id,state0,state_name0,state1,state_name1,operator,next_operator,operation,selection,time,note from iotequ.sys_flow_process;



truncate table ad_adjust;
truncate table ad_approve_list;
truncate table ad_data;
truncate table ad_day_result;
truncate table ad_employee;
truncate table ad_exception;
truncate table ad_org;
truncate table ad_shift;
truncate table ad_shift_time;
truncate table ad_special_shift;
truncate table ad_special_shift_time;


insert into iotequ3.ad_org(org_code,name,parent,shift_id,hr,manager,manage_limit,deviation,float_limit,absent_limit,free_work_limit) select org_code,name,parent,shift_id,hr,manager,manage_limit,deviation,float_limit,absent_limit,free_work_limit from iotequ.ad_org;
insert into iotequ3.ad_employee(id,real_name,sex,employee_no,org_code,em_level,birth_date,overtime_minutes,is_attendance,enter_date,leave_date,shift_id) select id,real_name,sex,employee_no,org_code,em_level,birth_date,overtime_minutes,is_attendance,enter_date,leave_date,shift_id from iotequ.ad_employee;
insert into iotequ3.ad_shift(id,name,start_date,end_date,minutes_per_day,description,ad_mode) select id,name,start_date,end_date,minutes_per_day,description,ad_mode from iotequ.ad_shift;
insert into iotequ3.ad_shift_time(id,shift_id,name,week_days,start_work_time,end_work_time) select id,shift_id,name,week_days,start_work_time,end_work_time from iotequ.ad_shift_time;
insert into iotequ3.ad_special_shift(id,shift_mode,name,start_date,end_date,org_codes,sex_property,age_property0,age_property1,level_property0,level_property1,description,mode_property) select id,shift_mode,name,start_date,end_date,org_codes,sex_property,age_property0,age_property1,level_property0,level_property1,description,mode_property from iotequ.ad_special_shift;
insert into iotequ3.ad_special_shift_time(id,special_shift_id,name,start_time,end_time) select id,special_shift_id,name,start_time,end_time from iotequ.ad_special_shift_time;
insert into iotequ3.ad_adjust(id,employee,org_code,hr,adjust_type,register_time,start_time,end_time,description,state,approver,approve_org) select id,employee,org_code,hr,adjust_type,register_time,start_time,end_time,description,state,approver,approve_org from iotequ.ad_adjust;
insert into iotequ3.ad_approve_list(id,adjust_id,state0,state1,approve_time,approver,approve_note) select id,adjust_id,state0,state1,approve_time,approver,approve_note from iotequ.ad_approve_list;
insert into iotequ3.ad_data(id,employee_no,rec_time,rec_type,org_code,rec_source,is_used) select id,employee_no,rec_time,rec_type,org_code,rec_source,is_used from iotequ.ad_data;
insert into iotequ3.ad_day_result(id,org_code,org_name,employee,employee_no,real_name,ad_date,shift_name,state,state_name,times,minutes,work_minutes,shift_id) select id,org_code,org_name,employee,employee_no,real_name,ad_date,shift_name,state,state_name,times,minutes,work_minutes,shift_id from iotequ.ad_day_result;
insert into iotequ3.ad_exception(id,shift_id,start_date,end_date,week_day,description) select id,shift_id,start_date,end_date,week_day,description from iotequ.ad_exception;


truncate table dev_auth_group;
truncate table dev_event;
truncate table dev_org_group;
truncate table dev_people;
truncate table dev_people_group;
truncate table dev_reader;
truncate table dev_reader_group;
truncate table dev_user_no;
truncate table dev_vein_info;

insert into iotequ3.dev_auth_group(id,name,auth) select id,name,auth from iotequ.dev_auth_group;
insert into iotequ3.dev_org_group(id,group_id,org_id,is_include_sub_org) select id,group_id,org_id,is_include_sub_org from iotequ.dev_org_group;
insert into iotequ3.dev_people_group(id,group_id,user_no) select id,group_id,user_no from iotequ.dev_people_group;

insert into iotequ3.dev_auth_role(id,name) select id,name from iotequ.dev_reader_group;
insert into iotequ3.dev_auth_config(role_id,start_time,end_time,only_work_day,auth) select id, auth_start,auth_end,is_only_work_day,org_auth from iotequ.dev_reader_group;
insert into iotequ3.dev_reader_group(id,name,parent,org_code,org_auth,sub_org_auth) select id,name,parent,org_code,id,id from iotequ.dev_reader_group;
insert into iotequ3.dev_event(id,dev_no,user_no,time,status,org_code) select id,dev_no,user_no,time,status,org_code from iotequ.dev_event;
insert into iotequ3.dev_people(dev_password,real_name,sex,mobile_phone,employee_no,org_code,email,valid_date,expired_date,icon,birth_date,id_type,id_number,user_no,user_type,register_type,reg_time,note,card_no,reg_fingers,id_nation) select dev_password,real_name,sex,mobile_phone,employee_no,org_code,email,valid_date,expired_date,icon,birth_date,id_type,id_number,user_no,user_type,register_type,reg_time,note,card_no,reg_fingers,idNation from iotequ.dev_people;
insert into iotequ3.dev_reader(id,reader_no,name,type,firmware,address,reader_group,ip,connect_type,is_online,is_time_sync,dev_mode,align_method,blacklight_time,voiceprompt,menu_time,wengenform,wengen_output,wengen_out_area,regfinger_out_time,authfinger_out_time) select id,reader_no,name,type,firmware,address,reader_group,ip,connect_type,is_online,is_time_sync,dev_mode,align_method,blacklight_time,voiceprompt,menu_time,wengenform,wengen_output,wengen_out_area,regfinger_out_time,authfinger_out_time from iotequ.dev_reader;
insert into iotequ3.dev_user_no(id,id_type,id_no,user_no,name,photo) select id,id_type,id_no,user_no,name,photo from iotequ.dev_user_no;
insert into iotequ3.dev_vein_info(id,user_no,finger_no,templates,warning,finger_type) select id,user_no,finger_no,templates,warning,finger_type from iotequ.dev_vein_info;


truncate table pm_people;
truncate table pm_people_group;
truncate table pm_project;
truncate table pm_version_application;



insert into iotequ3.pm_people_group(id,parent,name,group_type,enabled,description) select id,parent,name,group_type,enabled,description from iotequ.pm_people_group;
insert into iotequ3.pm_people(id,group_id,user_id) select id,group_id,user_id from iotequ.pm_people;
insert into iotequ3.pm_project(id,flow_state,flow_register_time,flow_register_by,name,type,customer,market_size,human_cost,material_cost,code,description,flow_note,add_file,flow_next_operator,flow_copy_to_list) select id,flow_state,flow_register_time,flow_register_by,name,type,customer,market_size,human_cost,material_cost,code,description,flow_note,add_file,flow_next_operator,flow_copy_to_list from iotequ.pm_project;
insert into iotequ3.pm_version_application(id,flow_state,flow_register_time,flow_register_by,project,application_type,customer,licence,contract_no,description,version_info,flow_note,add_file,flow_next_operator,flow_copy_to_list) select id,flow_state,flow_register_time,flow_register_by,project,application_type,customer,licence,contract_no,description,version_info,flow_note,add_file,flow_next_operator,flow_copy_to_list from iotequ.pm_version_application;


truncate sys_data_dict;
truncate sys_menu;
INSERT INTO `sys_data_dict` (`id`, `dict`, `code`, `text`, `order_num`) VALUES (1,'sys_id_type','1','system.message.idId',1),(2,'sys_id_type','2','system.message.idPhone',2),(3,'sys_id_type','3','system.message.idEmail',3),(4,'sys_id_type','4','system.message.idDri',4),(5,'sys_id_type','5','system.message.idPp',5),(6,'sys_id_type','6','system.message.idArmy',6),(7,'sys_id_type','7','system.message.idWork',7),(8,'sys_id_type','8','system.message.idStudent',8),(9,'sys_id_type','9','system.message.idOldman',9),(10,'sys_id_type','10','system.message.idLetter',10),(11,'sys_id_type','11','system.message.idOther',11),(12,'sys_operation_selection','approve','system.action.approve',1),(13,'sys_operation_selection','deny','system.action.deny',2),(14,'sys_operation_selection','close','system.action.close',3),(15,'sys_sex','1','system.message.male',1),(16,'sys_sex','0','system.message.female',2),(17,'sys_week','0','system.time.weekSu',1),(18,'sys_week','1','system.time.weekMo',2),(19,'sys_week','2','system.time.weekTu',3),(20,'sys_week','3','system.time.weekWe',4),(21,'sys_week','4','system.time.weekTh',5),(22,'sys_week','5','system.time.weekFr',6),(23,'sys_week','6','system.time.weekSa',7),(24,'dev_access_state','0','通过',1),(25,'dev_access_state','1','拒绝',2),(26,'dev_access_state','2','胁迫',3),(27,'dev_auth_type','1','指静脉',1),(28,'dev_auth_type','2','卡+指静脉',2),(29,'dev_auth_type','3','卡 或 指静脉',3),(30,'dev_auth_type','4','服务器验证',4),(31,'dev_auth_type','5','密码+指静脉',5),(32,'dev_auth_type','6','密码或指静脉',6),(33,'dev_auth_type','7','密码或指静脉或卡',7),(34,'dev_auth_type','8','指静脉或(卡+密码)',8),(35,'dev_connect_type','TCP','TCP',1),(36,'dev_connect_type','UDP','UDP',2),(37,'dev_mode','MJ','门禁',1),(38,'dev_mode','XG','巡更',2),(39,'dev_mode','AD','签到',3),(40,'dev_mode','ON','上班',4),(41,'dev_mode','OFF','下班',5),(42,'dev_permit_mode','0','未授权',1),(43,'dev_permit_mode','1','需要审核',2),(44,'dev_permit_mode','2','审核权限',3),(45,'dev_permit_mode','4','授权',4),(46,'dev_permit_mode','14','工作日授权',5),(47,'dev_register_type','1','PC',1),(48,'dev_register_type','2','D10',2),(49,'dev_register_type','3','U53',3),(50,'dev_register_type','4','C20',4),(51,'dev_type','D10','D10',1),(52,'dev_type','U53','U53',3),(53,'dev_type','C20','C20',4),(54,'dev_wg_form','1','26',1),(55,'dev_wg_form','2','34',2),(56,'dev_wg_form','3','自定义',3),(57,'dev_wg_out','1','固定',1),(58,'dev_wg_out','2','编号',2),(59,'dev_wg_out','3','卡号',3),(60,'dev_wg_out','4','区+编号',4),(61,'ad_shift_name','早','早班',6),(62,'ad_shift_name','中','中班',1),(63,'ad_shift_name','晚','晚班',3),(64,'ad_shift_name','白','白班',4),(65,'ad_shift_name','夜','夜班',5),(69,'cg_modules','framework','基本框架',1),(70,'cg_modules','codegenerator','代码生成',2),(71,'cg_modules','attendance','考勤',3),(72,'cg_modules','reader','设备管理',4),(73,'cg_modules','svas','集成服务器验证',5),(74,'cg_modules','SvasClient','验证服务客户端',6),(7523,'pm_project_state','1','待审核',1),(7524,'pm_project_state','2','已评估',2),(7525,'pm_project_state','3','已评审',3),(7526,'pm_project_state','4','待决策',4),(7527,'pm_project_state','5','立项完成',5),(7528,'pm_project_state','6','研发中',6),(7529,'pm_project_state','7','测试中',7),(7530,'pm_project_state','8','已发布',8),(7531,'pm_project_state','20','否决',9),(7532,'pm_project_state','21','废弃',10),(7533,'pm_group_type','MARKET','市场',1),(7534,'pm_group_type','CMO','市场总监',2),(7535,'pm_group_type','R&D','研发团队',3),(7536,'pm_group_type','PM','项目主管',4),(7537,'pm_group_type','CTO','技术总监',5),(7538,'pm_group_type','CEO','决策',6),(7539,'pm_product_type','1','新市场项目',1),(7540,'pm_product_type','2','客户维护项目',2),(7541,'pm_product_type','3','在研项目',3),(7542,'pm_product_type','4','预研项目',4),(7543,'pm_product_type','5','版本演进',5),(7544,'pm_va_type','1','合同项目',1),(7545,'pm_va_type','2','试验项目',2),(7546,'pm_va_type','3','工程演示',3),(7547,'pm_va_type','4','合作研发',4),(7548,'pm_va_type','5','其他',5),(7549,'pm_group_type','VM','版本管理',7),(7550,'pm_product_type','6','已发布版本',6),(7551,'dev_type','D10-DG','D10-DG',2);
INSERT INTO `sys_menu` (`id`, `sort_num`, `name`, `parent`, `is_divider`, `icon`, `action`, `class_name`, `data_action`, `bigIcon`, `mobile_hidden`, `js_cmd`) VALUES (1,10,'menu.systemManage',NULL,0,'fa fa-gear',NULL,'','','',0,''),(2,130,'sysMenu.title.code',1,0,'fa fa-bars','/framework/sysMenu/list',NULL,NULL,'',0,NULL),(3,70,'sysRole.title.code',1,0,'fa fa-chrome','/framework/sysRole/list',NULL,NULL,'',0,NULL),(4,110,'sysAction.title.code',1,0,'fa fa-qrcode','/framework/sysAction/list',NULL,NULL,'',1,NULL),(7,90,'sysOrg.title.code',1,0,'fa fa-sitemap','/framework/sysOrg/list',NULL,NULL,'',0,NULL),(8,100,'sysUser.title.code',1,0,'fa fa-user-plus','/framework/sysUser/list',NULL,NULL,'',0,NULL),(9,80,'sysDataDict.title.code',1,0,'fa fa-book','/framework/sysDataDict/list','','','',1,''),(10,140,'sysLog.title.code',1,0,'fa fa-file-text-o','/framework/sysLog/list','','','',0,''),(11,150,'sysTask.title.code',1,0,'fa fa-clock-o','/framework/sysTask/list','','','',1,''),(20,20,'考勤管理',NULL,0,'fa fa-address-card-o','','','','',0,''),(21,160,'人事管理',20,0,'fa fa-user-plus','','','','',0,''),(22,170,'人事调整',20,0,'fa fa-pencil-square-o','','','','',0,''),(23,180,'adData.title.code',20,0,'el-icon-s-data','/attendance/data/adData/list','','','',0,''),(24,190,'adDayResult.title.code',20,0,'fa fa-bar-chart','/attendance/dayresult/adDayResult/list','','','',0,''),(30,200,'adShift.title.code',21,0,'','/attendance/shift/adShift/list','','','',0,''),(31,220,'adOrg.title.code',21,0,'','/attendance/org/adOrg/list','','','',0,''),(32,210,'adSpecialShift.title.code',21,0,'','/attendance/specialshift/adSpecialShift/list','','','',0,''),(33,240,'adAdjust.title.code',22,0,'fa fa-file-text','/attendance/adjust/adAdjust/list','','','',0,''),(40,230,'申请单',22,1,'fa fa-plane','/attendance/adjust/adAdjust/record','','','',0,''),(102,280,'设备管理',103,0,'fa fa-tv','/reader/devReaderGroup/list','','','',0,''),(103,40,'设备与用户',NULL,0,'fa fa-user-circle','','','','',0,''),(104,250,'注册人员管理',103,0,'fa fa-address-book-o','/reader/devPeople/list','','','',0,''),(105,260,'设备事件浏览',103,0,'fa fa-calendar-check-o','/reader/devEvent/list','','','',0,''),(106,270,'设备授权',103,0,'fa fa-user-plus','/reader/devAuthGroup/list','','','',0,''),(108,30,'项目管理',NULL,0,'fa fa-group','','','','',0,''),(109,300,'pmProject.title.code',108,0,'fa fa-magic','/project/product/pmProject/list','','','',0,''),(110,290,'pmPeopleGroup.title.code',108,0,'fa fa-user-circle','/project/people/pmPeopleGroup/list','','','',0,''),(113,310,'pmVersionApplication.title.code',108,0,'fa fa-download','/project/version/pmVersionApplication/list','','','',0,''),(114,50,'电子支付',NULL,0,'el-icon-document','','','','',0,''),(115,340,'sysUser.title.code',119,0,'fa fa-shopping-bag',NULL,'','','',0,''),(116,350,'账单查询',119,0,'fa fa-bitcoin','/ewBill/list','','','',0,''),(117,360,'计次项目维护',119,0,'fa fa-stack-overflow','/ewCountProject/list','','','',0,''),(118,370,'计时项目维护',119,0,'fa fa-spinner','/ewTimeProject/list','','','',0,''),(119,320,'电子钱包',114,0,'fa fa-behance-square','','','','',0,''),(120,330,'支付运营管理',114,0,'fa fa-university','','','','',0,''),(121,380,'运营公司管理',120,0,'fa fa-tty','/payCorporation/list','','','',0,''),(122,390,'商店管理',120,0,'fa fa-cart-plus','/payShop/list','','','',0,''),(123,400,'pos机具管理',120,0,'fa fa-desktop','/payPos/list','','','',0,''),(124,410,'收银员管理',120,0,'fa fa-user-circle','/payOperator/list','','','',0,''),(125,420,'终端签到浏览',120,0,'fa fa-wpforms','/payLogin/list','','','',0,''),(126,120,'sysOauthClientDetails.title.code',1,0,'fa fa-user-circle-o','/framework/sysOauthClientDetails/list','','','',1,''),(130,60,'代码生成',NULL,0,'fa fa-pencil','','hidden-xs-down','','',0,''),(131,430,'cg项目维护',130,0,'fa fa-plus-circle','/codegenerator/cgProject/list','','','',0,''),(132,440,'代码定义',130,0,'fa fa-edit','/codegenerator/cgTable/list','','','',0,''),(200,460,'Icons',130,0,'el-icon-magic-stick','/icon/index',NULL,NULL,NULL,1,NULL),(203,450,'代码下载',130,0,'fa fa-cloud-download fa-fw','/codegenerator/cgProject/action/down',NULL,'{request: {timeout: 20000,responseType: \"blob\"}, fileName: \"cg.zip\"}',NULL,0,'request'),(204,470,'测试',130,1,NULL,'/codegenerator/cgTest/list',NULL,NULL,NULL,0,NULL),(205,10,'设备数据维护',103,0,'svg-guide','/reader/devData/record',NULL,NULL,NULL,1,NULL),(206,200,'adStat.title.statByOrg',20,0,'svg-chart/chart-rose','/attendance/dayresult/adDayResult/statByOrg',NULL,NULL,NULL,0,NULL),(207,220,'adStat.title.statRecTime',20,0,'svg-chart/chart-scatter','/attendance/dayresult/adDayResult/statRecTime',NULL,NULL,NULL,0,NULL);
