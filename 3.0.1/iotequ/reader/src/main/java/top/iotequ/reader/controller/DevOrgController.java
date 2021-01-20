package top.iotequ.reader.controller;
import top.iotequ.reader.pojo.DevOrg;

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
import top.iotequ.util.*;
import top.iotequ.util.RestJson;

import java.util.*;

/**************************************************
    Create by iotequ codegenerator 3.0.0
    Author : Qinyoyo
"**************************************************/
@SuppressWarnings("unused")
@RestController
@RequestMapping("/reader/devOrg")
public class DevOrgController  {
	private static final Logger log = LoggerFactory.getLogger(DevOrgController.class);
	@Autowired
	ICgService<DevOrg> cgService;
	@RequestMapping(value = "/list",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> listData(Boolean queryFlowProcess, Boolean needLoadDictionary,String resortFirstField, Integer pageSize,Integer pageNumber,String sort,String order, String search,HttpServletRequest request, HttpServletResponse response) {
		try {
			if (Objects.nonNull(queryFlowProcess) && queryFlowProcess) return cgService.getFlowProcessData(request.getParameter("flowId")).toResponse();
			else return cgService.getListPageData(needLoadDictionary,resortFirstField, pageSize, pageNumber, sort, order, search, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}

	@RequestMapping(value = "/action/download")
	public ResponseEntity<Map<String, Object>> actionDownload(String id,HttpServletRequest request, HttpServletResponse response){
		try {
		  return cgService.doAction("download",id,request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/action/deleteSpecifyUser")
	public ResponseEntity<Map<String, Object>> actionDeleteSpecifyUser(String id,HttpServletRequest request, HttpServletResponse response){
		try {
		  return cgService.doAction("deleteSpecifyUser",id,request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/action/get_data")
	public ResponseEntity<Map<String, Object>> actionGet_data(String id,HttpServletRequest request, HttpServletResponse response){
		try {
		  return cgService.doAction("get_data",id,request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
}