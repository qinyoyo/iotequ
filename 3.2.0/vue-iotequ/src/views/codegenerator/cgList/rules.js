
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
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.name'), trigger: 'blur' }
         ],
         path:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.path'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!/^[a-z][a-zA-Z0-9_]*$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('cgList.field.pathValid') ? 'cgList.field.pathValid' : 'system.message.errorValue')))
                    else callback()
                },
                trigger: 'blur'
            }
         ],
         tableId:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.tableId'), trigger: 'blur' }
         ],
         headTitle:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.headTitle'), trigger: 'blur' }
         ],
         tagTitle:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.tagTitle'), trigger: 'blur' }
         ],
         editInline:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.editInline'), trigger: 'blur' }
         ],
         detailInline:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.detailInline'), trigger: 'blur' }
         ],
         sonAlign:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.sonAlign'), trigger: 'blur' }
         ],
         generatorType:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.generatorType'), trigger: 'blur' }
         ],
         showSearch:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.showSearch'), trigger: 'blur' }
         ],
         toolbarMode:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.toolbarMode'), trigger: 'blur' }
         ],
         tableHeight:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.tableHeight'), trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!value) callback()
                    else {
                       if (!/^0$/.test(value)) callback(new Error(vueObject.$t(vueObject.$te('cgList.field.tableHeightValid') ? 'cgList.field.tableHeightValid' : 'system.message.errorValue')))
                       else callback()
                    }
                },
                trigger: 'blur'
            }
         ],
         pagination:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.pagination'), trigger: 'blur' }
         ],
         stripe:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.stripe'), trigger: 'blur' }
         ],
         border:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.border'), trigger: 'blur' }
         ],
         highlightCurrentRow:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.highlightCurrentRow'), trigger: 'blur' }
         ],
         multiple:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.multiple'), trigger: 'blur' }
         ],
         showHeader:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.showHeader'), trigger: 'blur' }
         ],
         localExport:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('cgList.field.localExport'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
