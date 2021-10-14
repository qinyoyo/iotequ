package top.iotequ.svas.service;

import java.util.List;

public class SvasTypes {
    static public class JniStringReturn {
        public String string;
    }
    static public class SvasUserInfo {
        public String userNo;
        public int    idType;
        public String idNo;
        public String name;
    }
    static public class SvasTemplates {
        public int	  	fingerNo;
        public int	  	fingerType;
        public String 	templates;
        public boolean 	warning;
    }
    static public class SvasFingerInfo {
        public int count;
        public List<SvasTemplates> list;
    }
    static public class SvasMatchInfo {  // 修改必须与svas.dll 同步修改，jni 使用 int 而不使用 Integer
        public int	 	id;  // 数据记录指静脉的主键,用于调试查错等
        public int 		score;
        public int 	    fingerNo;
        public int	  	fingerType;
        public boolean 	warning;
        public String 	userNo;
        public String 	name;
        public int      idType;
        public String   idNo;

    }
    static public class SvasMatched {  // 修改必须与svas.dll 同步修改
        public int       count;
        public int       state;
        public int       dictSize;
        public long      usUsed;
        public long      matchUsed;
        public List<SvasMatchInfo> list;
    }

    static public class SvasErrorInfo {
        public Boolean exists;
        public Boolean success;
        public int error;
        public String message;
    }
    static public class SvasSetting {
        public int  debug;
        public int  matchTrace;
        public int	thresh;

        public String userNoPrefix;
        public int    userNoMminLength;
        public String sn;


        public String host;
        public int	  port;
        public String user;
        public String password;
        public String schema;

        public int   autoUpdateTemplate;
        public int   templatesMustMatch;
        public int   multiMatchedWeight;
        public int   maxMatchedWeight;
        public int	 maxMatchedCondition;
    }

}
