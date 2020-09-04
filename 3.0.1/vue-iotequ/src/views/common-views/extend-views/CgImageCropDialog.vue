<template>
  <div class="cg-form" :class="dialogClass">
    <el-dialog ref="dialog" v-el-drag-dialog top="0px" :class="dialogClass" :width="mobile?'100%':'666px'" :height="mobile?'100%':'500px'" 
               :fullscreen="fullscreen" 
               :visible.sync="visible" 
               :close-on-click-modal="false" 
               :append-to-body="true"
               @closed="closeDialog(false)">
      <div slot="title" class="color-info">
        <cg-header icon="el-icon-picture" :title="$t('system.action.crop')" :content="content" @goBack="closeDialog(false)"/>
      </div>
      <input type="file" style="display:none" class="cg-image_input-file" accept="image/png, image/jpeg, image/gif, image/jpg" @change="($e)=>uploadImg($e)" />
      <div :style.sync="contentStyle" @drop="drop" @dragover.prevent>
        <vue-cropper ref="cropper" class="full-content"
                     :img.sync="image" 
                     :outputSize="option.size" 
                     :outputType="option.outputType" 
                     :info="true"
                     :full="option.full" 
                     :canMove="option.canMove" 
                     :canMoveBox="option.canMoveBox" 
                     :original="option.original"
                     :autoCrop="option.autoCrop"
                     :autoCropWidth="option.autoCropWidth"
                     :autoCropHeight="option.autoCropHeight" 
                     :fixed="option.fixed" 
                     :fixedNumber="option.fixedNumber" 
                     :centerBox="option.centerBox"
                     :infoTrue="option.infoTrue" 
                     :fixedBox="option.fixedBox"
                     @dblclick.native.stop="open"
                     >
        </vue-cropper>
      </div>
      <el-divider />
      <div class="el-message-box__btns">
        
        <el-button type="success" icon="fa fa-picture-o" circle @click="open"></el-button>
        <el-button type="primary" circle @click="scale1x()">1x</el-button>
        <el-button type="success" icon="fa fa-undo" circle @click="rotate(true)"></el-button>
        <el-button type="info" icon="fa fa-repeat" circle @click="rotate(false)"></el-button>
        <el-button type="warning" :icon="option.fixed?'fa fa-chain-broken':'fa fa-chain'" circle @click="fixedCrop()"></el-button>

        <el-button class="cg-button" type="primary" plain :disabled.sync="imageNotLoaded" icon="el-icon-check"
                   @click.native="closeDialog(true)"
        >
          确定
        </el-button>
        <el-button v-if="!mobile" class="cg-button" plain icon="el-icon-close"
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
import { VueCropper }  from 'vue-cropper'
const MAXINT = 100000
const CgImageCropDialog = {
  name: 'CgImageCropDialog',
  directives: { elDragDialog },
  components: {	VueCropper },
  props: {
    content: {
      type: String,
      default: ''
    },
    outputType: {
      type: String,
      default: 'png'
    },
    fullscreen: {
      type: Boolean,
      default: false
    },
    fixedWidth: {
      type: Number,
      default: MAXINT,
    },
    fixedHeight: {
      type: Number,
      default: MAXINT,
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
      visible: true,
      inputFile: null,
      fileName: null,
      contentStyle: 'width:100%;height:500px',
      imageNotLoaded: true,
      image: '', // 裁剪图片的地址
      option: {
        info: true, // 裁剪框的大小信息
        outputSize: 1, // 裁剪生成图片的质量
        outputType: this.outputType, // 裁剪生成图片的格式
        canScale: true, // 图片是否允许滚轮缩放
        autoCrop: true, // 是否默认生成截图框
        autoCropWidth: this.fixedWidth, // 默认生成截图框宽度
        autoCropHeight: this.fixedHeight, // 默认生成截图框高度
        fixedBox: false, // 固定截图框大小 不允许改变
        fixed: this.fixedWidth > 0 && this.fixedWidth < MAXINT && this.fixedHeight > 0 && this.fixedHeight < MAXINT , // 是否开启截图框宽高固定比例
        fixedNumber: [this.fixedWidth, this.fixedHeight], // 截图框的宽高比例
        full: false, // 是否输出原图比例的截图
        canMoveBox: true, // 截图框能否拖动
        original: false, // 上传图片按照原始比例渲染
        centerBox: true, // 截图框是否被限制在图片里面
        infoTrue: true // true 为展示真实输出图片宽高 false 展示看到的截图框宽高
      },
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
    setByFile(file) {
      const that = this
      if (/image\/(gif|jpg|jpeg|png|bmp)/i.test(file.type)) {
      const reader = new FileReader()
        reader.onload = function(e) {
          const blob = new Blob([e.target.result])
          that.image = window.URL.createObjectURL(blob)
          that.imageNotLoaded=false
          that.fileName = file.name
          that.option.outputType = file.type.substring(6)
          that.scale1x()
        }
        // 转化为base64
        // reader.readAsDataURL(file)
        // 转化为blob
        reader.readAsArrayBuffer(file)
      } else this.$message.warning('仅支持图片类型.gif,jpeg,jpg,png,bmp中的一种')
    },
    uploadImg($e){
      const file = $e.target.files[0]
      this.setByFile(file)
    },
    open() {
      if (!this.inputFile) {
        this.inputFile = this.$refs.dialog.$el.querySelectorAll('.cg-image_input-file')[0]
      }
      if (this.inputFile) this.inputFile.click()
    },
    scale1x() {
      this.$refs.cropper.scale=1
    },
    rotate(left) {
      if (left) this.$refs.cropper.rotateLeft()
      else this.$refs.cropper.rotateRight()
    },
    fixedCrop() {
      if (this.option.fixed) this.option.fixed = false
      else {
        this.option.fixedNumber = [this.$refs.cropper.cropW, this.$refs.cropper.cropH]
        this.option.fixed = true
      }
    },
    drop(ev) {
      if (ev) {
        ev.preventDefault()
        if (ev.dataTransfer && ev.dataTransfer.items && ev.dataTransfer.items.length > 0) {
          const item = ev.dataTransfer.items[0]
          if (item.kind==='file') {
            this.setByFile(item.getAsFile())
          }
        }
      }
    },
    closeDialog(confirm) {
      const that = this
      if (confirm) {
        that.$refs.cropper.getCropBlob((data) => {
          that.$emit('close', data)
          if (that.callback) that.callback({ blob: data, fileName: this.fileName })
          that.visible = false
        })
      } else {
        that.$emit('close')
        that.visible = false
      }
    }
  }
}
export default CgImageCropDialog
export function imageCrop(options) {
  window.$vue.$dialog(CgImageCropDialog, options)
}
</script>
<style scoped>
.full-content {
  width: 100%;
  height: 100%;
}
</style>
