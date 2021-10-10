package top.iotequ.pay.utility;

import lombok.NonNull;
import org.simpleframework.xml.Transient;
import top.iotequ.pay.exception.PayException;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static List<String> listAddStrings(List<String> list, @NonNull String... str) {
        List<String> ret = new ArrayList<String>();
        if (list != null && !list.isEmpty()) {
            ret.addAll(list);
        }
        for (String s : str) {
            if (!isEmpty(s)) {
                ret.add(s);
            }
        }
        return ret;
    }

    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        } else {
            return o.toString().trim().isEmpty();
        }
    }

    public static boolean isSame(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        } else if (o1 == null || o2 == null) {
            return false;
        } else {
            return o1.toString().equals(o2.toString());
        }
    }

    public static List<Class<?>> getSuperClass(Class<?> clazz) {
        List<Class<?>> superClassList = new ArrayList<Class<?>>();
        Class<?> superclassIterator = clazz.getSuperclass();
        while (superclassIterator != null) {
            if (superclassIterator.getName().equals("java.lang.Object")) {
                break;
            }
            superClassList.add(superclassIterator);
            superclassIterator = superclassIterator.getSuperclass();
        }
        return superClassList;
    }

    public static List<Field> getAllFields(@NonNull Class<?> clazz) {
        List<Field> list = new ArrayList<Field>(Arrays.asList(clazz.getDeclaredFields()));
        List<Class<?>> supers = getSuperClass(clazz);
        for (Class<?> c : supers) {
            list.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return list;
    }

    public static Field getField(@NonNull Class<?> clazz, @NonNull String fieldName) {
        List<Field> list = getAllFields(clazz);
        for (Field field : list) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    public static Object getFieldValue(@NonNull Object obj, @NonNull String fieldName) {
        try {
            Class<?> clazz = obj.getClass();
            Field field = getField(clazz, fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setFieldValue(@NonNull Object obj, @NonNull String fieldName, Object v) {
        try {
            Class<?> clazz = obj.getClass();
            Field field = getField(clazz, fieldName);
            field.setAccessible(true);
            if (v == null || (v.getClass().getName().equals(field.getType().getName())))
                field.set(obj, v);
            else {
                String s = v.toString();
                String typ = field.getType().getName();
                if (typ.endsWith("String")) field.set(obj, s);
                else if (typ.endsWith("Integer")) field.set(obj, Integer.parseInt(s));
                else if (typ.endsWith("Byte")) field.set(obj, Byte.parseByte(s));
                else if (typ.endsWith("Long")) field.set(obj, Long.parseLong(s));
                else if (typ.endsWith("Boolean")) field.set(obj, Boolean.valueOf(s));
            }
        } catch (Exception ignored) {
        }
    }

    public static String calculateSignature(String str) throws PayException {
        if (str == null || str.trim().isEmpty()) {
            return str;
        }
        try {
            MessageDigest md5;
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = str.getBytes();
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuilder hexValue = new StringBuilder();
            for (byte md5Byte : md5Bytes) {
                int val = ((int) md5Byte) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            throw new PayException(PayException.DataDestroyed, e.getMessage());
        }
    }

    public static String objectString(Object object) {
        try {
            Class<?> clazz = object.getClass();
            List<Field> fields = PayUtil.getAllFields(clazz);
            Collections.sort(fields, new Comparator<Field>() {
                @Override
                public int compare(Field o1, Field o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            StringBuilder sb = new StringBuilder();
            for (Field field : fields) {
                String fieldName = field.getName();
                Transient an = field.getAnnotation(Transient.class);
                if (an == null && !"signature".equals(fieldName)) {
                    field.setAccessible(true);
                    Object v = field.get(object);
                    if (v != null) {
                        if (v instanceof List) {
                            for (Object item : (List) v) {
                                String itemStr = objectString(item);
                                if (!isEmpty(itemStr)) {
                                    sb.append(v.toString());
                                }
                            }
                        } else {
                            sb.append(v.toString());
                        }
                    }
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void objectEncrypt(@NonNull Object object, String key, boolean encryption)
            throws PayException {
        List<Field> fields = getAllFields(object.getClass());
        for (Field fieldItem : fields) {
            Transient an = fieldItem.getAnnotation(Transient.class);
            if (an == null) {
                String field = fieldItem.getName();
                if ("operation".equals(field) || "deviceNo".equals(field) ||
                        "shopId".equals(field) || "code".equals(field) || "loginId".equals(field) ||
                        "message".equals(field) || "signature".equals(field)) {
                    continue;
                }
                // operateType,deviceNo,shopId,retCode 用于获取数据以及密钥等，不允许加密
                fieldItem.setAccessible(true);
                Object v;
                try {
                    v = fieldItem.get(object);
                } catch (IllegalAccessException e) {
                    v = null;
                }
                if (!PayUtil.isEmpty(v)) {
                    if (v instanceof String || v instanceof Integer || v instanceof Byte) {
                        try {
                            Object ev;
                            if (v instanceof String) {
                                ev = EncryptUtil.encryptString((String) v, key, encryption);
                            } else if (v instanceof Integer) {
                                ev = EncryptUtil.encryptInt((Integer) v, key, encryption);
                            } else {
                                ev = EncryptUtil.encryptByte((Byte) v, key, encryption);
                            }
                            fieldItem.set(object, ev);
                        } catch (Exception e) {
                            throw new PayException(PayException.DataDestroyed, e, field);
                        }
                    } else if (v instanceof List) {
                        for (Object item : (List) v) {
                            if (item != null) {
                                objectEncrypt(item, key, encryption);
                            }
                        }
                    }
                }

            }
        }
    }

    public static String date2String(@NonNull Date dt, String fmt) {
        return new SimpleDateFormat(fmt == null ? "yyyy-MM-dd HH:mm:ss" : fmt).format(dt);
    }

    /**
     * 将传输的串格式化为Date对象
     *
     * @param dt  串
     * @param fmt 格式
     * @return Date对象
     * @throws PayException 格式错误时抛出异常
     */
    public static Date string2Date(String dt, String fmt) throws PayException {
        if (dt == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(fmt == null ? "yyyy-MM-dd HH:mm:ss" : fmt).parse(dt);
        } catch (ParseException e) {
            throw new PayException(PayException.IllegalArgument, "请满足格式" + (fmt == null ? "yyyy-MM-dd HH:mm:ss" : fmt));
        }
    }

    /**
     * 判断当前时间是否在区间内(包含开始，不包含结束)
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param fmt 比较格式，null表示完全比较
     * @return 当前时间是否在区间内
     */
    public static boolean isValidNow(Date startTime, Date endTime, String fmt) {
        if (startTime==null || endTime==null) return true;
        else if (startTime==null) {
            String edt=PayUtil.date2String(endTime,fmt);
            String now=PayUtil.date2String(new Date(),fmt);
            return now.compareTo(edt)<0;
        } else if (endTime==null){
            String sdt=PayUtil.date2String(startTime,fmt);
            String now=PayUtil.date2String(new Date(),fmt);
            return now.compareTo(sdt)>=0;
        } else {
            String sdt=PayUtil.date2String(startTime,fmt);
            String edt=PayUtil.date2String(endTime,fmt);
            String now=PayUtil.date2String(new Date(),fmt);
            return now.compareTo(sdt)>=0 && now.compareTo(edt)<0;
        }
    }
}
