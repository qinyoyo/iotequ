
const itemParent = function(item) {
    let p = item.lastIndexOf('/')
    if (p>0) return item.substring(0,p)
    else return ''
}
const itemSimpleName = function(item) {
    let p = item.lastIndexOf('/')
    if (p>0) return item.substring(p+1)
    else return item
}
const staticSvgIcons = []
const xhr=new XMLHttpRequest()
xhr.open('GET','/static/svg/svglist.txt',false)
xhr.overrideMimeType("text/html;charset=gb2312")
xhr.send(null)
if(xhr.status===200) {
    const icons = []
    xhr.responseText.split('\n').forEach(s=>{
        s=s.trim().replace(/\\/g,'/') 
        if (s) {
            let p=s.indexOf('/static/svg/')
            if (p>0) s=s.substring(p+12)
            p=s.indexOf('.svg')
            if (p>0) s=s.substring(0,p)
            icons.push(s) 
        }
    })
    icons.sort((s1,s2)=>{
        const p1=itemParent(s1),p2=itemParent(s2)
        const i1=itemSimpleName(s1),i2=itemSimpleName(s2)
        if (p1==p2) return i2>i1
        else return p2>p1
    })
    let index = 0
    let parent = ''
    for (let i=0;i<icons.length;i++) {
        if (i==0) {
            staticSvgIcons.push([icons[i]])
            parent = itemParent(icons[i])
            index = 0
        } else {
            let p = itemParent(icons[i])
            if (p==parent) staticSvgIcons[index].push(icons[i])
            else {
                staticSvgIcons.push([icons[i]])
                parent = p
                index ++              
            }
        }
    }
}
export default staticSvgIcons
