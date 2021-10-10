
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
         canceled:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.canceled'), trigger: 'blur' }
         ],
         isCharge:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.isCharge'), trigger: 'blur' }
         ],
         userNo:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.userNo'), trigger: 'blur' }
         ],
         batchNo:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.batchNo'), trigger: 'blur' }
         ],
         dt:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.dt'), trigger: 'blur' }
         ],
         operationType:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.operationType'), trigger: 'blur' }
         ],
         costType:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.costType'), trigger: 'blur' }
         ],
         projectId:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.projectId'), trigger: 'blur' }
         ],
         projectName:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.projectName'), trigger: 'blur' }
         ],
         projectPrice:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.projectPrice'), trigger: 'blur' }
         ],
         amount:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.amount'), trigger: 'blur' }
         ],
         amountBefore:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.amountBefore'), trigger: 'blur' }
         ],
         bonus:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.bonus'), trigger: 'blur' }
         ],
         bonusBefore:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.bonusBefore'), trigger: 'blur' }
         ],
         deviceNo:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.deviceNo'), trigger: 'blur' }
         ],
         deviceStream:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.deviceStream'), trigger: 'blur' }
         ],
         deviceDt:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.deviceDt'), trigger: 'blur' }
         ],
         tradeNo:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.tradeNo'), trigger: 'blur' }
         ],
         operatorNo:[
            { required: true, message: vueObject.$t('system.message.needValue') + ':' + vueObject.$t('ewBill.field.operatorNo'), trigger: 'blur' }
         ],
      }, typeof extendRules == 'function' ? extendRules(vueObject, _=>{ return vueObject.record ? vueObject.record : this_.record} ) : extendRules)
   }
}
