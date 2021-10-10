<template>
  <div :class="'cg-'+type+'-picker'">
    <div v-if="mobile">
      <cg-input-for-picker :showText="showText" :pickerVisible="pickerVisible || fastPickerVisible"  
        :clearable="clearable" :readonly="readonly" :disabled="disabled"
        @clear="clear" 
        @inputClick="inputClick" 
        @caretClick="caretClick"/>
      <nut-datepicker
        :is-visible="pickerVisible && !selectEndRange"
        :title="getTitle()" 
        :type="type.replace('range','')"
        :isShowChinese="this.$i18n.locale=='zh'"
        :startDate="mobileParams0.startDate"
        :endDate="mobileParams0.endDate"
        :startHour="mobileParams0.startHour"
        :endHour="mobileParams0.endHour"        
        :defaultValue="mobileParams0.defaultValue"
        @close="mobileClose"
        @choose="mobileSetChooseValue"
      />
      <nut-datepicker v-if="pickerVisible && selectEndRange"
        :title="getTitle()" 
        :type="type.replace('range','')"
        :isShowChinese="this.$i18n.locale=='zh'"
        :startDate="mobileParams1.startDate"
        :endDate="mobileParams1.endDate"
        :startHour="mobileParams1.startHour"
        :endHour="mobileParams1.endHour"        
        :defaultValue="mobileParams1.defaultValue"
        @close="mobileClose"
        @choose="mobileSetChooseValue1"
      />
      <nut-picker v-if="isRange && fastPickerData"
        :is-visible="fastPickerVisible" 
        :title="title"
        :list-data="fastPickerData"
        @close="fastPickerVisible=false"
        @confirm="setFastChooseValue"
      />
    </div>
    <el-time-picker v-else-if="type==='time' || type==='timerange'" v-model="dtValue" :name="name" :align="align" :is-range="type==='timerange'"
      :class="readonly && !disabled ? 'readonly-by-disable' : ''" :placeholder="title" :readonly="readonly" 
      :disabled="disabled || readonly" :format="trueFormat" editable :clearable="clearable" :picker-options="curPickerOptions"
      @click.native = "$emit('pickerClick')" />   
    <el-date-picker v-else v-model="dtValue" :name="name" :align="align" :type="type" 
      :class="readonly && !disabled ? 'readonly-by-disable' : ''" :placeholder="title" :readonly="readonly" 
      :disabled="disabled || readonly" :picker-options="curPickerOptions"
      :default-value="defValue" :format="trueFormat" editable :clearable="clearable"
      @click.native = "$emit('pickerClick')" />
  </div>
