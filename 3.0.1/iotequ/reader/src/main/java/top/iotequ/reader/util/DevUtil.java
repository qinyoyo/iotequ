package top.iotequ.reader.util;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyna.bean.D10ClientBean.D10ClinetUserInfo;
import com.dyna.bean.D10ClientBean.DeviceAddParamter;
import com.dyna.bean.D10ClientBean.DeviceParam;
import com.dyna.bean.D10ClientBean.DeviceTime;
import com.dyna.bean.D10ClientBean.SpecifyUser;
import com.dyna.bean.D10ClientBean.UserInfo;
import com.dyna.bean.D10ClientBean.DeviceParam.DevParam;
import com.dyna.bean.D10ClientBean.UserInfo.FingerInfo;
import com.dyna.bean.D10ServerBean.SetDeviceAddParam;
import com.dyna.bean.D10ServerBean.UploadUserNoAck;
import com.dyna.codec.ResponseCode;

import top.iotequ.framework.event.PeopleInfoChangedEvent;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.util.*;
import top.iotequ.reader.pojo.DevAuthConfig;
import top.iotequ.reader.service.impl.DevPeopleService;
import top.iotequ.svasclient.SvasTypes.*;
import top.iotequ.svasclient.SvasService;
import top.iotequ.reader.dao.DevPeopleDao;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.pojo.DevReader;
import top.iotequ.reader.pojo.DevReaderGroup;
import top.iotequ.reader.service.impl.D10ClientService;

public class DevUtil {
	static final boolean DEBUG=false;
	private static final Logger log = LoggerFactory.getLogger(DevUtil.class);

	static public boolean isReaderOnline(DevReader reader) {
		if (reader!=null) { 
			try {
				Date dt=queryTime(reader);
				reader.setIsOnline(dt!=null);
			} catch(Exception e) {
				reader.setIsOnline(false);
			}
			return reader.getIsOnline();
		} else return false;
	}
// 上传D10用户到系统
 
	static public String getData(String readerGroupId,boolean includeSubGroup,String readerIds,boolean uploadAllUser,boolean includeNewUser,Integer orgCode,String emList,boolean includeSubOrg) {
		int total=0;
		String [] rr=null;
		if (!Util.isEmpty(readerGroupId)) {
			List<String> rl=getReaderIdList(readerGroupId,includeSubGroup);
			if (rl!=null && rl.size()>0) {
				rr = new String[rl.size()];
				rl.toArray(rr);
			}
		} else if (!Util.isEmpty(readerIds)) {
			rr=readerIds.split(",");
		}
		if (rr==null || rr.length==0) return "没有数据上传";
		StringBuilder sb=new StringBuilder();
		for (int i=0;i<rr.length;i++) {
			DevReader reader=null;
			try {
				reader = SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where id=?", rr[i]);
			} catch (Exception e1) {	}
			int pro0=i*100/rr.length ,pro1=(i+1)*100/rr.length;
			if (reader!=null)  {
				sb.append(reader.getName()).append(":\n");
				Util.setProgress(pro0);
				try {
					List<UserInfo>  userInfo=uploadUsers(reader);
					Util.setProgress((pro1+pro0)/2);
					total+= setUserInfos(userInfo,(pro1+pro0)/2,pro1,uploadAllUser, includeNewUser, orgCode, emList, includeSubOrg,sb);
				} catch (Exception e) {
					sb.append(e.getMessage()).append("\n");
				}
			}
		}	
		sb.insert(0,String.format("合计扫描人数:%d\n",total));
		return sb.toString();
	}
	
