<template>
  <div class="cg-form">
    <el-card shadow="hover">
      <div slot="header" :class="titleColor">
        <cg-header hoemMenu :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="goBack"
        />
      </div>
      <CgFormEvent ref="cgForm"
                   :routeParams="routeParams"
                   @openModeChanged="openModeChanged" />
    </el-card>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/record'
import CgFormEvent from './CgFormEvent'
const Comp = {
  name: 'EventForm',
  components: { CgFormEvent },
  mixins: [ParentForm],
  data() {
    return {
      allowEditRecord: false,
      allowAddRecord: false,
      defaultIcon: 'fa fa-user-circle-o',
      path: 'record',
      generatorName: 'devEvent',
      baseUrl: '/reader/devEvent'
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /record-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
