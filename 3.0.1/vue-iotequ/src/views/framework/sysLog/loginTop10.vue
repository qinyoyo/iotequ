<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="fa fa-sort-amount-desc" :hasMenu="hasMenu()"
                   :content="'sysLog.action.loginTop10'.local()" @goBack="goBack"
        />
      </div>
      <cg-chart ref="chart" width="100%" :height="clientHeight" :options="options" />
    </el-card>
  </div>
</template>

<script>
import cg from '@/utils/cg'
import { request } from '@/utils/request'
// 引入柱状图组件
require('echarts/lib/chart/bar')
export default {
  name: 'SysLoginTop10',
  data() {
    return {
      clientHeight: this.height ? this.height : cg.containerHeight(),
      fatherHeight: (this.height ? this.height : cg.containerHeight()),
      childHeight: (this.height?this.height:cg.containerHeight())-70,
      baseUrl: '/framework/sysLog',
      options: null
    }
  },
  mounted() {
    if (!this.options) {
        let req = {
            url: this.baseUrl + '/query',
            method: 'get',
            params: {action: 'loginTop10'}
        }
        request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success) {
                const category = {
                            type: 'category',
                            data: res.data.reduce((total, item)=>{ 
                                        total.push(item.userInfo)
                                        return total
                                    }, []),
                            axisTick: {
                                alignWithLabel: true
                            }
                        }
                let option = {
                    color: ['#3398DB'],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: [
                        this.mobile ? { type: 'value' } : category
                    ],
                    yAxis: [
                        this.mobile ? category : { type: 'value' }
                    ],
                    series: [
                        {
                            name: '',
                            type: 'bar',
                            barWidth: '60%',
                            data: res.data.reduce((total, item)=>{ 
                                        total.push(item.times)
                                        return total
                                    }, [])
                        }
                    ]
                }
                this.options = option
            }
        }).catch(error => {
        })
    }
  },
  
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    goBack() {
      if (this.mobile) cg.goBack()
    },
    hasMenu() {
      return this.mobile
    }
  }
}
</script>
