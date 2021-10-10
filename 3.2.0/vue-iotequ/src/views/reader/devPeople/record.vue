<template>
  <div class="cg-form cg-form-devPeople">
    <el-card shadow="hover">
      <div slot="header" :class="titleColor">
        <cg-header hoemMenu :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="goBack"
        />
      </div>
      <CgFormDevPeople ref="cgForm"
                       :routeParams="routeParams"
                       @openModeChanged="openModeChanged" />
    </el-card>
  </div>
</template>

<script>
import ParentForm from '@/views/common-views/components/record'
import CgFormDevPeople from './CgFormDevPeople'
const Comp = {
  name: 'DevPeopleForm',
  components: { CgFormDevPeople },
  mixins: [ParentForm],
  data() {
    return {
      defaultIcon: 'fa fa-user-circle-o',
      path: 'record',
      generatorName: 'devPeople',
      baseUrl: '/reader/devPeople'
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
