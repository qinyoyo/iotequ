package top.iotequ.reader.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.reader.Reader;

import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.util.OrgUtil;
import top.iotequ.util.RestJson;
import top.iotequ.util.SqlUtil;
import top.iotequ.util.Util;
import top.iotequ.reader.pojo.DevData;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.pojo.DevPeopleMapping;
import top.iotequ.reader.pojo.DevReader;
import top.iotequ.reader.pojo.DevReaderGroup;
import top.iotequ.reader.util.DevDownloadUtil;
import top.iotequ.reader.util.DevUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Service
public class DevDataService extends CgDevDataService {
    @Override
    public RestJson doSave(boolean isNew, String flowCode, Integer totalFilePart, DevData obj, String idSaved, HttpServletRequest request) throws Exception {
        RestJson j = new RestJson();
        String readerGroupId = ("1".equals(obj.getDeviceSelectionMode()) ? obj.getGroupSelection() : null);
        String readerIds = ("1".equals(obj.getDeviceSelectionMode()) ? null : obj.getReaderSelection());
        boolean includeSubOrg = Util.boolValue(obj.getIncludeSubOrg());
        boolean includeSubGroup = Util.boolValue(obj.getIncludeSubGroup());
        Integer orgSelecttion = ("1".equals(obj.getUserSelectionMode()) && obj.getOrgSelection()!=null ? Integer.parseInt(obj.getOrgSelection()) : 0);
        int orgCode = orgSelecttion == null ? 0 : orgSelecttion;
        String action=obj.getOperation();
        String emList = ("1".equals(obj.getUserSelectionMode()) ? null : obj.getUserSelection());
        //Boolean covered=Util.boolValue(obj.getCovered());
        try {
            String msg = null;
            if ("download".equals(action))
                msg = DevUtil.write2Device(readerGroupId, includeSubGroup, readerIds, orgCode, emList, includeSubOrg, 0, 100);
            else if ("remove".equals(action))
                msg = DevUtil.removeFromDevice(readerGroupId, includeSubGroup, readerIds, orgCode, emList, includeSubOrg, 0, 100);
            else if ("upload".equals(action)) {
            	 boolean uploadAllUser = Util.boolValue(obj.getUploadAllUser());
                 boolean includeNewUser = Util.boolValue(obj.getUploadNewUser());
                 if ("1".equals(obj.getUserSelectionMode()) && orgCode==0) {
                     j.setError(IotequThrowable.PARAMETER_ERROR, "请至少指定一个上传对象(部门,全部或新用户)", null);
                 }
                 msg = DevUtil.getData(readerGroupId, includeSubGroup, readerIds, uploadAllUser, includeNewUser,
                         orgCode, emList, includeSubOrg);
            }
            if (msg != null) j.setMessage(msg);
        } catch (Exception e) {
            j.setError(e);
        }
        return j;
    }
    