	static List<String> getReaderIdList(String groupId,boolean includeSubGroup) {
		List<String> list=new ArrayList<String>();
		if (Util.isEmpty(groupId)) return list;
		int gid=0;
		try {
			gid=Integer.parseInt(groupId);
		} catch (Exception e) {
			return list;
		}
		List<Map<String, Object>> rr=null;
		try {
			rr = SqlUtil.sqlQuery("select id from dev_reader where reader_group=?", gid);
		} catch (Exception e) {}
		if (rr!=null && rr.size()>0) {
			for (int i=0;i<rr.size();i++) {
				Map<String,Object> r = rr.get(i);
				list.add(r.get("id").toString());
			}
		}
		if (includeSubGroup) {
			List<Map<String, Object>> gg=null;
			try {
				gg = SqlUtil.sqlQuery("select id from dev_reader_group where parent=?", gid);
			} catch (Exception e) {}
			if (gg!=null && gg.size()>0) {
				for (int i=0;i<gg.size();i++) {
					Map<String,Object> g = gg.get(i);
					List<String> l=getReaderIdList(g.get("id").toString(),includeSubGroup);
					if (l!=null && l.size()>0) list.addAll(l);
				}
			}
		}
		return list;
	}
	static public String removeFromDevice(String readerGroupId,boolean includeSubGroup,String readerIds,int orgId,String employeeIds,boolean includesubOrg,int pro0,int pro1) {
		List<String> list=getReaderIdList(readerGroupId,includeSubGroup);
		StringBuilder sb=new StringBuilder();
		String msg=null;
		if (!Util.isEmpty(readerIds)) {
			String [] rr=readerIds.split(",");
			for (String r:rr) if (!r.trim().isEmpty()) list.add(r.trim());
		}
		if (list!=null &&list.size()>0 ) {
			for (int i=0;i<list.size();i++) {
				Util.setProgress(pro0+(i+1)*(pro1-pro0)/list.size());
				DevReader reader=null;
				try {
					reader = SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where id=?", list.get(i));
				} catch (Exception e1) {}
				if (reader!=null) {
					try {
						msg=downloadOrRemove2Device(false,reader,orgId,employeeIds,includesubOrg);
					} catch (Exception e) {
						msg=e.getMessage();
					}
					sb.append(reader.getName()).append(":").append(msg).append("\n");
				}
			}
		}
		else  {
			Util.setProgress(pro1);		
		}		
		return sb.toString();
	}
	/**
	 * 向单个设备写入或擦除用户信息
	 * @param isDownload        下发或擦除
	 * @param reader			设备
	 * @param orgId				部门编号，<=0不发送部门
	 * @param employeeIds		用户id序列，逗号分隔
	 * @param includeSubOrg	是否下发子部门
	 * @return 消息
	 * @throws IotequException 异常
	 */
	static private String downloadOrRemove2Device(boolean isDownload,DevReader reader,int orgId,String employeeIds,boolean includeSubOrg) throws IotequException {
		if (reader==null) return "null";
		List<DevPeople> list=new ArrayList<DevPeople>();
		if (orgId>0) {
			if (includeSubOrg) {
				String ccs= OrgUtil.getOrgAndChildrenOrgList(orgId);
				String sql="select * from dev_people"+(ccs==null?"":"  where org_code in ("+ccs+")");
				List<DevPeople> l=SqlUtil.sqlQuery(DevPeople.class, false,sql);
				if (l!=null && l.size()>0) list.addAll(l);					
			} else {
				String sql="select * from dev_people where org_code = ? ";
				List<DevPeople> l=SqlUtil.sqlQuery(DevPeople.class, sql,orgId);
				if (l!=null && l.size()>0) list.addAll(l);
			}
		}
		if (!Util.isEmpty(employeeIds)) {
			String fis=SqlUtil.findInSet("user_no", false, true, employeeIds, true);
			String sql="select * from dev_people where "+fis;
			List<DevPeople> l=SqlUtil.sqlQuery(DevPeople.class, sql);
			if (l!=null && l.size()>0) list.addAll(l);
		}
		int count=0;
		if (list!=null && list.size()>0) {
			count=list.size();
			if (isDownload) {
				downloadUsers(reader,list);
				return String.format("下发 %d 人",count);
			}
			else {
				deleteSpecifyUser(reader,list);
				return String.format("删除 %d 人",count);
			}
		}
		return "没有人员匹配";
	}
	//按组或批量下发用户

	static public String write2Device(String readerGroupId,boolean includeSubGroup,String readerIds,int orgId,String employeeIds,boolean includeSubOrg,int pro0,int pro1) {
		StringBuilder sb=new StringBuilder();
		String msg=null;
		if (!Util.isEmpty(readerGroupId)) {
				Integer id=Integer.parseInt(readerGroupId);
				List<Map<String, Object>> rr=null;
				try {
					rr = SqlUtil.sqlQuery("select id from dev_reader where reader_group=?", id);
				} catch (Exception e1) {}
				List<Map<String, Object>> list=null;
				try {
					list = includeSubGroup ?SqlUtil.sqlQuery("select id from dev_reader_group where parent=?", id) : null;
				} catch (Exception e1) {}
				int step=(pro1-pro0) / ((rr==null?0:rr.size()) +( list==null?0:list.size()));
				if (rr!=null && rr.size()>0) {
					for (int i=0;i<rr.size();i++) {
						Map<String,Object> r =rr.get(i);
						Util.setProgress(pro0+(i+1)*step);
						DevReader reader=null;
						try {
							reader = SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where id=?", r.get("id").toString());
						} catch (Exception e1) {}
						if (reader!=null) {
							try {
								msg=downloadOrRemove2Device(true,reader,orgId,employeeIds,includeSubOrg);
							} catch(Exception e) {
								msg=e.getMessage();
							}
							sb.append(reader.getName()).append(":").append(msg).append("\n");
						}
					}
				}
				if (includeSubGroup && list!=null && list.size()>0) {
						for (Map<String,Object> m : list) {
							msg=write2Device(m.get("id").toString(),true,null,orgId,employeeIds,includeSubOrg,pro0+(rr==null?0:step*rr.size()),pro1);
							sb.append(msg);
					}
				}
		}
		else if (!Util.isEmpty(readerIds)) {
			String [] rr=readerIds.split(",");
			for (int i=0;i<rr.length;i++) {
				Util.setProgress(pro0+(i+1)*(pro1-pro0)/rr.length);
				DevReader reader=null;
				try {
					reader = SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where id=?", rr[i]);
				} catch (Exception e1) {}
				if (reader!=null) {
					try {
						msg=downloadOrRemove2Device(true,reader,orgId,employeeIds,includeSubOrg);
					} catch (Exception e) {
						msg=e.getMessage();
					}
					sb.append(reader.getName()).append(":").append(msg).append("\n");
				}
			}			
		}
		return sb.toString();
	}
	

