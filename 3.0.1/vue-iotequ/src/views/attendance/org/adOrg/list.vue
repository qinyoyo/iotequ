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
          <CgListAdOrg ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('adEmployee.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgListAdEmployee ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="50" split="horizontal" @resize="resize">
          <template slot="paneL">
            <div class="cg-father">
              <CgListAdOrg ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('adEmployee.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgListAdEmployee ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListAdOrg from './CgListAdOrg.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgListAdEmployee from '@/views/attendance/employee/adEmployee/CgListAdEmployee.vue'
const mixins = [ParentList]
const mixinContext = require.context('.', false, /list-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'AdOrgList',
  components: { CgListAdOrg, CgListAdEmployee },
  mixins,
  data() {
    return {
      fatherHeightPercent: 50,
      titleField: 'name',
      sonCount: 1,
      son0Condition: {
        orgCode: 'null'
      },
      sonPkFields: ['orgCode'],
      path: 'list',
      generatorName: 'adOrg',
      baseUrl: '/attendance/org/adOrg'
    }
  }
}
</script>
