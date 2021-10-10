import Leftslip from './leftslip'
import Scroller from './scroller'
import Actionsheet from './actionsheet'
import Picker from './picker'
import Datepicker from './datepicker'
import Popup from './popup'
import Cell from './cell'
const nutui = function(Vue, opts = {}) {
  Vue.component(Leftslip.name, Leftslip)
  Vue.component(Scroller.name, Scroller)
  Vue.component(Actionsheet.name, Actionsheet)
  Vue.component(Picker.name, Picker)
  Vue.component(Datepicker.name, Datepicker)
  Vue.component(Popup.name, Popup)
  Vue.component(Cell.name, Cell)
}
export default nutui
