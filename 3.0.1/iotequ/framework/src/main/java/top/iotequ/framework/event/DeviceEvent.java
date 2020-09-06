package top.iotequ.framework.event;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class DeviceEvent extends ApplicationEvent  {
	private static final long serialVersionUID = 1L;
	String  deviceType;         // 设备类别，D10，D30，C20...
	String  deviceNo;           // 设备号，不同设备定义不一样，用于确定具体设备
	String  deviceMode;         // 设备业务属性，事件接受方根据该属性确定是否处理该属性，目前已定义的包括 MJ(门禁),XG(巡更),AD(签到),ON(上班打卡),OFF(下班打卡),通过数据字典dev_mode扩展
	String  userNo;             // 人员userNo
	Boolean warning;            // 是否胁迫
	Date    time;               // 事件事件
	Map<String,Object> data;    // 事件扩展参数
	public DeviceEvent(Object source) {
		super(source);
		time = new Date();
		data = new HashMap<>();
	}
	public void put(String key,Object value) {
		data.put(key,value);
	}
	public Object get(String key) {
		return data.get(key);
	}
}