	// 查询设备参数
	static public void queryDevice(DevReader reader) throws IotequException {
		String deviceId = reader.getReaderNo() ; // 演示的设备编号
		String deviceModel = reader.getType() ;  // 演示的设备型号,此参数不重要可以随便填 预留使用		
		DeviceParam param = D10ClientService.getD10client().queryDeviceParam(deviceId, deviceModel);
		log.debug("queryDevice({})={}",deviceId,param);
		if (param==null) throw new IotequException(IotequThrowable.PARAMETER_ERROR,D10ClientService.getD10client().getLastMessage());
		reader.setFirmware(param.getHardware());
		reader.setType(param.getDeviceType());
		reader.setIsTimeSync(param.getClocksyn()!=0);
		reader.setIsOnline(true);
		DevParam p=param.getDevParam();
		if (p!=null) {
			reader.setAlignMethod( p.getAlignMethod());
			reader.setBlacklightTime( p.getBlacklightTime());
			reader.setVoiceprompt(p.getVoiceprompt() != 0);
			reader.setMenuTime( p.getMenuTime());
			reader.setWengenform( p.getWengenform());
			reader.setWengenOutput( p.getWengenOutput());
			reader.setWengenOutArea( p.getWengenOutArea());
			reader.setRegfingerOutTime( p.getRegfingerOutTime());
			reader.setAuthfingerOutTime( p.getAuthfingerOutTime());
		}
	}

	// 查询设备时间
	static public Date queryTime(DevReader reader) throws IotequException {
		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		DeviceTime time = D10ClientService.getD10client().queryDeviceTime(deviceId, deviceModel);
		log.debug("queryTime({})={}",deviceId,time);
		if (time == null || time.getDate()==null) {
			throw new IotequException(IotequThrowable.NULL_OBJECT,D10ClientService.getD10client().getLastMessage());
		} else {// success
			return DateUtil.string2Date(time.getDate());
		}
	}

	// 设置设备参数
	static public void setDeviceParam(DevReader reader) throws IotequException {
		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		DevParam param=new DevParam();
		param.setDeviceId(deviceId); 
		param.setDeviceModel(deviceModel);

		/*
		 * 对比方式: 1：指静脉 2：卡 + 指静脉 3：卡 或 指静脉 4：卡 + 指静脉（服务器存储指静脉数据） 5：密码 + 指静脉 6：密码或指静脉
		 * 7：密码或指静脉或卡 8：指静脉或（卡+密码） 9：指静脉（后台比对）
		 */
		param.setAlignMethod((Byte)reader.getAlignMethod());

		/* 背光时间: 秒为单位 */
		param.setBlacklightTime(reader.getBlacklightTime());

		/* 菜单时间: 秒为单位 */
		param.setMenuTime(reader.getMenuTime());

		/* 语音提示： 0-关，1-开 */
		param.setVoiceprompt(reader.getVoiceprompt()?(byte)1:(byte)0);

		/* 韦根格式: 1-26，2-34，3-自定义 */
		param.setWengenform(reader.getWengenform());

		/* 韦根输出: 1-固定2-编号3-卡号4-区+编 */
		param.setWengenOutput(reader.getWengenOutput());

		/* 韦根输出区位码 26 */
		param.setWengenOutArea(reader.getWengenOutArea());

		/* 指静脉注册超时时长 ：秒为单位 */
		param.setRegfingerOutTime(reader.getRegfingerOutTime());

		/* 指静脉验证超时时长:秒为单位 */
		param.setAuthfingerOutTime(reader.getAuthfingerOutTime());
		boolean isSucc=D10ClientService.getD10client().setDeviceParam(param);
		log.debug("setDeviceParam({})={}",param,isSucc?"true":"false");
		if (!isSucc) {
			throw new IotequException("d10client_error_"+D10ClientService.getD10client().getLastErrCode(),D10ClientService.getD10client().getLastMessage());
		} 
	}

	static public void setDeviceTime(DevReader reader) throws IotequException {
		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		DeviceTime deviceTime = new DeviceTime(DateUtil.date2String(new Date(), "yyyyMMddHHmmss"), reader.getIsTimeSync()?(byte)1:(byte) 0);
		deviceTime.setDeviceId(deviceId);
		deviceTime.setDeviceModel(deviceModel);
		boolean isSucc=D10ClientService.getD10client().setDeviceTime(deviceTime);
		log.debug("setDeviceTime({})={}",deviceTime,isSucc?"true":"false");
		if (!isSucc) {
			throw new IotequException("d10client_error_"+D10ClientService.getD10client().getLastErrCode(),D10ClientService.getD10client().getLastMessage());
		} 
	}

