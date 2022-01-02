const ffi = require("ffi-napi")
const ref = require('ref-napi')
const callbackFunction = 'pointer'
const stringBuffer = 'pointer'
const byteBuffer = 'pointer'
const sdk = ffi.Library("libsveinsdk", {
  /***********************************
   * 获取 SDK 版本号
   * 出参：
   *      version 版本号字符串
   *      len 版本号长度
   **********************************/
  // int  STDCALL drvApiVersion(char *version, int *len);
  drvApiVersion: ["int", [stringBuffer, "int*"]],

  /***********************************
   * 获取 设备 版本号
   * 出参：
   *      version 版本号字符串
   *      len 版本号长度
   **********************************/
  // int  STDCALL drvDeviceVersion(char *version, int *len);
  drvDeviceVersion: ["int", [stringBuffer, "int*"]],

  /***********************************
   * 初始化指静脉设备的连接
   * 入参：
   *      path 串口名  比如： windows系统：com1   Linux系统：/dev/ttyS1, NULL 或者 ""将自动寻找串口
   *      isDelay 不再使用该参数，设为 1
   * 出参：
   *      path 如果输入path为"",将回传连接上的串口名
   **********************************/
  // int STDCALL drvConnect(char *path, int isDelay);
  drvConnect: ["int", [stringBuffer, "int"]],

  /***********************************
   * 断开设备的连接
   **********************************/
  // int STDCALL drvExit();
  drvExit: ["int", []],

  /**************************************
   * 中断驱动程序当前长时间等待的操作
   ************************************/
  // int STDCALL drvCancel();
  drvCancel: ["int", []],
  /***********************************
   * 中断驱动程序当前长时间等待的操作，同时向设备发出指令，终止设备正在进行的操作
   **********************************/
  // int STDCALL drvStop();
  drvStop: ["int", []],

  /***********************************
   * 注册新用户，采集1根手指信息，单个手指需要采集3次
   * 入参：
   *      userId  新用户编号 0~9的数字字符串  最长16个字符
   *      timeOut 超时时长  单位：毫秒
   *      cb 操作过程中的提示回调
   **********************************/
  // int   STDCALL drvRegister(const char *userId, int timeOut, RegisterCallback cb);
  drvRegister: ["int", ["string", "int", callbackFunction]],

  /***********************************
   * 注册新用户，采集2根手指信息，单个手指需要采集3次
   * 入参：
   *      userId  新用户编号 0~9的数字字符串  最长16个字符
   *      timeOut 超时时长  单位：毫秒
   *      cb 操作过程中的提示回调
   **********************************/
  //DLL_API int STDCALL drvRegister2Fingers(const char *userId, int timeOut, RegisterCallback cb);
  drvRegister2Fingers: ["int", ["string", "int", callbackFunction]],

  /***********************************
   * 用户指静脉验证,如果感应器上无手指，那么立刻返回，当认证成功时，返回验证数据
   * 入参：
   *      timeOut 超时时长  单位：毫秒    0表示不等待，如果感应器上无手指，那么立刻返回
   *      cb 验证过程中的提示回调函数，不同过程状态提示不同信息。当为null的时候，不提示
   * 出参：
   *      userId  验证成功，保存用户编号 0~9的数字字符串
   *      data    获取的指静脉数据缓冲区, data 预留空间需大于等于288字节
   *      length  实际获取指静脉数据大小
   **********************************/
  // int STDCALL drvAuth(char *userId, int *length, int timeOut, AuthCallback cb);
  drvAuth: ["int", [stringBuffer, "int*", "int", callbackFunction]],

  // int STDCALL drvAuthLoad(char *userId, int timeOut, unsigned char *data, int *length, AuthCallback cb);
  drvAuthLoad: ["int", [stringBuffer, "int", byteBuffer, "int*", callbackFunction]],

  /***********************************
   * 用户指静脉验证即时读取数据
   * 用于服务器认证
   * 入参：
   *      timeOut 超时时长  单位：毫秒    0表示不等待，如果感应器上无手指，那么立刻返回
   *      cb 验证过程中的提示回调函数，不同过程状态提示不同信息。当为null的时候，不提示
   *      dataSize data buffer 大小
   * 出参：
   *      data    获取的指静脉数据缓冲区
   *      length  实际获取指静脉数据大小
   **********************************/
  // int STDCALL drvLoad(unsigned char *data, int dataSize, int *length, int timeOut, AuthCallback cb);
  drvLoad: ["int", [byteBuffer, "int", "int*", "int", callbackFunction]],

  /***********************************
   * 用户获取手指静脉图像
   * 入参：
   *      dataSize 缓冲区大小，必须大于838
   *      compressImage 可能的话压缩图像数据
   *      timeOut  超时时长  单位：毫秒    0表示不等待，如果感应器上无手指，那么立刻返回
   *      cb       验证过程中的提示回调函数，不同过程状态提示不同信息。当为null的时候，不提示
   * 出参：
   *      img      获取成功，保存图像数据, >838 Bytes.
   *      length   获取成功,保存图像文件字节数
   **********************************/
  // int STDCALL drvReadImage(unsigned char *img, int dataSize, int *length, int compressImage, int timeOut, AuthCallback cb);
  drvReadImage: ["int", [byteBuffer, "int", "int*", "int", "int", callbackFunction]],

  /***********************************
   *   同时采集图像和模板
   ***********************************/
  //DLL_API int STDCALL drvSampleTemplateAndImage(unsigned char *templateData, int templateDataSize, int *templateLength,
  //  unsigned char *imgData, int imageDataSize, int *imageLength,
  //  int compressImage, int timeOut, AuthCallback cb);

  // int STDCALL drvCompressImage(const unsigned char *img, int length, unsigned char *data, int dataSize, int *dataLength);
  drvCompressImage: ["int", [byteBuffer, "int", byteBuffer, "int", "int*"]],

  // int STDCALL drvUncompressImage(const unsigned char *data, int length, unsigned char *img, int imgSize, int *imgLength);
  drvUncompressImage: ["int", [byteBuffer, "int", byteBuffer, "int", "int*"]],

  /***********************************
   * 发送POLL命令，查询感应器上手指的状态
   * 入参：
   * 出参：
   *      existed  感应器上是否有手指  AL_TRUE有手指 FALSE 无手指
   *      changed  手指的状态是否发生变化  TRUE有变化  FALSE无变化
   **********************************/
  // int STDCALL drvFingerStatus(int *existed, int *changed);
  drvFingerStatus: ["int", ["int*", "int*"]],

  /***********************************
   * 1:1用户指静脉验证,如果感应器上无手指，那么立刻返回
   * 入参：
   *      userId  指定待验证的用户编号  最长16个字符
   *      timeOut 超时时长  单位：毫秒    0表示不等待，如果感应器上无手指，那么立刻返回
   *      cb 验证过程中的提示回调函数，不同过程状态提示不同信息。当为null的时候，不提示
   * 出参：
   **********************************/
  //DLL_API int STDCALL drvAuth1By1(const char *userId, int timeOut, AuthCallback cb);
  drvAuth1By1: ["int", ["string", "int", callbackFunction]],

  /***********************************
   * 获取设备上指定范围内的用户列表
   * 入参：
   *      startUser 起始用户编号  最长16个字符
   *      endUser   终止用户编号  最长16个字符
   *      listBuffer 用于存储用户编号列表，实际为二维字符串数组list[][DYNA_VEIN_SIZE_USER_ID]地址
   *                 单个用户编号占据16字节，不足的用0填补。用户编号按照字符串进行排序
   *      bufferSize 缓冲区字节数， 大小 = DYNA_VEIN_SIZE_USER_ID * maxNum
   * 出参：
   *      userNum     查询成功，返回用户数量
   * 返回值：
   *       AL_STS_LESS_BUFFER  输入缓冲区不足以存放所有用户信息，成功查找到部分用户，userNum记录部分用户数量
   **********************************/
  // int STDCALL drvGetUserList(const char *startUser, const char *endUser, unsigned char *listBuffer, int bufferSize, int *userNum);
  drvGetUserList: ["int", ["string", "string", byteBuffer, "int", "int*"]],

  /***********************************
   * 查询特定用户是否存在
   * 入参：
   *      userId 用户编号  最长16个字符
   **********************************/
  // int STDCALL drvUserExist(const char *userId);
  drvUserExist: ["int", ["string"]],

  /***********************************
   * 清除已经注册的用户信息。清除命令将引发设备的复位。
   **********************************/
  // int STDCALL drvClearUsers();
  drvClearUsers: ["int", []],

  /***********************************
   * 发送DELE命令，清除特定的用户信息。
   * 入参：
   *      userId 用户编号   0~9的数字字符串，最大16个字节
   **********************************/
  // int STDCALL drvDeleteUser(const char *userId);
  drvDeleteUser: ["int", ["string"]],

  /***********************************
   * 设定设备的key。
   * 入参：
   *      key 设备key 8字节
   **********************************/
  // int STDCALL drvSetDeviceKey(const unsigned char *newKey);
  drvSetDeviceKey: ["int", ["string"]],

  /***********************************
   * 获取设备的设备号
   * 入参：
   * 出参：
   *      deviceId 操作成功，保存设备号  8字节
   **********************************/
  // int STDCALL drvGetDeviceID(unsigned char *deviceId);
  drvGetDeviceID: ["int", [stringBuffer]],

  /***********************************
   * 从设备获取特定用户特征数据流
   * 入参：
   *      userId 用户编号  最大16个字节
   *      buffer 用于保存特征模板的缓冲区
   *      bufferSize 缓冲区大小
   * 出参：
   *      length  查找成功，返回模板特征的字节数
   **********************************/
  // int  STDCALL drvLoadFeature(const char *userId, unsigned char *buffer, int bufferSize, int *length);
  drvLoadFeature: ["int", ["string", byteBuffer, "int", "int*"]],

  /***********************************
   * 在设备上保存用户的特征信息，登记新的用户
   * 入参：
   *      feature 特征数据流
   *      size 特征字节数,必须=864(单指)或1728(双指)
   **********************************/
  // int STDCALL drvSaveFeature(unsigned char *feature, int size);
  drvSaveFeature: ["int", [byteBuffer, "int"]],

  /***********************************
   * 在设备上批量保存用户特征
   * 入参：
   *      feature 特征数据流
   *      size 特征字节数 必须864的倍数
   **********************************/
  // int STDCALL drvSaveFeatures(const unsigned char *feature, int size);
  drvSaveFeatures: ["int", [byteBuffer, "int"]],

  /***********************************
   * 获取设备的软件版本号
   * 入参：
   * 出参：
   *      version 保存软件版本号，4个字符
   **********************************/
  // int STDCALL drvGetVersion(char *version);
  drvGetVersion: ["int", [stringBuffer]],

  /***********************************
   * 获取设备的用户容量
   * 入参：
   * 出参：
   *      userMax 保存最大用户数
   **********************************/
  // int STDCALL drvGetCapacity(int *userMax);
  drvGetCapacity: ["int", ["int*"]],

  /***********************************************************
   * 获取设备当前的用户数
   * 入参：
   * 出参：
   *      userNum 保存用户数目
   **********************************************************/
  // int  STDCALL drvGetUserNum(int *userNum);
  drvGetUserNum: ["int", ["int*"]],

  /***********************************
   * 设定蜂鸣器开启/关闭
   * 入参：
   *      buzzerOn    1 开启
   *                  0 关闭
   **********************************/
  // int  STDCALL drvSetBuzzer(unsigned char bzOn);
  drvSetBuzzer: ["int", ["byte"]],

  // int  STDCALL drvChangeBPS(int bps);
  drvChangeBPS: ["int", ["int"]],

  // int  STDCALL drvSetActiveAuth();
  drvSetActiveAuth: ["int", []],

  /***********************************
   * 获取错误信息字符串
   * 入参：
   *      errNo     错误编号
   *      buffer    返回缓冲区
   *      bufferLen 缓冲区大小
   * 出参：
   *      buffer    错误信息
   * 返回值：
   *       AL_STS_OK 始终返回成功
   **********************************/
  // int STDCALL drvMessage(int errNo, char *buffer, int bufferLen);
  drvMessage: ["int", ["int", stringBuffer, "int"]],

  // int STDCALL drvOpenDebug(int open);
  drvOpenDebug: ["int", ["int"]]
})
const AL_STS_OK  =                 0            // 运行正确
const AL_STS_ERROR  =              1            // 运行错误
const AL_STS_TIMEOUT =             2            // 操作超时
const AL_STS_LESS_BUFFER =         3            // 缓冲区空间不足
const AL_STS_NOT_FOUND =           4            // 未找到信息。
const AL_STS_CANCELED    =         5            // 操作过程被中断
const AL_STS_NO_FINGER    =        6            // 感应器上无手指
const AL_STS_AUTH_FAILED   =       7            // 验证指静脉失败
const AL_STS_ERROR_NOT_CONNECTED=  8            // 未连接设备
const AL_STS_ERROR_PARAMETER     = 9            // 函数调用入参错误
const AL_STS_ERROR_SEND          =10        // 发送报文出错
const AL_STS_ERROR_SEND_OVERLAOD =11        // 发送报文过长
const AL_STS_ERROR_CHECKXOR      =12        // 应答报文异或字错误
const AL_STS_ERROR_CHECKSUM      =13        // 应答报文校验和错误
const AL_STS_ERROR_RCV_CMD       =14        // 应答报文中的命令码错误
const AL_STS_ERROR_DATA_RCV      =15        // 接收应答数据错误
const AL_STS_CMD_ERROR           =16        // 命令应答中，反馈的错误命令数>0
const AL_STS_REPEAT_FINGER       =17        // 注册时重复手指
const AL_STS_FINGER_LEAVE        =18        // 认证时，手指中途离开
const AL_STS_ERROR_IMG           =19        // 获取验证图像失败
const AL_STS_ERROR_BUSY          =20        // 设备忙， add by qinyoyo

