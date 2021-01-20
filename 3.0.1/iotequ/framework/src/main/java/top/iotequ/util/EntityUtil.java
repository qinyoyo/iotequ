package top.iotequ.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import top.iotequ.framework.exception.IotequException;

import javax.servlet.http.HttpServletRequest;

public class EntityUtil extends ObjectUtil {

	/**
	 * 从Map构造类对象
	 * @param clazz   pojo类名,如果是派生类，基类的字段将被忽略
	 * @param map   map对象
	 * @param <T> 泛型类型
	 * @return  从一个对象生成一个指定的pojo对象，具有名称相同的值拷贝，否则为空
	 */
	public static <T> T entityFromMap(Map<String,Object> map, Class<T> clazz) {
		if (map == null || clazz==null)	return null;
		try {
			Map<String, Object> newMap = new HashMap<>();
			Field fields[] = clazz.getDeclaredFields();
			for (String p : map.keySet()) {
				Object v=map.get(p);
				if (p!=null) {
					for (Field f : fields) {
						if (Util.isEmpty(v) && f.getType() != String.class ) continue;
						String name=f.getName();
						if (p.equals(name) || StringUtil.camelString(p).equals(name)) {
							if (f.getType().equals(Boolean.class))  {
								newMap.put(name, Util.boolValue(v));
							} else newMap.put(name, v);
							break;
						}
					}
				}
			}
			Gson gson= Util.getGson();
			return gson.fromJson(gson.toJson(newMap), clazz);
		} catch (Exception e) { return null; }
	}

	/**
	 * 从json串获得一个pojo对象
	 * @param json json串
	 * @param clazz 类
	 * @param <T> 泛型类型
	 * @return 类对象，可能为null
	 */
	public static <T> T entityFromJson(String json,Class<T> clazz) {
		if (json == null)
			return null;
		try {
			Gson gson = Util.getGson();
			T obj = gson.fromJson(json, clazz);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从request获得一个pojo对象
	 * @param clazz 对象类
	 * @param request request
	 * @param <T> 泛型类型
	 * @return 对象，或null
	 */
	static public <T> T createEntity(Class<T> clazz, HttpServletRequest request) throws IotequException{
		if (request == null || clazz==null)	return null;
		try {
			String contentType = request.getHeader("content-type");
			String method = request.getMethod();
			if (!Util.isEmpty(contentType) && !Util.isEmpty(method)
					&& method.toLowerCase().equals("post") && contentType.toLowerCase().contains("json")) {  // json post 模式
				return HttpUtils.getRequestBody(clazz, request);
			}
			// FormData 或 get 模式
			T obj = clazz.newInstance();
			;
			Field fields[] = clazz.getDeclaredFields();
			for (Field f : fields) {
				String name = f.getName();
				String v = request.getParameter(name);
				if (!Util.isEmpty(v) && !v.equals("NaN")) {
					setPrivateField(obj, f, v);
				}
			}
			return obj;
		} catch (Exception e) {
			throw IotequException.newInstance(e);
		}
	}

	static public Map<String,Object> mapFromBody(HttpServletRequest request) throws Exception{
		if (request == null)	return null;
		String contentType = request.getHeader("content-type");
		String method = request.getMethod();
		if (!Util.isEmpty(contentType) && !Util.isEmpty(method)
				&& method.toLowerCase().equals("post") 	&& contentType.toLowerCase().contains("json")) {  // json post 模式
			return HttpUtils.getRequestBody(request);
		} else {
			Map<String,String[]> mm = request.getParameterMap();
			if (mm!=null) {
				Map<String,Object> map=new HashMap<>();
				for (String key: mm.keySet()) {
					map.put(key,mm.get(key)[0]);
				}
				return map;
			} else return null;
		}
		// FormData 或 get 模式
	}
	
	
	/**
	 * 获得 CgTableAnnotation
	 * @param clazz 实体类
	 * @return CgTableAnnotation对象
	 */
	static public CgTableAnnotation getCgTableAnnotation(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(CgTableAnnotation.class)) {
				return clazz.getAnnotation(CgTableAnnotation.class);
			}
		} catch (Exception e) {}
		return null;
	}

