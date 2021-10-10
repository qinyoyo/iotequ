package top.iotequ.c20.controller;


import java.util.List;


public class C20HttpServer {

    //用户基本信息
    public static class UserBaseInfo {

        private String idName;//用户姓名
        private String idCertNumber;//身份证号码
        private String idSex;//性别
        private String idNation;//民族
        private String idBirth;//出生年月
        private String idCertAddress;//出生地址
        private String idType;//用户类型
        private String userNo;//用户编号
        private String photo;//图像数据
        private String ExpireStart;//身份证开始日期
        private String ExpireEnd;//身份证失效日期
        private String Agency;//签证机关

        public String getExpireEnd() {
            return ExpireEnd;
        }

        public void setExpireEnd(String expireEnd) {
            ExpireEnd = expireEnd;
        }

        public String getAgency() {
            return Agency;
        }

        public void setAgency(String agency) {
            Agency = agency;
        }

        public String getExpireStart() {
            return ExpireStart;
        }

        public void setExpireStart(String expireStart) {
            ExpireStart = expireStart;
        }

       


        public String getIdName() {
            return idName;
        }

        public void setIdName(String idName) {
            this.idName = idName;
        }

        public String getIdCertNumber() {
            return idCertNumber;
        }

        public void setIdCertNumber(String idCertNumber) {
            this.idCertNumber = idCertNumber;
        }

        public String getIdSex() {
            return idSex;
        }

        public void setIdSex(String idSex) {
            this.idSex = idSex;
        }

        public String getIdNation() {
            return idNation;
        }

        public void setIdNation(String idNation) {
            this.idNation = idNation;
        }

        public String getIdBirth() {
            return idBirth;
        }

        public void setIdBirth(String idBirth) {
            this.idBirth = idBirth;
        }

        public String getIdCertAddress() {
            return idCertAddress;
        }

        public void setIdCertAddress(String idCertAddress) {
            this.idCertAddress = idCertAddress;
        }

        public String getIdType() {
            return idType;
        }

        public void setIdType(String idType) {
            this.idType = idType;
        }

        public String getUserNo() {
            return userNo;
        }

        public void setUserNo(String userNo) {
            this.userNo = userNo;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

    //用户基本信息+手指信息
    public static class UserBaseInfoRsp {

        public boolean retCode;//返回状态 0:成功  1:失败
        public UserBaseInfo RegReq;//注册用户请求
        public List<C20Finger> c20fingers; //手指信息
        public C20BindInfo c20info;//C20设备信息

        public static class C20Finger {

            public byte fingerType;   //手指型号
            public byte fignerIndex;  //手指序号 1,2
            public byte warningfinger;//手指是否是胁迫
            public String templates;  //手指模板

            public byte getFingerType() {
                return fingerType;
            }

            public void setFingerType(byte fingerType) {
                this.fingerType = fingerType;
            }

            public byte getFignerIndex() {
                return fignerIndex;
            }

            public void setFignerIndex(byte fignerIndex) {
                this.fignerIndex = fignerIndex;
            }

            public byte getWarningfinger() {
                return warningfinger;
            }

            public void setWarningfinger(byte warningfinger) {
                this.warningfinger = warningfinger;
            }

            public String getTemplates() {
                return templates;
            }

            public void setTemplates(String templates) {
                this.templates = templates;
            }
        }
    }

    // 上传的日志信息
    public static class C20LOG {
        public C20BindInfo c20info;//C20设备信息
        public String userNo;// 用户编号
        public String date;// 验证日志
        public int warningfinger;// 告警标识
    }

    // C20绑定信息
    public static class C20BindInfo {
        public String SN;// 序列号
        public String devNo;// 设备编号
    }

    // 上送服务器验证考勤
    public static class UploadAuthUser {
        public String SN;// 序列号
        public String devNo;// 设备编号
        public String template;//模板数据
    }

    public static class C20ServerAuth {
        public boolean AuthRetCode;// 认证结果 2：包含多人，请求按第二指以交叉验证 1：认证成功 0：认证失 -1：超出有效时间段 -2：超出有效期 -3：无此终端权限
        public UserBaseInfo userBaseInfo;//认证结果
    }

    public static class C20RetMsg {
        public boolean status;//true:成功,false失败
        public String msg;//返回结果信息
    }

    public static class BindRetMsg {
        public boolean success;//true:成功,false失败
        public String message;//返回结果信息
    }

}
