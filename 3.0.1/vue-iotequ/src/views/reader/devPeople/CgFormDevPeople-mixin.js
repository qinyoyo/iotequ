
export default {
  methods: {
    disabledAction(btn) {
      if (btn.action=='sample') return !this.record.userNo
      else return false
    }
   }
}
