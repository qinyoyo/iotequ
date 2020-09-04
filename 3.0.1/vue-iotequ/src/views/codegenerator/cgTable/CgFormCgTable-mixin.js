export default {
  methods: {
    getLabelOfField: function(field, defLabel) {
      if (field === 'name') {
        return this.$t(defLabel)
      }
    }
  }
}
