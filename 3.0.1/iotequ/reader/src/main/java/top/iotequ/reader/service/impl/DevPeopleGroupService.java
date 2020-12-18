package top.iotequ.reader.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.reader.dao.DevPeopleGroupDao;

@Service
public class DevPeopleGroupService extends CgDevPeopleGroupService {
    @Autowired
    DevPeopleGroupDao devPeopleGroupDao;
/*
    @Override
    public RestJson doSave(boolean isNew, String flowCode, Integer totalFilePart, DevPeopleGroup obj, String idSaved, HttpServletRequest request) throws Exception {
        if (isNew && Util.isEmpty(obj.getUserNo()))  {
            String userList = obj.getUserPicked();
            if (Util.isEmpty(userList)) throw new IotequException(IotequThrowable.NULL_OBJECT,"user_no");
            String [] uu = userList.split(",");
            int count = 0;
            for (String u : uu) {
                obj.setUserNo(u);
                obj.setId(null);
                try {
                    devPeopleGroupDao.insert(obj);
                    count ++;
                }catch (Exception e) {}
            }
            return new RestJson().parameter("rows",count).parameter("refresh",true);
        }
        else return super.doSave(isNew, flowCode, totalFilePart, obj, idSaved, request);
    }

    @Override
    public RestJson doAction(String action, String ids, HttpServletRequest request) throws IotequException {
		RestJson j = new RestJson();
        if ("insertBatch".equals(action)) {
            String s = request.getParameter("groupId");
            Integer groupId = Util.isEmpty(s) ? null : Integer.parseInt(s);
            if (Util.isEmpty(ids)) {
                j.setSuccess(false);
                j.setError(IotequThrowable.NULL_OBJECT,"sys.null_object","");
            } else {
                int total = 0;
                for (String id : ids.split(",")) {
                    DevPeopleGroup pg = new DevPeopleGroup();
                    pg.setGroupId(groupId);
                    pg.setUserNo(id);
                    try {
                        devPeopleGroupDao.insert(pg);
                        total++;
                    } catch (Exception e) {
                    }
                }
                j.setMessage(String.format("插入 %d 条记录", total));
            }
        }
        return j;
    }

 */
}
