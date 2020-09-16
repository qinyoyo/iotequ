<template>
  <div class="cg-form" :class="dialogClass">
    <el-dialog ref="dialog" v-el-drag-dialog :visible.sync="showDialog" top="0px" :class="dialogClass" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" :before-close="beforeClose" @closed="close">
      <div slot="title" :class="titleColor">
        <cg-header :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="close"
        />
      </div>
      <CgFormPmVersionFlow ref="cgForm"
                           :dialogParams="dialogParams"
                           :showInDialog="showDialog"
                           @closeDialog="handleClose"
                           @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/dialog'
import CgFormPmVersionFlow from './CgFormPmVersionFlow'
const Comp = {
  name: 'PmVersionFlowForm',
  components: { CgFormPmVersionFlow },
  mixins: [ParentForm],
  data() {
    return {
      isFlowRecord: true,
      defaultIcon: {
         assess: 'fa fa-binoculars',
         decision: 'fa fa-gavel',
         publish: 'fa fa-check',
         review: 'fa fa-search-plus'
      },
      path: this.openParams().flowAction ? this.openParams().flowAction : 'assess',
      generatorName: 'pmVersionApplication',
      baseUrl: '/project/version/pmVersionApplication'
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /assess-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
