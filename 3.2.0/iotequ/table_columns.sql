set @db = 'iotequ3';
set @t = 'dev_people';
select group_concat(column_name separator ',') FROM `information_schema`.`columns` 
where TABLE_SCHEMA = @db  and TABLE_NAME = @t
/*
select group_concat(column_name separator ',') FROM `information_schema`.`columns` 
where TABLE_SCHEMA = @db  and TABLE_NAME = @t  and IS_NULLABLE = 'NO' and COLUMN_DEFAULT is null 
union
select group_concat(column_name separator ',') FROM `information_schema`.`columns` 
where TABLE_SCHEMA = @db  and TABLE_NAME = @t  and IS_NULLABLE = 'NO' and COLUMN_DEFAULT is not null
union
select group_concat(column_name separator ',') FROM `information_schema`.`columns` 
where TABLE_SCHEMA = @db  and TABLE_NAME = @t  and IS_NULLABLE = 'YES' and COLUMN_DEFAULT is not null
union
select group_concat(column_name separator ',') FROM `information_schema`.`columns` 
where TABLE_SCHEMA = @db  and TABLE_NAME = @t  and IS_NULLABLE = 'YES' and COLUMN_DEFAULT is null
*/