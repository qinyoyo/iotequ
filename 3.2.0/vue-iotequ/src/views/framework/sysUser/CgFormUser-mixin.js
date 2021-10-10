export default {
  created() { 
    this.$emit('created',this)
  },
  computed: {
    admin() {
      return this.$store.state.user.roles.indexOf('admin') >= 0
    }
  },
  methods: {
    extendActionFilter(action) {
      if (this.baseUrl === '/login/profile') {
        if (action === 'add') return false
      }
      else if (action === 'changePassword') return false
      return true
    }
  }
}