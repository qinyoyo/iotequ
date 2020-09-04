<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="svg-chart/chart-scatter" :hasMenu="hasMenu()"
                   :content="'adStat.title.statRecTime'.local()" @goBack="goBack"
        />
      </div>
      <a class="link-text" @click="showQuery=!showQuery">{{ 'adStat.title.statMonth'.local() + ' : ' + toString(queryDate,'YYYY-MM') }}</a> 
      <el-divider />
      <cg-query-condition v-model="showQuery" :modal="true"
                          @refresh="refresh()" @reset="queryDate = dateAdd(new Date(), -1, 'month')">
        <el-form-item :label="$t('adStat.title.selectMonth')" prop="queryDate" :size="$store.state.app.size">
          <el-date-picker v-model="queryDate" type="month" />
        </el-form-item>        
      </cg-query-condition>
          <cg-chart ref="chart" width="100%" :height="clientHeight-120" :options="options" />
    </el-card>
  </div>
</template>

<script>
import cg from '@/utils/cg'
import { toString, dateAdd,endOf,partOf } from '@/utils/time'
import { request } from '@/utils/request'

require('echarts/lib/chart/scatter')
//import echarts from 'echarts'
const timeString = function (t) {
  const m = t % 100
  const h = (t - m) / 100
  return h+':'+m
}
const timeValue = function(t) {
  const m = t % 100
  const h = (t - m) / 100
  return h+m/60.0
}
export default {
  name: 'AdStatRecTime',
  data() {
    return {
      clientHeight: this.height ? this.height : cg.containerHeight(),
      fatherHeight: (this.height ? this.height : cg.containerHeight()),
      childHeight: (this.height?this.height:cg.containerHeight())-70,
      baseUrl: '/attendance/dayresult/adDayResult',
      queryDate: dateAdd(new Date(), -1, 'month'),
      showQuery: false,
      options: null
    }
  },
  mounted() {
    if (!this.options) this.refresh()
  },
  
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    toString,
    dateAdd,
    refresh() {
        this.showQuery = false
        const that = this
        let req = {
            url: this.baseUrl + '/query',
            method: 'get',
            params: {action: 'statRecTime', month: this.toString(this.queryDate,'YYYY-MM') }
        }
        request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success) {
              const workOn=[],workOff=[]
              let delta=0.0
              res.data.forEach(d=>{
                if (d.start_time) workOn.push([d.day-delta,timeValue(d.start_time),timeString(d.start_time)+' '+d.org_name+'/'+d.real_name])
                if (d.end_time) workOff.push([d.day-delta,timeValue(d.end_time),timeString(d.end_time)+' '+d.org_name+'/'+d.real_name])
                delta += 0.1
                if (delta>=1.0) delta = 0.0
              })
              const option = {
                  title: {
                      text: 'adStat.title.statRecTime'.local()
                  },
                  legend: {
                      right: 100,
                      data: ['上班', '下班']
                  },
                  xAxis: {
                      splitLine: {
                          lineStyle: {
                              type: 'dashed'
                          }
                      },
                      interval:1,
                      max: partOf(endOf(this.queryDate,'month'),'day')
                  },
                  yAxis: {
                      splitLine: {
                          lineStyle: {
                              type: 'dashed'
                          }
                      },
                      scale: true,
                      interval:1
                  },
                  series: [{
                      name: '上班',
                      data: workOn,
                      type: 'scatter',
                      symbolSize: function (data) {
                          return 5
                      },
                      emphasis: {
                          label: {
                              show: true,
                              formatter: function (param) {
                                  return param.data[2]
                              },
                              position: 'top'
                          }
                      }
                  }, {
                      name: '下班',
                      data: workOff,
                      type: 'scatter',
                      symbolSize: function (data) {
                          return 5;
                      },
                      emphasis: {
                          label: {
                              show: true,
                              formatter: function (param) {
                                  return param.data[2];
                              },
                              position: 'top'
                          }
                      }
                  }]
              };
              that.options = option
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
