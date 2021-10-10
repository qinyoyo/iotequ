
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
         orderNum:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgField.field.orderNum'), trigger: 'blur' }
         ],
         entityName:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgField.field.entityName'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    const checked = ((!getRecord().name && /^[a-z][a-z0-9]+([A-Z][a-z0-9]+)*$/.test(value)) || (value===vueObject.camelString(getRecord().name.split(':')[0])))
                    if (typeof checked === 'boolean') {
                       if (checked) callback()
                       else callback(new Error(vueObject.$t(vueObject.$te('cgField.field.entityNameValid') ? 'cgField.field.entityNameValid' : 'system.message.errorValue')))
                    } else if (typeof checked == 'string' && checked) callback(new Error(checked))
                    else callback()
                },
                trigger: 'blur'
            }
         ],
         title:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgField.field.title'), trigger: 'blur' }
         ],
         name:[
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                       if (!/^[a-z][a-z0-9]*(_[a-z0-9]+)*(:.*)*$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('cgField.field.nameValid') ? 'cgField.field.nameValid' : 'system.message.errorValue')))
                       else callback()
                    }
                },
                trigger: 'blur'
            }
         ],
         keyType:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgField.field.keyType'), trigger: 'blur' }
         ],
         showType:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgField.field.showType'), trigger: 'blur' }
         ],
         isNull:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgField.field.isNull'), trigger: 'blur' }
         ],
         dictMultiple:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgField.field.dictMultiple'), trigger: 'blur' }
         ],
         dictFullName:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgField.field.dictFullName'), trigger: 'blur' }
         ],
         type:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgField.field.type'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
