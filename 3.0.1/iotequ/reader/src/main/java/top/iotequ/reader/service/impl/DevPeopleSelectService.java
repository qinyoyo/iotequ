package top.iotequ.reader.service.impl;

public class DevPeopleSelectService  {
/*
	@Override
	public RestJson doAction(String action, String id, HttpServletRequest request)  {
		String readerGroupId=request.getParameter("reader_group_id");
		String readerIds=request.getParameter("reader_id");
		boolean includeSubGroup= Util.boolValue(request.getParameter("include_sub_group"));
		try {
			String msg=null;
			if ("download".equals(action)) msg= DevUtil.write2Device(readerGroupId, includeSubGroup, readerIds, 0, id, false,0,100);
			else if ("deleteSpecifyUser".equals(action)) msg=DevUtil.removeFromDevice(readerGroupId,includeSubGroup, readerIds, 0, id,false,0,100);
			else if ("get_data".equals(action)) {
				boolean uploadAllUser=Util.boolValue(request.getParameter("upload_all_user"));
				boolean includeNewUser=Util.boolValue(request.getParameter("include_new_user"));
				if (Util.isEmpty(id) && !uploadAllUser && !includeNewUser) {
					j.setMessage("请至少指定一个上传对象(用户,全部或新用户)");
					j.setSuccess(false);
					return;
				}
				msg=DevUtil.getData(readerGroupId, includeSubGroup, readerIds, uploadAllUser, includeNewUser, null, id,false);
			}
			if (msg!=null) j.setMessage(msg);
		} catch (Exception e) {
				j.setSuccess(false);
				j.setMessage(messageService,e,"db.f.devPeopleSelect");
		}
	}
*/
}


