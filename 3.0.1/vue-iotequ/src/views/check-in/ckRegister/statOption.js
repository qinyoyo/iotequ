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
  export function getEChartsOptions(data,legendField, xField, yField, charType) {
    var legend = (legendField ? distincOf(data,legendField) : [''])
    var xAxis = distincOf(data,xField)
    var series = []
    for (let i=0;i<legend.length;i++) {
      let s = {
          name: legend[i],
          type: charType,
          barGap: 0,
          emphasis: {
              focus: 'series'
          },
          data: []
       }
       if (charType == 'pie') s.roseType = 'radius'
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
            data: legend
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
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        xAxis: charType == 'pie' ? null : [
            {
                type: 'category',
                axisTick: {show: false},
                data: xAxis
            }
        ],
        yAxis: charType == 'pie' ? null : [
            {
                type: 'value'
            }
        ],
        series: series,
        notMerge: true,
        lazyUpdate: false,
        silent: true
    };
    return option
  }