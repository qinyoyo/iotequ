package top.iotequ.framework.exception;


import com.google.gson.GsonBuilder;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class GlobalErrorController extends BasicErrorController {
    public static ErrorProperties initProperties(){
        ErrorProperties properties = new ErrorProperties();
        properties.setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);
        return properties;
    }
    public GlobalErrorController() {
        super(new GlobalErrorAttributes(), initProperties());
    }
    String jsonResult(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        return new GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create().toJson(body);
    }
    @Override
    public ResponseEntity error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        return new ResponseEntity<String>(jsonResult(request), HttpStatus.OK);
    }
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        response.setContentType("text/json; charset=UTF-8");
        try {
            String json = jsonResult(request);
            response.getWriter().print(json);
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                response.getWriter().close();
            } catch (IOException e) {
            }
        }
        return null;
    }
}