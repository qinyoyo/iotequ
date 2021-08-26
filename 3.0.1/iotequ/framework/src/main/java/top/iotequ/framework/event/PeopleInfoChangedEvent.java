package top.iotequ.framework.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import top.iotequ.util.EntityUtil;
import top.iotequ.util.StringUtil;
import top.iotequ.util.Util;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class PeopleInfoChangedEvent extends ApplicationEvent {
	@Setter
	@Getter
	public static class People {
		private String  userNo;
		private String  photo;
		private String  realName;
		private String  sex;
		private Date    birthDate;
		private String  mobilePhone;
		private String  email;
		private String  wechatOpenid;
		private Integer idType;
		private String  idNumber;
		private Date    validDate;
		private Date    expiredDate;
		private String  idNation;
		private String  homeAddr;
		private Integer regFingers;
	}
	private static final long serialVersionUID = 1L;
	static public final int none   = 0;
	static public final int update = 1;
	static public final int insert = 2;
	static public final int delete = 3;
	private People people;
	private String userNoList;
	private int    mode;
	public PeopleInfoChangedEvent(Object source) {
		super(source);
	}

	/**
	 * 通过对象创建一个人员基本信息变化时间
	 * @param source 事件发起对象
	 * @param obj0 原对象，新建时为 null
	 * @param obj 更改后对象，不能为空
	 * @param userNoField 记录userNo的字段名，不能为空
	 * @return
	 */
	public static PeopleInfoChangedEvent createPeopleInfoChangedEvent(@NonNull  Object source, Object obj0, @NonNull Object obj, @NonNull String userNoField) {
		try {
			String userNo = StringUtil.toString(EntityUtil.getPrivateField(obj, userNoField));
			if (Util.isEmpty(userNo)) return null;
			Map<String, Object> changedFields = EntityUtil.mapOfNonEqualField(obj, obj0); // 取变化字段
			if (changedFields == null) return null;
			People people = EntityUtil.entityFromMap(changedFields, People.class);
			if (EntityUtil.isEntityEmpty(people)) return null;
			PeopleInfoChangedEvent event = new PeopleInfoChangedEvent(source);
			people.setPhoto(null);  // 不同步照片
			people.setUserNo(userNo);
			event.setMode(obj0 == null ? insert : update);
			event.setPeople(people);
			return event;
		} catch (Exception e) {
			return null;
		}
	}

	public static PeopleInfoChangedEvent createPeopleInfoChangedEvent(@NonNull  Object source, People people, int mode) {
		try {
			if (EntityUtil.isEntityEmpty(people)) return null;
			PeopleInfoChangedEvent event = new PeopleInfoChangedEvent(source);
			event.setMode(mode);
			event.setPeople(people);
			return event;
		} catch (Exception e) {
			return null;
		}
	}
}
