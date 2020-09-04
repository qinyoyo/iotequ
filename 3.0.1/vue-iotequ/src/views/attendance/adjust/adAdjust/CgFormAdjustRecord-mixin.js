export default {
  methods: {
    timeLabel(when) {
      if (this.record.adjustType=='10') {
        if (when=='end') return '下班时间'
        else return '上班时间'
      } else if (when=='end') return this.$t('adAdjust.field.endTime')
      else return this.$t('adAdjust.field.startTime')
    },
    validCheckDate() {
      if (this.record.adjustType=='10' && !this.record.startTime && !this.record.endTime) return '必须输入上班或下班时间'
      else if (this.record.adjustType!='10' && (!this.record.startTime || !this.record.endTime)) return '必须输入开始和结束时间'
      else return true
    }
  }
}