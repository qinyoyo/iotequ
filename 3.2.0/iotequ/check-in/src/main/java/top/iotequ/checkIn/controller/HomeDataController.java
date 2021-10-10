package top.iotequ.checkIn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.iotequ.checkIn.service.impl.CkRegisterService;
import top.iotequ.util.DateUtil;
import top.iotequ.util.OrgUtil;
import top.iotequ.util.RestJson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/common/home")
public class HomeDataController {
    @Autowired
    private CkRegisterService ckRegisterService;
    @RequestMapping(value = "/data",method = {RequestMethod.GET})
    public ResponseEntity<Map<String, Object>> listData(Boolean queryFlowProcess, Boolean needLoadDictionary, String resortFirstField, Integer pageSize, Integer pageNumber, String sort, String order, String search, HttpServletRequest request, HttpServletResponse response) {
        RestJson j = new RestJson();
        try {
            Date date0 = DateUtil.startOf(DateUtil.dateAdd(new Date(),-6, DateUtil.MONTH),DateUtil.MONTH);
            Date date1 = DateUtil.endOf(new Date(),DateUtil.MONTH);
            List<Map<String, Object>> areaData = ckRegisterService.queryStatData("amountByArea", OrgUtil.ALL_PERMISSION, date0, date1);
            List<Map<String, Object>> ageData = ckRegisterService.queryStatData("amountByAgeMonth", OrgUtil.ALL_PERMISSION, date0, date1);
            List<Map<String, Object>> amountData = ckRegisterService.queryStatData("amountByMonth", OrgUtil.ALL_PERMISSION, date0, date1);
            j.data("areaData",areaData)
                    .data("ageData",ageData)
                    .data("amountData",amountData)
                    .setSuccess(true);

        } catch (Exception e) {
            j.setError(e);
        }
        return j.toResponse();
    }
}
