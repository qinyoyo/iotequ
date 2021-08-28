<template>
  <div class="cg-form cg-form-cgFormField">
    <el-card shadow="hover">
      <div slot="header" :class="titleColor">
        <cg-header hoemMenu :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="goBack"
        />
      </div>
      <CgFormCgFormField ref="cgForm"
                         :routeParams="routeParams"
                         @openModeChanged="openModeChanged" />
    </el-card>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/record'
import CgFormCgFormField from './CgFormCgFormField'
const Comp = {
  name: 'CgFormFieldForm',
  components: { CgFormCgFormField },
  mixins: [ParentForm],
  data() {
    return {
      path: 'record',
      generatorName: 'cgFormField',
      baseUrl: '/codegenerator/cgFormField'
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
