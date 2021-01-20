package top.iotequ.reader.event;

import org.springframework.context.ApplicationListener;

import top.iotequ.svasclient.TemplatesChangedEvent;
import top.iotequ.util.SqlUtil;

public class TemplatesChangedEventHandler implements ApplicationListener <TemplatesChangedEvent> {
	@Override
	public void onApplicationEvent(TemplatesChangedEvent event) {
			try {
				SqlUtil.sqlExecute("update dev_people set reg_fingers=? where user_no=?", event.getFingers(),event.getUserNo());
			} catch (Exception e) {}	
	}
}
