import cg from '@/utils/cg'
import cgList from '@/utils/cgList'
import list from './list'
export default {
  mixins: [list],
  props: {
    height: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      clientHeight: this.height ? this.height : cg.containerHeight(),
      fatherHeightPercent : 100,
      fatherHeight: this.fatherHeightPercent<100 ? 
          (this.height ? this.height : cg.containerHeight()) * (this.$store.state.app.device === 'mobile' ? 1: this.fatherHeightPercent / 100)
          : (this.height ? this.height : cg.containerHeight()),
      childHeight: this.fatherHeightPercent<100 ? 
          (this.height?this.height:cg.containerHeight())*(this.$store.state.app.device === 'mobile' ? 1 : (100- this.fatherHeightPercent)/100) - 70
          :(this.height?this.height:cg.containerHeight()) - 70 ,
      sonCount:0,
      sonPkFields: [],
      showMaster: true,
      selectedSon: 'cgList_son0'
    }
  },
  watch: {
    fixedQueryRecord: {
      handler(n, o) {
        if (n && Object.keys(n).length > 0 && this.$refs.cgList && typeof this.$refs.cgList.doAction === 'function') this.$refs.cgList.doAction('refresh')
      },
      deep: true,
      immediate: true
    }
  },

  computed: {
    title() {
      if (this.showMaster || !this.mobile) return this.$t('system.action.list')
      else return this.$t('system.action.detail')
    },
    content() {
      if (this.showMaster || !this.mobile) return this.$t(this.generatorName+'.title.'+this.path) + (!this.mobile && this.contentSubTitle?' - '+this.contentSubTitle:'')
      else return this.contentSubTitle ? this.contentSubTitle : this.$t(this.generatorName+'.title.'+this.path)
    }
  },
  methods: {
    resize(percent) {
      if (this.fatherHeightPercent<100) {
        this.fatherHeightPercent = percent
        this.fatherHeight = this.clientHeight * percent / 100
        this.childHeight = this.clientHeight - this.fatherHeight - 70
      }
    },      
    goBack() {
      if (this.mobile) {
        if (this.showMaster) cg.goBack()
        else {
          if (this.$refs[this.selectedSon] && this.$refs[this.selectedSon].showMaster !== undefined) {
            if (!this.$refs[this.selectedSon].showMaster) this.$refs[this.selectedSon].showMaster = true
            else this.showMaster = true
          } else this.showMaster = true
        }
      }
    },
    hasMenu() {
      if (!this.mobile) return false
      else if (this.showMaster) return true
      else if (this.$refs[this.selectedSon] && typeof this.$refs[this.selectedSon].hasMenu === 'function') return this.$refs[this.selectedSon].hasMenu()
      else return false
    },
    showActionSheet() {
      if (this.showMaster && typeof this.$refs.cgList.showActionSheet === 'function') this.$refs.cgList.showActionSheet()
      else if (this.$refs[this.selectedSon] && typeof this.$refs[this.selectedSon].showActionSheet === 'function') this.$refs[this.selectedSon].showActionSheet()
    },
    getShowMaster() {
      if (!this.mobile) return undefined
      else if (this.showMaster) return true
      else if (this.$refs[this.selectedSon] && this.$refs[this.selectedSon].showMaster !== undefined) return this.$refs[this.selectedSon].showMaster
      else return undefined
    },
    showDetail() {
      if (!this.mobile) return
      else if (this.showMaster) this.doShowDetail()
      else if (this.$refs[this.selectedSon] && this.$refs[this.selectedSon].showMaster && typeof this.$refs[this.selectedSon].doShowDetail ==='function') this.$refs[this.selectedSon].doShowDetail()
    },
    setChildrenParams(row) {
      for (let i=0;i<this.sonCount;i++) {
         if (typeof this['son'+i+'Condition'] == 'object') {
          const key = Object.keys(this['son'+i+'Condition'])[0]
          if (row && row[this.sonPkFields[i]]) this['son'+i+'Condition'][key] = row[this.sonPkFields[i]]
          else this['son'+i+'Condition'][key] = 'null'
         } else {
          if (row && row[this.sonPkFields[i]]) this['son'+i+'Condition'] = row[this.sonPkFields[i]]
          else this['son'+i+'Condition'] = 'null'
         }
      }
    },
    rowClick({ row, column, event }) {
      if (!this.mobile) this.setChildrenParams(row)
      this.setContentSubTitle(row)
    },
    refreshed(listObject) {
        const row = cgList.list_getCurrentRow(listObject)
        for (let i=0;i<this.sonCount;i++)
            if (this.$refs['cgList_son'+i] && this.$refs['cgList_son'+i].hasOwnProperty('dictionary')) this.$refs['cgList_son'+i].needLoadDictionary = true
        this.setChildrenParams(row)
    },
    doShowDetail(row) {
      if (!row) row = cgList.list_getCurrentRow(this.$refs.cgList)
      this.showMaster = false
      this.setChildrenParams(row)
    }
  }
}

