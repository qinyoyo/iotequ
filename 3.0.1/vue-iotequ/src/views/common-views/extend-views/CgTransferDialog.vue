<template>
  <div class="cg-form" :class="dialogClass">
    <el-dialog ref="dialog" v-el-drag-dialog top="0px" :class="dialogClass" :width="mobile?'100%':'666px'" :visible.sync="visible" append-to-body
               :close-on-click-modal="false" @closed="closeDialog(false)">
      <div slot="title" class="color-info">
        <cg-header icon="el-icon-circle-plus" title="请选择" :content="content" />
      </div>
      <div style="text-align: center">
      <el-transfer ref="transfer" v-model="selected" :titles="['备选项', '已选项']" :data="data" @change="selectedChanged" />
      </div>
      <el-divider />
      <div class="el-message-box__btns">
        <el-button class="cg-button" type="primary" plain :disabled="!selectionChanged" icon="el-icon-check"
                   @click.native="closeDialog(true)"
        >
          确定
        </el-button>
        <el-button class="cg-button" plain icon="el-icon-close"
                   @click.native="closeDialog(false)"
        >
          取消
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import Vue from 'vue'
import dom from '@/utils/dom'
import i18n from '@/lang'

const CgTransferDialog = {
  name: 'CgTransferDialog',
  directives: { elDragDialog },
  props: {
    titleContent: {
      type: String,
      default: ''
    },
    sourceData: {
      type: Array,
      default: _ => { return [] }
    },
    selections: {
      type: Array,
      default: _ => { return [] }
    },
    callback: {
      type: Function,
      default: null
    },
    dialogClass: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      content: this.titleContent,
      data: this.sourceData,
      selected: this.selections,
      selectionChanged: false,
      visible: true
    }
  },
  computed: {
    mobile() {
      return $vue.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    selectedChanged(selections) {
      this.selectionChanged = true
      this.$emit('change', selections)
    },
    closeDialog(confirm) {
      this.visible = false
      this.$emit('close', confirm ? this.selected : null)
      if (this.callback) this.callback({ confirm, selections: this.selected })
    }
  }
}
export default CgTransferDialog
export function transfer(options) {
  window.$vue.$dialog(CgTransferDialog,{
      titleContent: options.title,
      sourceData: options.data,
      selections: options.selected,
      callback: function({ confirm, selections }) {
        if (confirm) {
          if (typeof options.onConfirm === 'function') options.onConfirm(selections)
        }
      }   
  })
}
</script>
