use iotequ3;
set sql_safe_updates=0;
update dev_people set photo = concat('~/headPortrait/photo_', user_no , '_01.jpg') where photo is null;