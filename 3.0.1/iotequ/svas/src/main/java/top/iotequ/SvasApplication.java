package top.iotequ;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@ServletComponentScan(basePackages = {"top.iotequ","svas"})
@SpringBootApplication(scanBasePackages= {"top.iotequ"})
public class SvasApplication {
    private static String additionalPropertyFile(String fileName) {
        String path = ".";
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException e) {
            path=".";
        }
        System.out.println("Current folder : "+path);
        PropertiesPropertySourceLoader propLoader = new PropertiesPropertySourceLoader();
        YamlPropertySourceLoader yamlLoader = new YamlPropertySourceLoader();
        String ext = null;
        File file = null;
        int dp = fileName.lastIndexOf(".");
        if (dp > 0) {
            ext = fileName.substring(dp + 1).toLowerCase();
            file = new File(path, fileName);
            if (file != null && file.exists()) {
                if (Arrays.asList(yamlLoader.getFileExtensions()).contains(ext)) {
                    return file.getAbsolutePath();
                } else {
                    if (Arrays.asList(propLoader.getFileExtensions()).contains(ext)) {
                        return file.getAbsolutePath();
                    }
                }
            }
        } else {
            for (String e : propLoader.getFileExtensions()) {
                file = new File(path, fileName + "." + e);
                if (file.exists()) {
                    return file.getAbsolutePath();
                }
            }
            if (file == null || !file.exists()) {
                for (String e : yamlLoader.getFileExtensions()) {
                    file = new File(path, fileName + "." + e);
                    if (file.exists()) {
                        return file.getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }
    public static void commonApplicationRun( Class<?> clazz, String applicationProperties, String customerProperties, String[] args) {
        SpringApplicationBuilder appBuilder = new SpringApplicationBuilder(clazz);
        appBuilder.properties("file.encoding=UTF-8");
        String myPropertyFile = additionalPropertyFile(customerProperties == null ? "iotequ" : customerProperties);
        if (applicationProperties != null) {
            String location = "spring.config.location=classpath:/" + applicationProperties +
                    (myPropertyFile == null ? "" : "," + myPropertyFile);
            appBuilder.properties(location);
        } else {
            if (myPropertyFile != null) {
                System.out.println("Setting file : "+myPropertyFile);
                appBuilder.properties("spring.config.additional-location=" + myPropertyFile);
            }
        }
        appBuilder.run(args);
    }
    public static void main(String[] args) {
        commonApplicationRun(SvasApplication.class,"application.yml",null,args);
    }
}