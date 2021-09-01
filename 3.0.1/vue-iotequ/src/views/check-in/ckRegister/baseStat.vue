<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :icon="icon" :hasMenu="hasMenu()"
                   :content="title" @goBack="goBack"
        />
      </div>
      <el-form>
      <el-row v-if="showCondition">
        <el-col :span="8">
          <el-form-item :label="$t('ckRegister.field.orgName')">
          <cg-cascader v-model="queryRecord.orgCode" name="orgCode" collapse-tags clearable
                       :dictionary="dictOrgCode" show-all-levels/>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item :label="dateLabel">
            <cg-date-picker v-model="queryRecord.inDate" :title="dateLabel" name="inDate" :align="mobile?'right':'center'" type="daterange" 
                            :picker-options="datePickerOptions()"
                            clearable />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <div class="cg-form-buttons">
            <el-button class="cg-button" type="default" plain icon="el-icon-refresh-left" @click.native="refresh()">
              {{ $t('system.action.refresh') }}
            </el-button>
            <el-button class="cg-button" type="primary" :disabled="noData" plain icon="el-icon-download" @click.native="download()">
              {{ $t('system.action.save') }}
            </el-button>
          </div>
        </el-col>
      </el-row> 
      <el-button v-else class="cg-button" type="primary" :disabled="noData" plain icon="el-icon-download" @click.native="download()">
         {{ $t('system.action.save') }}
      </el-button> 
      </el-form>    
      <el-divider v-if="showCondition" />
      <div>
        <cg-chart ref="chart" width="100%" :height="containerHeight() - (showCondition?120:20)" :options="chartOptions" />
      </div>
    </el-card>
  </div>
</template>

<script>
import { datePickerOptions, containerHeight } from '@/utils/cg'
import { startOf, endOf } from '@/utils/time'
import { request } from '@/utils/request'

// 引入柱状图组件
//require('echarts/lib/chart/bar')
//require("echarts/lib/component/polar")
import echarts from 'echarts'
import { toString } from '@/utils/time' 
import { getEChartsOptions } from './statOption'
export default {
  name: 'CkBaseStat',
  props: {
      title: {
          type: String,
          default: 'ckStat.title.amountByDay'.local()
      },
      dateLabel: {
        type: String,
        default: 'ckRegister.field.inDate'.local()
      },
      dataHeader : {
          type: Array,
          default: ()=> ['']
      },
      icon: {
          type: String,
          default: 'svg-chart/chart-rose'
      },
      showCondition: {
        type: Boolean,
        default: true
      },
      action: {
          type: String,
          default: 'amountByDay'
      },
      legendField: {
          type: String,
          default: 'org_name'
      },
      xField: {
          type: String,
          default: 'in_date'
      },
      xFieldName: {
          type: String,
          default: '日期'
      },
      yField: {
          type: String,
          default: 'amount',
      },
      yFieldName: {
          type: String,
          default: '人次'
      },
      charType: {
          type: String,
          default: 'bar'
      },
      initRange: {
          type: Array,
          default: ()=> [startOf(null,'month'),endOf(null,'month')]
      },
      exOption: {
        type: Object,
        default: ()=>null
      }
  },
  data() {
    return {
      baseUrl: '/check-in/ckRegister',
      dictOrgCode: [],
      queryRecord: {
        orgCode: this.$store.state.user.org,
        inDate: this.initRange
      },
      chartOptions: null
    }
  },
  mounted() {
    this.loadDictionary()
  },
  
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    noData() {
      return !this.chartOptions || !this.chartOptions.series || !this.chartOptions.series.length
    }
  },
  methods: {
    containerHeight,
    datePickerOptions,
    loadDictionary() {
        const that = this
        let req = {
            url: this.baseUrl + '/record',
            method: 'get',
            params: {
              needLoadDictionary: true, 
              loadDictionaryOnly: true,
              requestDynaFields: 'orgCode'
            }
        }
        request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success) {
              if (res && res.hasOwnProperty('success') && res.success && res.dictionary) {
                  that.dictOrgCode = res.dictionary.dictOrgCode
                  that.queryRecord.orgCode = this.$store.state.user.org
                  if (!that.chartOptions) that.refresh()
              }
            }
        }).catch(error => {
        })
    },
    dateRangeString() {
      if (!this.queryRecord || !this.queryRecord.inDate || !this.queryRecord.inDate.length) return ''
      if (typeof this.queryRecord.inDate == 'string') return this.queryRecord.inDate
      else if (this.queryRecord.inDate.length==1) return toString(this.queryRecord.inDate[0],'YYYY-MM-DD')
       else if (this.queryRecord.inDate.length==2) return toString(this.queryRecord.inDate[0],'YYYY-MM-DD') 
               + ' 至 ' + toString(this.queryRecord.inDate[1],'YYYY-MM-DD')
    },
    refresh() {
        const that = this
        let req = {
            url: that.baseUrl + '/query',
            method: 'get',
            params: {
              action: that.action, 
              orgCode: that.queryRecord.orgCode,
              date0: that.queryRecord.inDate.length==2 ?  that.queryRecord.inDate[0] : null,
              date1: that.queryRecord.inDate.length==2 ?  that.queryRecord.inDate[1] : null
            }
        }
        request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success) {
              that.chartOptions = getEChartsOptions(res.data, that.legendField, that.xField, that.yField, 
                                  that.charType,that.mobile,that.exOption)
            }
        }).catch(error => {
        })
    },
    download() {
      const titles = [this.title]
      const dateRange = [this.dateRangeString()]
      const header = [this.xFieldName]
      if (this.chartOptions.legend && this.chartOptions.legend.data && this.chartOptions.legend.data.length>1) {
        this.chartOptions.legend.data.forEach(e => {
          header.push(e)
          titles.push('')
          dateRange.push('')
        })
      } else {
        header.push(this.yFieldName)
        titles.push('')
        dateRange.push('')
      }
      const multiHeader = [titles,dateRange]
      const merges = []
      merges.push("A1:"+String.fromCharCode(header.length+64)+"1")
      merges.push("A2:"+String.fromCharCode(header.length+64)+"2")
      const data = []
      let xAxis = null
      if (this.chartOptions.angleAxis && this.chartOptions.angleAxis.data)  xAxis = this.chartOptions.angleAxis.data
      else if (this.chartOptions.xAxis && this.chartOptions.xAxis.length>0) xAxis = this.chartOptions.xAxis[0].data
      if (xAxis) {
        for (let i=0;i< xAxis.length ;i++) {
          const d = [xAxis[i]]
          const series = this.chartOptions.series.forEach(e => {
            d.push(typeof e.data[i] == 'object' ? e.data[i].value : e.data[i])
          })
          data.push(d)
        }
      } else {
        const series = this.chartOptions.series.forEach(e => {
          e.data.forEach(v=>{
            const d = []
            d.push(typeof v == 'object' ? v.name : '')
            d.push(typeof v == 'object' ? v.value : v)
            data.push(d)
          })
        })
      }
      import('@/utils/Export2Excel').then(excel => {
        excel.export_json_to_excel({
          multiHeader,
          header,
          data,
          merges,
          filename: this.title,
          autoWidth: true,
          bookType: 'xlsx'
        })
      })
    },
    goBack() {
      if (this.mobile) cg.goBack()
    },
    hasMenu() {
      return this.mobile
    }
  }
}
</script>
<style scoped>
  .el-divider {
    margin:0 0 12px 0;
  }
</style>
