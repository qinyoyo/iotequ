package top.iotequ.util;

import java.util.*;

/**
 * 通用工具类，提供一些常用的静态函数方法
 */

public class Utils {
    static public Long toLong(Object o) {
        if (o == null)
            return null;
        else {
            try {
                Double d = Double.parseDouble(o.toString());
                return d.longValue();
            } catch (Exception e) {
                return null;
            }
        }
    }

    static public Integer toInt(Object o) {
        if (o == null)
            return null;
        else {
            try {
                Double d = Double.parseDouble(o.toString());
                return (int) d.longValue();
            } catch (Exception e) {
                return null;
            }
        }
    }

    static public <T> T null2Default(T obj, T def) {
        if (obj == null) return def;
        else return obj;
    }

    static public String getTextFromDict(List<Map<String, Object>> dict, Object value) {
        if (dict == null || dict.isEmpty() || value == null) return null;
        for (Map<String, Object> map : dict) {
            if (ObjectUtil.entityEquals(value, map.get("value"))) return StringUtil.toString(map.get("text"));
        }
        return null;
    }
    static public String getTextFromDict(Object value,Object [] valueList,String [] textList) {
        if (value == null || isEmpty(valueList) || isEmpty(textList)) return null;
        for (int i=0;i<valueList.length;i++) {
            if (ObjectUtil.entityEquals(value, valueList[i])) return i<textList.length ? textList[i] : StringUtil.toString(value);
        }
        return null;
    }
    static public Object getValueFromDict(List<Map<String, Object>> dict, String text) {
        if (dict == null || dict.isEmpty() || text == null) return null;
        for (Map<String, Object> map : dict) {
            if (ObjectUtil.entityEquals(text, map.get("text"))) return map.get("value");
        }
        return null;
    }

    /**
     * @param o : 对象
     * @return ：判断对象是否为null或空串，包括全部为空白字符的串
     */
    static public boolean isEmpty(Object o) {
        if (o == null) return true;
        else if (o.getClass().isArray()) {
           return ((Object[])o).length <= 0;
        } else if (o instanceof Collection) {
            return ((Collection)o).size() <= 0;
        } else return StringUtil.toString(o).trim().isEmpty();
    }

    /**
     * 判断串是否相等
     *
     * @param s1 第一个串
     * @param s2 第二个串
     * @return 两个串相等或均为空时返回真，大小写敏感
     */
    static public boolean equals(Object s1, Object s2) {
        if (s1==null && s2==null) return true;
        else if (s1 != null && s2 != null)
            return s1.toString().trim().equals(s2.toString().trim());
        else if (s1 == null)
            return isEmpty(s2);
        else
            return isEmpty(s1);
    }

    /**
     * 为空时返回缺省值，否则返回对象的toString
     *
     * @param o   对象
     * @param def 缺省值
     * @return o为空时返回缺省值，否则返回对象的toString
     */
    static public String isEmpty(Object o, String def) {
        if (o == null)
            return def;
        else
            return (o.toString().isEmpty() ? def : o.toString());
    }

    /**
     * 根据字符串解析一个boolean值
     *
     * @param obj 对象
     * @return 解析一个boolean值
     */
    static public boolean boolValue(Object obj) {
        if (obj == null) return false;
        else if (obj instanceof Boolean) return (Boolean) obj;
        else if (obj instanceof Integer) return (Integer) obj != 0;
        else if (obj instanceof Short) return (Short) obj != 0;
        else if (obj instanceof Byte) return (Byte) obj != 0;
        else if (obj instanceof Long) return (Long) obj != 0;
        else {
            String s = obj.toString().trim().toLowerCase();
            if (s.equals("1") || s.equals("true") || s.equals("yes") || s.equals("t") || s.equals("y") || s.equals(".t."))
                return true;
            else
                return false;
        }
    }

    public static Object valueOf(Object obj, Class<?> clazz) {
        if (obj == null || clazz == null) return null;
        String type = clazz.getName();
        String s = obj.toString();
        try {
            if (obj.getClass().equals(clazz)) return obj;
            if (type.endsWith("Boolean")) return Utils.boolValue(s);
            else if (type.endsWith("String")) return s;
            else if (type.endsWith("Integer")) return Integer.valueOf(s);
            else if (type.endsWith("Long")) return Long.valueOf(s);
            else if (type.endsWith("Short")) return Short.valueOf(s);
            else if (type.endsWith("Byte")) return Byte.valueOf(s);
            else if (type.endsWith("Double")) return Double.valueOf(s);
            else if (type.endsWith("Float")) return Float.valueOf(s);
            else if (type.endsWith("Date")) return DateUtil.string2Date(s);
            else if (type.endsWith("byte[]")) return s.getBytes();
        } catch (Exception e) {
        }
        if (type.equals("boolean")) return Utils.boolValue(s);
        else if (type.equals("char")) {
            if (s.isEmpty() || s == null) return (char) 0;
            else return s.charAt(0);
        } else if (type.equals("int")) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                return (int) 0;
            }
        } else if (type.equals("long")) {
            try {
                return Long.parseLong(s);
            } catch (Exception e) {
                return (long) 0;
            }
        } else if (type.equals("short")) {
            try {
                return Short.parseShort(s);
            } catch (Exception e) {
                return (short) 0;
            }
        } else if (type.equals("byte")) {
            try {
                return Byte.parseByte(s);
            } catch (Exception e) {
                return (byte) 0;
            }
        } else if (type.equals("double")) {
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                return (double) 0;
            }
        } else if (type.equals("float")) {
            try {
                return Float.parseFloat(s);
            } catch (Exception e) {
                return (float) 0;
            }
        }
        return null;
    }

}
