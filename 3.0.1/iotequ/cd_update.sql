set sql_safe_updates = 0;
truncate table iotequ3.sys_role;
insert into  iotequ3.sys_role SELECT * FROM iotequ_simp.sys_role;

truncate table iotequ3.sys_org;
insert into  iotequ3.sys_org SELECT * FROM iotequ_simp.sys_org;

truncate table iotequ3.oauth_client_details;
insert into  iotequ3.oauth_client_details SELECT * FROM iotequ_simp.oauth_client_details;

truncate table iotequ3.sys_user;
insert into  iotequ3.sys_user SELECT id,icon,name,real_name,sex,birth_date,reg_time,mobile_phone,email,wechat_openid,org_code,org_privilege,role_list,locked,state,id_type,id_number,expired_time,password_expired_time,password,password_error_times
 FROM iotequ_simp.sys_user;

delete from iotequ3.dev_reader_group;
insert into  iotequ3.dev_reader_group (name,org_code) SELECT name,org_code FROM iotequ_simp.sys_org;

delete from iotequ3.dev_reader;
update iotequ_simp.dev_c20 set sn_no = dev_no where sn_no is null;
insert into  iotequ3.dev_reader (id,reader_no,name,sn_no,type,reader_group,connect_type,ip,dev_mode)
 SELECT replace(uuid(),'-',''),dev_no,sn_no,sn_no,'C20',g.id,'HTTP','192.168.1.1','AD' FROM iotequ_simp.dev_c20 d, iotequ3.dev_reader_group g where d.org_code=g.org_code; 

truncate table iotequ3.dev_event;
insert into  iotequ3.dev_event (id,dev_type,dev_no,org_code,user_no,status,time) SELECT id,'U53',dev_no,org_code,user_no,status,time
 FROM iotequ_simp.dev_event;

truncate table iotequ3.dev_people;
insert into  iotequ3.dev_people 
SELECT user_no,real_name,sex,birth_date,org_code,null,card_no,id_type,id_number,user_type,mobile_phone,email,register_type,valid_date,expired_date,reg_time,dev_password,reg_fingers,note,'汉',null,null
 FROM iotequ_simp.dev_people;

truncate table iotequ3.dev_user_no;
insert into  iotequ3.dev_user_no 
SELECT u.id,u.user_no,p.real_name,p.sex,p.birth_date,p.mobile_phone,p.email,null,u.id_type,u.id_no,p.id_nation,null,p.home_addr,p.valid_date,
  p.expired_date,u.reg_time,p.photo
 FROM iotequ_simp.dev_user_no u, iotequ3.dev_people p where p.user_no = u.user_no;

truncate table iotequ3.dev_vein_info;
insert into  iotequ3.dev_vein_info SELECT id,user_no,finger_no,templates,warning,finger_type
 FROM iotequ_simp.dev_vein_info;
 
truncate table iotequ3.ck_sign_in; 
insert into iotequ3.ck_sign_in SELECT replace(uuid(),'-',''),p.user_no,p.org_code,
  d.rec_source,'U53',2,rec_time FROM iotequ_simp.ad_data d, iotequ_simp.dev_people p where d.employee_no = d.employee_no; 
  
truncate table iotequ3.ck_register;
insert into iotequ3.ck_register(id,org_code,user_no,in_time,out_time) 
SELECT replace(uuid(),'-',''),org_code, user_no, min(rec_time),max(rec_time) FROM iotequ3.ck_sign_in group by org_code, user_no, date(rec_time);  