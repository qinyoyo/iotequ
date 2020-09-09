
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
         code:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgTable.field.code'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!/^[a-z][a-z0-9]+(_[a-z][a-z0-9]+)*$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('cgTable.field.codeValid') ? 'cgTable.field.codeValid' : 'system.message.errorValue')))
                    else callback()
                },
                trigger: 'blur'
            }
         ],
         name:[
            {
                validator: (rule, value, callback) => {
                    const checked = (!value || value==(getRecord().project+'_'+getRecord().code))
                    if (typeof checked === 'boolean') {
                       if (checked) callback()
                       else callback(new Error(vueObject.$t(vueObject.$te('cgTable.field.nameValid') ? 'cgTable.field.nameValid' : 'system.message.errorValue')))
                    } else if (typeof checked == 'string' && checked) callback(new Error(checked))
                    else callback()
                },
                trigger: 'blur'
            }
         ],
         title:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgTable.field.title'), trigger: 'blur' }
         ],
         subPackage:[
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                       if (!/^[a-z,A-Z,_]+$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('cgTable.field.subPackageValid') ? 'cgTable.field.subPackageValid' : 'system.message.errorValue')))
                       else callback()
                    }
                },
                trigger: 'blur'
            }
         ],
         entity:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgTable.field.entity'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!/^[A-Z][a-z0-9]+([A-Z][a-z0-9]+)*$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('cgTable.field.entityValid') ? 'cgTable.field.entityValid' : 'system.message.errorValue')))
                    else callback()
                },
                trigger: 'blur'
            }
         ],
         template:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgTable.field.template'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
