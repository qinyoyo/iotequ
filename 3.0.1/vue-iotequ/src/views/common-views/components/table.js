import cgList from '@/utils/cgList'
import cg from '@/utils/cg'
import time from '@/utils/time'
export default {
  props: {
    joinMode: {
      type: Boolean,
      default: false
    },
    joinMultiple: {
      type: Boolean,
      default: false
    },
    joinShow: {
      type: Boolean,
      default: true
    },
    openID: {
      type: String,
      default: ''
    },
    originSelections: {
      type: String,
      default: ''
    },
    height: {
      type: Number,
      default: 0
    },
    fixedQueryRecord: {
      type: Object,
      default: () => { return {} }
    }
  },
  data() {
    return {
      cgList,
      myself: this,
      rulesObject: null,
      path: '',
      showQuery: false,
      showActionView: false,
      defaultOrder: '',
      queryRecord: this.initialQueryRecord(),
      queryRecordFields: [],
      formPath: '',
      listLoading: false,
      rows: [],
      selections: this.originSelections,
      localExport: false,
      removeLeftRecordOnRightJoin: false,
      parentField: null,
      idField: null,
	  dictionary: {},
      needLoadDictionary: false,
      paginationPageSize: 0,
      sortableFields: [],
      sortableFieldsOrder: [],
      editInlineFields: null,
      hasSonTables: false,
      contextMenu: { top: 0, left: 0, visible: false, row: null, actions: [], trElement: null },
      generatorName: '',
      baseUrl: '/',
      listName: '',
      multipleSelection: false,
      defaultTableHeight: 0
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    title() {
      return this.$t(this.generatorName+'.title.'+this.path)
    },
    multiple() {
      return this.joinMode ? this.joinMultiple : (this.multipleSelection ? this.isTableMode() : false)
    },
    className() {
      return this.openID ? 'cg-list-'+this.listName.toLowerCase()+' cg-list-' +this.listName.toLowerCase()+ '-'+this.openID : 'cg-list-'+this.listName.toLowerCase()
    },
    allActions() {
      return ''
    },
    additionalActions() {
      return []
    }
  },
  watch: {
    fixedQueryRecord: {
      handler(n, o) {
        n && Object.keys(n).length && Object.keys(n).some(k=>{
          if (cg.hasValue(n[k])) return true
        }) && this.doAction('refresh')
      },
      deep: true,
      immediate: true
    },
    joinShow(newValue, oldValue) {
      if (oldValue && !newValue && this.joinMode) {
        this.$emit('closeJoinList', this.multiple ? this.$refs.cgList.store.states.selection : [this.$refs.cgList.store.states.currentRow])
      }
    }
  },
  created() {
    this.doAction('refresh')
  },
  activated() {
    cgList.list_activedRefresh(this)
  },
  destroyed() {
    cgList.list_destroyScroll(this)
  },
  methods: {
    rowClassName({row, rowIndex}){
      return this.editInlineFields && row && row.inlineEditting ? 'edit-inline' : ''
    },
    defaultEditMode(row) {
      if (this.hasAuthorityOf(this,this.baseUrl,'edit',row)) return 'edit'
      else if (this.hasAuthorityOf(this,this.baseUrl,'view',row)) return 'view'
      else return ''
    },
    isTableMode() {
      return this.joinMode || !this.mobile || this.isLandscape() || typeof this.rowRender !== 'function'
    },
    hasMenu() {
      return this.mobile
    },
    isLandscape() {
      return window.orientation == 90 || window.orientation == -90
    },
    showActionSheet(hidden){
      this.showActionView = !hidden
    },
    tableHeight() {
      if (this.height > 0) return this.height - (this.paginationPageSize? (this.mobile ? 0 : 40) : 0)
      else if (this.defaultTableHeight) return this.defaultTableHeight
      else return this.containerHeight()- (this.paginationPageSize? (this.mobile ? 0 : 40) : 0)
    },
    initialQueryRecord() {
      return this.fixedQueryRecord
    },
    editInlineAdd() {
      cgList.list_doAction(this, 'editInline_add')
    },
    doAction(action, options) {
      this.queryRecord = Object.assign(this.queryRecord, this.fixedQueryRecord)
      cgList.list_doAction(this, action, Object.assign(options ? options : {}, this.additionalActions.find(e => e.action === action)))
    },
    ...cg,
    time2String: time.toString
  }
}
