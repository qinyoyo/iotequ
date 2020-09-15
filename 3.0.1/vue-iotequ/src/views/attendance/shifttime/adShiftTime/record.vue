<template>
  <div class="cg-form">
    <el-card shadow="hover">
      <div slot="header" :class="titleColor">
        <cg-header hoemMenu :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="goBack"
        />
      </div>
      <CgFormAdShiftTime ref="cgForm"
                         @openModeChanged="openModeChanged" />
    </el-card>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/record'
import CgFormAdShiftTime from './CgFormAdShiftTime'
const mixins = [ParentForm]
const mixinContext = require.context('.', false, /record-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'AdShiftTimeForm',
  components: { CgFormAdShiftTime },
  mixins,
  data() {
    return {
      path: 'record',
      generatorName: 'adShiftTime',
      baseUrl: '/attendance/shifttime/adShiftTime'
    }
  }
}
</script>
