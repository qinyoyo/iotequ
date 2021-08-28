<template>
  <div class="cg-form cg-form-cgListField">
    <el-dialog ref="dialog" v-el-drag-dialog :visible.sync="showDialog" top="0px" :class="'cg-form cg-form-cgListField'+(dialogClass?' '+dialogClass:'')" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" :before-close="beforeClose" @closed="close">
      <div slot="title" :class="titleColor">
        <cg-header :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="close"
        />
      </div>
      <CgFormCgListField ref="cgForm"
                         :routeParams="routeParams"
                         :showInDialog="showDialog"
                         @closeDialog="handleClose"
                         @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/dialog'
import CgFormCgListField from './CgFormCgListField'
const Comp = {
  name: 'CgListFieldForm',
  components: { CgFormCgListField },
  mixins: [ParentForm],
  data() {
    return {
      path: 'record',
      generatorName: 'cgListField',
      baseUrl: '/codegenerator/cgListField'
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
