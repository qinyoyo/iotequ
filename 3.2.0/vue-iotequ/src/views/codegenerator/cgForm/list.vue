<template>
  <div class="cg-list cg-list-cgForm">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListCgForm ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('cgForm.title.record')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgFormCgForm ref="cgList_son0" :height="childHeight" :queryById.sync="son0Condition" />
            </el-tab-pane>
            <el-tab-pane :label="$t('cgFormField.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
              <CgListCgFormField ref="cgList_son1" :tableId="tableId" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="15" split="horizontal" @resize="resize">
          <template slot="paneL">
            <div class="cg-father">
              <CgListCgForm ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('cgForm.title.record')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgFormCgForm ref="cgList_son0" :height="childHeight" :queryById.sync="son0Condition" />
              </el-tab-pane>
              <el-tab-pane :label="$t('cgFormField.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
                <CgListCgFormField ref="cgList_son1" :tableId="tableId" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListCgForm from './CgListCgForm.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgFormCgForm from '@/views/codegenerator/cgForm/CgFormCgForm.vue'
import CgListCgFormField from '@/views/codegenerator/cgFormField/CgListCgFormField.vue'
const Comp = {
  name: 'CgFormList',
  components: { CgListCgForm, CgFormCgForm, CgListCgFormField },
  mixins: [ParentList],
  data() {
    return {
      fatherHeightPercent: 15,
      titleField: 'name',
      sonCount: 2,
      son0Condition: 'null',
      son1Condition: {
        formId: 'null'
      },
      sonPkFields: ['id', 'id'],
      path: 'list',
      generatorName: 'cgForm',
      baseUrl: '/codegenerator/cgForm'
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
