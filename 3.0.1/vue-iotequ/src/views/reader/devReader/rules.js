
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
         readerNo:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.readerNo'), trigger: 'blur' }
         ],
         name:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.name'), trigger: 'blur' }
         ],
         type:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.type'), trigger: 'blur' }
         ],
         readerGroup:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.readerGroup'), trigger: 'blur' }
         ],
         connectType:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.connectType'), trigger: 'blur' }
         ],
         ip:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.ip'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                       if (!/^(25[0-5]|2[0-4]\d|1\d{2}|0\d{0,2}|[0-9]{1,2})(\.(25[0-5]|2[0-4]\d|1\d{2}|0\d{0,2}|[0-9]{1,2})){3}$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('devReader.field.ipValid') ? 'devReader.field.ipValid' : 'system.message.errorValue')))
                       else callback()
                    }
                },
                trigger: 'blur'
            }
         ],
         devMode:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.devMode'), trigger: 'blur' }
         ],
         alignMethod:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.alignMethod'), trigger: 'blur' }
         ],
         blacklightTime:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.blacklightTime'), trigger: 'blur' }
         ],
         voiceprompt:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.voiceprompt'), trigger: 'blur' }
         ],
         menuTime:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.menuTime'), trigger: 'blur' }
         ],
         wengenform:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.wengenform'), trigger: 'blur' }
         ],
         wengenOutput:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.wengenOutput'), trigger: 'blur' }
         ],
         wengenOutArea:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.wengenOutArea'), trigger: 'blur' }
         ],
         regfingerOutTime:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.regfingerOutTime'), trigger: 'blur' }
         ],
         authfingerOutTime:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('devReader.field.authfingerOutTime'), trigger: 'blur' }
         ],
         fixedValue:[
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                       if (!/^[0-9A-F]{8}$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('devReader.field.fixedValueValid') ? 'devReader.field.fixedValueValid' : 'system.message.errorValue')))
                       else callback()
                    }
                },
                trigger: 'blur'
            }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
