package top.iotequ.test;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import top.iotequ.ewallet.event.UserInfoChangedEvent;
import top.iotequ.framework.util.Util;

@Service
public class TaskTest {
    public void send() {
        ApplicationContext context = Util.getApplicationContext();
        context.publishEvent(new UserInfoChangedEvent(this,"send test"));
    }
    static public void ssend() {
        ApplicationContext context = Util.getApplicationContext();
        TaskTest instance=Util.getBean(TaskTest.class);
        context.publishEvent(new UserInfoChangedEvent(instance,"static send test "));
    }
}