	// 注册设备,D10首次添加数据库里 ,调用的操作.
	static public void RegistDevice(DevReader reader) throws IotequException {
		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		boolean isSucc = D10ClientService.getD10client().RegistDevice(deviceId, deviceModel);
		log.debug("RegistDevice({})={}",deviceId,isSucc?"true":"false");
		if (!isSucc) {
			throw new IotequException("d10client_error_"+D10ClientService.getD10client().getLastErrCode(),D10ClientService.getD10client().getLastMessage());
		} 
	}

	// 取消注册设备
	static public void unRegistDevice(DevReader reader) throws IotequException {
		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		boolean isSucc = D10ClientService.getD10client().unRegistDevice(deviceId, deviceModel);
		log.debug("unRegistDevice({})={}",deviceId,isSucc?"true":"false");
		if (!isSucc) {
			if(D10ClientService.getD10client().getLastErrCode()!=ResponseCode.NOTFINDDEVICE.getCode()) {
				throw new IotequException("d10client_error_"+D10ClientService.getD10client().getLastErrCode(),D10ClientService.getD10client().getLastMessage());
			}
		} 
	}

	// 重启设备
	static public void RestDevice(DevReader reader) throws IotequException {
		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		boolean isSucc = D10ClientService.getD10client().RestDevice(deviceId, deviceModel);
		log.debug("RestDevice({})={}",deviceId,isSucc?"true":"false");
		if (!isSucc) {
			throw new IotequException("d10client_error_"+D10ClientService.getD10client().getLastErrCode(),D10ClientService.getD10client().getLastMessage());
		} 
	}

	// 删除指定用户
	static public void deleteSpecifyUser(DevReader reader,List<DevPeople>list) throws IotequException {
		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		SpecifyUser users = new SpecifyUser();
		users.setDeviceId(deviceId);
		users.setDeviceModel(deviceModel);
		String [] userList = new String[list.size()];
		for (int i=0;i<list.size();i++) userList[i]=list.get(i).getUserNo();
		users.setDelusersNo(userList); // 要删除的设备的用户编号数组
		if (DEBUG) {
			System.out.printf("%s remove %d users\n",deviceId,userList.length);
		} else {
			boolean isSucc = D10ClientService.getD10client().DeleteSpecifyUser(users);
			log.debug("DeleteSpecifyUser({})={}",deviceId,isSucc?"true":"false");
			if (!isSucc) {
				throw new IotequException("d10client_error_"+D10ClientService.getD10client().getLastErrCode(),D10ClientService.getD10client().getLastMessage());
			} 
		}
	}

	// 删除设备上的所有用户
	static public void deleteAllUsers(DevReader reader) throws IotequException {
		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		boolean isSucc = D10ClientService.getD10client().DeleteAllUsers(deviceId, deviceModel);
		log.debug("DeleteAllUsers({})={}",deviceId,isSucc?"true":"false");
		if (!isSucc) {
			throw new IotequException("d10client_error_"+D10ClientService.getD10client().getLastErrCode(),D10ClientService.getD10client().getLastMessage());
		}
	}

