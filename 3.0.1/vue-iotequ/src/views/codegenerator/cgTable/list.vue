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
          <CgListCgTable ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('cgTable.title.record')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgFormCgTable ref="cgList_son0" :height="childHeight" :queryById.sync="son0Condition" />
            </el-tab-pane>
            <el-tab-pane :label="$t('cgField.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
              <CgListCgField ref="cgList_son1" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
            </el-tab-pane>
            <el-tab-pane :label="$t('cgButton.title.list')" name="cgList_son2" :style="{height: childHeight+'px'}">
              <CgListCgButton ref="cgList_son2" :height="childHeight" :fixedQueryRecord.sync="son2Condition" />
            </el-tab-pane>
            <el-tab-pane :label="$t('cgList.title.list')" name="cgList_son3" :style="{height: childHeight+'px'}">
              <ListViewCgList ref="cgList_son3" :height="childHeight" :fixedQueryRecord.sync="son3Condition" />
            </el-tab-pane>
            <el-tab-pane :label="$t('cgForm.title.list')" name="cgList_son4" :style="{height: childHeight+'px'}">
              <ListViewCgForm ref="cgList_son4" :height="childHeight" :fixedQueryRecord.sync="son4Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="40" split="vertical">
          <template slot="paneL">
            <div class="cg-father">
              <CgListCgTable ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('cgTable.title.record')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgFormCgTable ref="cgList_son0" :height="childHeight" :queryById.sync="son0Condition" />
              </el-tab-pane>
              <el-tab-pane :label="$t('cgField.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
                <CgListCgField ref="cgList_son1" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
              </el-tab-pane>
              <el-tab-pane :label="$t('cgButton.title.list')" name="cgList_son2" :style="{height: childHeight+'px'}">
                <CgListCgButton ref="cgList_son2" :height="childHeight" :fixedQueryRecord.sync="son2Condition" />
              </el-tab-pane>
              <el-tab-pane :label="$t('cgList.title.list')" name="cgList_son3" :style="{height: childHeight+'px'}">
                <ListViewCgList ref="cgList_son3" :height="childHeight" :fixedQueryRecord.sync="son3Condition" />
              </el-tab-pane>
              <el-tab-pane :label="$t('cgForm.title.list')" name="cgList_son4" :style="{height: childHeight+'px'}">
                <ListViewCgForm ref="cgList_son4" :height="childHeight" :fixedQueryRecord.sync="son4Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListCgTable from './CgListCgTable.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgFormCgTable from '@/views/codegenerator/cgTable/CgFormCgTable.vue'
import CgListCgField from '@/views/codegenerator/cgField/CgListCgField.vue'
import CgListCgButton from '@/views/codegenerator/cgButton/CgListCgButton.vue'
import ListViewCgList from '@/views/codegenerator/cgList/list.vue'
import ListViewCgForm from '@/views/codegenerator/cgForm/list.vue'
const Comp = {
  name: 'CgTableList',
  components: { CgListCgTable, CgFormCgTable, CgListCgField, CgListCgButton, ListViewCgList, ListViewCgForm },
  mixins: [ParentList],
  data() {
    return {
      titleField: 'title',
      sonCount: 5,
      son0Condition: 'null',
      son1Condition: {
        tableId: 'null'
      },
      son2Condition: {
        tableId: 'null'
      },
      son3Condition: {
        tableId: 'null'
      },
      son4Condition: {
        tableId: 'null'
      },
      sonPkFields: ['id', 'id', 'id', 'id', 'id'],
      path: 'list',
      generatorName: 'cgTable',
      baseUrl: '/codegenerator/cgTable'
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
