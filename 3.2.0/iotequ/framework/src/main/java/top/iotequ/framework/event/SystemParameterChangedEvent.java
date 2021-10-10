package top.iotequ.framework.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import top.iotequ.framework.bean.SpringContext;
import top.iotequ.util.Util;

@Setter
@Getter
public class SystemParameterChangedEvent extends ApplicationEvent {
    public static final String SP_PERMISSION_INITIAL = "SP_PERMISSION_INITIAL";
    public static final String SP_DATA_DICT = "SP_DATA_DICT";
    public static final String SP_ORG_RELOAD = "SP_ORG_RELOAD";
    public static void sendPeemissionInitialEvent(Object source, Object value) {
        SystemParameterChangedEvent event=new SystemParameterChangedEvent(source,value);
        Util.sendRedisMessage(SP_PERMISSION_INITIAL,event);
    }
    public static void sendDataDictEvent(Object source,Object value) {
        SystemParameterChangedEvent event=new SystemParameterChangedEvent(source,value);
        Util.sendRedisMessage(SP_DATA_DICT,event);
    }
    public static void sendReloadOrgEvent(Object source,Object value) {
        SystemParameterChangedEvent event=new SystemParameterChangedEvent(source,value);
        Util.sendRedisMessage(SP_ORG_RELOAD,event);
    }
    public SystemParameterChangedEvent(Object source, Object val) {
        super(source);
        sender = SpringContext.nodeId;
        value = val;
    }
    String sender;
    Object  value;
}