	// 指定设备上送用户信息
	static public List<UserInfo> uploadUsers(DevReader reader) throws IotequException {
		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		if (DEBUG) {
			System.out.println("Upload test");
			return null;
		} else {
			List<UserInfo> userInfos = D10ClientService.getD10client().uploadUsers(deviceId, deviceModel);
			log.debug("uploadUsers({})={}",deviceId,userInfos==null?"null":String.valueOf(userInfos.size()));
			if (userInfos == null) {
				throw new IotequException("d10client_error_"+D10ClientService.getD10client().getLastErrCode(),D10ClientService.getD10client().getLastMessage());
			} else {
				return userInfos;
			}
		}
	}
	//从终端上传用户到系统
	static private int setUserInfos(List<UserInfo>  userInfos,int pro0,int pro1,boolean uploadAllUser,boolean includeNewUser,Integer orgCode,String emList,boolean includeSubOrg,StringBuilder sb)  {
		int total=0;
		int success=0;
		int error=0;
		int exists=0;
		int nouserno=0;
		int noname=0;
		int ignore=0;
		if (!Util.isEmpty(emList)) emList=","+emList+",";
		if (userInfos!=null && userInfos.size()>0 ) {
			Integer defOrg=0;
			try {
				defOrg = SqlUtil.sqlQueryInteger("select min(org_code) from sys_org");
			} catch (Exception e1) {	}
			for (int i=0;i<userInfos.size();i++) {
				Util.setProgress(pro0 + (i+1)*(pro1-pro0)/userInfos.size());
				UserInfo u = userInfos.get(i);
				String usernum=u.getUsernum();
				int oid=u.getDepartmenno();
				if (oid>0) {
					if (!OrgUtil.isOrgChildren(oid)) {
						ignore++;;
						continue;   // 只上传本部门的人员 
					}
				}
				if (!Util.isEmpty(usernum)) {
					SvasUserInfo userInfo=null;
					try {
						Object o=EntityUtil.runMethod(Util.getBean(SvasService.class),"getUserInfo", usernum);
						if (o!=null) userInfo=(SvasUserInfo)o;
					} catch(Exception e) {
						sb.append(u.getUsername()).append(" : ").append(e.getMessage()).append("\n");
					}
					if (userInfo==null)  {
						nouserno++;
						sb.append(u.getUsername()).append(" : ").append("未知身份信息").append("\n");
						continue;  // 未知的身份信息，不建立用户
					}
					DevPeople p=null,p0=null;
					try {
						p = SqlUtil.sqlQueryFirst(DevPeople.class, "select * from dev_people where user_no=?", usernum);
						EntityUtil.entityCopy(p0,p);
					} catch (Exception e1) {}
					boolean newUser=false;
					if (p==null) { 
						if (!uploadAllUser && !includeNewUser)  continue;
						newUser=true; 
						p=new DevPeople(); 
					} else if (!uploadAllUser) {
						if (Util.isEmpty(orgCode)) {
							if (Util.isEmpty(emList) || !emList.contains(","+p.getUserNo()+",")) {
								ignore++;
								continue;
							}
						} else if (!EntityUtil.entityEquals(orgCode, p.getOrgCode()))  {
							if (!includeSubOrg) {
								ignore++;
								continue;	
							}
							if (orgCode!=null && !OrgUtil.isOrgChildren(p.getOrgCode(),orgCode)) {
								ignore++;
								continue;	
							}
						} 
					}
					p.setIdType(userInfo.idType);
					p.setIdNumber(userInfo.idNo);
					DevPeopleDao dao=Util.getBean(DevPeopleDao.class);
					p.setUserNo(usernum);
					if (!Util.isEmpty(u.getUsername()))  p.setRealName(u.getUsername());
					else if (newUser) p.setRealName(userInfo.name);
					if (Util.isEmpty(p.getRealName())) {
						noname++;
						sb.append(u.getUsername()).append(" : ").append("没有给定姓名").append("\n");
						continue;
					}
					if (!Util.isEmpty(u.getUsercardno()))  p.setCardNo(u.getUsercardno());

					if (oid>0) {
						p.setOrgCode(oid);
					} else if (newUser) p.setOrgCode(defOrg);
					if (!Util.isEmpty(u.getPhonenum())) p.setMobilePhone(u.getPhonenum());// 手机号码
					if (!Util.isEmpty(u.getEmail())) p.setEmail(u.getEmail());// email地址
					if (!Util.isEmpty(u.getInductiontime())) p.setValidDate(DateUtil.string2Date(u.getInductiontime()));
					if (!Util.isEmpty(u.getLeavedate())) p.setExpiredDate(DateUtil.string2Date(u.getLeavedate()));
					p.setDevPassword(u.getUserpsw());// 设备端密码
					p.setUserType((int)u.getUsertype());
					if (newUser) {
						p.setRegisterType(2);
						p.setRegTime(new Date());// 用户登记时间
					}
					if (!Util.isEmpty(u.getUsermark())) p.setNote(u.getUsermark());// 用户备注
					int fingers=0;
					FingerInfo[] ff=u.getFingers();
					if (ff!=null && ff.length>0) {
						for (FingerInfo f:ff) {
							if (f!=null) {
								Object bean=Util.getBean(SvasService.class);
								try {
//									EntityUtil.runMethod(bean,"removeTemplate", usernum,Integer.valueOf(f.getFignerIndex()));
									EntityUtil.runMethod(bean,"addTemplate", usernum,Integer.valueOf(f.getFignerIndex()),10+Integer.valueOf(f.getFignerIndex()),f.getTemplates(),f.getWarningfinger()==1);
									fingers++;
								} catch(Exception e) {		
									e.printStackTrace();
									error++;
									String msg=e.getMessage();
									if (msg==null && e instanceof InvocationTargetException ) {
										Throwable t = ((InvocationTargetException)e).getTargetException();
										msg=t.getMessage();
									}
									sb.append(u.getUsername()).append(" : ").append(msg).append("\n");
									if (msg!=null && (msg.contains("重复") || msg.contains("exist") )) exists++;
								}
							}
						}
					}
					if (fingers>0) {    //  不同步指静脉数据为空或未验证成功的用户
						p.setRegFingers(fingers);
						if (newUser) {
							dao.insert(p);
							Util.publishEvent(PeopleInfoChangedEvent.createPeopleInfoChangedEvent(Util.getBean(DevPeopleService.class),null,p,"userNo"));
						}
						else {
							dao.update(p);
							Util.publishEvent(PeopleInfoChangedEvent.createPeopleInfoChangedEvent(Util.getBean(DevPeopleService.class),p0,p,"userNo"));
						}
						success++;
					}
					total++;
				}
			}
		}
		if (success!=total || error!=0 || exists!=0 || nouserno!=0 )
			sb.append(String.format("总计:%d  导入:%d  忽略:%d 未知用户号: %d 姓名缺失:%d 重复指静脉信息:%d  指静脉注册失败: %d \n", total,success,ignore,nouserno,noname,exists,error));
		return total;
	}
	
