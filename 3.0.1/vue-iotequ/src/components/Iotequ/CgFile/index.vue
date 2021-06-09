<template>
  <div class="cg-file">
    <el-upload
      :class="'cg-file-upload'+(readonly?' readonly-by-disable':'')"
      action=""
      :disabled="readonly"
      :accept="accept"
      :auto-upload="false"
      :on-preview="handlePreview"
      :before-remove="beforeRemove"
      :on-remove="handleChange"
      :on-change="handleChange"
      :multiple="value.multiple"
      :file-list="fileList">
      <el-button v-if="!readonly" size="small" type="primary">{{'system.action.selectFile'.local()}}</el-button>
      <div v-if="!readonly" slot="tip" class="el-upload__tip">{{'system.message.fileType'.local()}}:{{accept}}</div>
    </el-upload>
  </div>
</template>
<script>
import { apiUrl } from '@/utils/requestService'
function shortName(name) {
   const p = name.replace(/\\/g,'/').lastIndexOf('/')
   if (p>=0) return name.substring(p+1)
   else return name
}
export default {
  name: 'CgFile',
  props: {
    value: Object,
    accept: String,
    name: String,
    id: {
      type: String,
      default: ''
    },
    baseUrl: {
      type: String,
      required: true
    },
    field: {
      type: String,
      required: true
    },
    readonly: {
      type: Boolean,
      default: false
    },
    appendonly: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      fileList: this.fileListFromValue()
    }
  },
  computed: {
  },
  watch: {
  },
  methods: {
    src(name) {
      return apiUrl(this.baseUrl + '/download?id=' + this.id + '&field=' + this.field + '&fileName=' + name)
    },
    fileListFromValue() {
      if (this.value) {
        const list = []
        if (this.value.multiple && this.value.name) {
          this.value.name.forEach(name => {
            list.push({
              name: shortName(name),
              url: this.src(name)
            })
          })
        } else if (!this.value.multiple && this.value.name) {
          list.push({
            name: shortName(this.value.name),
            url: this.src(this.value.name)
          })
        }
        return list
      } else return []
    },
    handleChange(file, fileList) {
      if (fileList && fileList.length > 0) {
        if (this.value.multiple) {
          this.value.name = []
          this.value.blob = []
          fileList.forEach(f => {
            this.value.name.push(f.name)
            this.value.blob.push(f.raw ? f.raw : null)
          })
        } else {
          this.fileList = [file]
          this.value.name = file.name
          this.value.blob = file.raw ? file.raw : null
        }
      } else {
        this.value.name = this.value.multiple ? [] : null
        this.value.blob = this.value.multiple ? [] : null
      }
      this.$emit('input', this.value)
    },
    handlePreview(file) {
      if (file && file.url) window.open(file.url, '_blank')
      else if (file && file.raw) {
        const windowURL = window.URL || window.webkitURL
        window.open(windowURL.createObjectURL(file.raw), '_blank')
      }
    },
    beforeRemove(file, fileList) {
      if (this.readonly) return false
      else if (file.raw) return true
      else if (this.appendonly) {
        this.$message(`不允许移除 ${ file.name }`)
        return false
      }
      return this.$confirm(`确定移除 ${ file.name }？`)
    }
  }
}
</script>
