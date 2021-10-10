<template>
  <div class="cg-form" :class="dialogClass">
    <el-dialog ref="dialog" v-el-drag-dialog top="0px" :class="dialogClass" :width="mobile?'100%':'666px'" :height="mobile?'100%':'500px'" 
               :visible.sync="visible" 
               :close-on-click-modal="false" 
               :append-to-body="true"
               @closed="closeDialog(false)">
      <div slot="title" class="color-info">
        <cg-header icon="el-icon-picture" content="选择新设备" @goBack="closeDialog()"/>
      </div>
      <CgListNewDevice ref="cgList" :height="width" />
    </el-dialog>
  </div>
</template>

<script>
import CgListNewDevice from '@/views/reader/devNewDevice/CgListNewDevice.vue'
import elDragDialog from '@/directive/el-drag-dialog'
const CgSelectNewDeviceDialog = {
  name: 'CgSelectNewDeviceDialog',
  directives: { elDragDialog },
  components: { CgListNewDevice },
  props: {
    callback: {
      type: Function,
      default: null
    },
    width: {
      type:Number,
      default: 400
    },
    dialogClass: {
      type: String,
      default: null
    }
  },
  data() {
    return {
       visible: true
    }
  },
  computed: {
    mobile() {
      return $vue.$store.state.app.device === 'mobile'
    }
  },
  mounted() {
    this.$nextTick(_=>{
      this.open()
      this.contentStyle = 'width:100%;height:' + (this.$refs.dialog ? this.$refs.dialog.$el.offsetHeight - 220 : 500) + 'px'
    })
  },
  methods: {
    closeDialog() {
        const data = this.$refs.cgList.$refs.cgList.store.states.currentRow
        this.$emit('close', data)
        if (this.callback) this.callback(data)
        this.visible = false
    }
  }
}
export default CgSelectNewDeviceDialog
export function selectNewDeviceDialog(options) {
  window.$vue.$dialog(CgSelectNewDeviceDialog, options)
}
</script>