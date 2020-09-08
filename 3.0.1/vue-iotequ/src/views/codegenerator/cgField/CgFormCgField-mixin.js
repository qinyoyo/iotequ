import { doRegTest } from '@/views/common-views/extend-views/CgRegTest'
export default {
  data() {
    return {
      formatterPrepend: 'const',
      defaultPrepend: 'const',
      dictionaryJs: [{ value: 'const', text: '常量' }, { value: 'js:', text: 'js代码' }],
      dictionaryJsJava: [{ value: 'const', text: '常量' }, { value: 'js:', text: 'js代码' }, { value: 'f:', text: 'java代码' }]
    }
  },
  computed: {
    tableId() {
      return this.record.tableId
    },
    joinMode() {
      return this.record.showType && (this.record.showType.indexOf('join') >= 0 || this.record.showType === 'dict_list')
    },
    selectMode() {
      return this.record.showType && ['select', 'radio', 'checkbox'].indexOf(this.record.showType) >= 0
    },
    useSystemDataDict() {
      return this.selectMode && this.record.dictTable && this.record.dictTable.toLowerCase().indexOf('dict:') !== 0
        && this.record.dictTable.toLowerCase().indexOf('js:') !== 0 && this.record.dictTable.toLowerCase().indexOf('f:') !== 0
        && !this.record.dictField && !this.record.dictText
    },
    useDataDictFunction() {
      return this.selectMode && this.record.dictTable 
        && (this.record.dictTable.toLowerCase().indexOf('js:') == 0 || this.record.dictTable.toLowerCase().indexOf('f:') == 0)
    },
    useConstValueText() {
      return this.selectMode && (!this.record.dictTable || !this.record.dictTable.trim())
    },
    useSelect() {
      return this.selectMode && this.record.dictTable && this.record.dictTable.toLowerCase().indexOf('dict:') !== 0
      && (this.record.dictField || this.record.dictText || this.record.dictTable.trim().toLowerCase().indexOf('select')>=0)
    },
    useOtherDict() {
      return this.selectMode && this.record.dictTable && this.record.dictTable.toLowerCase().indexOf('dict:') === 0
    }
  },
  created() {
    this.doAction('extDict', { silence: true })
  },
  watch: {
    formatterPrepend(n, o) {
      this.setFieldValueByPrepend(n, 'formatter')
    },
    defaultPrepend(n, o) {
      this.setFieldValueByPrepend(n, 'defaultValue')
    },
    record: {
      handler() {
        this.checkAndSetDomProperties()
      },
      deep: true
    },
    tableId: {
      handler() {
        this.doAction('extDict', { silence: true, tableId: this.record.tableId })
      },
      immediate: true
    }
  },
  activated() {
    this.checkAndSetDomProperties()
  },
  methods: {
    dictDictTable() {
      return this.joinMode ? (this.dictionary.dictCgList ? this.dictionary.dictCgList : [])
        : (this.dictionary.dictDataDict ? this.dictionary.dictDataDict : [])
    },
    checkAndSetDomProperties() {
      this.displayTabPane(this.$refs.tabs, 1, this.joinMode || this.selectMode)
      this.displayTabPane(this.$refs.tabs, 2, this.record.showType !== 'html' && this.record.name && this.record.name.indexOf(':') < 0)
    },
    getActionParams(action, options, id) {
      return {
        tableId: this.record.tableId
      }
    },
    useMixinMethodsFirst() {
      return true
    },
    setFieldValueByPrepend(prepend, field) {
      const v = this.record[field] ? this.record[field].split(':')[this.record[field].split(':').length - 1] : ''
      this.record[field] = (prepend === 'const' ? '' : prepend) + v
      this.recordChanged = true
    },
    getTitle(field) {
      if (field === 'validExpression' && this.record.showType === 'number') return '最小值,最大值,步长  逗号前没有值表示省略 ，如,,10表示步长为10，最小值最大值省略'
      else if (field === 'validTitle' && this.record.showType === 'html') return '插入显示的html片段'
      else if (field === 'formatter' && this.record.showType === 'image') return '输入一个或三个整数(逗号分隔的宽度,高度,是否居中)指定图像最大尺寸，默认64,64,0'
      else if (field === 'formatter' && this.record.showType === '2dcode') return '输入一个整数指定二维码的尺寸，默认128'
      else if (field === 'formatter' && this.record.showType === 'file') return '输入文件后缀名或imei列表，如.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,image/*'
      else if (field === 'dictTable' && this.joinMode) return '关联选择的列表视图定义名称'
      else if (field === 'dictTable') {
        if (this.useConstValueText) return '请在字典code/text处设置键值对'
        else if (this.useSystemDataDict) return '使用系统字典，不需更多设置'
        else if (this.useOtherDict) return '请在dict:后输入引用的其他字段定义的字典格式的字段名称,不可嵌套'
        else if (this.useSelect) return '请输入数据库表名或者以select 开头的sql查询语句'
      }
      else if (field === 'dynaCondition') return this.joinMode
        ? '关联选择的列表视图定义筛选条件列表, join视图字段=本表字段，字段相同可省略,如 orgCode,name=userName' : '字典的sql查询条件,用${field}表示本表字段,如 org_code=${orgCode}'
      else if (field === 'dictField') {
        if (this.joinMode) return '与本字段关联的列表视图字段'
        else if (this.useConstValueText) return '逗号分隔的可能的值列表'
        else if (this.useSystemDataDict) return '使用系统数据字典，该值必须设置为空'
        else if (this.useSelect) return '数据库字段名称，该字段值用于设置表单字段的值'
      }
      else if (field === 'dictText') {
        if (this.joinMode) return '关联视图需要引入到本表的字段列表'
        else if (this.useConstValueText) return '逗号分隔的可能的显示值列表，设置为空，显示值与实际值相同'
        else if (this.useSystemDataDict) return '使用系统数据字典，该值必须设置为空'
        else if (this.useSelect) return '数据库字段名称，该字段值用于设置表单页面上的显示值'        
      }
      else return this.$t('cgField.field.' + field + 'Valid')
    },
    getLabel(field) {
      if (field === 'validExpression' && this.record.showType === 'number') return '最小值,最大值,步长'
      else if (field === 'validTitle' && this.record.showType === 'html') return 'html片段'
      else if (field === 'formatter' && this.record.showType === 'image') return '图像属性'
      else if (field === 'formatter' && this.record.showType === '2dcode') return '二维码尺寸'
      else if (field === 'formatter' && this.record.showType === 'file') return '文件类型'
      else if (field === 'dictTable' && this.joinMode) return '列表视图名'
      else if (field === 'dictField' && this.joinMode) return '关联字段'
      else if (field === 'dictText' && this.joinMode) return '插入字段列表'
      else if (field === 'dynaCondition') return this.joinMode ? '关联筛选条件' : 'sql动态查询条件'
      else return this.$t('cgField.field.' + field)
    },
    groupPaneTitle(defTitle) {
      if (defTitle === 'cgField.title.groupCgFieldDictTable') {
        if (this.useSystemDataDict) return '系统数据字典'
        else if (this.useConstValueText) return '自定义键值对'
        else if (this.useSelect && this.record.dictTable.toLowerCase().indexOf('select ') === 0) return 'SQL语句'
        else if (this.useSelect) return '数据库表'
        else if (this.useOtherDict) return '字典引用'
        else if (this.joinMode) return '关联视图'
      }
      return this.$t(defTitle)
    },
    dictTableChange() {
      if (this.joinMode) {
        this.record.dictMultiple = (this.record.showType === 'dict_list')
        this.record.dictFullName = false
        this.record.dictParent = null
        this.record.dictParentField = null
      } else if (this.useConstValueText || this.useSystemDataDict) {
        if (this.record.showType === 'radio') this.record.dictMultiple = false
        this.record.dictFullName = false
        this.record.dictParent = null
        this.record.dictParentField = null
      }
    },
    testValidExpression: function() {
      const that = this
      if (this.record.validExpression) {
        doRegTest({
          reg: that.record.validExpression,
          callback: ({ confirm, reg }) => {
            if (confirm) that.record.validExpression = reg
          }
        })
      }
    }
  }
}