	static public void downloadUsers(DevReader reader,List<DevPeople>list) throws IotequException {

		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		D10ClinetUserInfo d10ClinetUserInfors = new D10ClinetUserInfo();
		d10ClinetUserInfors.setDeviceId(deviceId);
		d10ClinetUserInfors.setDeviceModel(deviceModel);
		d10ClinetUserInfors.setCovered(true);// 是否覆盖已经存在的用户 true--覆盖 false--不覆盖

		List<UserInfo> userInfos = new ArrayList<>();

		for (DevPeople em : list) {
			UserInfo user = new UserInfo();
			user.setUsernum(em.getUserNo());// 用户编号
			user.setUsername(em.getRealName());// 用户姓名
			user.setDepartmenno(em.getOrgCode());// 部门编码
			user.setPhonenum(em.getMobilePhone());// 手机号码
			user.setInductiontime(em.getValidDate()==null?"20180101": DateUtil.date2String(em.getValidDate(),"yyyyMMdd"));// 入职时间
			user.setLeavedate(em.getExpiredDate()==null?"21001231":DateUtil.date2String(em.getExpiredDate(),"yyyyMMdd"));// 离职时间
			user.setUserpsw(em.getDevPassword());// 设备端密码
			int emtype=em.getUserType();
			user.setUsertype((byte) emtype);// 1 -管理员 2-普通用户
			user.setDatasources((byte) 2);// 从服务器同步获得 这里应该固定为2
			user.setUserregtime(DateUtil.date2String(em.getRegTime(),"yyyyMMddHHmmss"));// 用户登记时间
			user.uerpermissions = (byte)4;//
			user.dayStartTime="000000";
			user.dayEndTime="235959";
			if ("D10-DG".equals(reader.getType())) {
				user.setEmail(em.getDutyRank());
				user.setUsermark(em.getOrgCode()==null ? "" : OrgUtil.getOrgFullName(em.getOrgCode()));
				String eno = SqlUtil.sqlQueryString(false,"select employee_no from ad_employee where id=?",em.getUserNo());
				user.setUsercardno(eno);
			} else {
				user.setEmail(em.getEmail());// email地址
				user.setUsermark(em.getNote());// 用户备注
				user.setUsercardno(em.getCardNo());// 用户卡号
			}
			try {
				Object o=EntityUtil.runMethod(Util.getBean(SvasService.class),"getFingerInfo", em.getUserNo());
				if (o!=null) {
					@SuppressWarnings("unchecked")
					List<SvasTemplates> infos=(List<SvasTemplates>)o;
					int fingernum =0 ;
					if (infos!=null && infos.size()>0) {
						 fingernum = infos.size() ;// 手指数量最大为2
						if (fingernum>2) fingernum=2;
						user.setFingernum((byte) fingernum);// 用户手指数量
						FingerInfo[] fingerInfos = new FingerInfo[fingernum];
						for (int i = 0; i < fingernum; i++) { // 手指信息
							FingerInfo finger = new FingerInfo();
							SvasTemplates map=infos.get(i);
							finger.setFignerIndex((byte)(map.fingerNo));
							finger.setWarningfinger(map.warning ? (byte) 1 : (byte)0);// 0：非胁迫手指 1：胁迫手指
							finger.setTemplates(map.templates);
							fingerInfos[i] = finger;
						}
						user.setFingers(fingerInfos);	
					}
				}
			} catch(Exception e) {}
			Map<String,String> params = new HashMap<>();
			user.uerpermissions=getPermission(em,deviceId,params);
			if (params.get("start")!=null) user.dayStartTime=params.get("start");
			if (params.get("end")!=null)   user.dayEndTime=params.get("end");
			if (user.uerpermissions >0) userInfos.add(user);
		}
		if (userInfos.size() > 0) {
			UserInfo[] userArray = new UserInfo[userInfos.size()];
			userInfos.toArray(userArray);
			d10ClinetUserInfors.setUserInfos(userArray);// 数组输入
			if (DEBUG) {
				System.out.printf("Download %d users to %s\n", userInfos.size(), deviceId);
			} else {
				boolean isSucc = D10ClientService.getD10client().DownloadUsers(d10ClinetUserInfors);
				log.debug("DownloadUsers({})={}", deviceId, isSucc ? "true" : "false");
				if (!isSucc) {
					throw new IotequException("d10client_error_" + D10ClientService.getD10client().getLastErrCode(), D10ClientService.getD10client().getLastMessage());
				}
			}
		} else log.debug("No user need download to {}", deviceId);
	}
	
	private static final byte no_permission = 0;
	private static final byte permission = 4;
	private static final byte workday_permission = 14;
	private static final byte no_permission_time= -1;
	private static final byte expired = -2;
	private static final byte no_device_permission = -3;

