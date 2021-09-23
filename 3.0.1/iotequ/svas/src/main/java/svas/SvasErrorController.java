package svas;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.WebResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SvasErrorController implements ErrorController {

    private final static String ERROR_PATH = "/error";

    @ResponseBody
    @RequestMapping(path  = ERROR_PATH )
    public Map<String,Object> error(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        map.put("success",false);
        map.put("error",404);
        map.put("message","Invalid url");
        return map;
    }
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}