    @Override
    @ResponseBody
	public RestJson doAction(String action, String id, HttpServletRequest request) throws IotequException {
		RestJson j = new RestJson();
		if(action.equals("queryReader")) {
			List<Map<String,String>> listMap=new ArrayList<Map<String,String>>();
			try {
				String readerSelection=request.getParameter("readerSelection");//选择设备
				String deviceSelectionMode=request.getParameter("deviceSelectionMode");//设备模式
				String includeSubGroup=request.getParameter("includeSubGroup");//设备是否包含下级部门
				String groupSelection=request.getParameter("groupSelection");//设备分组
				if("1".equals(deviceSelectionMode)&&groupSelection!=null) {
					boolean include=(includeSubGroup!=null && includeSubGroup.equals("true"))?true:false;
					allReader(Integer.valueOf(groupSelection),include,listMap);
				}else if("2".equals(deviceSelectionMode)) {
					List<Map<String, String>> list=listReader(readerSelection);
					listMap.addAll(list);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			j.put("listReader", listMap);
			
		}else if(action.equals("queryDownloadPeople")) {
			List<DevPeople> listPeople=new ArrayList<>();
			List<DevPeople> listFail=new ArrayList<>();
			String readerNo=request.getParameter("readerNo");
			DevReader dr=SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where reader_no=?", readerNo);
			Integer capacity=Integer.valueOf(request.getParameter("capacity"));
			try {
			String userSelectionMode=request.getParameter("userSelectionMode");
			List<DevPeople> list=new ArrayList<DevPeople>();
			if("1".equals(userSelectionMode)) {
				String orgSelection=request.getParameter("orgSelection");
				String includeSubOrg=request.getParameter("includeSubOrg");
				if (orgSelection!=null&&Integer.valueOf(orgSelection)>0) {
					Integer orgId=Integer.valueOf(orgSelection);
					if (includeSubOrg!=null&&includeSubOrg.equals("true")) {
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
				
			}else if("2".equals(userSelectionMode)) {
				String userSelection=request.getParameter("userSelection");
				if (!Util.isEmpty(userSelection)) {
					String fis=SqlUtil.findInSet("user_no", false, true, userSelection, true);
					String sql="select * from dev_people where "+fis;
					List<DevPeople> l=SqlUtil.sqlQuery(DevPeople.class, sql);
					if (l!=null && l.size()>0) list.addAll(l);
				}
			}
			StringBuilder sb=new StringBuilder();
			sb.append("下发用户数已超过设备容量是否继续执行操作？").append("\n");
			if(list.size()>0) {
				for(DevPeople d:list) {
					Map<String,String> params = new HashMap<>();
					int col=DevUtil.getPermission(d,readerNo,params);
					if(col>0) {
						listPeople.add(d);
					}else {
						listFail.add(d);
					}
				}
			}
			if(dr.getAlignMethod()!=4) {
				List<DevPeople> listP=new ArrayList<>();
				if(dr.getType().indexOf("D30")!=-1) {
					int readerNumber=0;//设备人数
					int repeatNumber=0;//重复人数
					int addNumber=0;//新增人数
					String msg="";
					int status=0;
					List<DevPeopleMapping> listM=SqlUtil.sqlQuery(DevPeopleMapping.class, "select * from dev_people_mapping where reader_no=?", readerNo);
					if(listM!=null && listM.size()>0) {
						readerNumber=listM.size();
						for(DevPeople l:listPeople) {
							boolean isDownload=true;
							for(DevPeopleMapping d:listM) {
								if(d.getUserNo().equals(l.getUserNo())) {
									repeatNumber++;
									isDownload=false;
									break;
								}
							}
							if(isDownload) {
								listP.add(l);
								addNumber++;
							}
						}
						if(capacity-readerNumber-addNumber<0) {//超出设备容量
							msg="设备编号为:"+readerNo+" 容量:"+capacity+" 设备现有人数:"+readerNumber+" 新增人数:"+addNumber+"重复人数:"+repeatNumber+" 超出设备容量，请重新选择！";
							status=2;
						}else if(addNumber==0){//没有改变
							msg="设备没有改变";
							status=1;
						}
					}else {
						if(capacity<listPeople.size()) {
							addNumber=listPeople.size();
							status=2;
							msg="设备编号为:"+readerNo+" 容量:"+capacity+" 设备现有人数:"+readerNumber+" 新增人数:"+addNumber+"重复人数:"+repeatNumber+" 超出设备容量，请重新选择！";
						}else {
							listP.addAll(listPeople);
						}
						if(status!=2&&listP.size()<=0) {
							status=1;
						}
					}
					j.put("msg", msg);
					j.put("status", status);
					j.put("reader", readerNo);
					j.put("listPeople", listP);
					j.put("listFail", listFail);
					j.put("repeatNumber", repeatNumber);
				}else {
					j.put("status", 0);
					j.put("reader", readerNo);
					j.put("listPeople", listPeople);
					j.put("listFail", listFail);
				}
			  }else {
				  j.put("status", 4);
			  }
			} catch (Exception e) {
				// TODO: handle exception
				j.setSuccess(false);
				j.setMessage("失败");
			}
			
		}else if("download".equals(action)) {
			boolean isTrue=false;//下发是否成功状态
			boolean isReset=true;//当前设备是否还继续下发
			StringBuilder sb = new StringBuilder();
			try(BufferedReader reader = request.getReader();) {
			char[]buff = new char[1024];
			int len;
			while((len = reader.read(buff)) != -1) {
			sb.append(buff,0, len);
			}
			}catch (IOException e) {
			e.printStackTrace();
			}
			String ss="["+sb.toString()+"]";
			JSONArray arrayList= JSONArray.parseArray(ss);
			System.out.println(arrayList.toString());
			if(arrayList.size()>0) {
				for(int i=0;i<arrayList.size();i++) {
					String readerNo=arrayList.getJSONObject(i).getString("readerNo");
					j.put("readerNo", readerNo);
					boolean covered=arrayList.getJSONObject(i).getBoolean("covered");
					JSONArray listPeople=arrayList.getJSONObject(i).getJSONArray("listPeople");
					double all=arrayList.getJSONObject(i).getInteger("all");
					double number=arrayList.getJSONObject(i).getInteger("number");
					boolean busyOccupy=arrayList.getJSONObject(i).getBoolean("busyOccupy");
					int a=(int) (number/all*100);
					boolean freeOccupy=all==number?true:false;
					System.out.println(all);
					System.out.println(number);
					System.out.println(a);
					Util.setProgress(a);
					List<DevPeople> list = JSONObject.parseArray(listPeople.toJSONString(), DevPeople.class);
					System.out.println(list.size());
					DevReader d=SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where reader_no=?", readerNo);
					isTrue=DevDownloadUtil.downloadUsers(d, list, covered,busyOccupy,freeOccupy,false);
					if(isTrue) {
						for(DevPeople dp:list) {
							if(!SqlUtil.sqlExist("select * from dev_people_mapping where reader_no=? and user_no=?", d.getReaderNo(),dp.getUserNo()))
								SqlUtil.sqlExecute("insert into dev_people_mapping(reader_no,user_no,status) values(?,?,?)", d.getReaderNo(),dp.getUserNo(),"0");
							else 
								SqlUtil.sqlExecute("update dev_people_mapping set status=? where reader_no=? and user_no=?","0", d.getReaderNo(),dp.getUserNo());
						}
					}
					System.out.println(isTrue);
					if(all==number) {
						isReset=false;
					}
				}
			}
			j.put("isTrue", isTrue);
			j.put("isReset", isReset);
		}
		return j;
    }
    
    public List<Map<String,String>> allReader(int groupSelection,boolean includeSubGroup,List<Map<String,String>> listMap) throws Exception{
    	List<DevReader> list=SqlUtil.sqlQuery(DevReader.class, "select * from dev_reader where reader_group=?", groupSelection);
    	if(list!=null&&list.size()>0) {
    		for(DevReader d:list) {
    			int col=0;
    			if(d.getCapacity()==null||d.getCapacity()==0) {
    				try {
    					DevUtil.uploadD30Parameter(d);
    					col=d.getCapacity();
					} catch (Exception e) {
						// TODO: handle exception
					}
    			}else {
    				col=d.getCapacity();
    			}
    				Map<String,String> map=new HashMap<String, String>();
            		map.put("readerNo", d.getReaderNo());
            		map.put("capacity", String.valueOf(col));
            		listMap.add(map);
    		}
    	}
    	if(includeSubGroup) {
    		List<DevReaderGroup> listDg= SqlUtil.sqlQuery(DevReaderGroup.class, "select * from dev_reader_group where parent=?", groupSelection);
    		if(listDg!=null && listDg.size()>0) {
    			for(DevReaderGroup drg:listDg) {
    				allReader(drg.getId(), includeSubGroup, listMap);
    			}
    		}
    	}
    	return listMap;
    }
    
    
    public List<Map<String,String>> listReader(String readerSelection) throws Exception {
    	List<Map<String,String>> list =new ArrayList<>();
    	String fis=SqlUtil.findInSet("id", false, true, readerSelection, true);
		String sql="select * from dev_reader where "+fis;
		List<DevReader> l=SqlUtil.sqlQuery(DevReader.class, sql);
		if(l!=null&&l.size()>0) {
			for(DevReader d:l) {
				int col=0;
    			if(d.getCapacity()==null||d.getCapacity()==0) {
    				if(d.getType().indexOf("D30")!=-1) {
    					try {
        					DevUtil.uploadD30Parameter(d);
        					col=d.getCapacity();
    					} catch (Exception e) {
    						// TODO: handle exception
    					}
    				}else {
    					col=5000;//除D30外  其他类型的设备默认容量为5000
    				}
    				
    			}else {
    				col=d.getCapacity();
    			}
    				Map<String,String> map=new HashMap<String, String>();
            		map.put("readerNo", d.getReaderNo());
            		map.put("capacity", String.valueOf(col));
            		map.put("type", d.getType());
            		list.add(map);
    			
			}
		}
		return list;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
