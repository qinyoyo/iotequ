package top.iotequ.ewallet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
@PropertySource("ewallet.properties")
@Component(value="top.iotequ.ewallet.iotequModule")
public class IotequModule {
    @Value("${module.ewallet.name}")
    private String name;
    @Value("${module.ewallet.groupId}")
    private String groupId;
    @Value("${module.ewallet.version}")
    private String version;
    @Value("${module.ewallet.buildTime}")
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
