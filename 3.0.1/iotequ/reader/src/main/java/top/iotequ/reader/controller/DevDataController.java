package top.iotequ.reader.controller;
import top.iotequ.reader.pojo.DevData;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.iotequ.framework.service.ICgService;
import top.iotequ.framework.util.*;
import java.util.*;

/**************************************************
    Create by iotequ codegenerator 3.0.0
    Author : Qinyoyo
"**************************************************/
@SuppressWarnings("unused")
@RestController
@RequestMapping("/reader/devData")
public class DevDataController  {
	private static final Logger log = LoggerFactory.getLogger(DevDataController.class);
	@Autowired
	ICgService<DevData> cgService;
	@RequestMapping(value = "/default",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> defaultValue(Boolean needLoadDictionary, String requestDynaFields, DevData devData,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(cgService.getDictionary(devData,true, requestDynaFields)).toResponse();
			else {
				devData = cgService.getDefaultObject(devData);
                return new RestJson().data(devData)
                    .dictionary(Objects.nonNull(needLoadDictionary) && needLoadDictionary ? cgService.getDictionary(devData,true,null) : null).toResponse();
			}
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, Boolean loadDictionaryOnly, String requestDynaFields, String id, DevData devData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			RestJson j = new RestJson();
			if (!Util.isEmpty(requestDynaFields) || (loadDictionaryOnly!=null && loadDictionaryOnly)) {
				return j.dictionary(cgService.getDictionary(devData,true, requestDynaFields)).toResponse();
			}
			if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(cgService.getDictionary(devData,true,null));
			if (!Util.isEmpty(id)) devData=cgService.getRecord(id,request);
			j.data(devData);
			return j.toResponse();
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/download",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> download(String id,String field,String fileName,HttpServletResponse response) throws Exception {
		try {
			cgService.download(field,id,fileName,response);
			return null;
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/save",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> save(String flowCode, Integer total_filepart, DevData devData, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean isNew = (Util.isEmpty(idSaved) || Util.isEmpty(devData.getId()));
			return 	cgService.doSave(isNew, flowCode,total_filepart, devData, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/updateSelective",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> updateSelective(@RequestBody List<Map<String,Object>> devDataList, HttpServletRequest request, HttpServletResponse response) {
		try {
			return cgService.updateSelective(devDataList).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}

}