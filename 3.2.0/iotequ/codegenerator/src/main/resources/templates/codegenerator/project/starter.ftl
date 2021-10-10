package ${gp.groupId};
<#macro scan><#if gp.groupId=='top.iotequ'>"top.iotequ"<#else>"top.iotequ","${gp.groupId}"</#if></#macro>
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import top.iotequ.util.Util;

@MapperScan({<@scan />})
@ServletComponentScan(basePackages = {<@scan />})
@SpringBootApplication(scanBasePackages= {<@scan /><#if gp.modules?? && (','+gp.modules)?index_of(',reader') gte 0>,"com.svein.*"</#if>})
@EnableAuthorizationServer
@EnableResourceServer
public class ${camelName?cap_first}Application {
	public static void main(String[] args) {	
		Util.commonApplicationRun(${camelName?cap_first}Application.class,"application.yml",null,args);
	}
}
