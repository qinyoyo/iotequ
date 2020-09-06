set sql_safe_updates=0;

ALTER TABLE `iotequ3`.`sys_user` 
DROP COLUMN `employee_no`,
DROP INDEX `employee_no` ;

ALTER TABLE `iotequ3`.`dev_people` 
DROP COLUMN `employee_no`,
DROP INDEX `dev_4eno` ;


ALTER TABLE `iotequ3`.`ad_org` 
DROP COLUMN `parent`,
DROP COLUMN `name`;


ALTER TABLE `iotequ3`.`ad_employee` 
DROP COLUMN `birth_date`,
DROP COLUMN `org_code`,
DROP COLUMN `sex`,
DROP COLUMN `real_name`;

ALTER TABLE `iotequ3`.`ad_data` 
DROP COLUMN `org_code`;

delete  FROM iotequ3.ad_employee where id not in (select id from sys_user);
delete  FROM iotequ3.ad_org where org_code not in (select org_code from sys_org);

ALTER TABLE `ad_org` ADD CONSTRAINT `fk_ad_org_org_code_sys_org_org_code` FOREIGN KEY(`org_code`) REFERENCES `sys_org`(`org_code`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `ad_employee` ADD CONSTRAINT `fk_ad_employee_id_sys_user_id` FOREIGN KEY(`id`) REFERENCES `sys_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `iotequ3`.`dev_reader` 
CHANGE COLUMN `blacklight_time` `blacklight_time` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '背光时间' ,
CHANGE COLUMN `menu_time` `menu_time` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '菜单时间' ;

ALTER TABLE `iotequ3`.`dev_event` 
ADD COLUMN `dev_type` VARCHAR(45) NOT NULL DEFAULT 'D10' COMMENT '设备类别' AFTER `id`;

ALTER TABLE `iotequ3`.`ad_data` 
ADD COLUMN `rec_source_type` VARCHAR(45) NOT NULL DEFAULT 'D10' COMMENT '来源类别' AFTER `employee_no`;

update dev_event set dev_type = 'C20';
update dev_event e, dev_reader r set e.dev_type = r.type where e.dev_no = r.reader_no;

update ad_data set rec_source_type = 'C20';
update ad_data e, dev_reader r set e.rec_source_type = r.type where e.rec_source = r.reader_no;
