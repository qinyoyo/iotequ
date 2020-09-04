import CgCascader from './CgCascader'
import CgSelect from './CgSelect'
import CgRadio from './CgRadio'
import CgCheckbox from './CgCheckbox'
import CgHeader from './CgHeader'
import CgAction from './CgAction'
import CgImage from './CgImage'
import CgZxing from './CgZxing'
import CgFile from './CgFile'
import CgProgress from './CgProgress'
import CgTableColumn from './CgTableColumn'
import CgContextMenu from './CgContextMenu'
import CgJoin from './CgJoin'
import CgIcon from './CgIcon'
import CgCardList from './CgCardList'
import CgInputForPicker from './CgInputForPicker'
import CgDatePicker from './CgDatePicker'
import CgNumberInput from './CgNumberInput'
import CgFlow from './CgFlow'
import CgChart from './CgChart'
import CgQueryCondition from './CgQueryCondition'
const components = {
  CgCascader,
  CgSelect,
  CgRadio,
  CgCheckbox,
  CgHeader,
  CgAction,
  CgImage,
  CgZxing,
  CgFile,
  CgTableColumn,
  CgCardList,
  CgProgress,
  CgContextMenu,
  CgJoin,
  CgIcon,
  CgInputForPicker,
  CgDatePicker,
  CgNumberInput,
  CgFlow,
  CgChart,
  CgQueryCondition
}
const iotequ = function(Vue, opts = {}) {
  Object.keys(components).forEach((key) => { Vue.component(key, components[key]) })
}
export default iotequ
