<template>
  <div class="cg-form cg-form-devPeopleMapping">
    <el-card shadow="hover">
      <div slot="header" :class="titleColor">
        <cg-header hoemMenu :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="goBack"
        />
      </div>
      <CgFormPeopleMapping ref="cgForm"
                           :routeParams="routeParams"
                           @openModeChanged="openModeChanged" />
    </el-card>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/record'
import CgFormPeopleMapping from './CgFormPeopleMapping'
const Comp = {
  name: 'PeopleMappingForm',
  components: { CgFormPeopleMapping },
  mixins: [ParentForm],
  data() {
    return {
      allowViewRecord: false,
      allowEditRecord: false,
      path: 'record',
      generatorName: 'devPeopleMapping',
      baseUrl: '/reader/devPeopleMapping'
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
