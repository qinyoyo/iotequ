import cg from '@/utils/cg'
import time from '@/utils/time'
import cgForm from '@/utils/cgForm'
import {setStyle, onTransitionEnd, offTransitionEnd } from '@/utils/dom'
export default {
  props: {
    showInDialog: {
       type: Boolean,
       default: false
    },
    height: {
       type: Number,
       default: 0
    },
    queryById: [Number, String]
  },
  data() {
    return {
      myself: this,
      cgForm,
      rules: {},
      idField: null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: this.openParams().record && typeof this.openParams().record === 'object' ? Object.assign({},this.openParams().record) : {},
      blobRecord: null,
      needDefaultFromServer: false,
      needLoadDictionary: false,
      allowViewRecord: true,
      allowEditRecord: true,
      allowAddRecord: true,
      isFlowRecord: false,
      isDialogForm: false,
      isFirstTimeActived: true,
      continueAdd: false,
      defaultLabelPosition: 'top',
      rulesObject: null,
      path: '',
      formName: '',
      generatorName: '',
      baseUrl: '/'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    title() {
      return this.$t(this.generatorName+'.title.'+this.path)
    },
    hasMenu() {
      return false
    },
    className() {
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-'+this.formName.toLowerCase()
    },
    labelWidth() {
      return this.className.indexOf('cg-form-cell')>=0? '100px' : undefined
    },
    labelPosition() {
       return this.className.indexOf('cg-form-cell')>=0? 'left':this.defaultLabelPosition
    },
    isDetail() {
       return this.allowViewRecord && (this.openMode === 'detail' || this.openMode === 'view')
    },
    isNew() {
       return this.allowAddRecord && (!this.openMode || this.openMode === 'new' || this.openMode === 'add')
    },
    isEdit() {
       return this.allowEditRecord && (this.openMode === 'edit' || this.openMode === 'modify')
    },
  },
  watch: {
    record: {
      handler() {
        if (this.isEdit || this.isNew) {
            this.just4elInputNumberNullBug()
            this.recordChanged = true
        }
      },
      deep: true
    },
    queryById: {
      handler(n, o) {
        if (n) this.doAction('refresh', {id: n})
      },
      immediate: true
    }
  },
  created() {
    cgForm.form_getQueryDictionary(this)
    let promise = null
    if (this.rulesObject) this.rules = this.rulesObject.getRules(this)
    if (this.isFlowRecord) this.record.flowSelection = 'approve'
    if (this.queryById) {
      promise = cgForm.form_getRecordFromServer(this,this.queryById)
      this.queryRefreshId = this.queryById
    } 
    else if (this.isNew) promise = cgForm.form_createNewRecord(this)
    else if ((this.isEdit || this.isDetail) && this.openParams().id && typeof this.openParams().record !== 'object') {
      promise = cgForm.form_getRecordFromServer(this,this.openParams().id)
      this.queryRefreshId = this.openParams().id
    }
    else if (this.needLoadDictionary) promise = cgForm.form_getDictionary(this)
    else if (typeof this.initDynaDict == 'function') promise = this.initDynaDict()
    const _this=this
    const initFunc = function() {
      _this.just4elInputNumberNullBug()
      _this.initialized()
      setTimeout(_=>{
        _this.recordChanged = false
      },200)
    }
    if (!promise) initFunc()
    else promise.then(_=>{
      initFunc()
    }).catch(_=>{
      initFunc()
    })
  },
  mounted() {
    onTransitionEnd(this.$el,this.afterTransitionEnd)
  },
  activated() {  
    if (!this.isDialogForm) {
    	cgForm.form_activedRefresh(this)
      const _this=this  
      if (this.isFirstTimeActived) {
      	this.isFirstTimeActived = false
      	setTimeout(_=>{
           _this.recordChanged = false
        },200)
      } 
    }
  },
  
  methods: {
    cgUtils() {
      return {
        cg,
        time,
        cgForm
      }
    },
    afterTransitionEnd() {
      offTransitionEnd(this.$el,this.afterTransitionEnd)
      this.recordChanged = false
      if (this.isDialogForm) {
        if (!this.isDetail) cg.autoFocus(this)
        const panes = this.$el.querySelectorAll('.el-dialog__body .el-tabs__content .el-tab-pane')
        if (panes && panes.length>1) {
          if (panes[0].clientHeight) {
            const height = panes[0].clientHeight+'px'
            for (let i=0;i<panes.length;i++) {
              setStyle(panes[i], 'height', height)
            }
          }
        }        
      }
      this.$emit('transitionEnd')
    },
    openParams: function() {
      return this.dialogParams ? this.dialogParams : this.$route.query
    },
    just4elInputNumberNullBug: function() {
    },
    initDynaDict() {   	
    },
    initialized() {
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, this.isFlowRecord? 'f_'+this.flowAction : 'save', this.continueAdd && this.isDialogForm)
    },
    doAction(action, options) {
      cgForm.form_doAction(this, action, this.addtionalActions? Object.assign(options ? options : {}, this.addtionalActions.find(e => e.action === action)) : options)
    },
    ...cg
  }
}

