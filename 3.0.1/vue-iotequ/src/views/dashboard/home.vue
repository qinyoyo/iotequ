<template>
  <div class="home-editor-container">
    <div class="info-container" style="text-align:center; font-size:24px;">
        <span>医疗保障局血透中心指静脉监管系统</span>
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
  created() {
        const that = this
        let req = {
            url: '/common/home/data',
            method: 'get'
        }
        request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success && res.data) {
              if (res.data.hasOwnProperty('ageData')) {
                  that.chartOptions1 = getEChartsOptions(res.data.ageData, "age", "month", 'amount', 'bar')
                  that.chartOptions1.toolbox=null
                  that.chartOptions1.title = {
                      text: '实名制患者总览',
                      x: 'center'
                  }
                  that.chartOptions1.legend.top = '32px'
              }
              if (res.data.hasOwnProperty('amountData')) {
                  that.chartOptions2 = getEChartsOptions(res.data.amountData, "org_name", 'month', 'amount', 'line')
                  that.chartOptions2.toolbox=null
                  that.chartOptions2.title = {
                      text: '实名制就医流量统计',
                      x: 'center'
                  }
                  that.chartOptions2.legend.top = '32px'
              }
              if (res.data.hasOwnProperty('areaData')) {
                  that.chartOptions3 = getEChartsOptions(res.data.areaData, "", 'area', 'amount', 'pie')
                  that.chartOptions3.toolbox=null
                  that.chartOptions3.title = {
                      text: '实名制患者区域',
                      x: 'center'
                  } 
                  that.chartOptions3.legend.top = '32px'                 
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
