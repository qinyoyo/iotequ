set sql_safe_updates=0;
delete FROM svas.dev_user_no where user_no not in (select user_no from svas.dev_vein_info);