<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :icon="icon" :hasMenu="hasMenu()"
                   :content="title" @goBack="goBack"
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

import { getEChartsOptions } from './statOption'
export default {
  name: 'CkBaseStat',
  props: {
      title: {
          type: String,
          default: 'ckStat.title.amountByDay'.local()
      },
      icon: {
          type: String,
          default: 'svg-chart/chart-rose'
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
      yField: {
          type: String,
          default: 'amount',
      },
      charType: {
          type: String,
          default: 'bar'
      },
      initRange: {
          type: Array,
          default: [startOf(null,'month'),endOf(null,'month')]
      }
  },
  data() {
    return {
      clientHeight: containerHeight(),
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
              action: that.action, 
              orgCode: that.queryRecord.orgCode,
              date0: that.queryRecord.inDate.length==2 ?  that.queryRecord.inDate[0] : null,
              date1: that.queryRecord.inDate.length==2 ?  that.queryRecord.inDate[1] : null
            }
        }
        request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success) {
              that.chartOptions = getEChartsOptions(res.data, that.legendField, that.xField, that.yField, that.charType)
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
