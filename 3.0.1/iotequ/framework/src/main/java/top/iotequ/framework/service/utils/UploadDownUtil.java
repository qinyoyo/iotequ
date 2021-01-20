package top.iotequ.framework.service.utils;

import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.EntityUtil;
import top.iotequ.util.FileUtil;
import top.iotequ.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FilenameFilter;

public class UploadDownUtil {
    protected static String getFileNames(Class<?> clazz, String entityName, String pkId, boolean multiple, boolean nullable) {
        File dir = FileUtil.uploadFileDir(clazz);
        if (dir==null) return null;
        String prefix = FileUtil.uploadFilename(entityName, pkId,null) + "_";
        if (!dir.exists()) dir.mkdirs();
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(prefix);
            }
        });
        if (multiple) {
            if (files!=null && files.length>0) {
                String ff="";
                for (File f: files) {
                    ff += (ff.isEmpty()?"":",")+f.getName().substring(prefix.length());
                }
                return ff;
            } else return (nullable?null:"");
        } else {
            if (files!=null && files.length>0) {
                for (int i=1;i<files.length;i++) files[i].delete();
                String name = files[0].getAbsolutePath();
                CgFieldAnnotation fa = EntityUtil.getCgFieldAnnotation(clazz,entityName);
                if (fa!=null) {
                    if (name.length()<=fa.length()) return name;
                    else return files[0].getName().substring(prefix.length());
                }
                else return name;
            } else return (nullable?null:"");
        }
    }
    public static String uploadFile(Class<?> clazz, String entityName, String pkId, boolean multiple, boolean nullable, HttpServletRequest request) throws Exception {
        File dir = FileUtil.uploadFileDir(clazz);
        if (dir==null) throw new IotequException(IotequThrowable.ERROR,"upload dir not exists");
        String prefix = FileUtil.uploadFilename(entityName, pkId,null)+"_";
        if (!dir.exists()) dir.mkdirs();
        FileUtil.uploadFile(request, "filepart_"+entityName, dir, prefix, null,multiple);
        return getFileNames(clazz,entityName, pkId, multiple,nullable);
    }
    public static String removeFilesExclusive(Class<?> clazz,String entityName,String pkId,String fileNames,boolean nullable) {
        try {
            if (Util.isEmpty(entityName) || Util.isEmpty(pkId)) return fileNames;
            String [] names = (fileNames==null ? new String[] {} : fileNames.split(","));
            File dir = FileUtil.uploadFileDir(clazz);
            if (dir==null) return null;
            String prefix = FileUtil.uploadFilename(entityName, pkId,null);
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return  name.startsWith(prefix);
                }
            });
            fileNames="";
            for (File f: files) {
                String name=f.getCanonicalPath();
                boolean matched=false;
                for (String n:names) {
                    File f0 = FileUtil.uploadFile(clazz,entityName,pkId,n);
                    if (f0!=null && name.equals(f0.getCanonicalPath())) {
                        fileNames += (fileNames.isEmpty()?"":",")+n;
                        matched=true;
                        break;
                    };
                }
                if (!matched) f.delete();
            }
            if (nullable && fileNames.isEmpty()) return null;
            else return fileNames;
        } catch (Exception e) {
            return fileNames;
        }
    }
    public static void removeFilesWithId(Class<?> clazz,String pkId) {
        try {
            if (Util.isEmpty(pkId)) return;
            File dir = FileUtil.uploadFileDir(clazz);
            if (dir==null) return;
            String [] pkIdList = pkId.split(",");
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    for (String s: pkIdList) {
                        if (name.indexOf("_"+s+"_") > 0 ) return true;
                    }
                    return  false;
                }
            });
            for (File f: files) {
                f.delete();
            }
        } catch (Exception e) {  }
    }

    public static void downloadFile(Class<?> clazz,String entityName,String pkId,String fileName, HttpServletResponse response) throws IotequException {
        if (!Util.isEmpty(fileName) && (fileName.indexOf("/")>=0 || fileName.indexOf("\\")>=0)) {
            FileUtil.downloadFile(new File(fileName), response,true);
        } else {
            if (Util.isEmpty(entityName) || Util.isEmpty(pkId)) throw new IotequException(IotequThrowable.NULL_OBJECT,"参数不全");
            File dir = FileUtil.uploadFileDir(clazz);
            if (dir==null) return;
            String prefix = FileUtil.uploadFilename(entityName, pkId,null) +"_";
            FileUtil.downloadFile(new File(dir,prefix+fileName), response,true);
        }
    }
}