	/**
	 * 判断是否工作日
	 * @param p  注册人员
	 * @return 是否工作日。如果没有考勤系统，工作日为礼拜1-5.否则根据考勤系统计算
	 */
	static boolean isWorkDayNow(DevPeople p) {

		try {
			Object o=EntityUtil.runMethod(Class.forName("top.iotequ.attendance.util.AdUtil"), "calcEmployeeAttendanceTime", p.getUserNo(),new Date());
			if (o==null)  return false;
			else return true;
		} catch (Exception e) {
			int week = DateUtil.weekOf(new Date());
			return week >= 1 && week <= 5;
		}
	}
	// sql 返回为 dev_auth_role 的 id
	private static List<DevAuthConfig> getAuthConfigBySql(String sql) {
		try {
			List<Map<String,Object>> auths = SqlUtil.sqlQuery(false,sql);
			String aa="";
			if (!Util.isEmpty(auths)) {
				for (Map<String,Object> m : auths) {
					if (m!=null) {
						String a = StringUtil.toString(m.get("auth"));
						if (!Util.isEmpty(a)) aa = aa+(aa.isEmpty()?"":",")+a;
					}
				}
			}
			if (Util.isEmpty(aa)) return null;
			List<DevAuthConfig> list = SqlUtil.sqlQuery(DevAuthConfig.class, false, "SELECT * FROM dev_auth_config where role_id = ? ",aa);
			return list;
		} catch (Exception e) {
			return null;
		}
	}
	// 用户权限 0：无权限 1：需要审核 2：审核权限 4：直接开门
	/* 授权管理策略 ：
	 * 
	*/
	public static byte getPermission(DevPeople p,String devnum, Map<String,String> forDownload) {
		// Step 1 : 判断员工的有效时间
		Date date = DateUtil.startOf(new Date(),DateUtil.DAY);
		Date vd=DateUtil.startOf(p.getValidDate(),DateUtil.DAY);
		if (vd != null && vd.getTime()>date.getTime()) return expired;
		vd=DateUtil.startOf(p.getExpiredDate(),DateUtil.DAY);
		if (vd != null && vd.getTime()<date.getTime()) return expired;
		
		DevReaderGroup devGroup=null;
		try {
			devGroup = SqlUtil.sqlQueryFirst(DevReaderGroup.class, false, "SELECT g.* FROM dev_reader_group g, dev_reader r where r.reader_group = g.id and r.reader_no = ?", devnum);
		} catch (Exception e) {}

		// Step 2 : 设备未分组，直接拒绝
		if (devGroup==null) return no_device_permission;

		String peopleAuthGroups = devGroup.getAuthGroupList();  // 设备组的人员权限分组
		List<DevAuthConfig> authConfigList = new ArrayList<>();
		List<DevAuthConfig> authConfigs = null;
		// Step 3 判断人员是否在people分组里
		if (!Util.isEmpty(peopleAuthGroups)) {
			// 人员直接在设备组包含的人员分组中
			authConfigs = getAuthConfigBySql(SqlUtil.sqlString("select auth from dev_auth_group where id in (select group_id from dev_people_group where group_id = ? and user_no = ?)"
					 ,peopleAuthGroups,p.getUserNo()));
			// 人员部门直接在设备组包含的部门中
			if (!Util.isEmpty(authConfigs)) authConfigList.addAll(authConfigs);
			authConfigs = getAuthConfigBySql(SqlUtil.sqlString("select auth from dev_auth_group where id in (select group_id from dev_org_group where group_id = ? and org_id = ?)"
						,peopleAuthGroups,p.getOrgCode()));
			// 人员部门是设备组包含部门的子部门
			if (!Util.isEmpty(authConfigs)) authConfigList.addAll(authConfigs);
			authConfigs = getAuthConfigBySql(SqlUtil.sqlString("select auth from dev_auth_group where id in (select group_id from dev_org_group where group_id = ? and org_id = ? and is_include_sub_org = 1)"
							,peopleAuthGroups,OrgUtil.getOrgParantList(p.getOrgCode())));
			if (!Util.isEmpty(authConfigs)) authConfigList.addAll(authConfigs);
		}
		try {
			// 设备组归属部门与人员同部门
			if (devGroup.getOrgCode() == p.getOrgCode() && !Util.isEmpty(devGroup.getOrgAuth())) {
				authConfigs = SqlUtil.sqlQuery(DevAuthConfig.class, false, "SELECT * FROM dev_auth_config where role_id = ? ", devGroup.getOrgAuth());
				if (!Util.isEmpty(authConfigs)) authConfigList.addAll(authConfigs);
			}
			// 设备组归属部门子部门包含人员部门
			if (!Util.isEmpty(devGroup.getSubOrgAuth())) {
				Integer [] cc = OrgUtil.getChildrenList(devGroup.getOrgCode());
				if (!Util.isEmpty(cc)) {
					for (Integer c: cc) {
						if (c==p.getOrgCode()) {
							authConfigs = SqlUtil.sqlQuery(DevAuthConfig.class, false, "SELECT * FROM dev_auth_config where role_id = ? ", devGroup.getSubOrgAuth());
							if (!Util.isEmpty(authConfigs)) authConfigList.addAll(authConfigs);
							break;
						}
					}
				}
			}
		} catch (Exception e) {}
		if (Util.isEmpty(authConfigList)) return no_device_permission; // 没有找到配置
		if (forDownload!=null) {
			Integer auth = null;
			for (DevAuthConfig config : authConfigList) {
				if (auth==null || auth<config.getAuth()) {
					auth = config.getAuth();
					forDownload.put("start",config.getStartTime() == null?"000000":DateUtil.date2String(config.getStartTime(), "HHmmss"));
					forDownload.put("end",config.getEndTime() == null?"235959":DateUtil.date2String(config.getEndTime(), "HHmmss"));
				}
			}
			if (auth != null) {
				byte ppp = (byte) (auth & 0xff);
				return ppp;
			} else return no_permission;
		} else {
			// Step 4 判断权限
			Boolean isWorkDay = isWorkDayNow(p);
			Integer auth = null;
			Date now = new Date();
			String dat = DateUtil.date2String(now, "yyyy-MM-dd");
			String time = DateUtil.date2String(now, "HH:mm");
			for (DevAuthConfig config : authConfigList) {
				if (config.getOnlyWorkDay() && !isWorkDay) {
					if (config.getAuth()==permission) auth=(int)workday_permission;
					continue;
				}
				if (config.getBeginAt() != null && dat.compareTo(DateUtil.date2String(config.getBeginAt(), "yyyy-MM-dd")) < 0) {
					if (config.getAuth()==permission) auth=(int)expired;
					continue;
				}
				if (config.getEndAt() != null && dat.compareTo(DateUtil.date2String(config.getEndAt(), "yyyy-MM-dd")) >= 0) {
					if (config.getAuth()==permission) auth=(int)expired;
					continue;
				}
				String startTime = config.getStartTime() == null ? "00:00" : DateUtil.date2String(config.getStartTime(), "HH:mm");
				String endTime = config.getEndTime() == null ? "24:00" : DateUtil.date2String(config.getEndTime(), "HH:mm");
				if (startTime.compareTo(endTime) <= 0) {
					if (time.compareTo(startTime) < 0) {
						if (config.getAuth()==permission) auth=(int)no_permission_time;
						continue;
					}
					if (time.compareTo(endTime) > 0) {
						if (config.getAuth()==permission) auth=(int)no_permission_time;
						continue;
					}
				} else {
					if (time.compareTo(endTime) > 0 && time.compareTo(startTime) < 0) {
						if (config.getAuth()==permission) auth=(int)no_permission_time;
						continue;
					}
				}
				auth = config.getAuth(); // 匹配一个权限即可
				break;
			}
			if (auth != null) {
				byte ppp = (byte) (auth & 0xff);
				if (ppp == workday_permission) {
					if (isWorkDay == null) isWorkDay = isWorkDayNow(p);
					if (isWorkDay) return permission;
					else return no_permission_time;
				} else return ppp;
			} else return no_permission;
		}
	}
	
