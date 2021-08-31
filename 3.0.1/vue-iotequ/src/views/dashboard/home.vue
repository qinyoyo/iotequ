<template>
  <div style="padding:16px">
    <div style="text-align:center; font-size:24px;">
        <span>成都市郫都区医疗保障局</span>
    </div>
    <div style="text-align:center; font-size:18px; padding-top:12px;">
        <span>血透中心指静脉医保基金监管系统</span>
    </div>
    <el-divider/>
    <div>
      <el-row>
        <el-col :span="12">
          <div>
            <cg-chart ref="chart1" width="100%" :height="containerHeight()/2" :options="chartOptions1" />
          </div>
          <div>
            <cg-chart ref="chart2" width="100%" :height="containerHeight()/2" :options="chartOptions2" />
          </div>
        </el-col>
        <el-col :span="12">
          <cg-chart ref="chart3" width="100%" :height="containerHeight()" :options="chartOptions3" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { containerHeight } from '@/utils/cg'
import { getEChartsOptions } from '@/views/check-in/ckRegister/statOption'
import { request } from '@/utils/request'
import echarts from 'echarts'
export default {
  name: 'Home',
  data() {
    return {
      chartOptions1: {},
      chartOptions2: {},
      chartOptions3: {}
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  created() {
        const that = this
        let req = {
            url: '/common/home/data',
            method: 'get'
        }
        request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success && res.data) {

              let ex =  {
                  toolbox:null,
                  legend:{
                    top: '32px',
                  },
                  title: {
                    x: 'center'
                  } 
              }
              if (res.data.hasOwnProperty('ageData')) {
                that.chartOptions1 = Object.assign(getEChartsOptions(res.data.ageData, "age", "month", 'amount', 'bar',that.mobile),
                  ex,{
                  title: { text : '实名制就医流量按年龄段月度汇总(人次)',x: 'center'}
                })
              }
              if (res.data.hasOwnProperty('amountData')) {
                  that.chartOptions2 = Object.assign(getEChartsOptions(res.data.amountData, "org_name", 'month', 'amount', 'line',that.mobile),
                  ex,{
                  title: { text : '各医院实名制就医流量月度统计(人次)',x: 'center'}
                })
              }
              if (res.data.hasOwnProperty('areaData')) {
                  that.chartOptions3 = Object.assign(getEChartsOptions(res.data.areaData, "", 'area', 'amount', 'pie',that.mobile,{series: {radius: [0, '40%']}}),
                  ex,{
                    legend: {show: false},
                    title: { text : '实名制患者户籍分布图',x: 'center'}
                  })     
              }
            }
        }).catch(error => {
        })
  },
  methods: {
    containerHeight
  }
}
</script>

<style lang="scss" scoped>
  .emptyGif {
    display: block;
    width: 50%;
    margin: 20px auto;
  }

  .home-editor-container {
    height: 100%;
    padding: 50px 60px 0px;
    .user-name {
      position: relative;
      top: -20px;
      font-size: 48px;
    }
    .info-container {
      position: relative;
      height: 32px;
      font-size: 24px;
    }
  }
</style>
