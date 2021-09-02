
export default {
  computed: {
    admin() {
      return this.$store.state.user.name === 'admin'
    }
  },  
  methods: {
    disabledAction(btn) {
      if (btn.action=='sample') return !this.record.userNo
      else return false
    }
   }
}
