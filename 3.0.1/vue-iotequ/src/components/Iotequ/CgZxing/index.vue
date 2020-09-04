<template>
  <div class="cg-image">
    <el-image fit="scale-down" :src="src()">
      <cg-icon slot="error" icon="el-icon-question"/>
    </el-image>
  </div>
</template>
<script>
import { apiUrl } from '@/utils/requestService'
export default {
  name: 'CgZxing',
  props: {
    value: String,
    size: {
      type: Number,
      required: true
    },
    icon: String
  },
  methods: {
    src() {
      return apiUrl('/res/get2dCode?text=' + encodeURI(this.value)
        + '&size=' + this.size
        + (this.icon ? '&icon=' + this.icon : ''))
    }
  }
}
</script>
<style scoped>
  .cg-header i {
    font-size: 18px;
    margin-right: 6px;
    -ms-flex-item-align: center;
    align-self: center;
  }
</style>