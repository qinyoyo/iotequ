
export default {
    created() {
        this.ignoreRecordChanged = true
    },
    methods: {
        extendActionFilter(action) {
            return action!='add'
      }
     }
  }