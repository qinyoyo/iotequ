package svas;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class SvasErrorController {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String,Object> errorHandler(HttpServletRequest request, HttpServletResponse response,Exception e){
        Map<String,Object> map = new HashMap<>();
        map.put("success",false);
        if (e instanceof NoHandlerFoundException) {
            map.put("error",404);
            map.put("url", ((NoHandlerFoundException) e).getRequestURL());
            map.put("message",e.getMessage());
        } else {
            map.put("error", 500);
            map.put("message", e.getMessage());
        }
        return map;
    }
}