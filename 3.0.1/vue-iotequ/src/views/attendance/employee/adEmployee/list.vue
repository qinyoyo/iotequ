<template>
  <div class="cg-list cg-list-adEmployee">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListAdEmployee ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord" @rowClick="rowClick"/>
    </el-card>
  </div>
</template>

<script>
import CgListAdEmployee from './CgListAdEmployee.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'AdEmployeeList',
  components: { CgListAdEmployee },
  mixins: [ParentList],
  data() {
    return {
      titleField: 'realName',
      path: 'list',
      generatorName: 'adEmployee',
      baseUrl: '/attendance/employee/adEmployee'
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /list-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
