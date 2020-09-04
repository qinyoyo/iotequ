export default {
  data() {
    return {
      tableId: ''
    }
  },
  methods: {
    useMixinMethodsFirst() {
      return true
    }, 
    rowClick({ row, column, event }) {
      if (row) this.tableId = row.tableId
      this.super_rowClick({ row, column, event })
    }
  }
}
