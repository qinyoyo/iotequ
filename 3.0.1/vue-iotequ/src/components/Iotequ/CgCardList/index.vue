<template>
  <div :class="'cg-card-list' + (mainClass?(' '+mainClass):'')" :style="contentStyle">   
    <slot name="header"/>
    <nut-scroller ref="scroller"
      :is-un-more="isUnMore" 
      :is-loading="isLoading"
      :is-busy="isBusy"
      :type="'vertical'"
      :backtop="backtop"
      :pulldownTxt="$t('system.message.pullDownRefresh')"
      :loadMoreTxt="$t('system.message.pullUpLoad')"
      :unloadMoreTxt="$t('system.message.noMoreData')"
      @loadMore="$emit('loadMore')"
      @pulldown="$emit('refresh')"
    > 
      <div div slot="list" :class="'cg-card-list__wrapper' + (containerClass?(' '+containerClass):'')" >
        <div class="cg-card-list__content">
          <div v-for="(item,index) in data" :key="index" :class="getRowClass(item,index)"
              @click="(e)=>rowClick(item,e)"
              @dblclick="(e)=>rowDblClick(item,e)"
              @contextMenu="(e)=>rowRightClick(item,e)"
          >
            <div v-if="groupTitle(index)" class="cg-crad-list__group">{{groupTitle(index)}}</div>
            <nut-leftslip v-if="leftslipActions.length > 0" :actions="leftslipActions(item)" 
              @leftslipStart="isBusy=true" @leftslipEnd="isBusy=false"
              @doAction="(action)=>doAction(action,item)"
            >
              <CgCardListRow slot="slip-main" :row="item" :index="index" :rowRender="render" />
            </nut-leftslip>
            <CgCardListRow v-else slot="slip-main" :row="item" :index="index" :rowRender="render"/>
          </div>
        </div>      
      </div>
      <slot slot="more" ><div class="nut-hor-jump-more">查看更多</div></slot>
    </nut-scroller>
    <slot name="append" />
  </div>
</template>
<script>
const CgCardListRow  = {
  name: 'CgCardListRow',
  props: {
    row: {},
    index: Number,
    rowRender: Function
  },
  data() {
    return {
      forRefresh: true
    }
  },
  watch: {
    row: {
      handler(n) {
        this.forRefresh = false
        this.$nextTick(()=>this.forRefresh = true)
      },
      deep: true
    }
  },
  render(h) {
    return this.forRefresh ? (
      <div>
        {this.rowRender(this.row, this.index)}
      </div>
    ) : ( <template />)
  }
}
import cg from '@/utils/cg'
import cgList from '@/utils/'
import { systemActionParams,randomColor } from '@/utils/cg'
import { createStore, mapStates } from 'element-ui/packages/table/src/store/helper';
import { template } from '@babel/core'
export default {
  name: 'CgCardList',
  components: { CgCardListRow },
  props: {
    render: {
      type: Function,
      required: true
    },
    backtop: {
      type: Boolean,
      default: true
    },
    multiple: Boolean,
    cgList: Object,
    groupBy: [String, Function],
    actions: {
      type: Array,
      default: ()=>[]
    },
    isUnMore: { type: Boolean, default: false },
    isLoading: { type: Boolean, default: false },
    mainClass: String,
    containerClass: String,
    height: [String,Number,Function],
    rowClassName:[String, Function],
    data: {
      type: Array,
      default: function() {
        return [];
      }
    }
  },
  data() {
    this.store = createStore(this, {
      rowKey: this.rowKey,
      defaultExpandAll: false,
      selectOnIndeterminate: true,
      indent: 16,
      lazy: false,
      lazyColumnIdentifier: 'hasChildren',
      childrenColumnName: 'children'
    })
    return {
      isBusy: false
    }
  },
  watch: {
    data: {
      immediate: true,
      handler(value) {
        this.store.commit('setData', value);
      }
    }
  },
  computed: {
    contentStyle() {
      let hs = ''
      if (this.height) {
        const h = (typeof this.height === 'function' ? this.height.apply.call() : this.height) 
        if (typeof h === 'string') hs=h
        else if (typeof h === 'number') hs=h + 'px'
        else hs = (document.documentElement.clientHeight - 60) + 'px'
      } else hs = (document.documentElement.clientHeight - 60) + 'px'
      return 'height:'+hs
    },
    ...mapStates({
      selection: 'selection',
      columns: 'columns',
      tableData: 'data',
      fixedColumns: 'fixedColumns',
      rightFixedColumns: 'rightFixedColumns'
    })
  },
  methods: {
    reset() {
      this.$refs.scroller.reset()
    },
    leftslipActions(row) {
      let aa=[]
      let index=0
      if (this.cgList) {
        const actions = this.cgList.list_allActions(this.cgList,'context',row)
        actions.forEach( a=> {
          if (a.action=='delete') aa.push({
            ...a, color: 'rgb(245, 108, 108)'})
          else if (a.action=='edit') aa.push({
            ...a, color:'rgb(230, 162, 60)'})
          else if (a.action=='view') aa.push({
            ...a, color:'rgb(103, 194, 58)'})
          else if (a.rowProperty==='sr')  aa.push({
              ...a, color: randomColor(index++) })
        }) 
      }
      return aa
    },    
    doAction(action,row) {
      this.$emit('doAction',action,row)
    },
    groupTitle(index) {
      if (!this.groupBy) return ''
      let cur = '', pre=''
      if (typeof this.groupBy === 'function') {
        cur=this.groupBy(index)
        if (index>0) pre=this.groupBy(index-1)
      } else {
        let ff=this.groupBy.split(',')
        let cc=[], pp=[]
        ff.forEach(f=>{
          cc.push(this.data[index][f])
          if (index>0) pp.push(this.data[index-1][f])
        })
        cur = cc.join(' ')
        if (index>0) pre = pp.join(' ')
      }
      if (cur==pre) return ''
      else return cur
    },
    rowClick(row,event) {
      this.$emit('row-click', row,event)
    },
    rowDblClick(row,event) {
      this.$emit('row-dblclick', row,event)
    },
    rowRightClick(row,event) {
      this.$emit('row-contextmenu', row,event)
    },
    clearSelection() {
      this.store.clearSelection();
    },
    setCurrentRow(row) {
      this.store.commit('setCurrentRow', row);
    },
    toggleRowSelection(row, selected) {
      this.store.toggleRowSelection(row, selected, false);
      this.store.updateAllSelected();
    },
    sort(prop, order) {
      this.store.commit('sort', { prop, order });
    },
    toggleAllSelection() {
      this.store.commit('toggleAllSelection');
    },
    getRowClass(row, rowIndex) {
      const classes = ['cg-card-list__row']
      classes.push('cg-card-list__row-'+rowIndex)
      if (this.multiple) {
        if (this.store.states.selection.indexOf(row) >=0 ) classes.push('selected')
      } else if (row === this.store.states.currentRow) {
        classes.push('current')
        classes.push('selected')
      }
      const rowClassName = this.rowClassName;
      if (typeof rowClassName === 'string') {
        classes.push(rowClassName);
      } else if (typeof rowClassName === 'function') {
        classes.push(rowClassName.call(null, {
          row,
          rowIndex
        }));
      }
      return classes.join(' ')
    },
  }
}
</script>

<style>
  .cg-card-list__wrapper{
    overflow-y: auto;
  }
  .cg-card-list__row.selected {
    background-color: #e7e7e7
  }
  .cg-crad-list__group {
    font-weight: bold;
    padding: 10px 10px 0 10px;
    border-bottom: 1px solid #9e9e9e;
    background-color: #f4f4f4;
  }
</style>