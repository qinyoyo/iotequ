package top.iotequ.reader.service.impl;

import org.springframework.stereotype.Service;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.util.RestJson;
import top.iotequ.framework.util.Util;
import top.iotequ.reader.pojo.DevData;
import top.iotequ.reader.util.DevUtil;

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
}
