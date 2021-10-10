<template>
  <div class="cg-form cg-form-adOrg">
    <el-dialog ref="dialog" v-el-drag-dialog :visible.sync="showDialog" top="0px" v-if="showDialog || !destroyOnClose"
               :class="'cg-form cg-form-adOrg'+(dialogClass?' '+dialogClass:'')" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" :before-close="beforeClose" @closed="close">
      <div slot="title" :class="titleColor">
        <cg-header :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="close"
        />
      </div>
      <CgFormAdOrg ref="cgForm"
                   :routeParams="routeParams"
                   :showInDialog="showDialog"
                   @closeDialog="handleClose"
                   @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/dialog'
import CgFormAdOrg from './CgFormAdOrg'
const Comp = {
  name: 'AdOrgForm',
  components: { CgFormAdOrg },
  mixins: [ParentForm],
  data() {
    return {
      destroyOnClose: true,
      allowAddRecord: false,
      path: 'record',
      generatorName: 'adOrg',
      baseUrl: '/attendance/org/adOrg'
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
