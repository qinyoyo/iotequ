<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="svg-chart/chart-rose" :hasMenu="hasMenu()"
                   :content="'ckStat.title.amountByDay'.local()" @goBack="goBack"
        />
      </div>
      <el-row>
        <el-col :span="8">
          <cg-cascader v-model="queryRecord.orgCode" name="orgCode" collapse-tags clearable
                       :dictionary="dictOrgCode" show-all-levels/>
        </el-col>
        <el-col :span="12">
            <cg-date-picker v-model="queryRecord.inDate" :title="$t('ckRegister.field.inDate')" name="inDate" :align="mobile?'right':'center'" type="daterange" 
                            :picker-options="datePickerOptions()"
                            clearable />
        </el-col>
        <el-col :span="4">
           <el-button class="cg-button" type="primary" plain icon="el-icon-check" @click.native="refresh()">
             {{ $t('system.action.refresh') }}
           </el-button>
        </el-col>
      </el-row>      
      <el-divider />
      <div>
        <cg-chart ref="chart" width="100%" :height="clientHeight-120" :options="chartOptions" />
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

function distincOf(data,field) {
  let r = []
  for (let i=0;i<data.length;i++) {
     if (r.indexOf(data[i][field])<0) r.push(data[i][field]) 
  }
  return r
}
function getValue(data, field, test) {
    for (let i=0;i<data.length;i++) {
      if (test(data[i])) return data[i][field] 
    }
    return 0
}
function getEChartsOptions(data) {
  var legend = distincOf(data,'org_name')
  var xAxis = distincOf(data,'in_date')
  var series = []
  for (let i=0;i<legend.length;i++) {
    let s = {
        name: legend[i],
        type: 'bar',
        barGap: 0,
        emphasis: {
            focus: 'series'
        },
        data: []
     }
     for (let j=0;j<xAxis.length;j++) {
       let test = function(d) {
         return (d.org_name == legend[i] && d.in_date == xAxis[j])
       }
       s.data.push(getValue(data,'amount',test))
     } 
     series.push(s)
  }
  var option = {
      tooltip: {
          trigger: 'axis',
          axisPointer: {
              type: 'shadow'
          }
      },
      legend: {
          data: legend
      },
      toolbox: {
          show: true,
          orient: 'vertical',
          left: 'right',
          top: 'center',
          feature: {
              mark: {show: true},
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
              restore: {show: true},
              saveAsImage: {show: true}
          }
      },
      xAxis: [
          {
              type: 'category',
              axisTick: {show: false},
              data: xAxis
          }
      ],
      yAxis: [
          {
              type: 'value'
          }
      ],
      series: series,
      notMerge: true,
      lazyUpdate: false,
      silent: true
  };
  return option
}
export default {
  name: 'CkAmountByDay',
  data() {
    return {
      clientHeight: containerHeight(),
      baseUrl: '/check-in/ckRegister',
      dictOrgCode: [],
      queryRecord: {
        orgCode: this.$store.state.user.org,
        inDate: [startOf(null,'month'),endOf(null,'month')]
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
    }
  },
  methods: {
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
    refresh() {
        const that = this
        let req = {
            url: that.baseUrl + '/query',
            method: 'get',
            params: {
              action: 'amountByDay', 
              orgCode: that.queryRecord.orgCode,
              date0: that.queryRecord.inDate.length==2 ?  that.queryRecord.inDate[0] : null,
              date1: that.queryRecord.inDate.length==2 ?  that.queryRecord.inDate[1] : null
            }
        }
        request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success) {
              that.chartOptions = getEChartsOptions(res.data)
            }
        }).catch(error => {
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