	/**
	 * 获得字段的CgFieldAnnotation
	 * @param clazz 实体类
	 * @param entityName 字段
	 * @return CgFieldAnnotation对象
	 */
	static public CgFieldAnnotation getCgFieldAnnotation(Class<?> clazz,String entityName) {
		try {
			Field field = clazz.getDeclaredField(entityName);
			if (field != null && field.isAnnotationPresent(CgFieldAnnotation.class)) {
				return field.getAnnotation(CgFieldAnnotation.class);
			}
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 获得数据库表名
	 * @param clazz ： 实体类
	 * @return 数据库表名
	 */
	static public String getDBTableNameFrom(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(CgTableAnnotation.class)) {
				CgTableAnnotation an = clazz.getAnnotation(CgTableAnnotation.class);
				if (an != null) {
					return an.name();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得数据库join表名及条件
	 * @param clazz ： 实体类
	 * @return join表
	 */
	static public String getDBJoinConditionFrom(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(CgTableAnnotation.class)) {
				CgTableAnnotation an = clazz.getAnnotation(CgTableAnnotation.class);
				if (an != null) {
					return an.join();
				}
			}
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 根据entity名获得数据库字段名 
	 * @param clazz    cg生成的entity类
	 * @param entityName   属性名
	 * @return 数据库字段名
	 */
	static public String getDBFieldNameFrom(Class<?> clazz, String entityName) {
		try {
			Field field = clazz.getDeclaredField(entityName);
			if (field.isAnnotationPresent(CgFieldAnnotation.class)) {
				CgFieldAnnotation an = field.getAnnotation(CgFieldAnnotation.class);
				if (an != null) {
					return an.name();
				}
			}
		} catch (Exception e) {
		}
		return null;
	}
	static public boolean getNullableFrom(Class<?> clazz, String entityName) {
		try {
			Field field = clazz.getDeclaredField(entityName);
			if (field.isAnnotationPresent(CgFieldAnnotation.class)) {
				CgFieldAnnotation an = field.getAnnotation(CgFieldAnnotation.class);
				if (an != null) {
					return an.nullable();
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
	static public boolean getMultipleFrom(Class<?> clazz, String entityName) {
		try {
			Field field = clazz.getDeclaredField(entityName);
			if (field.isAnnotationPresent(CgFieldAnnotation.class)) {
				CgFieldAnnotation an = field.getAnnotation(CgFieldAnnotation.class);
				if (an != null) {
					return an.multiple();
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
	/**
	 * 根据entity名获得数据库字段表达式
	 * @param clazz    cg生成的entity类
	 * @param entityName   属性名
	 * @return 数据库计算字段表达式
	 */
	static public String getDBFieldExpressionFrom(Class<?> clazz, String entityName) {
		try {
			Field field = clazz.getDeclaredField(entityName);
			if (field.isAnnotationPresent(CgFieldAnnotation.class)) {
				CgFieldAnnotation an = field.getAnnotation(CgFieldAnnotation.class);
				if (an != null) {
					return an.expression();
				}
			}
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 根据entity名获得数据库字段jdbc类型
	 * @param clazz    cg生成的entity类
	 * @param entityName   属性名
	 * @return 数据库字段jdbc类型
	 */
	static public String getJdbcTypeFrom(Class<?> clazz, String entityName) {
		try {
			Field field = clazz.getDeclaredField(entityName);
			if (field.isAnnotationPresent(CgFieldAnnotation.class)) {
				CgFieldAnnotation an = field.getAnnotation(CgFieldAnnotation.class);
				if (an != null) {
					return an.jdbcType();
				}
			}
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 获得数据库主键字段
	 * @param clazz   实体类
	 * @return 数据库主键
	 */
	static public String getDBTablePkFieldFrom(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(CgTableAnnotation.class)) {
				CgTableAnnotation an = clazz.getAnnotation(CgTableAnnotation.class);
				if (an != null) {
					return an.pk();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得数据库主键字段
	 * @param clazz   实体类
	 * @return 模块名
	 */
	static public String getModuleFrom(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(CgTableAnnotation.class)) {
				CgTableAnnotation an = clazz.getAnnotation(CgTableAnnotation.class);
				if (an != null) {
					return an.module();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static public String getParentEntityFieldFrom(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(CgTableAnnotation.class)) {
				CgTableAnnotation an = clazz.getAnnotation(CgTableAnnotation.class);
				if (an != null) {
					return an.parentEntityField();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	static public String getPkTypeFrom(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(CgTableAnnotation.class)) {
				CgTableAnnotation an = clazz.getAnnotation(CgTableAnnotation.class);
				if (an != null) {
					return an.pkType();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得数据库主键字段
	 * @param clazz   实体类
	 * @return 是否有licence控制
	 */
	static public boolean hasLicenceFor(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(CgTableAnnotation.class)) {
				CgTableAnnotation an = clazz.getAnnotation(CgTableAnnotation.class);
				if (an != null) {
					return an.hasLicence();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 获得数据库主键字段
	 * @param clazz   实体类
	 * @return 模块名
	 */
	static public String getBaseUrlFrom(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(CgTableAnnotation.class)) {
				CgTableAnnotation an = clazz.getAnnotation(CgTableAnnotation.class);
				if (an != null) {
					return an.baseUrl();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得实体类主键字段
	 * @param clazz   实体类
	 * @return 实体类主键
	 */
	static public String getEntityPkFieldFrom(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(CgTableAnnotation.class)) {
				CgTableAnnotation an = clazz.getAnnotation(CgTableAnnotation.class);
				if (an != null) {
					return an.entityPk();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得实体类主键字段
	 * @param obj   对象
	 * @return 实体类主键值
	 */
	static public Object getPkValueFrom(Object obj) {
		String pk=getEntityPkFieldFrom(obj.getClass());
		if (pk!=null) {
			try {
				return getPrivateField(obj,pk);
			} catch (Exception e) {
				return null;
			}
		} else return null;
	}
	/**
	 * 获得实体类字符串主键字段
	 * @param obj   对象
	 * @return 实体类主键值
	 */
	static public String getStringPkValueFrom(Object obj) {
		Object v =  getPkValueFrom(obj);
		if (v!=null) return v.toString();
		else return null;
	}
	/**
	 * 获得实体类整数主键字段
	 * @param obj   对象
	 * @return 实体类主键值
	 */
	static public Integer getIntPkValueFrom(Object obj) {
		Object v =  getPkValueFrom(obj);
		if (v!=null) return (Integer)v;
		else return null;
	}
	/**
	 * @param o  对象
	 * @return ：对象转换为json string
	 */
	static public String toJsonString(Object o) {
		if (o == null)
			return null;
		else {
			Gson gson = Util.getGson();
			String s=gson.toJson(o);
			return s;
		}
	}
}
