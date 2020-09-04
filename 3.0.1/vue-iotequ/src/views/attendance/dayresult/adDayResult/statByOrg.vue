<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="svg-chart/chart-rose" :hasMenu="hasMenu()"
                   :content="'adStat.title.statByOrg'.local()" @goBack="goBack"
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
      <el-row>
        <el-col :span="12">
          <cg-chart ref="chart" width="100%" :height="clientHeight-120" :options="options1" />
        </el-col>
        <el-col :span="12">
          <cg-chart ref="chart" width="100%" :height="clientHeight-120" :options="options2" />
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import cg from '@/utils/cg'
import { toString, dateAdd } from '@/utils/time'
import { request } from '@/utils/request'
// 引入柱状图组件
require('echarts/lib/chart/bar')
require('echarts/lib/chart/pie')
require("echarts/lib/component/polar")
//import echarts from 'echarts'
export default {
  name: 'AdStatByOrg',
  data() {
    return {
      clientHeight: this.height ? this.height : cg.containerHeight(),
      fatherHeight: (this.height ? this.height : cg.containerHeight()),
      childHeight: (this.height?this.height:cg.containerHeight())-70,
      baseUrl: '/attendance/dayresult/adDayResult',
      queryDate: dateAdd(new Date(), -1, 'month'),
      showQuery: false,
      options1: null,
      options2: null
    }
  },
  mounted() {
    if (!this.options1) this.refresh()
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
            params: {action: 'statByOrg', month: this.toString(this.queryDate,'YYYY-MM') }
        }
        request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success) {
                const option1 = {
                    tooltip: {
                        trigger: 'axis',
                        triggerOn :'mousemove',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    angleAxis: {
                    },
                    radiusAxis: {
                        type: 'category',
                        data: res.data.orgList.reduce((total, item)=>{ 
                                        total.push(item.orgName)
                                        return total
                                    }, []),
                        axisLabel: { interval: 0 },
                        z: 10
                    },
                    polar: {
                    },
                    series: [],
                    legend: {
                        show: true,
                        data: res.data.stateList.reduce((total, item)=>{ 
                                        total.push(item.stateName)
                                        return total
                                    }, []),
                    }
                }
                res.data.stateList.forEach(st=>{
                    const d=[]
                    res.data.orgList.forEach(org=>{
                        if (!res.data.data.some(rec=>{
                            if (rec.org_code == org.org_code && rec.state == st.state) {
                                d.push(rec.times)
                                return true
                            }
                        })) d.push(0)
                    })
                    option1.series.push({
                        type: 'bar',
                        data: d,
                        coordinateSystem: 'polar',
                        name: st.stateName,
                        stack: 'a'
                    })
                })
                that.options1 = option1
                const option2 = {
                  tooltip: {
                      trigger: 'item',
                      formatter: '{a} <br/>{b} : {c} ({d}%)'
                  },

                  legend: {
                      left: 'center',
                      top: 'bottom',
                      data: option1.radiusAxis.data
                  },
                  toolbox: {
                      show: true,
                      feature: {
                          mark: {show: true},
                          dataView: {show: true, readOnly: false},
                          magicType: {
                              show: true,
                              type: ['pie', 'funnel']
                          },
                          restore: {show: true},
                          saveAsImage: {show: true}
                      }
                  },
                  series: [],
                   title: [{
                        text: '违例考勤分布'
                    }, {
                        subtext: '旷工',
                        left: '25%',
                        top: '75%',
                        textAlign: 'center'
                    }, {
                        subtext: '迟到',
                        left: '75%',
                        top: '48%',
                        textAlign: 'center'
                    }, {
                        subtext: '早退',
                        left: '75%',
                        top: '80%',
                        textAlign: 'center'
                    }],
               }
               res.data.stateList.forEach(st=>{
                 if (st.state == 2 || st.state == 4 || st.state == 5) {
                    const d=[]
                    res.data.orgList.forEach(org=>{
                        if (!res.data.data.some(rec=>{
                            if (rec.org_code == org.org_code && rec.state == st.state) {
                                d.push({value: rec.times, name: org.orgName})
                                return true
                            }
                        })) d.push(0)
                    })
                    option2.series.push({
                        type: 'pie',
                        data: d,
                        name: st.stateName,
                        label: { show : st.state==2 },
                        radius: ['5%', '25%'],
                        center: [st.state==2?'25%':'75%', st.state==2?'50%':(st.state==4?'33%':'67%')],
                        roseType: 'radius'

                    })
                  }
                })
               console.log(option2)
               that.options2 = option2
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
