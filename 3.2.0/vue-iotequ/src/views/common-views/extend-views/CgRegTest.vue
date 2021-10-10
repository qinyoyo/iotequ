<template>
  <div class="cg-form" :class="dialogClass">
    <el-dialog ref="dialog" v-el-drag-dialog top="0px" :class="dialogClass" :width="mobile?'100%':'560px'" :visible.sync="visible" 
               append-to-body :close-on-click-modal="false" @closed="closeDialog(false)">
      <div slot="title" class="color-info">
        <cg-header icon="el-icon-circle-check" title="Test" content="Reg expression" />
      </div>
      <el-form>
        <el-form-item label="Reg expression" prop="validExpression">
          <el-input v-model="validExpression" type="text" show-word-limit clearable resize autofocus @input="doTest()" />
        </el-form-item>
        <el-form-item label="String text" prop="text">
          <el-input v-model="text" type="text" show-word-limit clearable resize autofocus @input="doTest()" />
        </el-form-item>
        <el-form-item label="Test result" prop="result">
          <cg-radio v-model="result" :dictionary="dictResult" numberic readonly /> 
        </el-form-item>
      </el-form>
      <el-divider />
      <div class="el-message-box__btns">
        <el-button v-if="callback" class="cg-button" type="primary" plain icon="el-icon-check"
                   @click.native="closeDialog(true)"
        >
          Just it is!
        </el-button>
        <el-button v-if="!mobile && callback" class="cg-button" plain icon="el-icon-close"
                   @click.native="closeDialog(false)"
        >
          No more modify
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
        { text: 'Completely matched', value: 0 },
        { text: 'Found mathched', value: 1 },
        { text: 'None mathched', value: 2 },
        { text: 'Error Reg expression', value: 3 },
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
