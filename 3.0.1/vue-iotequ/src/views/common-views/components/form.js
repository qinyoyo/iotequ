import cg from '@/utils/cg'
import cgForm from '@/utils/cgForm'

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
      record: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {},
      blobRecord: {},
      needDefaultFromServer: false,
      dictionary: {},
      needLoadDictionary: false,
      allowViewRecord: true,
      allowEditRecord: true,
      allowAddRecord: true,
      isFlowRecord: false,
      isDialogForm: false,
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
    if (this.rulesObject) this.rules = this.rulesObject.getRules(this)
    if (this.isFlowRecord) this.record.flowSelection = 'approve'
    if (cg.hasValue(this.dictionary))  cgForm.form_getQueryDictionary(this)
    if (this.queryById) {
      cgForm.form_getRecordFromServer(this,this.queryById)
      this.queryRefreshId = this.queryById
    } else if (this.isNew) cgForm.form_createNewRecord(this)
    else if ((this.isEdit || this.isDetail) && this.openParams().id && typeof this.openParams().record !== 'object') {
      cgForm.form_getRecordFromServer(this,this.openParams().id)
      this.queryRefreshId = this.openParams().id
    }
    if (!cg.hasValue(this.dictionary) && this.needLoadDictionary) cgForm.form_getDictionary(this)
    this.just4elInputNumberNullBug()
  },
  mounted() {
    const _this=this
    if (this.isDialogForm) cgForm.form_mounted(this)
    setTimeout(_=>{
        _this.recordChanged = false
    },200)
  },
  activated() {
    const _this=this      
    if (!this.isDialogForm) cgForm.form_activedRefresh(this)
    setTimeout(_=>{
        _this.recordChanged = false
    },200)
  },
  
  methods: {
    openParams: function() {
      return this.dialogParams ? this.dialogParams : this.$route.query
    },
    just4elInputNumberNullBug: function() {
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

