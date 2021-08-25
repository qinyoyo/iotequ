package top.iotequ.c20.controller;

import com.google.gson.Gson;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.iotequ.c20.controller.C20HttpServer.*;
import top.iotequ.c20.controller.C20HttpServer.UserBaseInfoRsp.C20Finger;
import top.iotequ.framework.event.DeviceEvent;
import top.iotequ.framework.event.PeopleInfoChangedEvent;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.reader.dao.DevNewDeviceDao;
import top.iotequ.reader.pojo.DevNewDevice;
import top.iotequ.util.*;
import top.iotequ.reader.dao.DevPeopleDao;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.pojo.DevReader;
import top.iotequ.reader.pojo.DevReaderGroup;
import top.iotequ.svasclient.SvasService;
import top.iotequ.svasclient.SvasTypes.SvasMatchInfo;
import top.iotequ.svasclient.SvasTypes.SvasMatched;
import top.iotequ.svasclient.SvasTypes.SvasTemplates;
import top.iotequ.svasclient.TemplatesChangedEvent;
import top.iotequ.reader.service.impl.DevPeopleService;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class RegisterUserController {

	private static final Logger C20log = LoggerFactory.getLogger(RegisterUserController.class);

	@Autowired
	private SvasService svasService;
	@Autowired
	private DevPeopleDao devPeopleDao;
	@Autowired
	private DevNewDeviceDao devNewDeviceDao;
	@Autowired
	private DevPeopleService devPeopleService;
	@Autowired
	ApplicationContext applicationContext;
	// 注册设备
	@RequestMapping(value = Links.C20DeviceBIND, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject BindingDevice(HttpServletRequest request, @RequestBody C20HttpServer.C20BindInfo bindInfo) {
		C20log.error("upload C20  readerNo:"+bindInfo.devNo+",snNo:"+bindInfo.SN);
		JSONObject js=new JSONObject();
		Boolean status=true;
		String sql = "select * from dev_reader where reader_no=?";// 查询编号是否存在
		if (!SqlUtil.sqlExist(sql, bindInfo.devNo)) {
			try {
				DevNewDevice device = devNewDeviceDao.select(bindInfo.devNo);
				if (device!=null) devNewDeviceDao.delete(bindInfo.devNo);
				device = new DevNewDevice();
				device.setType("C20");
				device.setIp(Util.getIpAddr(request));
				device.setReaderNo(bindInfo.devNo);
				device.setSnNo(bindInfo.SN);
				devNewDeviceDao.insert(device);
			} catch (Exception e) {
				// TODO: handle exception
				C20log.error(e.getMessage());
				status=false;
			}
		} else {
			try {
				SqlUtil.sqlExecute("update dev_reader set is_online = 1 where reader_no=?",bindInfo.devNo);
			} catch (Exception e) {
				// TODO: handle exception
				C20log.error(e.getMessage());
				status=false;
			}
		}
		js.put("status", status);
		return js;
	}


	private File photoFile(String userNo) {
		File uploadFile = FileUtil.uploadFileDir("headPortrait");
		return new File(uploadFile, "photo_"+ userNo + "_01.jpg");
	}
	// 注册指静脉用户信息
	@RequestMapping(value = Links.REGISTER_USER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject RegistUser(@RequestBody C20HttpServer.UserBaseInfo regUser) throws Exception {
		C20log.error("REGISTER_USER:" + new Gson().toJson(regUser));
		UserBaseInfoRsp rsp = new UserBaseInfoRsp();
			rsp.retCode = false;
			regUser.setUserNo(svasService.getUserNo(Integer.valueOf(regUser.getIdType()), regUser.getIdCertNumber(),
					regUser.getIdName(), null, null));
			PeopleInfoChangedEvent.People people = new PeopleInfoChangedEvent.People();
			people.setUserNo(regUser.getUserNo());
			people.setBirthDate(DateUtil.string2Date(regUser.getIdBirth()));
			people.setSex(regUser.getIdSex());
			people.setIdNation(regUser.getIdNation());
			people.setHomeAddr(regUser.getIdCertAddress());
			String photo=regUser.getPhoto();
			if (!Util.isEmpty(photo)) {
				File outf = photoFile(regUser.getUserNo());
				if (!outf.getParentFile().exists()) outf.getParentFile().mkdirs();
				else if (outf.exists()) outf.delete();
				byte test2[] = DatatypeConverter.parseHexBinary(photo);
				OutputStream out = new FileOutputStream(outf);
				out.write(test2);
				out.flush();
				out.close();
			}
			svasService.changeUserInfo(people);

			List<C20Finger> list =new ArrayList<>();
			List<SvasTemplates> listT=svasService.getFingerInfo(regUser.getUserNo());
			if(!Util.isEmpty(listT)&&listT.size()!=0) {
				for(SvasTemplates a: listT) {
					C20Finger c=new C20Finger();
					c.fignerIndex=(byte) a.fingerNo;
					c.fingerType=(byte) a.fingerType;
					c.templates=a.templates;
					c.warningfinger=(byte) (a.warning?1:0);
					list.add(c);
				}
			}
			rsp.c20fingers=list;
			rsp.retCode = true;
			rsp.RegReq = regUser;
		return JSONObject.fromObject(new Gson().toJson(rsp));
	}

	// 上传用户信息
	@RequestMapping(value = Links.USER_FINGER_INFO, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject GetUserFinger(@RequestBody UserBaseInfoRsp rsp) throws Exception {
		C20RetMsg crm=new C20RetMsg();
		if(SqlUtil.sqlExist("select * from dev_reader where reader_no=?", rsp.c20info.devNo)) {
			crm.status=true;
			crm.msg="上传成功";
			C20log.error("Upload User and Fingers:" + new Gson().toJson(rsp));
			// 直接注册用户 || 检查用户是否存在
			String userNo = rsp.RegReq.getUserNo();// 用户编号
			List<C20Finger> Fingers = rsp.c20fingers;

			int orgCode = getOrgCode(rsp.c20info, rsp.RegReq.getUserNo());
			
			if (SqlUtil.sqlExist("select * from dev_people where user_no=?", userNo)) {

				SqlUtil.sqlExecute(
						"update dev_people set id_type=1,id_number=?,register_type=1,sex=?,"
								+ "real_name=?,birth_date=?,home_addr=?,id_nation=?,org_code=? where user_no=?",
						rsp.RegReq.getIdCertNumber(), rsp.RegReq.getIdSex().equals("男") ? "1" : "0", rsp.RegReq.getIdName(),
						rsp.RegReq.getIdBirth(), rsp.RegReq.getIdCertAddress(), rsp.RegReq.getIdNation(), orgCode,
						userNo);
			} else {
				
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 获取String类型的时间
				String createdate = sdf.format(date);
				SqlUtil.sqlExecute("insert into dev_people  (user_no,id_type,id_number,register_type,sex,"
						+ "real_name,birth_date,home_addr,id_nation,org_code,reg_time) values (?,1,?,4,?,?,?,?,?,?,?)",
						userNo, rsp.RegReq.getIdCertNumber(), rsp.RegReq.getIdSex().equals("男") ? "1" : "0", rsp.RegReq.getIdName(),
						rsp.RegReq.getIdBirth(), rsp.RegReq.getIdCertAddress(), rsp.RegReq.getIdNation(), orgCode, createdate);
			}

			DevPeople p=devPeopleDao.select(userNo);
			Util.getApplicationContext().publishEvent(PeopleInfoChangedEvent.createPeopleInfoChangedEvent(devPeopleService,null,p,"userNo"));

			// 手指信息更新
			Integer type1=null,type2=null;
			Boolean warning1=null,warning2=null;
			String temp1=null,temp2=null;
			for (int i = 0; i < Fingers.size(); i++) {
				C20Finger finger = Fingers.get(i);// 手指信息
				if (finger.fignerIndex == 1) {
					type1 = (int) finger.fingerType;
					warning1 = finger.warningfinger == 0 ? false : true;
					temp1 = finger.templates;
				} else {
					type2 = (int) finger.fingerType;
					warning2 = finger.warningfinger == 0 ? false : true;
					temp2 = finger.templates;
				}
			}
			try {
				if (!Util.isEmpty(temp1) || !Util.isEmpty(temp2)) {   //已存在的更新手指信息模板信息
					svasService.setTemplates(userNo, type1,warning1,temp1,type2,warning2,temp2);
					int rs=svasService.getFingerCount(userNo);
					SqlUtil.sqlExecute("update dev_people set reg_fingers=? where user_no=?", rs,userNo);
				}
			} catch (Exception e) {
				crm.status=false;
				crm.msg=e.getMessage();
				e.printStackTrace();
			}
		}else {
			crm.status=false;
			crm.msg="设备未注册";
		}
		return JSONObject.fromObject(new Gson().toJson(crm));
	}

	// 删除指静脉数据
	@RequestMapping(value = Links.DELETE_FINGER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject delFinger(@RequestBody JSONObject body) throws Exception { // 日志
		JSONObject js=new JSONObject();
		Boolean status=false;
		String msg = "删除失败";
			C20log.error("delMsg=" + body.toString());
			String userNo = body.getString("userNo");
			int fingerType = body.getInt("fingerType");
			List<SvasTemplates> listT=svasService.getFingerInfo(userNo);
			if(!Util.isEmpty(listT)&&listT.size()!=0) {
				for(SvasTemplates a: listT) {
					if(a.fingerType==fingerType) {
						try {
							svasService.removeTemplate(userNo, a.fingerNo);
							Integer fingerNumber= svasService.getFingerCount(userNo);
							SqlUtil.sqlExecute("update dev_people set reg_fingers=? where user_no=?", fingerNumber,userNo);
							Util.getApplicationContext().publishEvent(new TemplatesChangedEvent(this,userNo,"remove",fingerNumber));
							status=true;
							msg="删除成功";
						} catch (Exception e) {
							
						}
					}
				}
			}
		
		js.put("status", status);
		js.put("msg", msg);
		return js;
	}

	// 上传打卡日志
	@RequestMapping(value = Links.C20_Log, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject UploadLog(@RequestBody C20LOG log) throws Exception { // 日志
		JSONObject js=new JSONObject();
		Boolean status=false;
		String msg = "上传失败";
		if(SqlUtil.sqlExist("select * from dev_reader where reader_no=?", log.c20info.devNo)) {
			C20log.error("C20 LogInfo" + new Gson().toJson(log));
			int orgCode = getOrgCode(log.c20info, log.userNo);
			Date dt = DateUtil.string2Date(log.date, "yyyy/MM/dd HH:mm:ss");
			try {

				DeviceEvent event = new DeviceEvent(this);
				event.setDeviceType("C20");
				event.setDeviceNo(log.c20info.devNo);
				event.setDeviceMode("AD");
				event.setTime(dt);
				event.setUserNo(log.userNo);
				event.put("orgCode", orgCode);
				event.setWarning(log.warningfinger ==-4);
				event.put("authType", (byte)0);
				event.put("auditeeAuthType", (byte)0);
				applicationContext.publishEvent(event);
				status=true;
				msg="上传成功";
			} catch (Exception e) {
				
			}
		}else {
			msg="设备未注册";
		}
		js.put("status", status);
		js.put("msg", msg);
		return js;
	}

	// 验证手指
	@RequestMapping(value = Links.AUTH_USER_FINGER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public JSONObject AuthUserFinger(@RequestBody UploadAuthUser upload) throws Exception {
		C20log.error("Auth Begin:" + new Gson().toJson(upload));
		C20ServerAuth sevAuth = new C20ServerAuth();
		if(SqlUtil.sqlExist("select * from dev_reader where reader_no=?",upload.devNo )) {
			sevAuth.AuthRetCode = false;
			SvasMatched math_list = svasService.auth(upload.template,0);
			if (math_list != null && math_list.count > 0) {// 找到用户了
				SvasMatchInfo first = math_list.list.get(0);
				DevPeople devPeople =  devPeopleDao.select(first.userNo);
				if(devPeople!=null) {
					sevAuth.userBaseInfo = new UserBaseInfo();
					sevAuth.userBaseInfo.setIdType(StringUtil.toString(devPeople.getIdType()));
					sevAuth.userBaseInfo.setIdSex(devPeople.getSex().equals("1") ? "男" : "女");
					sevAuth.userBaseInfo.setIdCertNumber(devPeople.getIdNumber());
					sevAuth.userBaseInfo.setUserNo(devPeople.getUserNo());
					sevAuth.userBaseInfo.setIdName(devPeople.getRealName());
					sevAuth.userBaseInfo.setIdCertAddress(devPeople.getHomeAddr());
					sevAuth.userBaseInfo.setIdBirth(DateUtil.date2String(devPeople.getBirthDate(),"yyyyMMdd"));
					sevAuth.userBaseInfo.setIdNation(devPeople.getIdNation());

					File photo = photoFile(first.userNo);
					if (photo.exists()) {
						InputStream input = new FileInputStream(photo);
						byte[] buf=new byte[(int)photo.length()];
						input.read(buf);
						sevAuth.userBaseInfo.setPhoto(DatatypeConverter.printHexBinary(buf));
						input.close();
					}
					sevAuth.AuthRetCode = true;
					//sevAuth.capacity =  math_list.dictSize;
				}
			}
		}else {
			sevAuth.AuthRetCode = false;
		}
		String reslut = new Gson().toJson(sevAuth);
		C20log.error("Auth end:" + reslut);
		return JSONObject.fromObject(reslut);
	}

	private int getOrgCode(C20BindInfo c20info, String userNo) throws Exception {

		Integer orgCode = null;
		try {
			Integer readerGroup = SqlUtil.sqlQueryInteger("select reader_group from dev_reader where reader_no=? and sn_no=?",
					c20info.devNo, c20info.SN);// 查询编号是否存在
			if(readerGroup!=null&&readerGroup!=0) {
				orgCode=SqlUtil.sqlQueryInteger("select org_code from dev_reader_group where id=?", readerGroup);
			}
			
		} catch (Exception e) {}
		if (orgCode == null && userNo != null) {
			orgCode = SqlUtil.sqlQueryInteger("select org_code from dev_people where user_no=?", userNo);
			if (orgCode != null) {
				return orgCode.intValue();
			}
		} else {
			if (orgCode != null)
				return orgCode.intValue();
		}
		return 0;
	}
	/*
	@RequestMapping(value = Links.GET_PEOPLE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public  JSONObject  getPeople(@RequestBody GetData data) {
		Boolean status=false;
		String msg = "获取失败";
		UserDataList dl=new UserDataList();
		if(data!=null&&data.c20info!=null) {
			try {
				DevReader dr=SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where reader_no=?", data.c20info.devNo);
				if(dr!=null) {
					DevReaderGroup drg=SqlUtil.sqlQueryFirst(DevReaderGroup.class, "select * from dev_reader_group where id=?", dr.getReaderGroup());
					if(drg!=null) {
						List<DevPeople> list=SqlUtil.sqlQuery(DevPeople.class, false, "select * from dev_people where org_code=? limit ?,?;", drg.getOrgCode(),data.currentPage,data.PageSize);
						List<UserBaseInfo> userList=new ArrayList<>();
						if(list!=null&&list.size()>0) {
							for(DevPeople d:list) {
								UserBaseInfo u=new UserBaseInfo();
								u.setUserNo(d.getUserNo());
								u.setIdType(String.valueOf(d.getIdType()));
								u.setIdSex(d.getSex());
								u.setIdNation(d.getIdNation());
								u.setIdName(d.getRealName());
								u.setIdCertNumber(d.getIdNumber());
								u.setIdCertAddress(d.getHomeAddr());
								u.setIdBirth(d.getBirthDate()!=null?DateUtil.date2String(d.getBirthDate(), "yyyy-MM-dd"):null);
								u.setExpireStart(d.getValidDate()!=null?DateUtil.date2String(d.getValidDate(), "yyyy-MM-dd HH:mm:ss"):null);
								u.setExpireEnd(d.getExpiredDate()!=null?DateUtil.date2String(d.getExpiredDate(), "yyyy-MM-dd HH:mm:ss"):null);
								if(d.getPhoto()!=null&&!d.getPhoto().equals("")) {
									File file = photoFile(d.getUserNo());
									InputStream in = null;
							        byte[] data1 = null;
									if (file.getParentFile().exists()) {
										in = new FileInputStream(file);
										BASE64Encoder encoder = new BASE64Encoder();
										data1 = new byte[in.available()];
										in.read(data1);
							            in.close();
							            u.setPhoto(encoder.encode(data1));
									}
								}
								userList.add(u);
							}
						}
						status=true;
						msg = "获取成功";
						Integer col=SqlUtil.sqlQueryInteger("select count(*) from dev_people where ", drg.getOrgCode());
						dl.col=col;
						dl.list=userList;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dl.status=status;
		dl.msg=msg;
		String reslut = new Gson().toJson(dl);
		return JSONObject.fromObject(reslut);
	}*/
	
	
/*	@RequestMapping(value = Links.GET_AD_DATA, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public  JSONObject  getAdDate(@RequestBody GetData data) {//获取考勤数据
		Boolean status=false;
		String msg = "获取失败";
		UserDataList dl=new UserDataList();
		if(data!=null&&data.c20info!=null) {
			try {
				DevReader dr=SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where reader_no=?", data.c20info.devNo);
				if(dr!=null) {
					DevReaderGroup drg=SqlUtil.sqlQueryFirst(DevReaderGroup.class, "select * from dev_reader_group where id=?", dr.getReaderGroup());
					if(drg!=null) {
						List<DevPeople> list=SqlUtil.sqlQuery(DevPeople.class, false, "select * from dev_people where org_code=? limit ?,?;", drg.getOrgCode(),data.currentPage,data.PageSize);
						List<UserBaseInfo> userList=new ArrayList<>();
						if(list!=null&&list.size()>0) {
							for(DevPeople d:list) {
								UserBaseInfo u=new UserBaseInfo();
								u.setUserNo(d.getUserNo());
								u.setIdType(String.valueOf(d.getIdType()));
								u.setIdSex(d.getSex());
								u.setIdNation(d.getIdNation());
								u.setIdName(d.getRealName());
								u.setIdCertNumber(d.getIdNumber());
								u.setIdCertAddress(d.getHomeAddr());
								u.setIdBirth(d.getBirthDate()!=null?DateUtil.date2String(d.getBirthDate(), "yyyy-MM-dd"):null);
								u.setExpireStart(d.getValidDate()!=null?DateUtil.date2String(d.getValidDate(), "yyyy-MM-dd HH:mm:ss"):null);
								u.setExpireEnd(d.getExpiredDate()!=null?DateUtil.date2String(d.getExpiredDate(), "yyyy-MM-dd HH:mm:ss"):null);
								if(d.getPhoto()!=null&&!d.getPhoto().equals("")) {
									File file = photoFile(d.getUserNo());
									InputStream in = null;
							        byte[] data1 = null;
									if (file.getParentFile().exists()) {
										in = new FileInputStream(file);
										BASE64Encoder encoder = new BASE64Encoder();
										data1 = new byte[in.available()];
										in.read(data1);
							            in.close();
							            u.setPhoto(encoder.encode(data1));
									}
								}
								userList.add(u);
							}
						}
						status=true;
						msg = "获取成功";
						Integer col=SqlUtil.sqlQueryInteger("select count(*) from dev_people where ", drg.getOrgCode());
						dl.col=col;
						dl.list=userList;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dl.status=status;
		dl.msg=msg;
		String reslut = new Gson().toJson(dl);
		return JSONObject.fromObject(reslut);
	}*/
	
	
	

}
