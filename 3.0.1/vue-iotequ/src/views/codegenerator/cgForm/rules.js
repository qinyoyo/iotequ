
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
         name:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgForm.field.name'), trigger: 'blur' }
         ],
         path:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgForm.field.path'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                  if (!value) callback()
                  else {
                    const checked = ((getRecord().isFlow && /^[a-z][a-zA-Z0-9_]*(,[a-z][a-zA-Z0-9_]*)*$/.test(value)) || (!getRecord().isFlow && /^[a-z][a-zA-Z0-9_]*$/.test(value)))
                    if (typeof checked === 'boolean') {
                        if (checked) callback()
                        else callback(new Error(vueObject.$t(vueObject.$te('cgForm.field.pathValid') ? 'cgForm.field.pathValid' : 'system.message.errorValue')))
                    } else if (typeof checked == 'string' && checked) callback(new Error(checked))
                    else callback()
                  }
                },
                trigger: 'blur'
            }
         ],
         tableId:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgForm.field.tableId'), trigger: 'blur' }
         ],
         headTitle:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgForm.field.headTitle'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                  if (!value) callback()
                  else {
                    const checked = ((getRecord().isFlow && /^[^,]+(,[^,]+)*$/.test(value)) || (!getRecord().isFlow && /^[^,]+$/.test(value)))
                    if (typeof checked === 'boolean') {
                        if (checked) callback()
                        else callback(new Error(vueObject.$t(vueObject.$te('cgForm.field.headTitleValid') ? 'cgForm.field.headTitleValid' : 'system.message.errorValue')))
                    } else if (typeof checked == 'string' && checked) callback(new Error(checked))
                    else callback()
                  }
                },
                trigger: 'blur'
            }
         ],
         isFlow:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgForm.field.isFlow'), trigger: 'blur' }
         ],
         icon:[
            {
                validator: (rule, value, callback) => {
                  if (!value) callback()
                  else {
                    const checked = ((getRecord().isFlow && /^[a-zA-z0-9 \-_]+(,[a-zA-z0-9 \-_]+)*$/.test(value)) || (!getRecord().isFlow && /^[a-zA-z0-9 \-_]+$/.test(value)))
                    if (typeof checked === 'boolean') {
                        if (checked) callback()
                        else callback(new Error(vueObject.$t(vueObject.$te('cgForm.field.iconValid') ? 'cgForm.field.iconValid' : 'system.message.errorValue')))
                    } else if (typeof checked == 'string' && checked) callback(new Error(checked))
                    else callback()
                  }
                },
                trigger: 'blur'
            }
         ],
         tagTitle:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgForm.field.tagTitle'), trigger: 'blur' }
         ],
         labelPosition:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgForm.field.labelPosition'), trigger: 'blur' }
         ],
         isDialog:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgForm.field.isDialog'), trigger: 'blur' }
         ],
         continueAdd:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgForm.field.continueAdd'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
