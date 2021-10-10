package top.iotequ.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ObjectUtil {

	/**
	 * 获得对象属性
	 * @param obj   对象
	 * @param fieldName  属性名
	 * @return 返回对象的私有属性值
	 * @throws NoSuchFieldException 异常
	 * @throws IllegalAccessException 异常
	 */
	static public Object getPrivateField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
			Class<?> clazz = obj.getClass();
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
	}
	/**
	 * 设置对象属性
	 * @param obj  对象
	 * @param fieldName 属性名
	 * @param value  属性值
	 * @throws NoSuchFieldException 异常
	 * @throws IllegalAccessException 异常
	 */
	static public void setPrivateField(Object obj, String fieldName,Object value) throws NoSuchFieldException, IllegalAccessException {
		Class<?> clazz = obj.getClass();
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(obj, value);
	}
	/**
	 * 设置对象字段属性,仅支持 cg java 类型
	 * @param obj  对象
	 * @param field 属性名
	 * @param value  属性值
	 * @throws IllegalAccessException 异常
	 */
	static public void setPrivateField(Object obj, Field field,String value) throws IllegalAccessException {
		field.setAccessible(true);
		Class<?> clazz = field.getType();
		if (clazz.equals(String.class)) field.set(obj, value);
		else if (clazz.equals(Integer.class)) field.set(obj, Integer.valueOf(value));
		else if (clazz.equals(Boolean.class)) field.set(obj, Utils.boolValue(value));
		else if (clazz.equals(Short.class)) field.set(obj, Short.valueOf(value));
		else if (clazz.equals(Long.class)) field.set(obj, Long.valueOf(value));
		else if (clazz.equals(Date.class)) field.set(obj, DateUtil.string2Date(value));
		else if (clazz.equals(Double.class)) field.set(obj, Double.valueOf(value));
		else if (clazz.equals(Byte.class)) field.set(obj, Byte.valueOf(value));
		else if (clazz.equals(Float.class)) field.set(obj, Float.valueOf(value));
		else if (clazz.equals(byte[].class)) field.set(obj, value.getBytes());
	}
	/**
	 * 获得属性的类型
	 * @param clazz 类
	 * @param fieldName 属性名
	 * @return 属性类型
	 * @throws NoSuchFieldException 异常
	 */
	static public Class<?> getFieldTypeName(Class<?> clazz, String fieldName) throws  NoSuchFieldException {

			Field field = clazz.getDeclaredField(fieldName);
			return field.getType();

	}
	/**
	 * 获得方法
	 * @param clazz 类
	 * @param methodName  方法名
	 * @param params  参数表
	 * @return 匹配名称和参数类型的类方法
	 * @throws NoSuchMethodException 异常
	 */
	public static Method getMethod(Class<?> clazz, String methodName, Object... params) throws NoSuchMethodException {
		Method[] methods = clazz.getMethods();
		for (Method m : methods) {
			if (methodName.equals(m.getName())) {
				Class<?>[] pm = m.getParameterTypes();
				if (pm.length == params.length) {
					boolean allMatch = true;
					for (int i = 0; i < params.length; i++) {
						if (params[i]!=null && !pm[i].isInstance(params[i])) {
							allMatch = false;
							break;
						}
					}
					if (allMatch)
						return m;
				}
			}
		}
		throw new NoSuchMethodException("no such method "+methodName);
	}

	/**
	 * 执行对象方法
	 * @param obj   对象，静态方法传入类对象clazz
	 * @param methodName  方法名
	 * @param params  参数表
	 * @return 执行对象的指定方法
	 * @throws NoSuchMethodException 异常
	 * @throws InvocationTargetException 异常
	 * @throws IllegalAccessException 异常
	 */
	static public Object runMethod(Object obj, String methodName, Object... params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		if (obj == null) return null;
		Class<?> clazz = (obj instanceof Class<?> ? ( Class<?>)obj : obj.getClass());
		Method method = getMethod(clazz, methodName, params);
		method.setAccessible(true);
		return method.invoke(obj instanceof Class<?> ? null : obj, params);
	}

	public static  Object clone(Object obj) {
		return obj;
	}

	public static Map<String,Object> mapCopy(Map<String,Object> paramsMap) {
		if (paramsMap == null) return null;
		Map<String,Object> resultMap = new HashMap();
		for (String key : paramsMap.keySet()) {
			Object obj = paramsMap.get(key);
			resultMap.put(key,  obj!= null ? clone(obj) : null);
		}
		return resultMap;
	}
	public static Map<String,Object> mapFromEntity(Object object) {
		if (object == null)	return null;
		Map<String, Object> map = new HashMap<>();
		try {
			Field fields[] = object.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object v=field.get(object);
				if (v!=null) {
					map.put(field.getName(), v);
				}
			}
		} catch (Exception e) { return null; }
		return map;
	}

	/**
	 * 从对象构造类对象
	 * @param to   被修改的对象
	 * @param from   源对象
	 */
	public static void entityCopy(Object to, Object from) {
		if (to == null || from == null)
			return ;
		try {
			Field fields[] = to.getClass().getDeclaredFields();
			for (Field f : fields) {
				try {
					Object v = getPrivateField(from, f.getName());
					f.setAccessible(true);
					f.set(to, v);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 合并对象，空字段用另一个对象填写
	 * @param target   被修改的对象
	 * @param from   源对象
	 */
	public static void entityMerge(Object target, Object from) {
		if (target == null || from == null)
			return ;
		try {
			Field fields[] = target.getClass().getDeclaredFields();
			for (Field f : fields) {
				try {
					Object v0 = getPrivateField(target, f.getName());
					if (v0==null) {
						Object v = getPrivateField(from, f.getName());
						if (v!=null) {
							f.setAccessible(true);
							f.set(target, v);
						}
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 从对象构造类对象
	 * @param clazz   pojo类名,如果是派生类，基类的字段将被忽略
	 * @param s   对象
	 * @param <T> 泛型类型
	 * @return  从一个对象生成一个指定的pojo对象，具有名称相同的值拷贝，否则为空
	 */
	public static <T> T entityCopyFrom(Class<T> clazz, Object s) {
		if (s == null)
			return null;
		try {
			T obj = clazz.newInstance();
			Field fields[] = clazz.getDeclaredFields();
			for (Field f : fields) {
				try {
					Object v = getPrivateField(s, f.getName());
					f.setAccessible(true);
					f.set(obj, v);
				} catch (Exception e) {
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void copyNonNullField(Object copyTo,Object copyFrom) {
		try {
			Class<?> clazzCopyTo = copyTo.getClass();
			Class<?> clazzCopyFrom = copyFrom.getClass();
			Field[] copyToFields = clazzCopyTo.getDeclaredFields();
			Field[] copyFromFields = clazzCopyFrom.getDeclaredFields();
			for (Field  f : copyFromFields) {
				f.setAccessible(true);
				Object v=f.get(copyFrom);
				if (v!=null) {
					for (Field f1 : copyToFields) {
						if (f1.getName().equals(f.getName())) {
							f1.setAccessible(true);
							f1.set(copyTo, v);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 获得变化的字段以及值map
	 * @param dest 变化后的对象
	 * @param compareTo 原对象
	 * @return 变化后的对象与原对象不同的字段值map,null表示无变化
	 */
	public static Map<String,Object> mapOfNonEqualField(Object dest,Object compareTo) {
		if (dest==null) return null;
		if (compareTo==null) return mapFromEntity(dest);
		try {
			Class<?> clazzDest = dest.getClass();
			Class<?> clazzCompareTo = compareTo.getClass();
			Field[] destFields = clazzDest.getDeclaredFields();
			List<Field> compareToFields = Arrays.asList(clazzCompareTo.getDeclaredFields());
			Map<String,Object> map=new HashMap<>();
			for (Field f : destFields) {
				f.setAccessible(true);
				String name = f.getName();
				Object v=f.get(dest);
				for (Field f1 : compareToFields) {
					if (name.equals(f1.getName())) {
						f1.setAccessible(true);
						Object v1=f1.get(compareTo);
						if (!entityEquals(v,v1)) map.put(name,v);
						if (compareToFields instanceof ArrayList) ((ArrayList<Field>)compareToFields).remove(f1);
						break;
					}
				}
			}
			if (map.isEmpty()) return null;
			else return map;
		} catch (Exception e) {}
		return null;
	}
	/**
	 * 判断两个entity对象是否相等，均为空认为相等
	 * @param o1  pojo1
	 * @param o2  pojo2
	 * @return 是否相等
	 */
	static public boolean entityEquals(Object o1, Object o2) {
		if (Utils.isEmpty(o1) && Utils.isEmpty(o2))
			return true;
		else if (Utils.isEmpty(o1) || Utils.isEmpty(o2))
			return false;
		return o1.toString().equals(o2.toString());
	}
	static public boolean isEntityEmpty(Object o) {
		if (o==null) 	return true;
		else {
			try {
				Class<?> clz = o.getClass();
				Field[] fields = clz.getDeclaredFields();
				for (Field f : fields) {
					PropertyDescriptor pd = new PropertyDescriptor(f.getName(), clz);
					Method rM = pd.getReadMethod();
					rM.setAccessible(true);
					Object v = rM.invoke(o);
					if (!Utils.isEmpty(v)) return false;
				}
			} catch (Exception e) {
				return Utils.isEmpty(o);
			}
		}
		return true;
	}
}
