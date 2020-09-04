import { rows2Dictionary } from '@/utils/cg'
export default {
  created() {
    this.$on('refreshed',this.changeDict)
  },
  methods: {
    changeDict() {
      this.dictionary.dictParent=rows2Dictionary(this.rows,'name','orgCode','orgCode','parent')
    }
  }
}
