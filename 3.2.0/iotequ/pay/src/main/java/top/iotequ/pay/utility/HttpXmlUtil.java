package top.iotequ.pay.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import top.iotequ.pay.controller.PayCorporationController;
import top.iotequ.pay.dto.DeviceChecker;
import top.iotequ.pay.dto.ParameterChecker;
import top.iotequ.pay.dto.request.BaseRequest;
import top.iotequ.pay.dto.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class HttpXmlUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpXmlUtil.class);
    public static <T extends BaseRequest> T getPayRequestDtoFrom(
            @NonNull HttpServletRequest request,
            DeviceChecker checker,
            ParameterChecker parameterChecker,
            String encryptKey,
            @NonNull Class<T> clazz
    ) throws Exception {
        return XmlUtil.getPayRequestDtoFrom(getBodyString(request), checker, parameterChecker,encryptKey, clazz);
    }
    public static <T extends BaseResponse> T getPayResponseDtoFrom(
            @NonNull HttpServletRequest request,
            DeviceChecker checker,
            ParameterChecker parameterChecker,
            String encryptKey,
            @NonNull Class<T> clazz
    ) throws Exception {
        return XmlUtil.getPayResponseDtoFrom(getBodyString(request), checker, parameterChecker,encryptKey, clazz);
    }
    private static String getBodyString(@NonNull HttpServletRequest request)
            throws IOException {
        BufferedReader br = request.getReader();
        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str).append("\n");
        }
        br.close();
        String ret=sb.toString();
        log.debug("body string:\n{}",ret);
        return ret;
    }

}
