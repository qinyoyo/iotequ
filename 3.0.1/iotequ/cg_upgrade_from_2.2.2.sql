use iotequ3;

INSERT INTO `cg_project` (id,creator,code,name,group_id,version,modules,maven_server) 
SELECT distinct concat(creator,'.',left(name,POSITION('_' IN name)-1)) as id, creator, left(name,POSITION('_' IN name)-1), left(name,POSITION('_' IN name)-1),
'top.iotequ','3.0.0-SNAPSHOT','framework','http://192.168.1.120:8081'
 FROM iotequ.cg_table where name is not null and name !='' and POSITION('_' IN name)<5;


insert into cg_table (id,code,name,title,project_id,template,creator,action_list,
                      sub_package,entity,imports,trial_licence,trial_days,pojo_java_code,pojo_imports,pojo_extends,controller_extends )
   select id,code,name,title,concat(creator,'.',left(name,POSITION('_' IN name)-1)),'vue-element',creator,action_list,
      sub_package,entity,imports,trial_licence,trial_days,pojo_java_code,pojo_imports,pojo_extends,controller_extends from iotequ.cg_table 
      where name is not null and name !='' and POSITION('_' IN name)<5 and POSITION('_' IN name)>2;

insert into cg_table (id,code,name,title,project_id,template,creator,action_list,
                      sub_package,entity,imports,trial_licence,trial_days,pojo_java_code,pojo_imports,pojo_extends,controller_extends )
   select id,code,name,title,concat(creator,'.',left(code,POSITION('_' IN code)-1)),'vue-element',creator,action_list,
      sub_package,entity,imports,trial_licence,trial_days,pojo_java_code,pojo_imports,pojo_extends,controller_extends from iotequ.cg_table 
      where (name is null or name = '') and POSITION('_' IN code)<5 and POSITION('_' IN code)>2;      
      
    
insert into cg_field (id,order_num,entity_name,title,table_id,show_type,type,key_type,is_null,dict_multiple,dict_full_name,length,
name,formatter,valid_expression,valid_title,dict_table,dict_field,dict_text,dyna_condition,dict_parent,dict_parent_field,numeric_precision,numeric_scale,default_value,fk_table,fk_column,fk_on_delete,fk_on_update,default_query_value)
select id,order_num,entity_name,title,table_id,show_type,type,key_type,is_null,dict_multiple,dict_full_name,length,
name,formatter,valid_expression,valid_title,dict_table,dict_field,dict_text,dyna_condition,dict_parent,dict_parent_field,numeric_precision,numeric_scale,default_value,fk_table,fk_column,fk_on_delete,fk_on_update,default_query_value
 from iotequ.cg_field where table_id in (select id from cg_table);

update iotequ3.cg_field set dict_table = left(dict_table,POSITION(':' IN dict_table)-1) where (show_type like '%join' or show_type='dict_list') and dict_table not in (select id from cg_list) and POSITION(':' IN dict_table)>1;

insert into cg_button (id,table_id,order_num,action,title,icon,action_property,row_property,is_refresh_list,append_class,display_properties,confirm_text)
select id,table_id,order_num,action,title,icon,action_property,row_property,is_refresh_list,append_class,display_properties,confirm_text 
  from iotequ.cg_button where table_id in (select id from cg_table);

insert into cg_list (id,name,table_id,head_title,tag_title,edit_inline,detail_inline,generator_type,show_search,toolbar_mode,pagination,
path,son_align,multiple,max_height,icon,sons,son_fields,title_field,parent_entity,tree_show_entity,order_by,images)
select id,entity,id,title,title,edit_inline,detail_inline,50,show_search,toolbar_mode,pagination,
'list','h',0,0,icon,sons,son_fields,title_field,parent_entity,tree_show_entity,order_by,list_images
 from iotequ.cg_table where id in (select id from cg_table);

insert cg_list_field (list_id,order_num,entity_field,query_mode,align,width,hidden,edit_inline,default_query_value)
select table_id,order_num,entity_name,0,'left',0,0,edit_inline,default_query_value 
  from iotequ.cg_field where is_show_list = 1 and table_id in (select id from cg_table);

insert cg_form (id,name,table_id,head_title,tag_title,path,is_flow,label_position,is_dialog,continue_add,icon,images)
select id,entity,id,title,title,'record',0,'top',is_dialog_mode,is_continue_add,icon,update_images 
  from iotequ.cg_table where id in (select id from cg_table);

insert cg_form_field (form_id,order_num,entity_field, width,readonly,must_input,hidden,validate_as_title,icon,href)
select table_id,order_num,entity_name, 2*col_width,is_readonly,must_input,0,1,fa_icon,href 
  from iotequ.cg_field where is_show=1 and table_id in (select id from cg_table);

update iotequ3.cg_field f, iotequ3.cg_list l set f.dict_table = l.id 
 where (f.show_type like '%join' or f.show_type='dict_list') and f.dict_table not in (select id from cg_list)
   and l.id like concat('%',f.dict_table);
   
   
