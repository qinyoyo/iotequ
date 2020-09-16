<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListRole ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('sysPermission.title.record')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgFormPermissionTree ref="cgList_son0" :height="childHeight" :queryById.sync="son0Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="50" split="vertical">
          <template slot="paneL">
            <div class="cg-father">
              <CgListRole ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('sysPermission.title.record')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgFormPermissionTree ref="cgList_son0" :height="childHeight" :queryById.sync="son0Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListRole from './CgListRole.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgFormPermissionTree from '@/views/common-views/extend-views/CgFormPermissionTree.vue'
const Comp = {
  name: 'RoleList',
  components: { CgListRole, CgFormPermissionTree },
  mixins: [ParentList],
  data() {
    return {
      titleField: 'name',
      sonCount: 1,
      son0Condition: 'null',
      sonPkFields: ['id'],
      path: 'list',
      generatorName: 'sysRole',
      baseUrl: '/framework/sysRole'
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
