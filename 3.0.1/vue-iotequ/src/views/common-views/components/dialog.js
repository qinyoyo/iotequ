
import elDragDialog from '@/directive/el-drag-dialog'
import record from './record'
export default {
  directives: { elDragDialog },
  mixins: [record],
  props: {
    dialogParams: {
      type: Object,
      default: null
    },
    dialogClass: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      showDialog: true
    }
  },
  methods: {
    close() {
      this.showDialog = false
      this.$emit('close')
      if (!this.dialogParams) this.$store.dispatch('tagsView/activeLastAfterRemove', this.$route)
    },
    beforeClose(done) {
      if (this.$refs.cgForm && this.$refs.cgForm.recordChanged && !this.$refs.cgForm.ignoreRecordChanged) {
        this.$confirm(this.$t('system.message.cancelModified'), this.$t('system.action.confirm'), {
          confirmButtonText: this.$t('system.action.yes'),
          cancelButtonText: this.$t('system.action.cancel'),
          closeOnClickModal: false,
          type: 'warning'
        }).then(_ => {
          done()
        }).catch(_ => {
        })
      } else done()
    },
    handleClose() {
      this.$refs.dialog.handleClose()
    }
  }
}
