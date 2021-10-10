package top.iotequ.codegenerator.util;

import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;

public class CgException extends IotequException {
    public static String tableCode="";
    public static String projectCode="";
    public static void setTableCode(String code) {
        tableCode = code;
    }
    public static void setProjectCode(String code) {
        projectCode = code;
    }
    public static String title() {
        return "<project = "+projectCode+", code = "+tableCode+">";
    }
    public CgException(String error, String msg) {
        super(error, title() + " : " + msg);
    }
    public CgException(String msg) {
        super(IotequThrowable.ERROR, title() + " : " + msg);
    }
    public CgException(Exception e) {
        super(IotequThrowable.ERROR, title() + " : " + e.getMessage());
    }
}
