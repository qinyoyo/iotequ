function distincOf(data,field) {
    let r = []
    for (let i=0;i<data.length;i++) {
       if (r.indexOf(data[i][field])<0) r.push(data[i][field]) 
    }
    return r
  }
  function getValue(data, field, test) {
      for (let i=0;i<data.length;i++) {
        if (test(data[i])) return data[i][field] 
      }
      return 0
  }
  export function getEChartsOptions(data,legendField, xField, yField, charType, mobile,exOption) {
    var legend = (legendField ? distincOf(data,legendField) : [''])
    var xAxis = distincOf(data,xField)
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
         if (charType == 'pie') {
             s.data.push({
                value:getValue(data,yField,test),
                name:xAxis[j]
            })
        } else s.data.push(getValue(data,yField,test))
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
        legend: {
            show: charType != 'pie',
            data: legend,
            type:'scroll',
            orient:'horizontal'
        },
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