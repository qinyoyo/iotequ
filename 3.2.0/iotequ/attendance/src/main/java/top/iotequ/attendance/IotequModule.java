package top.iotequ.attendance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@PropertySource({"module.properties"})
@Component(value="top.iotequ.attendance.iotequModule")
public class IotequModule {
    @Value("${module.attendance.name}")
    private String name;

    @Value("${module.attendance.groupId}")
    private String groupId;

    @Value("${module.attendance.version}")
    private String version;

    @Value("${module.attendance.buildTime}")
    private String buildTime;

    public boolean isIotequModule() { return true; }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Date getBuildTime() {
        if (buildTime==null || buildTime.isEmpty()) return new Date();
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
            try {
                return sdf.parse(buildTime);
            } catch (Exception e) {
                return new Date();
            }
        }
    }
}
