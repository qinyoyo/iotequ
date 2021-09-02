
export default {
  computed: {
    admin() {
      return this.$store.state.user.roles.indexOf('admin') >= 0
    }
  },  
  methods: {
    disabledAction(btn) {
      if (btn.action=='sample') return !this.record.userNo
      else return false
    }
   }
}
