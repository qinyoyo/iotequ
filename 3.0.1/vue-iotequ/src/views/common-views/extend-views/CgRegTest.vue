<template>
  <div class="cg-form" :class="dialogClass">
    <el-dialog ref="dialog" v-el-drag-dialog top="0px" :class="dialogClass" :width="mobile?'100%':'560px'" :visible.sync="visible" 
               append-to-body :close-on-click-modal="false" @closed="closeDialog(false)">
      <div slot="title" class="color-info">
        <cg-header icon="el-icon-circle-check" title="测试" content="正则表达式" />
      </div>
      <el-form>
        <el-form-item label="正则表达式" prop="validExpression">
          <el-input v-model="validExpression" type="text" show-word-limit clearable resize autofocus @input="doTest()" />
        </el-form-item>
        <el-form-item label="测试字符串" prop="text">
          <el-input v-model="text" type="text" show-word-limit clearable resize autofocus @input="doTest()" />
        </el-form-item>
        <el-form-item label="测试结果" prop="result">
          <cg-radio v-model="result" :dictionary="dictResult" numberic readonly /> 
        </el-form-item>
      </el-form>
      <el-divider />
      <div class="el-message-box__btns">
        <el-button v-if="callback" class="cg-button" type="primary" plain icon="el-icon-check"
                   @click.native="closeDialog(true)"
        >
          就使用这个吧
        </el-button>
        <el-button v-if="!mobile && callback" class="cg-button" plain icon="el-icon-close"
                   @click.native="closeDialog(false)"
        >
          不想修改了
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
const CgRegTest = {
  name: 'CgRegTest',
  directives: { elDragDialog },
  props: {
    reg: {
      type: String,
      default: ''
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
      validExpression: this.reg,
      text: '',
      visible: true,
      dictResult: [
        { text: '完全匹配', value: 0 },
        { text: '找到匹配项', value: 1 },
        { text: '完全不匹配', value: 2 },
        { text: '错误的正则表达式', value: 3 },
      ],
      result: 2
    }
  },
  computed: {
    mobile() {
      return $vue.$store.state.app.device === 'mobile'
    }
  },  
  methods: {
    doTest() {
      try {
        const r1 = new RegExp(this.validExpression, 'gm')
        const t1 = r1.test(this.text)
        const r2 = new RegExp('^' + this.validExpression + '$', 'gm')
        const t2 = r2.test(this.text)
        this.result = (t2 ? 0 : (t1 ? 1 : 2))
      } catch (e) {
        this.result = 3
      }
    },
    closeDialog(confirm) {
      this.visible = false
      this.$emit('close', confirm ? this.selected : null)
      if (this.callback) this.callback({ confirm, reg: this.validExpression })
    }
  }
}
export default CgRegTest
export function doRegTest({ reg, callback }) {
  window.$vue.$dialog(CgRegTest,{
    reg,
    callback
  })
}
</script>
