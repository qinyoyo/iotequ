<#assign D = "$" />
<#assign J = "#" />
<#function hasRules f>
    <#if f.showType == 'number'>
        <#if !f.validExpression?? || (f.validExpression?contains(",")) >
           <#return  false >
        <#else>
           <#return f.validExpression?? && f.validExpression?trim!=''>
        </#if>
    <#elseif f.showType=='url' || f.showType=='email'>
        <#return true>
    <#else>
        <#return f.validExpression?? && f.validExpression?trim!=''>
    </#if>
</#function>

<#list fields as f>
<#if f.showType=='url' || f.showType=='email' || (f.validExpression?? && (f.validExpression=='mobile' || f.validExpression=='fixed' || f.validExpression=='phone' || f.validExpression=='passport' || f.validExpression=='idcard'))>
import { invalidMessage } from '@/utils/validate'
<#break>
</#if>
</#list>
let extendRules = null
const mixinContext = require.context('.', false, /rules-mixin\.(js|vue)${D}/)
mixinContext.keys().forEach(key => { extendRules = mixinContext(key).default })
export default  {
   object: null,
   record: null,
   init: function(obj, rec) {
      this.object=obj
      if (rec) this.record=rec
      else if (this.object) this.record = this.object.record
   },
   getRules: function(obj, rec) {
      if (obj) this.init(obj,rec)
      else if (rec) this.record=rec
      const vueObject=this.object
      const this_ = this
      const getRecord = function() {
         if (vueObject.record) return vueObject.record
         else if (this_.record) return this_.record
         else return {}
      }
      return Object.assign({
         <#list fields as f>
         <#assign USERULE = hasRules(f) />
         <#if f.mustInput || !f.isNull || USERULE>
         ${f.entityName}:[
            <#if f.mustInput || !f.isNull>
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('${f.title}'), trigger: 'blur' }<#if USERULE>,</#if>
            </#if>
            <#if USERULE>
            <#assign checked = false />
            {
                validator: (rule, value, callback) => {
                  <#if !f.mustInput>
                  if (!value<#if f.type=='int' || f.type=='double'> && value!=0</#if>) callback()
                  else {
                  </#if>
                    <#if f.validExpression?? && f.validExpression?trim!=''>
                    <#if f.validExpression?trim?length gt 3 && f.validExpression?trim?substring(0,3)=="js:">
                    const checked = (${f.validExpression?trim?substring(3)?replace("this.record.","getRecord().")?replace("this.","vueObject.")})
                    <#assign checked = true />
                    <#elseif f.validExpression=='mobile' || f.validExpression=='fixed' || f.validExpression=='phone' || f.validExpression=='passport' || f.validExpression=='idcard'>
                    const checked = invalidMessage('${f.validExpression}',value)
                    <#assign checked = true />
                    <#else>
                    if (!/<#if !f.validExpression?trim?starts_with('^')>^</#if>${f.validExpression?trim}<#if !f.validExpression?trim?ends_with('$')>$</#if>/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('${f.title}Valid') ? '${f.title}Valid' : 'system.message.errorValue')))
                    else callback()
                    </#if>
                    <#elseif f.showType=='url' || f.showType=='email'>
                    const checked = invalidMessage('${f.showType}',value)
                    <#assign checked = true />
                    </#if>
                    <#if checked>
                    if (typeof checked === 'boolean') {
                        if (checked) callback()
                        else callback(new Error(vueObject.$t(vueObject.$te('${f.title}Valid') ? '${f.title}Valid' : 'system.message.errorValue')))
                    } else if (typeof checked == 'string' && checked) callback(new Error(checked))
                    else callback()
                    </#if>
                  <#if !f.mustInput>
                  }
                  </#if>
                },
                trigger: 'blur'
            }
            </#if>
         ],
         </#if>
         </#list>
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
