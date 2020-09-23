<#assign D = "$" />
<#assign J = "#" />
<#assign index = 0 />
<#assign count = 0 />
<#if forms??>
    <#list forms as f>
        <#if f.isFlow>
            <#assign count = count + f.path?split(",")?size />
        <#else>
            <#assign count = count + 1 />
        </#if>
    </#list>
</#if>
<#if lists?? >
  <#assign count = count + lists?size />
</#if>
<#macro f>
<#if f.isFlow>
  '/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/save','/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/f_${f.path}'
<#else>
  <#if table.actionList ?? && (table.actionList?index_of(",edit,") gte 0 || table.actionList?index_of(",add,") gte 0) >'/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/save',</#if><#if table.actionList ?? && (table.actionList?index_of(",edit,") gte 0 || table.actionList?index_of(",view,") gte 0) >'/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/record'</#if>
</#if>
</#macro>
<#if count gt 0>
import Layout from '@/layout'
export default {
  path: '/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}',
  name: '${generatorName}',
  component: Layout,
  meta: {
    authorities: ['/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}'],
    title: '${generatorName}.title.code',
    breadcrumb: false,
    tagView: false
  },
  children: [
<#if lists??>
  <#list lists as l>
    <#assign index = index + 1 />
    {
      path: '${l.path}',
      component: () => import('@/views/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/${l.path}'),
      name: '${l.name?cap_first}List',
      props: true,
      meta: {
        <#if l.tagTitle?? && l.tagTitle?trim !=''>
        title: '${generatorName}.route.${l.path}Tag',
        </#if>
        <#if l.icon?? && l.icon?trim !=''>
        icon: '${l.icon}',
        </#if>
        authorities: ['/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/list'],
        breadcrumb: <#if l.tagTitle?? && l.tagTitle?trim !=''>true<#else>false</#if>,
        tagView: <#if l.tagTitle?? && l.tagTitle?trim !=''>true<#else>false</#if>,
        noCache: <#if l.tagTitle?? && l.tagTitle?trim !=''>false<#else>true</#if>
      }
    }<#if index lt count>,</#if>
   </#list>
</#if>
<#if forms?? >
  <#list forms as l>
    <#if l.isFlow && l.path?split(",")?size gt 1>
    <#if l.icon?? && l.icon?trim !=''>
        <#assign icons = l.icon?split(",") />
    </#if>
    <#list l.path?split(",") as path>
    <#assign index = index + 1 />
    {
      path: '${path}',
      component: () => import('@/views/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/${l.path?split(",")[0]}'),
      name: '${l.name?cap_first}${path?cap_first}Form',
      props: true,
      meta: {
        title: '${generatorName}.title.${path}',
      <#if icons??>
        icon: <#if path_index lt icons?size>'${icons[path_index]}'<#else>'${icons[0]}'</#if>,
      </#if>
      <#if l.isDialog>
        dialog: true,
      </#if>
        authorities: ['/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/f_${path}'],
        breadcrumb: <#if !l.isDialog && l.tagTitle?? && l.tagTitle?trim !=''>true<#else>false</#if>,
        tagView: <#if !l.isDialog && l.tagTitle?? && l.tagTitle?trim !=''>true<#else>false</#if>,
        noCache: <#if !l.isDialog && l.tagTitle?? && l.tagTitle?trim !=''>false<#else>true</#if>
      }
    }<#if index lt count>,</#if>
    </#list>
    <#else>
    <#assign index = index + 1 />
    {
      path: '${l.path}',
      component: () => import('@/views/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/${l.path}'),
      name: '${l.name?cap_first}Form',
      props: true,
      meta: {
      <#if l.tagTitle?? && l.tagTitle?trim !=''>
        title: '${generatorName}.route.${l.path}Tag',
      </#if>
      <#if l.icon?? && l.icon?trim !=''>
        icon: '${l.icon}',
      </#if>
      <#if l.isDialog>
        dialog: true,
      </#if>
        authorities: ['/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}/<#if l.isFlow>f_${l.path}<#else>record</#if>'],
        breadcrumb: <#if !l.isDialog && l.tagTitle?? && l.tagTitle?trim !=''>true<#else>false</#if>,
        tagView: <#if !l.isDialog && l.tagTitle?? && l.tagTitle?trim !=''>true<#else>false</#if>,
        noCache: <#if !l.isDialog && l.tagTitle?? && l.tagTitle?trim !=''>false<#else>true</#if>
      }
    }<#if index lt count>,</#if>
    </#if>
  </#list>
</#if>
  ]
}
</#if>