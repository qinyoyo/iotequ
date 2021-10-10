<template>
  <div v-if="actions.length>0" ref="cgAction" class="cg-action" >
    <el-drawer v-if="mobile" ref="drawer" :direction="isLandscape()?'rtl':'btt'" :visible.sync="showActionView" append-to-body :size="mobileSize()">
      <div class="grid-box" :style="gridStyle()">
        <div class="grid-cell" v-for="(item,index) in actions" :key="index" @click="doAction(item)">
            <el-button :icon="item.icon" plain />
            <p class="button-text">{{item.title.local()}}</p>
        </div>
      </div>
      <div slot="title">
        <i class="fa fa-home action-sheet-home" @click="doAction({action: 'goHome'})"></i>
        <span class="action-sheet-title">{{$t('system.action.pleaseSelect')}}</span>
      </div>
    </el-drawer>
    <el-popover v-else v-model="showActionView" placement="top-start" width="100%" :visible-arrow="false" trigger="manual">
        <div v-if="mode==='1'">
          <el-button v-for="(item,index) in actions" :key="index"
                      :title="item.title.local()"
                      :class="item.splited?'cg-button-group':''"
                      :icon="item.icon" plain circle :type="item.type" 
                      @click="doAction(item)" />
        </div>
        <div v-else>
          <el-dropdown-item v-for="(item,index) in actions" :key="index" :command="index" 
                            :divided="index > 0 && actions[index-1].splited" 
                            @click.native="doAction(item)" >
            <cg-icon :size="16" :icon="item.icon+' fa-fw'"></cg-icon>
            <span>{{item.title.local()}}</span>         
          </el-dropdown-item>
        </div>
      <div slot="reference" :class="'popover-toolbar-'+this.mode"></div>
    </el-popover>
    <el-upload v-if="hasImport" :action="actionUrl('import')" ref="upload"
        accept=".xlsx,.xls" :limit="1" with-credentials :show-file-list="false"
        :on-success="uploadSuccess" :on-error="uploadFailure" :before-upload="startUpload">
        <el-button v-show="false" ref="doImport"/>
    </el-upload>
  </div>
</template>
<script>

import cg from '@/utils/cg'
import { systemActionParams} from '@/utils/cg'
import { showMessageFromServer,showProgress,closeProgress } from '@/utils/request'
export default {
  name: 'CgAction',
  props: {
    value: {
      type: Boolean,
      default: false
    },
    mode: {
      type:String,
      default:'1'
    },
    actions: {
      type: Array,
      default: ()=>[]
    },
    url: String
  },
  data() {
    return {
      mobile: this.$store.state.app.device === 'mobile',
      showActionView: this.value
    }
  },
  computed:{
    style() {
      const height = 40 * this.actions.length
      let width = 32
      this.actions.forEach(a => {
        let w = cg.getTextWidth(a.title.local())
        if (w > width) width = w
      })
      width = (60 + width)
      return {top: '0px', left: '0px', width : width + 'px' }
    },
    hasImport() {
      return this.actions.some(a=>{ if (a.action=='import') return true })
    }
  },
  watch: {
    value(newValue) {
      this.showActionView = newValue
      if (!this.mobile) {
        if (newValue) window.addEventListener('click', this.hideActionView, true)
        else window.removeEventListener('click', this.hideActionView, true)
      }
    },
    showActionView(newValue) {
      this.$emit('input', newValue)
    }
  },
  methods: {
    isLandscape() {
      return window.orientation == 90 || window.orientation == -90
    },
    gridStyle() {
      let cols = 3, rows = 3
      if (this.isLandscape()) {
        rows = 3
        cols = parseInt((this.actions.length + rows - 1) / rows)
      } else {
        cols = 4
        rows = parseInt((this.actions.length + cols - 1) / cols)
      }
      return 'grid-template-columns: repeat(' + cols + ', 1fr); grid-template-rows: repeat(' + rows + ', 1fr);'
    },
    mobileSize() {
      if (this.isLandscape()) {
        return (parseInt((this.actions.length + 2) / 3) * 90 )  + 'px'
      } else return (parseInt((this.actions.length + 3) / 4) * 90 + 60)  + 'px'
    },
    doAction(item) {
      this.showActionView=false
      if (item.action==='import') {
        this.$refs.doImport.$el.click()
      }
      else this.$emit('actionClick',(item.prefix ? item.prefix : '') + item.action)
    },
    actionUrl(action) {
      return cg.apiUrl(cg.concatUrl(this.url ,action))
    },
    startUpload() {
        showProgress()
        return true
    },
    uploadSuccess(response, file, fileList) {
      closeProgress()
      showMessageFromServer(response)
      this.$refs.upload.clearFiles()
      this.$emit('actionClick','refresh')
    },
    uploadFailure(error, file, fileList) {
      closeProgress()
      showMessageFromServer(error)
    },
    hideActionView(evt) {
      this.showActionView = false
    },
  }
}
</script>
<style scoped>
  .popover-toolbar-1 {
    width: 1px;
    height:1px;
    position:absolute;
    left:2px;
    top: 76px;
  }
  .popover-toolbar-2 {
    width: 1px;
    height:1px;
    position:absolute;
    left:0px;
    top: 20px;
  }
  .cg-action {
    height:36px; 
    position:fixed;  
  }
  li {
      list-style-type:none;
  }
  .el-button {
    padding: 0;
    margin: 0 5px;
    width: 36px;
    height: 36px;
  }
  .el-button .fa {
      font-size: 16px;
      width: 16px;
      height: 16px;
  }
  .cg-button-group {
      margin-right: 15px;
      position:relative
  }
  .cg-button-group::after {
    content: "";
    position: absolute;
    width: 1px;
    height: 32px;
    right: -12px;
    top: 50%;
    -webkit-transform: translateY(-50%);
    transform: translateY(-50%);
    background-color: #DCDFE6;
  }
  .mobile-action {
    height: 50px;
    text-align: center;
  }
  .el-drawer__header {
    padding: 5px!important;
  }
  .cg-menu {
    position: relative!important;
  }
  .cg-menu-item {
    height: 40px;
    line-height: 40px;
    padding: 0!important;
  }
  </style>