</template>
<script>
import time from '@/utils/time'
export default {
  name: 'CgDatePicker',
  props: {
    value: [String, Date, Array],
    minValue: { type: [Date,String], default: '1900-01-01' },
    maxValue: { type: [Date,String], default: '2100-12-31 23:59:59.999' },
    defValue: {
      type: [Date,String,Array],
      default: ()=>new Date()
    },
    step: String,
    pickerOptions: {},
    name: String,
    title:String,
    align: { type: String, default: 'center' },
    clearable: {
      type: Boolean,
      default: false
    },
    readonly: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: 'date'
    },
    format: String,
  },
  data() {
    return {
      pickerVisible: false,
      selectEndRange: false,
      fastPickerVisible: false,
      curPickerOptions : ()=>this.pickerOptions,
      dtValue: this.getValue(this.value),
      showText: '',
      mobileParams0 : ()=>this.getMobileParams(0),
      mobileParams1 : ()=>this.isRange ? this.getMobileParams(1) : null
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device==='mobile'
    },
    isRange() {
      return this.type.indexOf('range') > 0
    },
    trueFormat() {
      if (this.format) return this.format
      else if (this.type === 'date' || this.type === 'daterange') return 'yyyy-MM-dd'
      else if (this.type === 'datetime' || this.type === 'datetimerange') return 'yyyy-MM-dd HH:mm'
      else if (this.type === 'time' || this.type === 'timerange') return 'HH:mm'
    },
    timePickerOptions() {
      if (this.type==='time' || this.type==='timerange') {
        const start = this.minValue ? time.toString(time.toDate(this.minValue),'HH:mm:ss') : '00:00:00'
        const end = this.maxValue ? time.toString(time.toDate(this.maxValue),'HH:mm:ss') : '23:59:59'
        const options = {
          selectableRange: start+' - '+end ,
          step: this.step ? this.step : '00:05'
        }
        return options
      } 
    },
    fastPickerData() {
      if (!this.pickerOptions || !this.pickerOptions.shortcuts || this.pickerOptions.shortcuts.length == 0) return null
      let v=[]
      for (let i=0;i<this.pickerOptions.shortcuts.length;i++)
        v.push({ label:i,value:this.pickerOptions.shortcuts[i].text})
      return [v]
    }
  }, 
  watch: {
    value(n) {
      const v = this.getValue(n)
      if (this.empty(v) && this.empty(this.dtValue)) return
      else if (this.empty(v) !== this.empty(this.dtValue)) this.dtValue = v
      else if (this.empty(v) && ((!v && this.dtValue) || (v && !this.dtValue))) this.dtValue = v
      else if (this.isRange) {
        if ((!time.equals(v[0],this.dtValue[0]) || !time.equals(v[1],this.dtValue[1]))) this.dtValue = v
      }
      else if (!time.equals(v,this.dtValue)) this.dtValue = v
    },
    dtValue: {
      handler(n) {
        if (this.isRange) this.mobileParams1 = this.getMobileParams(1)
        this.showText = this.getShowText(n)
        if (!n && this.isRange) this.$emit('input',[null,null])
        else this.$emit('input',n)
      },
      immediate: false
    },
    minValue(n) {
      this.changeLayout()
    },
    maxValue(n) {
      this.changeLayout()
    },
    defValue(n) {
      this.changeLayout()
    }
  }, 
  created() {
    this.dtValue = this.getValue(this.value)
    if (this.mobile) this.showText = this.getShowText(this.dtValue)
    this.changeLayout()
  },
  methods: {
    changeLayout() {
      if (this.mobile) {
        this.mobileParams0 = this.getMobileParams(0)
        if (this.isRange) this.mobileParams1 = this.getMobileParams(1)
      } else {
        const that = this
        this.curPickerOptions=Object.assign({
          disabledDate (dt) {
            let diff = time.subtract(dt, time.startOf(that.minValue,'day'))
            if (diff < 0) return true
            diff = time.subtract(time.endOf(that.maxValue,'day'), dt)
            if (diff < 0) return true
            else return false
          },
          defaultValue: that.defValue
        },that.pickerOptions)
      }
    },
    empty(v) {
      if (!v) return true
      else if  (v instanceof Array ) {
        if (v.length ==0) return true
        else if (v.length==1 && !v[0]) return true
        else if (v.length==2 && !v[0] && !v[1]) return true
      }
      return false
    },
    getTitle() {
      if (!this.isRange) return this.title
      else return this.title + '(' + this.$t('system.message.'+(this.selectEndRange?'end':'start')) + ')'
    },
    getStartValue(v) {
      if (this.empty(v)) return null
      if (typeof v ==='string') {
        let ss=v.split(',')
        return ss[0] ? time.toDate(ss[0]) : null
      } else if (v instanceof Array ) { 
        if (v.length>0) return v[0] ? time.toDate(v[0]) : null     
        else return null
      } else return v 
    },
    getEndValue(v) {
      if (this.empty(v)) return null
      if (typeof v ==='string') {
        let ss=v.split(',')
        return ss.length >1 && ss[1] ? time.toDate(ss[1]) : null
      } else if (v instanceof Array ) { 
        return (v.length>1 && v[1] ?time.toDate(v[1]) : null)    
      } else return null  
    },
    getMobileParams(isEnd) {
      let min = (isEnd && this.isRange ? this.getStartValue(this.dtValue) : this.minValue) 
      if (!min) min = this.minValue
      const op = {
        startDate: this.type.indexOf('date')==0 ? time.toString(min,'YYYY-MM-DD') : '1970-01-02',
        endDate:this.type.indexOf('date')==0 ?  time.toString(this.maxValue,'YYYY-MM-DD') : '2100-12-31',
        startHour: this.type.indexOf('time')==0 ? time.toString(min,'HH')  : 0,
        endHour:this.type.indexOf('time')==0 ? time.toString(this.maxValue,'HH') : 23,
        defaultValue: this.mobileDefaultValue(isEnd)
      }
      return op
    },
    mobileDefaultValue(isEnd) {
      let v
      if (!isEnd) {
        v=this.getStartValue(this.dtValue)
        if (!v) v=this.getStartValue(this.defValue)
        if (!v) v=new Date()
        if (time.subtract(v,this.minValue)<0) v = this.minValue
      }
      else {
        v=this.getEndValue(this.dtValue)
        if (!v) v=this.getEndValue(this.defValue)
        if (!v) v=new Date()
        let min = this.getStartValue(this.dtValue)
        if (!min) min = this.minValue
        if (time.subtract(v,min)<0) v = min
      }
      if (time.subtract(v,this.maxValue) > 0) v=this.maxValue 
      return time.toString(v,this.trueFormat)
    },
    mobileClose() {
      if (!this.pickerVisible) return
      if (this.isRange) {
        if (this.selectEndRange) {
          this.pickerVisible=false
          this.selectEndRange = false
        } else if (this.pickerVisible) this.selectEndRange = true
      }
      else this.pickerVisible=false
    },
    mobileSetChooseValue(vv) {
      if (!this.pickerVisible || this.selectEndRange) return
      let v=null
      if (this.type.indexOf('time')==0) v=vv[vv.length-1]
      else v=vv[vv.length-2]
      const value = time.toDate(v)
      if (!this.isRange) this.dtValue = value
      else this.dtValue[0] = value
      if (this.isRange) {
        this.pickerVisible = false // cancel close event
        let e = this.getEndValue(this.dtValue)
        if (e && time.subtract(value,e) >0 ) this.dtValue[1] = value
        this.mobileParams1 = this.getMobileParams(1)
        setTimeout(()=>{ // cancle event
          this.selectEndRange = true
          this.pickerVisible = true 
        },200)
      }
      this.showText = this.getShowText(this.dtValue)
    }, 
    mobileSetChooseValue1(vv) {
      if (!this.isRange || !this.pickerVisible || !this.selectEndRange) return
      let v=null
      if (this.type.indexOf('time')==0) v=vv[vv.length-1]
      else v=vv[vv.length-2]
      const value = time.toDate(v)
      this.dtValue[1] = value
      this.showText = this.getShowText(this.dtValue)
    },    
    getValue(v) {
      const nv = this.isRange ? (this.type=='timerange' && !this.mobile ? null : ['','']) : null
      if (this.empty(v)) return nv   // el-timer-picker 控件用一个null表示
      if (!this.isRange) return this.getStartValue(v)
      else {
        const ret = [this.getStartValue(v),this.getEndValue(v)]
        if (this.empty(ret)) return nv
        else return ret
      }
    },
    getShowText(v) {
      if (!v) return ''
      let s = this.getStartValue(v)
      let t = (s?time.toString(s,this.trueFormat):'')
      if (this.isRange) {        
        let e = this.getEndValue(v)
        let et = (e?time.toString(e,this.trueFormat):'')
        if (t || et) t = t + '  -  ' + et
      }
      return t
    },

    inputClick(event) {
      if (this.readonly || this.disabled) return
      if (!this.readonly && !this.disabled) this.pickerVisible=!this.pickerVisible
    },
    clear() {
      if (!this.readonly && !this.disabled)  this.dtValue=(this.isRange?[null,null]:null)
    },
    caretClick() {
      if (this.isRange && this.fastPickerData) this.fastPickerVisible = true
      else this.inputClick()
    },
    setFastChooseValue(v) {
      const rg = time.timeRange(this.pickerOptions.shortcuts[v[0].label].range)
      if (rg) this.dtValue = rg
      else this.dtValue=[null,null]
    }
  }
}
</script>