	//上传d30用户
	static public UploadUserNoAck UploadUserNos(DevReader reader, int pageId, byte pageNum) {
		String deviceId = reader.getReaderNo() ;
		String deviceModel= reader.getType();
		UploadUserNoAck una=D10ClientService.getD10client().UploadUserNos(deviceId, deviceModel, pageId, pageNum);
		return una;
	}

//上传d30额外参数
	static public DeviceAddParamter uploadD30Parameter(DevReader reader) throws IotequException {
		String deviceId = reader.getReaderNo() ;
		String deviceModel= reader.getType();
		DeviceAddParamter da=D10ClientService.getD10client().queryAddParameter(deviceId, deviceModel);
		if(da!=null) {
			reader.setWgOrder(da.getWgOrder());
			reader.setRelayTime(da.getRelayTime());
			reader.setFixedValue(da.getFixedValue());
			reader.setAlarmEnable(da.getAlarmEnable());
			reader.setOpenEnable(da.getOpenEnable());
			reader.setDoorState(da.getDoorState());
			reader.setRelayEnable(da.getRelayEnable());
			reader.setDoorbellEnable(da.getDoorbellEnable());
			reader.setWifiSsid(da.getWifi_ssid());
			reader.setWifiPsw(da.getWifi_psw());
		}else {
			
			throw new IotequException("get_D30_Paramet",D10ClientService.getD10client().getLastMessage());
		}
		return da;
	}
	
	//设置d30额外参数
	static public void setD30Parameter(DevReader r) throws IotequException {
		DeviceAddParamter d=new DeviceAddParamter();
		d.setAlarmEnable(r.getAlarmEnable());
		d.setDeviceId(r.getReaderNo());
		d.setDeviceModel(r.getType());
		d.setDoorbellEnable(r.getDoorbellEnable());
		d.setDoorState(r.getDoorState());
		d.setFixedValue(r.getFixedValue());
		d.setOpenEnable(r.getOpenEnable());
		d.setRelayEnable(r.getRelayEnable());
		d.setRelayTime(r.getRelayTime());
		d.setWgOrder(r.getWgOrder());
		d.setWifi_psw(Util.isEmpty(r.getWifiPsw())?"":r.getWifiPsw());
		d.setWifi_ssid(Util.isEmpty(r.getWifiSsid())?"":r.getWifiSsid());
		SetDeviceAddParam sda=D10ClientService.getD10client().setAddParameter(d);
		if(!sda.isSucc())throw new IotequException("set_D30_Paramet",sda.getInfo());
	}
}
