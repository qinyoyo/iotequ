package top.iotequ;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan(basePackages = {"top.iotequ"})
@SpringBootApplication(scanBasePackages= {"top.iotequ"})
public class SvasApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SvasApplication.class).run(args);
    }
}