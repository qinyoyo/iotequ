<#assign D = "$" />
package top.iotequ.${camelName};
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
@PropertySource("${camelName}.properties")
@Component(value="top.iotequ.${gp.name}.iotequModule")
public class IotequModule {
    @Value("${D}{module.${gp.name}.name}")
    private String name;
    @Value("${D}{module.${gp.name}.groupId}")
    private String groupId;
    @Value("${D}{module.${gp.name}.version}")
    private String version;
    @Value("${D}{module.${gp.name}.buildTime}")
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
