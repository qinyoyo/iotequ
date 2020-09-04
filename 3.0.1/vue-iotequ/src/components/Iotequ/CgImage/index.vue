<template>
  <div class="cg-image">
    <el-image fit="scale-down" :class="readonly?'readonly':''" :src="src" :style="imgStyle" 
              @load="load"
              @click.native.stop="loadFile">
      <div slot="error">
        <cg-icon icon="el-icon-picture-outline" extStyle="padding-top: 6px" :size="20" color="#c46525" />
      </div>
    </el-image>
    <i v-show="showRemoveIcon" class="el-icon-error cg-image-close-icon" :style="removeIconStyle" @click.stop="removeImage" />
  </div>
</template>
<script>
import { apiUrl } from '@/utils/requestService'
import { imageCrop } from '@/views/common-views/extend-views/CgImageCropDialog'
function min(a, b) {
  return a > b ? b : a
}
function max(a, b) {
  return a > b ? a : b
}
export default {
  name: 'CgImage',
  props: {
    value: Object,
    id: {
      type: String,
      default: ''
    },
    readonly: {
      type: Boolean,
      default: false
    },
    title:String,
    generatorName: {
      type: String,
      required: true
    },
    field: {
      type: String,
      required: true
    },
    maxWidth: {
      type: Number,
      default: 0
    },
    maxHeight: {
      type: Number,
      default: 0
    },
    alignCenter: {
      type: Boolean,
      default: false
    },
    nullIcon: {
      type: String,
      default: 'fa fa-sticky-note-o'
    },
    tickName: {
      type: Boolean,
      default: true
    },
    def: String
  },
  data() {
    return {
      image: null, // 图像对象
      width: 36, // 容器大小
      height: 36,
      canShowRemoveIcon: true,
      imgStyle: 'width:100%; height:26px; left: 0px; top: 0px;',
      removeIconStyle: '',
      fileName: null
    }
  },
  computed: {
    src() {
      if (this.value && this.value.blob) {
        var windowURL = window.URL || window.webkitURL
        return windowURL.createObjectURL(this.value.blob)
      }
      else if (this.value && this.value.name) {
        return this.value.name.indexOf('data:')==0 ? 
           this.value.name : apiUrl('/res/getUploadImage?name='+this.value.name + 
            (this.value.name.indexOf('/')>=0 || this.value.name.indexOf('\\')>=0 ? 
                '' :
                ( '&id='+this.id
                + '&path=' + this.generatorName 
                + '&field=' + this.field
                + (this.def ? '$def=' + this.def : ''))
            )
            + '&tick='+new Date().getTime())
      }
      else return null
    },
    showRemoveIcon() {
      return (!this.readonly && this.canShowRemoveIcon && this.image)
    }
  },
  beforeMount() {
    window.addEventListener('resize', this.resizeHandler, false)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeHandler, false)
  },
  methods: {
    load(p) {
      this.image = this.$el.getElementsByTagName('img')[0]
      this.calcSize()
    },
    removeImage() {
      this.image = null
      this.fileName = null
      this.value.name = null
      this.value.blob = null
      this.$emit('input', this.value)
      this.calcSize()
    },
    changeName(name) {
      if (this.tickName) {
        let nn = name.split('\.')
        if (nn.length>1) {
          const n = parseInt((new Date().getTime())/1000) % 100000000 + '.'+nn[nn.length-1]
          return n
        }
      } 
      return name
    },
    loadFile() {
      if (this.readonly) return
      const _this=this
      imageCrop({
        content: _this.title,
        fullscreen: true,
        fixedWidth: _this.maxWidth,
        fixedHeight: _this.maxHeight,
        callback: ({blob, fileName}) => {
          _this.fileName = _this.changeName(fileName)
          const value = {
            blob: blob,
            name: _this.fileName
          }
          _this.$emit('input',value)
        }
      })
    },
    resizeHandler() {
      this.calcSize()
    },
    calcSize() {
      let left = 0, top = 0
      if (!this.image) {
        this.imgStyle = 'width:100%; height:36px; left: 0px; top: 0px;'
        this.removeIconStyle =  ''
        return
      } else {
        let iw = this.image.naturalWidth
        let ih = this.image.naturalHeight
        let mw = this.$el ? min(this.$el.clientWidth, this.maxWidth) : this.maxWidth
        let mh = this.$el ? min(max(this.$el.clientHeight, this.maxHeight), this.maxHeight) : this.maxHeight
        if (iw > mw) {
          let scale = mw / iw
          if (ih*scale > mh) {
            scale = mh / ih
            this.width = iw * scale
            this.height = mh
          } else {
            this.width = mw
            this.height = ih * scale
          }
        } else if (ih > mh) {
          scale = mh / ih
          this.width = iw * scale
          this.height = mh        
        } else {
          this.width = iw
          this.height = ih
        }
        top = 0
        if (this.alignCenter) left =  ((this.$el ? this.$el.clientWidth : this.maxWidth) - this.width) / 2
        else left = 0
      }
      this.imgStyle = 'width: ' + this.width + 'px; height: ' 
        + this.height +'px; left: ' + left + 'px; top: ' + top + 'px;'
      this.removeIconStyle = 'left:' + (left + this.width - 20) + 'px; top: ' + top + 'px;'
    }
  }
}
</script>
<style scoped>
.cg-image {
  padding: 0;
  margin: 0;
  position: relative;
  cursor: pointer;
}
.cg-image-close-icon {
  position: absolute; 
  color: blue; 
  font-size: 20px; 
  width: 20px; 
  height: 20px;
}
.readonly {
  cursor: default;
}
.image-error-icon {
    padding-top:6px;
    width: 100%;
    color: #c46525;
    font-size: 20px;
}
</style>
