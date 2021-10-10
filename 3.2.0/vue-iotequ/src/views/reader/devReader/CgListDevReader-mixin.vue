
<script>
const vue = window.$vue
import { request } from '@/utils/request'
import { Message } from 'element-ui'
import axios from 'axios'
import cgList from '@/utils/cgList'
import cgForm from '@/utils/cgForm'
import clipboard from '@/utils/clipboard'
export default {
  methods: {
    
    reportPeople(action, options, id){
        if(action.action=='reportPeople'){
          this.$router.replace({
                    path: '/reader/devReaderPeople/list',
                    query:{
                      fixedQueryRecord:{readerNo: action.row.readerNo,action:'reportPeople'}
                      
                    },
                    replace: true
                    })
      }
      else return {}
    },  
    extendActionFilter(action, row){
      if(row){
        if(row.type=='C20'){
          if(action=='reportPeople'||action=='queryTime'||action=='setDeviceTime'||action=='resetDevice'||action=='data'||action=='deleteAllUsers'){
            return false
          }else return true
        }else if(row.type=='D10'){
          if(action=='reportPeople')return false
          else return true
        }else if(row.type=='D30-N'){
            return true
        }
      }else {
        return true
      }
     
    }
  }
}
</script>