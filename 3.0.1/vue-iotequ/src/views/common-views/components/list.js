import cg from '@/utils/cg'
import cgList from '@/utils/cgList'
export default {
  props: {
    fixedQueryRecord: {
      type: Object,
      default: () => { return {} }
    }
  },
  data() {
    return {
      clientHeight: cg.containerHeight(),
      fatherHeight: cg.containerHeight(),
      childHeight: cg.containerHeight()-70,
      titleField: '',
      contentSubTitle: '',
      path: '',
      generatorName: '',
      baseUrl: '/'
    }
  },
  created() {
    this.$route.query && this.$route.query.fixedQueryRecord && (this.fixedQueryRecord = Object.assign(this.fixedQueryRecord,this.$route.query.fixedQueryRecord))
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    title() {
      return this.$t('system.action.list')
    },
    content() {
      return this.$t(this.generatorName+'.title.'+this.path) + (!this.mobile && this.contentSubTitle?' - '+this.contentSubTitle:'')
    }
  },
  methods: {
    goBack() {
      if (this.mobile) cg.goBack()
    },
    hasMenu() {
      return this.mobile
    },
    showActionSheet() {
      this.$refs.cgList.showActionSheet()
    },
    setContentSubTitle(row) {
      if (this.titleField) this.contentSubTitle = row && row[this.titleField] ? cg.localeText(String(row[this.titleField])).local() : ''  
    },
    rowClick({ row, column, event }) {
      this.setContentSubTitle(row)
    },
    doShowDetail(row) {
      if (!row) row = cgList.list_getCurrentRow(this.$refs.cgList)
      cgList.list_rowDblclick(this.$refs.cgList, { row })
    }
  }
}

