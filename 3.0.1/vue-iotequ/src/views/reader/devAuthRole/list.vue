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
          <CgListAuthRole ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('devAuthConfig.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgListAuthConfig ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="20" split="vertical">
          <template slot="paneL">
            <div class="cg-father">
              <CgListAuthRole ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('devAuthConfig.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgListAuthConfig ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListAuthRole from './CgListAuthRole.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgListAuthConfig from '@/views/reader/devAuthConfig/CgListAuthConfig.vue'
const Comp = {
  name: 'AuthRoleList',
  components: { CgListAuthRole, CgListAuthConfig },
  mixins: [ParentList],
  data() {
    return {
      sonCount: 1,
      son0Condition: {
        roleId: 'null'
      },
      sonPkFields: ['id'],
      path: 'list',
      generatorName: 'devAuthRole',
      baseUrl: '/reader/devAuthRole'
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
