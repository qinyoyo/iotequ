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
      <CgFormRole ref="cgForm"
                  :dialogParams="dialogParams"
                  :showInDialog="showDialog"
                  @closeDialog="handleClose"
                  @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/dialog'
import CgFormRole from './CgFormRole'
const Comp = {
  name: 'RoleForm',
  components: { CgFormRole },
  mixins: [ParentForm],
  data() {
    return {
      allowViewRecord: false,
      allowEditRecord: false,
      path: 'record',
      generatorName: 'sysRole',
      baseUrl: '/framework/sysRole'
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
