<template>
  <div class="cg-form cg-form-cgField">
    <el-card shadow="hover">
      <div slot="header" :class="titleColor">
        <cg-header hoemMenu :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="goBack"
        />
      </div>
      <CgFormCgField ref="cgForm"
                     :routeParams="routeParams"
                     @openModeChanged="openModeChanged" />
    </el-card>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/record'
import CgFormCgField from './CgFormCgField'
const Comp = {
  name: 'CgFieldForm',
  components: { CgFormCgField },
  mixins: [ParentForm],
  data() {
    return {
      path: 'record',
      generatorName: 'cgField',
      baseUrl: '/codegenerator/cgField'
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
