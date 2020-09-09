
let extendRules = null
const mixinContext = require.context('.', false, /rules-mixin\.(js|vue)$/)
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
         flowState:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('pmProject.field.flowState'), trigger: 'blur' }
         ],
         flowRegisterTime:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('pmProject.field.flowRegisterTime'), trigger: 'blur' }
         ],
         name:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('pmProject.field.name'), trigger: 'blur' }
         ],
         type:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('pmProject.field.type'), trigger: 'blur' }
         ],
         code:[
            {
                validator: (rule, value, callback) => {
                    const checked = (value || !vueObject.flowAction ||  vueObject.flowAction!='publish')
                    if (typeof checked === 'boolean') {
                       if (checked) callback()
                       else callback(new Error(vueObject.$t(vueObject.$te('pmProject.field.codeValid') ? 'pmProject.field.codeValid' : 'system.message.errorValue')))
                    } else if (typeof checked == 'string' && checked) callback(new Error(checked))
                    else callback()
                },
                trigger: 'blur'
            }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
