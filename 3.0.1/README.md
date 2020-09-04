<p align="center">
  <img width="320" src="https://wpimg.wallstcn.com/ecc53a42-d79b-42e2-8852-5126b810a4c8.svg">
</p>

<p align="center">
  <a href="https://github.com/vuejs/vue">
    <img src="https://img.shields.io/badge/vue-2.6.10-brightgreen.svg" alt="vue">
  </a>
  <a href="https://github.com/ElemeFE/element">
    <img src="https://img.shields.io/badge/element--ui-2.7.0-brightgreen.svg" alt="element-ui">
  </a>
</p>

简体中文 

## 简介

[vue-iotequ-admin] 是一个前后端分离的综合解决方案，它基于 [vue](https://github.com/vuejs/vue) 和 [element-ui](https://github.com/ElemeFE/element)实现。它使用了最新的前端技术栈，内置了 i18n 国际化解决方案，动态路由，权限验证，提炼了典型的业务模型，提供了丰富的功能组件，它可以帮助你快速搭建企业级中后台产品原型。后台采用spring boot实现，集成spring security ，oauth2.0提供权限管理方案。同时提供基于数据库访问操作的代码生成工具(code-generator)，快速提供基本的数据操作的前后端代码。

- [在线预览](https://www.iotequ.top/admin)

- [使用文档](https://www.iotequ.top/admin/res/doc)

- [Donate](https://www.iotequ.top/admin/res/donate)

**该项目不支持低版本浏览器(如 ie)，有需求请自行添加 polyfill

**目前版本为 `v3.0.0` 基于 `vue-cli` 进行构建，若发现问题，欢迎提[issue](https://www.iotequ.top/admin/res/issues/new)。

## 前序准备

你需要在本地安装 [node.js](http://nodejs.org/) 。本项目技术栈基于 [ES2015+](http://es6.ruanyifeng.com/)、[vue](https://cn.vuejs.org/index.html)、[vuex](https://vuex.vuejs.org/zh-cn/)、[vue-router](https://router.vuejs.org/zh-cn/) 、[vue-cli](https://github.com/vuejs/vue-cli) 、[axios](https://github.com/axios/axios) 和 [element-ui](https://github.com/ElemeFE/element)，所有的请求数据都使用[Mock.js](https://github.com/nuysoft/Mock)进行模拟，提前了解和学习这些知识会对使用本项目有很大的帮助。

**如有问题请先看上述使用文档和文章，若不能满足，欢迎 issue 和 pr**


## 功能

```
- 登录 / 注销
  - 账号密码登录
  - 短线验证码登录
  - 第三方授权登录
  - 短信注册
  - 个人信息中心

- 权限验证
  - 路由权限
  - 后台访问控制
  - 角色权限
  - 部门权限
  - 权限配置

- 消息 / 任务
  - 日志
  - 消息
  - 事件
  - 任务

- 字典
  - 系统数据字典
  - 常量字典
  - sql选择数据字典
  - 视图检索字典
  - 动态条件检索
  - 多选、层级字典

- 流程控制
  - 有限状态机
  - 时间线
  - 消息与待办事务
  - 权限、人员、状态、迁移

- 代码生成
  - 基于maven项目的生成工具
  - 数据访问dao
  - 实体类entity
  - Restful controller
  - 通用服务模板
  - 前端列表
  - 前端表单
  - 路由
  - 多语言文件
  - 基于mixin机制的代码扩展

- Excel
  - 导出excel
  - 导入excel
  - 前端本地化导出

- 列表
  - 动态表格
  - 拖拽表格
  - 内联编辑
  - 内嵌菜单
  - 右键菜单
  - 表格表单联动编辑

- 手机端
  - 自动适配
  - 统一的视图组件  
  - 适配手机的卡片表格
  - 下拉、上滑、左滑
  - 抽屉效果
  - actionSheet

- 組件
  - 图像上传
  - 文件上传
  - 二维码
  - 返回顶部
  - 拖拽Dialog
  - 列表拖拽
  - 可拖动分栏

- 页面框架
  - 国际化多语言
  - 消息
  - 动态侧边栏
  - 动态面包屑
  - 快捷导航(标签页)
  - Svg 图标
  - Screenfull全屏
  - 页脚  

- 多环境发布
  - dev sit stage prod

```

## 前端开发

```bash
# 克隆项目
git clone -b git@github.com:qinyoyo/iotequ.git

# 进入项目目录
cd vue-iotequ

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev

```

## 后端开发

```bash
# 克隆项目
git clone -b git@github.com:qinyoyo/iotequ.git

# 进入项目目录
cd iotequ

# 开发环境
idea / eclipse 导入maven项目

```
默认浏览器访问 http://localhost:8188

## 发布
```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod

# 后端
maven package
```

## 其它

```bash
# 预览发布环境效果
npm run preview

# 预览发布环境效果 + 静态资源分析
npm run preview -- --report

# 代码格式检查
npm run lint

# 代码格式检查并自动修复
npm run lint -- --fix
```

## 简化的后端java代码结构
#### 生成的文件目录结构：

- controller : controller文件，自定义代码请通过服务扩展实现
-	dao 数据访问接口
- pojo：所有的生成的实体类均实现接口 CgEntity，实现CgTableAnnotation标注接口
- service/impl：服务，ICgService的实现类。
  命名规则为：
  XxxController 注入 CgXxxService实例，CgXxxService为Cg自动生成文件
  自定义代码文件，请实现服务：
  @Service
  public class XxxService extends CgXxxService
  如果定义了自定义的XxxService，controller将注入该实例而不注入CgXxxService实例
  如果定义了流程控制，请实现服务
  public class XxxFlowService extends SysFlowProcessService<Xxx> 或
  public class XxxFlowService implements IFlowService<Xxx>
-	util目录请自行定义
-	resources/mybatis ： mapper定义文件
-	cg自定义代码原则：
  -	不要修改由cg生成的文件
  -	通过服务实现特定功能
  -	所有的url请求必须通过button的完整定义，以保持前后端一致。具体的功能实现在自定义服务代码中实现

## 前端vue程序目录
####	src主要目录结构
-   assets：静态资源
-	components通用组件,iotequ cg组件
-	directive 通用指令，iotequ cg指令
-	filters：过滤器
-	icons 图标，可以扩展
-	lang 多语言
-	layout 页面框架
-	router 路由控制
-	store vuex状态控制
-	styles css文件
-	utils 一般通用函数
-	views 页面定义，所有cg生成的文件均在该目录下
  -	子目录名 为模块名
  -	二级子目录名为 代码定义名
    - 多语言文件 zh-cg.js，en-cg.js。自己需要的多语言文件定义请参照生成的两个文件格式定义，文件名为 zh-xxx.js 和 en-xxx.js。文件为一个map定义，用 export default 导出
    - route-cg.js。如果需要扩展路由定义，请编写文件 route-*.js
    - vue文件 cg 生成的页面定义组件
    -	*-mixin.js,*-mixin.vue ：自定义功能扩展文件，见3）
  -	common-views：系统使用的页面

####	Cg组件功能扩展
-	cg_list定义生成CgListXxx.vue, xxxx.vue组件, cg_form定义生成CgFormXxx.vue和xxxx.vue组件
-	Cg组件名为 CgListX.vue,定义一个 CgListX-mixin.js或CgListX-mixin.vue，Cg组件会自动将该组件通过 mixin 混入到Cg组件。该文件的定义与普通组件定义相同（必须是自定义文件混入到Cg文件，反过来使用则自定义方法无法在模板中使用。Mixin类似继承，父类不可能使用子类的方法）
-	mixin规则: 
  - 遵从vue通用规则
  
  -	如果需要重写生成组件的方法，按照以下规则定义：
    i.	自定义文件methods中定义一个优先级方法：
        useMixinMethodsFirst () { return true }
    ii.	通过 “super_函数名” 调用被覆盖的组件方法
    
  - 如果需要修改组件的data，请在created中定义，如自定义验证规则rules等。确保修改data属性不会影响组件的基本功能
  
  -	预定义函数，以下函数可以在自定义代码中重定义
    i.	useMixinMethodsFirst () 返回boolean 定义 自定义函数覆盖组件同名函数
    ii.	disabledAction(btn) 返回 Boolean 判断自定义按钮当前是否禁用
    iii.	extendActionFilter(action,record) 判断制定操作是否可用，
    
    ​		action：string，record：一个对象。返回true可用，false 不可用
    iv.	rowRender(row,index) 仅对mobile有效。自定义列表的显示。返回一个jsx定义
    v.	rowRenderGroupTitle(index) 仅对mobile有效，当时用自定义列表显示时，返回分组的标题
    vi.	rowClassName({row,rowIndex}) 仅对mobile有效，自定义行class
    vii.	newRecordForEditInline() 对于支持行内编辑的表格，可以通过在最后一行的最后一个字段回车新建一行，该重载该函数可以自定义缺省参数。结合后台 beforSave重载，可以快速输入表格数据。
    viii.	editInlineValidate(row, originalRow, editInlineFields)行内编辑校验函数
-	各个组件的模板属性，在cg里面添加定义（详见cg定义）
-	表单定义附加属性
-	列表定义附加属性
	
####	一个完整的自定义例子：
- cgListField为cgList子表，关联字段为 listName对应name。
- 在cgListField 中的所有字段必须为cgField中定义的字段，该字段列表通过table_id分类。但cgListField没有tableId字段，我们需要将cgList的tableId字段传递到cgListField
- 主从表定义文件为cgList目录下对应的list.vue,因此扩展该组件，定义文件list-mixin.js,定义一个属性 tableId，并修改 rowClick方法，点击cgList行时设置tableId。该方法覆盖原有方法，需要配置优先级方法
- 扩展cgListField，定义文件CgListCgListField-mixin.js，扩展一个props，命名为tableId，该值需要从 list表传递过来，需要设置主从表sonsProperties配置，配置一句 :tableId=”tableId”即可
- CgListCgListField-mixin.js配置watch，tableId改变时，从cgField获得字段列表
- CgListCgListField-mixin.js修改函数doAction，当为add或new时，执行一个transfer操作，打开对话框，获得选择，修改列表

## Cg定义简述
####	Cg定义层次
- 用户：用户只能编辑自己的cg定义
- 项目：一个用户下可以创建多个项目，项目对应spring boot的一个maven项目(pom)
- 代码定义：项目的子表，用以定义java后端的代码
- 字段：定义数据库字段定义或计算字段定义，生成实体类
- 按钮：也叫action，定义前后端的扩展操作
- 列表视图：定义前端列表视图，可以定义多个
- 表单：定义前端表单，可以定义多个

#### 代码自动生成步骤
- 第一种方式：按照Cg定义层次先后定义，禁止使用admin账户定义，即：
  先创建用户 -> 分配cg权限 ->
  
  创建项目 -> 
  
  创建代码定义->
  
  字段定义 -> 
  
  按钮定义-> 
  
  列表定义 -> 
  
  表单定义
  
- 第二种方式：本地快捷方式，从数据库表导入定义后再修改，即：
  
  先创建用户 -> 分配cg权限 -> 
  
  创建项目->
  
  自动导入数据库表(表名必须以项目简写和下划线开始，表包含主键)->
  
  修改字段->
  
  按钮定义->
  
  修改列表定义->
  
  修改表单定义

#### 定义
- ##### 项目定义
  

与pom对项目的定义相同，增加一个代码定义字段，用于标记数据库表的前缀
  其余的按照设计填写即可
  所有字段取值请按照统一规则填写，受正则表达式约束
  项目定义一般很少修改，配置好后生成pom文件可以根据需要修改
代码生成时不生成项目定义的pom文件

  

-	##### 代码定义
  
  - 项目，组织机构：通过定义的项目列表选择
  
  - cg代码：小写单词，下划线连接，可以使用数字(不推荐)
  
  - 标题：代码的名称
  
  - 数据库表：为空或 项目代码_code
  
  - 模板:vue-element
  
- Entity类名: 推荐使用 项目简写+code的驼峰表达式的首位大写格式
  
  - 子包，一般可设置为空。如果项目比较复杂，可以定义子包
  
  - Licence，使用天数：不需要就设置为空
  
  - Import列表：java代码使用到的应用，逗号分隔
  
  - 功能列表：需要生成哪些系统功能
  
  - 实体类的代码定义：import列表，基类以及接口，自定义代码。不再推荐使用
  
    
  
-	##### 字段定义
  
  - 主键：cg定义（包括非数据库cg定义）字段均需定义一个且仅有一个主键
  
  - 标题
  
  - 字段名：
    .	数据库字段名，小写加下划线组成
    .	为空，表示非数据库字段，仅仅是一个实体类字段，用于传递数据
    .	字段名 加 冒号 加 数据库表达式 ：数据库计算字段。不建议使用，会影响不同数据库的适配
    
  - Entity名：字段名的驼峰格式
  
  - 控件类型
  
  - 是否可以多选（如果有此属性的话，select，file模式下需要设置）
  
  - 排序
  
  - 默认值
    .	常量，字符串或数字等
    .	js:js代码 用一句js代码赋值，多为函数调用
    .	f:java后台代码，需要从后台设置默认值。为减少交互，尽量减少使用
    
  - 显示格式
    
    .	为空，使用默认格式，推荐
    .	js:js代码，用js函数设置显示内容。仅对列表有效。使用{field}来表示当先记录.如：
    
    ​		js:userFormatter(${field},’%d’)
    
  - 校验表达式
    .	正则表达式
    .	js:js代码 当js代码返回true时，通过验证，返回false 或非空字符串时不通过，提示默认提示语或返回的字符串
    
  - 校验提示语
  
  - 数据库属性：字段类型，字段长度，可空，主键或索引，小数位长，小数精度，主从表定义等（主从表请先定义主表后再定义从表）
  
  - 字典定义
    
    - 字典表或sql语句
      - 为空，通过后面的code，text对配置固定选择值
      - 为表名，全表选择作为字典
      - 系统数据字典名（code和text为空时）
      - Sql语句，必须以小写的select开头，如果需要部门权限限制，必须使用该模式，而且选择的字段中必须包含org_code字段。如 select org_code,name from sys_org
      - f:java函数，后端通过一个函数设置字典
      - js:js代码函数，前端函数设置字典
      - dict:字段entity名，引用一个字段对应的字典，当两个字段的字典完全一致时，使用字典应用提高效率
  - 字典code
      - 空，使用函数，应用或系统数据字典
      - 逗号分隔序列，定义固定的字典值
      - 数据库字段名
    - 字典text：字典显示值
      - 空，使用函数，应用或系统数据字典，或使用与code相同的序列
      - 逗号分隔序列，定义固定的字典显示值
      - 数据库字段名
    - 动态条件：使用sql查询时，可以配置动态查询条件。该条件为标准的sql语句的where(不含)子句
      - ${entity名} ：引用本记录字段值
      - ${ system.user.xxx} ：引用登录用户的某个属性
      - ${ System.orgListWithSuborg} ：允许的部门数据列表
      - ${ System.clientIp}：客户端ip
    - 树形结构字典
      - 是否在显示完整的名称，可能需要较宽的显示空间
      - 父亲字段名：数据库字段名而非entity名
    - 树键值字段：缺省使用主键，数据库字段名而非entity名
    
  - join 表连接,dict_list
  
    - join:标准的sql语法的join子句，将关联表字段加入到本表。一般对于数据量较大的主表字段引用（字典的效率较低）采用join方式。Join数据选择与字典的选择类似，但是是采用的弹出列表视图的方式。
  
    - join默认为单选，当设置为多选时，可以批量选择快速插入数据（关联字段为join列表选择，其余字段值相同。用户必须有 updateSelective 权限）。例如，向用户分组批量插入用户
  
    - join,dict_list必须要设置数据库字段名才有效。如果没有关联数据表，请设置一个虚拟的数据库字段名
  
    - dict_list: 支持多选的类似join用法，仅能插入单个字段。一般对于数据量较大字典应用且需要支持多选时，使用
  
      - 列表视图名：join选择表连接必须指定一个已定义的列表视图，该列表视图
  
      - 关联字段：与本字段相等的关联视图的字段
  
      - 插入字段列表：join到本记录的关联视图的entity字段列表(dict_list只能插入一个字段)
    
      - 关联筛选条件：
    
          > 定义关联视图查询的查询条件，为 关联视图Entity字段名 = 本记录Entity字段名的逗号分隔序列，如两个名称相同，可以省略 等号及本记录字段名，如
          > orgCode,name=username



  - ##### 按钮（功能行为）定义

      - 系统功能在cg代码定义里面选择。当需要自定义功能时，可通过增加按钮来扩展。
      - 系统功能和扩展的后台功能受角色权限控制
      - 操作代码：唯一的标识功能的驼峰表达式，同一个代码定义不能重复，也不可与系统预定义的功能代码重复
      - 标题：提示语
      - 图标：显示的图标
      - 行为特性：
          - 仅前端函数：不生成后台java代码。不受权限控制约束
          - 页面跳转：路由跳转，也不生成后台代码
          - 自定义函数调用后端：生成后端代码，受权限控制。调用后端功能是由前端代码定义执行的，因此比较灵活
          - 仅后端操作：生成后台代码，由cg自动执行调用。上传一个id参数
      - 行属性，仅对列表按钮操作有效，单行、多行或行无关
      - 执行函数或参数
          - 对于行为特性为仅前端和自定义调用后端时，这儿为一个函数调用。如果为组件的方法，请加上this.前缀，如this.connect(0)。如果该操作与行相关，调用时会自动将row或 [rows] 作为最后一个参数传递,实际调用为 this.connect(0,{row:选择的行记录})
          - 参数，object写法。如对于页面跳转，必须输入url属性，以及其他的附加属性，如 ：{url:"/reader/devPeople/sample",openMode:"edit"}。如果有后台操作，可以设置超时, timeout:超时时长。设为0表示无限等待，会弹出进度条。后台操作需要使用Util.setProgress函数设置当前的进度。完成时，需要调用Util.setProgress(100)
      - 显示属性：决定改按钮的使用环境（手机端、pc端，列表工具，行显示(暂不支持)或表单显示）
      - 操作前提示：对于危险操作可以在操作前提示用户选择是否执行
      - 操作后刷新列表：仅对列表后端操作有效



  - ##### 列表视图定义

      - 一个代码定义可以有多个列表视图。譬如定义一个普通的数据列表视图，定义一个join选择视图等。也可以没有列表视图

      - 列表视图属性
        
          - 名称：驼峰格式，用于指定组件名称，类名等。一个项目下唯一
          
          - 路由最后一级：默认使用list,join等，在vue路由里指定子路由的名称
          
          - 图标、标题、tag标题：图标和标题
          
          - 拖拽排序字段：该字段必须为一个整数值。通过列表的拖拽自动排序
          
          - 行内编辑：允许直接在列表中修改记录
          
          - 行内详情：保留，未使用
          
          - 主从表：
          
              - 子表：可选的列表，多选.可以输入一个组件（c|组件名|组件文件|标题）。如：
              
                c|CgFormPermissionTree|/common-views/extend-views/CgFormPermissionTree|sysPermission.title.record
              
              - 子表外键字段列表：与子表顺序对应,一个子表输入一项 : 子表关联字段Entity名|主表关联字段Entity名,引用主表主键时可以省略主表主键。如 orgCode,tableId|id
              
              - 主从表排列方式
              
              - 主表宽度：百分比，上下排列时为高度比例
              
              - 子组件属性：指定子组件的模板属性而不是采用cg的默认设置。与子表排列顺序相同，逗号分隔。如 ： ,:tableId="tableId" 会在第二个子表组件模板加上属性:tableId="tableId"
          
          - 树形结构列表
          
              - 树显示entity字段：以该字段作为树结构显示标识
              - 树的父级entity：定义上级id的字段，必须与主键对应
          
          - 合并字段：指定合并分组的字段，可多选
          
          - 表格属性：
          
              - 表高度：0表示自动适配，建议使用0
              - 表最大高度：0表示自动适配
              - 显示在标题里的字段：多用于主从表标识当前的主表
              - 分页：pc模式使用分页插件，手机模式使用 loadmore模式
                工具条模式：1：工具条 2：下拉菜单，其余值保留，均当成1处理
              - 多选：
              - 其余属性保留，请使用默认值
          
          - 页面定义
          
              - 顶部轮播图像列表，保留
              
              - 功能清单：选择本列表使用的功能
              
              - 本地导出：本地导出仅导出列表数据，前端操作，数据不可重新导入
              
              - 默认排序：数据库查询的默认排序order by 子句
              
          - 视图属性：一个列表会生成两个组件，视图组件可以直接在路由中显示，列表组件必须在其他视图模板中使用。试图组件会使用列表组件以及可能的子表组件。视图属性定义了视图组件渲染列表组件时的属性
            
              - 列表属性：列表视图的自定义属性
                例如 定义 一个 devPeople的列表，路由为 list,会生成 列表组件 CgListDevPeople和视图组件list
                List的模板大致为 ：
                
                ```
                <template>
                  <el-card shadow="hover">
                    <div slot="header">
                      <cg-header …/>
                    </div>
                    <CgListDevPeople 视图属性 … />
                  </el-card>
                 </div>
                </template>
                ```
                
                而CgListDevPeople大致为
                
                <template>
                  <div class="cg-list">
                    <el-table v-if=”” 列表属性 >
                      <el-table-column 列属性>
                        <template slot-scope="scope">
                          自定义字段显示 或 cg默认显示
                        </template>
                      </el-table-column>
                    </el-table>
                    <cg-card-list v-else 列表属性/>
                  </div>
                </template>
              
              

  - ##### 列表视图字段
    
      - 列表视图的字段为代码定义的字段（包括join加入的字段）的子集
        
      - 也可以定义或增加字段用于特定的数据传输（与后台数据无关）。自定义的字段不支持字典选择
        
      - 查询模式：
        
          - 自动,系统自动设置。一般都选用改模式
          - 筛选：通过查询的列表数据集合进行筛选
          - 范围：目前暂且只支持数字和时间的范围选择（时间自动也是范围选择），要实现时间的匹配选择（=），请使用模糊查询，然后选定字段名
        
      - 缺省查询条件：该字段查询匹配的默认值，为js常量或计算值如： 
        
        ```
        [time.startOf(time.dateAdd(new Date(),-1,'day'),'day'),
        time.endOf(time.dateAdd(new Date(),-1,'day'))] 
        ```
        
        定义了一个时间范围
        
      - 修改控件类型为 ：允许修改字段的控件类型，非字典类型不能修改为字典类
        
      - 隐藏字段：字段只做传值使用，不显示。**存在字典且允许双击编辑的字段，必须设置为隐藏字段**。
        
      - 对齐方式
        
      - 行内编辑：该字段可编辑，如果列表打开了行内编辑的话
        
      - 默认宽度：0位自动设置
        
      - 列属性：见列表视图页面定义例子
        
      - 自定义字段显示： 用自定义的字段显示替代默认的显示如：
      
      ```
      <i :class="scope.row.isRunning?'fa fa-play':'fa fa-pause'" />
      ```
      
      
      
    - 其余扩展属性保留，未使用
      
      
    
  - ##### 表单定义
      - 一个代码定义可以有多个表单视图，譬如流程类处理，每个流程可能需要定义不同的表单
      
      - 表单属性
          - 名称：驼峰格式，用于指定组件名称，类名等。一个项目下唯一
          - 路由最后一级：默认使用record等，在vue路由里指定子路由的名称
          - 图标、form标题、tag标题、字段标题位置（仅对pc生效）：图标和标题
          - 是否流程定义。cg会自动添加流程处理相关字段的处理。
          - 是否对话框模式
          - 是否支持连续新建
          - 顶部轮播图像（保留）
          - 可选的功能：如果存在的话，可以选择
          
      - 视图属性：与列表类似一个表单会生成两个组件，视图组件可以直接在路由中显示，表单组件必须在其他视图模板中使用。视图组件会使用表单组件。视图属性定义了视图组件渲染表单组件时的属性
      
- 表单属性：表单视图的自定义属性
        例如 定义 一个 devPeople的表单，路由为 record,会生成 表单组件 CgFormDevPeople和视图组件 record
        
        ```
         record的模板大致为 ：
         <template>
           <div class="cg-form">
             <cg-header />
          <CgFormDevPeople 视图属性/>
         </div>
         </template>
         而CgFormDevPeople大致为
          <template>
            <div class="cg-form">
              <el-form 表单属性 >
                <el-form-item form-item属性>
                  <el-xxx 输入控件属性>
                    Slot模板属性
                 </el-xxx>
               </el-form-item>
             </el-form>
           </div>
         </template>
        ```
    
    
    ​    


- ##### 表单字段
  
      - 分组标签：表单字段是严格排序的。如果需要对表单进行tabs分类处理，在每一个tab的第一个字段中协商分组标签文字即可。如果分组，那么第一个字段必须设置分组标签
      - 更改控件类型：与列表视图字段类似
      - 图标
      - 宽度： 最大宽度为24。在pc模式下，会按照24的总宽度进行匹配，自动合并行
      - 隐藏：不显示，不占宽度，仅传递数据
      - 显示title提示
      - 只读：如果为新建并且字段必须赋值(必填为true)，该只读属性自动失效
      - 必填
      - 输入控件属性：见上例
      - Form_item属性
      - Slot模板属性：仅对显示类型为text有效
      - 超链接：标题为连接的锚点

#### 字段有效性验证

rules.js定义了表单字段的验证条件

可以自定义 rules-mixin.js 定义自己的验证条件。rules-mixin的定义优先

自定义验证条件模板 1 :

`export default {`

  `entityName : [`

​	`{`

​     `.......`

​    `},`

​    `......`

  `],`

  `.....`

`}`

如果验证函数需要应用对象属性，请使用自定义验证模板2：

`export default function getRules(vueObject, getRecordfunc)  {`

`return {`

  `entityName : [`

​	`{`

​     `.......`

​    `},`

​    `......`

  `],`

  `.....`

`}`

`}`

vueObject 为表单cgForm，列表cgList对象，getRecordfunc为一个函数，返回整条记录

列表行内编辑引用相同的验证规则

#### 自定义属性意义

  前面定义了有很多自定义属性，目前在于有效的对模板进行自定义配置。
  譬如，一般的form-item的标题是固定设置的 
    :label = “$t(‘label’)”
  如何做到动态变化form-item的标题呢？你可以 在form-item属性里这样 设置
    :label =  “labelFunc($t(‘label’))” 
  而通过一个labelFunc函数来动态设置。
  可以定义的属性取决于具体的组件，也并非所有属性都可定义（cg默认不允许修改的属性），一般编程就够了



 ## 行内编辑

  - 在列表表格内对数据进行编辑。
  - 仅对pc有效
  - 列表视图需要打开行内编辑
  - 选择需要编辑的字段打开行内编辑。行内编辑不允许对字段设置空值或空白，0等，必须输入值才可以保存。因此设置行内编辑，默认就是打开了必须输入开关
  - 行内编辑提供一种快速的编辑模式，没有自动校验。如果需要对数据进行校验，请扩展定义函数editInlineValidate(row, originalRow, editInlineFields)。返回true为验证通过。row为修改后的记录，originalRow为修改前记录, editInlineFields为允许修改的字段序列。对于新建行，不传后面两个参数
  - 新增加系统功能 editInline_add。打开该开关，可以在列表中快速插入行数据并进行修改，保存。修改方式为行内编辑，并遵从行内编辑规则。对于新建的行其余的字段赋值，请通过重写函数newRecordForEditInline，和editInlineValidate函数来修改字段。
  - 行编辑行操作通过右键处理，多行或插入操作通过主菜单处理
  - 行内编辑支持回车键向后快速切换焦点。到达最后一个字段回车，如果支持插入操作将自动插入一行记录



## 生成代码

- 项目定义代码在项目维护中生成

- 代码定义生成代码在代码定义里生成

- 通过代码下载生成的代码

- 生成的代码包含了sql脚本。第一次你可用该脚本来初始化数据库

- 代码按照前后端目录分开，直接拷贝即可。

  

## 常用参考

- 定义菜单项

  - 功能地址必填，指定路由路径。定义路由跳转地址和设定菜单执行权限，如果用户不具备该路由权限，该菜单项不会显示
  - 操作函数：为一个menu-actions.js文件定义的函数名称。为空时，进行路由切换。如果为request，则通过get访问后端地址，后端地址为路由路径指定
  - 附加参数为一个json串，用于给操作函数传递参数，或为空

- 对话框函数

  - 先定义一个组件，模板格式为：

    <template>
      <div class="cg-form" :class="dialogClass">
        <el-dialog ref="dialog" 
                   v-el-drag-dialog 
                   top="0px" 
                   :class="dialogClass" 
                   :visible.sync="visible" 
                   append-to-body 
                   :close-on-click-modal="false" 
                   @closed="closeDialog(false)">
           ......
        </el-dialog>
      </div>
    </template>


    <script>
    export default  {
      name: 'CgDialogDemo',
      directives: { elDragDialog },
      props: {
        dialogClass: {
          type: String,
          default: null
        },
        ......
      },
      data() {
        return {
          visible: true,
          ......
        }
      },
      methods: {
        closeDialog(confirm) {
          this.visible = false
          this.$emit('close', confirm ? this.selected : null)
          ......
        },
        ......
      }
    }
    </script>

  - 调用 window.$vue.$dialog(对话框组件类, 传递的参数map)

  - 参考 views/common-views/extend-views/CgRegTest.vue

- 

## 更多信息请参考 [使用文档](https://www.iotequ.top/admin/res/doc)


## Online Demo

[在线 Demo](https://www.iotequ.top/admin)

## Donate

如果你觉得这个项目帮助到了你，你可以帮作者买一杯果汁表示鼓励 ​[donate](https://www.iotequ.top/admin/res/donate)

## License

[MIT](https://www.iotequ.top/admin/res/LICENSE)

Copyright (c) 2020-present Qinyoyo
