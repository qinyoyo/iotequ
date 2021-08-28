import cg from '@/utils/cg'
export default {
  props: {
    routeParams: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      path: '',
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      generatorName: '',
      allowViewRecord: true,
      allowEditRecord: true,
      allowAddRecord: true,
      isFlowRecord: false,
      defaultIcon: '',
      baseUrl: '/'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
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
    titleColor() {
      if (this.isNew) return 'color-danger'
      else if (this.isEdit) return 'color-warning'
      else return 'color-info'
    },
    icon() {
      if (this.openParams().icon) return this.openParams().icon
      else if (this.defaultIcon) {
        if (typeof this.defaultIcon == 'string') return this.defaultIcon
        else if (typeof this.defaultIcon == 'object') return this.defaultIcon[this.path]
        else return this.isNew?'el-icon-circle-plus':(this.isEdit?'el-icon-edit-outline':'el-icon-view')
      }
      else return this.isNew?'el-icon-circle-plus':(this.isEdit?'el-icon-edit-outline':'el-icon-view')
    },
    content() {
      if (this.newContent) return this.newContent()
      else if (this.openParams().content) return this.openParams().content
      else return this.$t(this.generatorName+'.title.'+this.path)
    },
    title() {
      if (this.newTitle) return this.newTitle()
      else if (this.openParams().title) return this.openParams().title
      else return this.isNew ? this.$t('system.action.new'):(this.isDetail ? this.$t('system.action.view') : this.$t('system.action.edit'))
    }
  },
  activated() {
    this.openMode = this.openParams().openMode
  },
  beforeRouteLeave(to,from,next) {
    if (!this.hasOwnProperty('showDialog') && this.$refs.cgForm && this.$refs.cgForm.recordChanged && !this.$refs.cgForm.ignoreRecordChanged) {
      this.$confirm(this.$t('system.message.cancelModified'), this.content, {
        confirmButtonText: this.$t('system.action.yes'),
        cancelButtonText: this.$t('system.action.cancel'),
        closeOnClickModal: false,
        type: 'warning'
      }).then(_ => {
        next()
      }).catch(_ => {
        if (from.name && from.meta.tagView) {
          this.$store.dispatch('tagsView/addView', from)
        }
      })
    } else next()
  },
  methods: {
    openParams: function() {
      return this.routeParams ? this.routeParams : this.$route.query
    },
    goBack() {
      cg.goBack()
    },
    submit() {
      this.$refs.cgForm.submit()
    },
    openModeChanged(v) {
      this.openMode = v
    }
  }
}
