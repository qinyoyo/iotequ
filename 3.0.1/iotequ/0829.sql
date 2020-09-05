ALTER TABLE `iotequ3`.`dev_people` 
DROP COLUMN `employee_no`,
DROP INDEX `dev_4eno` ;

ALTER TABLE `iotequ3`.`sys_user` 
DROP COLUMN `employee_no`;

ALTER TABLE `iotequ3`.`ad_org` 
DROP COLUMN `name`,
DROP COLUMN `parent`;

ALTER TABLE `iotequ3`.`ad_employee` 
DROP COLUMN `real_name`,
DROP COLUMN `sex`,
DROP COLUMN `org_code`,
DROP COLUMN `birth_date`;