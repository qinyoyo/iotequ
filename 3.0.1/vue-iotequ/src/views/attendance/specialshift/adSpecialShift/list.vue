<template>
  <div class="cg-list cg-list-adSpecialShift">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListAdSpecialShift ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('adSpecialShift.title.record')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgFormAdSpecialShift ref="cgList_son0" :height="childHeight" :queryById.sync="son0Condition" />
            </el-tab-pane>
            <el-tab-pane :label="$t('adSpecialShiftTime.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
              <CgListAdSpecialShiftTime ref="cgList_son1" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="50" split="vertical">
          <template slot="paneL">
            <div class="cg-father">
              <CgListAdSpecialShift ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('adSpecialShift.title.record')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgFormAdSpecialShift ref="cgList_son0" :height="childHeight" :queryById.sync="son0Condition" />
              </el-tab-pane>
              <el-tab-pane :label="$t('adSpecialShiftTime.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
                <CgListAdSpecialShiftTime ref="cgList_son1" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListAdSpecialShift from './CgListAdSpecialShift.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgFormAdSpecialShift from '@/views/attendance/specialshift/adSpecialShift/CgFormAdSpecialShift.vue'
import CgListAdSpecialShiftTime from '@/views/attendance/specialshifttime/adSpecialShiftTime/CgListAdSpecialShiftTime.vue'
const Comp = {
  name: 'AdSpecialShiftList',
  components: { CgListAdSpecialShift, CgFormAdSpecialShift, CgListAdSpecialShiftTime },
  mixins: [ParentList],
  data() {
    return {
      titleField: 'name',
      sonCount: 2,
      son0Condition: 'null',
      son1Condition: {
        specialShiftId: 'null'
      },
      sonPkFields: ['id', 'id'],
      path: 'list',
      generatorName: 'adSpecialShift',
      baseUrl: '/attendance/specialshift/adSpecialShift'
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
