package top.iotequ;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import top.iotequ.util.Util;

@MapperScan({"top.iotequ"})
@ServletComponentScan(basePackages = {"top.iotequ"})
@SpringBootApplication(scanBasePackages= {"top.iotequ","com.svein.*"})
@EnableAuthorizationServer
@EnableResourceServer
public class CodegeneratorApplication {
	public static void main(String[] args) {	
		Util.commonApplicationRun(CodegeneratorApplication.class,"application.yml",null,args);
	}
}
