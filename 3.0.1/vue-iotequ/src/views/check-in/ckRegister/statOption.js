import { chineseSort } from '@/utils/cg'
function distincOf(data,field) {
    let r = []
    for (let i=0;i<data.length;i++) {
       if (r.indexOf(data[i][field])<0) r.push(data[i][field]) 
    }
    return r
  }

  function getValueIndex(data, field, test) {
    for (let i=0;i<data.length;i++) {
      if (test(data[i])) return i 
    }
    return 0
}
  export function getEChartsOptions(data,legendField, xField, yField, charType, mobile,exOption) {
    var legend = (legendField ? distincOf(data,legendField).sort((a,b)=>chineseSort(a,b)) : [''])
    var xAxis = distincOf(data,xField).sort((a,b)=>chineseSort(a,b))
    var series = []
    for (let i=0;i<legend.length;i++) {
      let s = {
          name: legend[i],
          type: charType=="polar" ?'bar':charType,
          barGap: 0,
          smooth: true,
          emphasis: {
              focus: 'series'
          },
          data: []
       }
       if (charType=="polar") s.coordinateSystem = 'polar'
       if (exOption && exOption.series) s=Object.assign(s,exOption.series)
       if (!mobile) {
        if (charType == 'pie') {
            s.label = {
                normal: {
                    formatter: '{b}:{c}({d}%)'
                }
            }
            s.itemStyle = {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                 },
                normal:{
                    color:function(params) {
                    //自定义颜色
                    var colorList = [          
                        '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
                        '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                        '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0',
                        ];
                        return colorList[params.data.originIndex % colorList.length]
                     }
                }
            }
        }
        else {
            s.itemStyle = {
                normal: {
                    label: {
                        show: true,
                        position: 'top'
                    }
                }
            }
        }
       }
       for (let j=0;j<xAxis.length;j++) {
         let test = function(d) {
           return (legendField ? (d[legendField] == legend[i] && d[xField] == xAxis[j]) : d[xField] == xAxis[j])
         }
         let index = getValueIndex(data,yField,test)
         if (charType == 'pie') {            
             s.data.push({
                value: data[index][yField],
                name:xAxis[j],
                originIndex: index 
            })
        } else s.data.push(data[index][yField])
       } 
       series.push(s)
    }
    var option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: legendField ? {
            //show: charType != 'pie',
            data: legend,
            type:'scroll',
            orient:'horizontal'
        } : null,
        toolbox: {
            show: true,
            orient: 'vertical',
            left: 'right',
            top: 'center',
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: charType == 'pie' ? null : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                restore: {show: false},
                saveAsImage: {show: true}
            }
        },
        series: series,
        notMerge: true,
        lazyUpdate: false,
        silent: true
    }

    if (charType=="polar") {
        option.radiusAxis = {
            type: 'value'
        }
        option.angleAxis = {
            type: 'category',
            data: xAxis
        }
        option.polar = {}
    } else if (charType == 'pie') {
        
    } else {
        option.xAxis = [
            {
                type: 'category',
                axisTick: {show: false},
                data: xAxis
            }
        ]
        option.yAxis = [
            {
                type: 'value'
            }
        ]
    }
    if (exOption && exOption.option) option=Object.assign(option,exOption.option)
    return option
  }