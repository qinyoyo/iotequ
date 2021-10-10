export default {
  data() {
    return {
      tableId: null
    }
  },
  methods: {
    useMixinMethodsFirst() {
      return true
    }, 
    setChildrenParams( row) {
      this.super_setChildrenParams(row)
      if (row) this.tableId = row.tableId
      else this.tableId = null
    }
  }
}
