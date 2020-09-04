package top.iotequ.ewallet.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;
import top.iotequ.ewallet.pojo.EwUser;
@Data
public class UserInfoChangedEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    private EwUser user;
    private Boolean remove;
    public UserInfoChangedEvent(Object source,EwUser user) {
        super(source);
        setUser(user);
        remove=false;
    }
    public UserInfoChangedEvent(Object source,String userNoListForRemove) {
        super(source);
        user=new EwUser();
        user.setUserNo(userNoListForRemove);
        remove=true;
    }
}