//系统提示信息
const AL_INFO_DETECT_FINGER               = 1      //提示用户安放手指，采集手指指静脉图片
const AL_INFO_RELEASE_FINGER              = 2      //提示用户手指离开指静脉设备
const AL_INFO_GET_IMAGE                   = 3      //提示用户正在捕获指静脉图像
const AL_INFO_PROCESS_IMAGE               = 4      //正在处理图像
const AL_INFO_COMPARE_USER                = 5      //正在比对用户
const AL_INFO_GOT_IMAGE                   = 6      // 提示用戶已經獲取指靜脈數據

function allocInt(def) {
  const buf = new Buffer(4)
  buf.writeInt32LE(def, 0)
  return buf
}
function getInt(buf) {
  buf.type = ref.types.int
  return buf.deref()
}
function allocString(len,def) {
  const buf = new Buffer(len)
  buf.writeCString(def,0,"gbk")
  return buf
}

function hexStringOf(buf,len) {
  const tab=['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F']
  let s=''
  for (let i=0;i<len;i++) {
    const b=buf.readUInt8(i)
    s+=tab[b>>4]
    s+=tab[b&0xf]
  }
  return s
}
function messageOf(code) {
  let buf = new Buffer(100)
  sdk.drvMessage(code,buf,100)
  return buf.toString('GBK')
}
export default {
  connect: function() {
    let buf = allocString(16,'')
    let r=sdk.drvConnect(buf,0)
    return {
      errorCode: r,
      device: r ? messageOf(r) : buf.toString('GBK')
    }
  }
}
