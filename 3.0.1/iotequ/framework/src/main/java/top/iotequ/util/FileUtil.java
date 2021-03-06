package top.iotequ.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.ParameterParser;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import top.iotequ.framework.bean.SpringContext;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequIOException;
import top.iotequ.framework.exception.IotequThrowable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import lombok.NonNull;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtil extends FileIOUtil {
    public static String getImageBase64String(File file) throws IotequException {
        if (file==null) throw new IotequException(IotequThrowable.NULL_OBJECT,"file is null");
        String fileContentBase64 = null;
        String fileType = "image/";
        if (file.getName().lastIndexOf('.')>=0)  fileType+=file.getName().substring(file.getName().lastIndexOf('.')+1).toLowerCase();
        else throw new IotequException(IotequThrowable.FAILURE,"file type unknown");
        String base64Str = "data:" + fileType + ";base64,";
        String content = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte [] data = new byte[in.available()];
            in.read(data);
            in.close();
            if (data == null || data.length == 0) {
                return null;
            }
            content = Base64.encodeBase64String(data);
            if (content == null || "".equals(content)) {
                return null;
            }
            fileContentBase64 = base64Str + content;
        } catch (Exception e) {
            throw IotequException.newInstance(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {}
            }
        }
        return fileContentBase64;
    }

    public static String saveBase64Image(String nameWhithoutExt, String imgBase64) {
        if (Util.isEmpty(nameWhithoutExt) || Util.isEmpty(imgBase64)) return null;
        String ext = StringUtil.regGroup("image\\/([a-z]+);",imgBase64,1);
        String data = StringUtil.regGroup(";base64,(.*)$",imgBase64,1);
        if (Util.isEmpty(ext) || Util.isEmpty(data)) return null;
        String name = nameWhithoutExt + "." + ext;
        File outf = new File(name);
        if (!outf.getParentFile().exists()) outf.getParentFile().mkdirs();
        else if (outf.exists()) outf.delete();
        byte bin[] = Base64.decodeBase64(data);
        try {
            OutputStream out = new FileOutputStream(outf);
            out.write(bin);
            out.flush();
            out.close();
        } catch (Exception e) { return null; }
        return name;
    }
    /**
     * 获得上传文件保存目录
     * @param generatorName cg代码名
     * @return 目录
     */
    static public File uploadFileDir(String generatorName) {
        String uploadPath = Util.getBean(Environment.class).getProperty("spring.upload-path");
        return new File(Util.isEmpty(uploadPath, ""), "/" + (Util.isEmpty(generatorName) ? "" : generatorName + "/")).getAbsoluteFile();
    }
    /**
     * 获得上传文件保存目录
     * @param clazz 实体类
     * @return 目录
     */
    static public File uploadFileDir(Class<?> clazz) {
        CgTableAnnotation annotation=clazz.getAnnotation(CgTableAnnotation.class);
        if (annotation!=null) {
            return uploadFileDir(annotation.generatorName());
        } else return null;
    }

    /**
     * 获得上传文件的文件名，不包含路径
     * @param entityName 字段名
     * @param pkId 主键值
     * @param name 参考文件名
     * @return 上传文件存储文件名，不包含路径
     */
    static public String uploadFilename(String entityName, String pkId, String name) {
        String nm = Util.isEmpty(entityName, "");
        if (!Util.isEmpty(pkId)) {
            if (nm.isEmpty()) nm = pkId;
            else nm += ("_" + pkId);
        }
        if (!Util.isEmpty(name)) {
            if (nm.isEmpty()) nm = name;
            else nm += ("_" + name);
        }
        return nm;
    }

    /**
     * 获得上传文件对象
     * @param generatorName cg代码名
     * @param entityName 字段
     * @param pkId 主键值
     * @param name 参考名
     * @return File对象
     */
    static public File uploadFile(String generatorName,String entityName, String pkId, String name) {
        String uploadPath = Util.getBean(Environment.class).getProperty("spring.upload-path");
        if (!Util.isEmpty(name) && (name.indexOf("/")>=0 || name.indexOf("\\")>=0)) { // 文件名包含路径，直接读取
            if (name.startsWith("~/") || name.startsWith("~\\")) return new File(uploadPath,name.substring(2));
            else return new File(name);
        } else return new File(uploadFileDir(generatorName),uploadFilename(entityName, pkId, name));
    }
    /**
     * 获得上传文件对象
     * @param clazz 实体类
     * @param entityName 字段
     * @param pkId 主键值
     * @param name 参考名
     * @return File对象
     */
    static public File uploadFile(Class<?> clazz,String entityName, @NonNull String pkId, String name) {
        CgTableAnnotation annotation=clazz.getAnnotation(CgTableAnnotation.class);
        if (annotation!=null) {
            return uploadFile(annotation.generatorName(),entityName,pkId,name);
        } else return null;
    }

    /**
     * 将上传的part写入文件
     *
     * @param request  request
     * @param partName request记录part的属性名，多个文件依次为 partName，partName1，partName2...
     * @param dir      保存文件的目录
     * @param prefix   文件名加上这个前缀为新的文件名，为空忽略
     * @param newName  采用新的文件名，为空忽略
     * @param multiple 是否允许上传多个文件
     * @return 写入的文件列表, 支持多个文件上传
     * @throws IotequException 异常
     */
    static public List<File> uploadFile(HttpServletRequest request, String partName, File dir, String prefix, String newName, boolean multiple) throws IotequException {
        try {
            List<File> list = new ArrayList<File>();
            int index = 0;
            while (true) {
                Part part = request.getPart(partName + (index == 0 ? "" : "_"+String.valueOf(index)));
                if (part == null) return list;
                index++;
                String name = "";//part.getSubmittedFileName() 这个函数ie下有问题
                String cd = part.getHeader("Content-Disposition");
                if (cd != null) {
                    String cdl = cd.toLowerCase();
                    if (cdl.startsWith("form-data") || cdl.startsWith("attachment")) {
                        ParameterParser paramParser = new ParameterParser();
                        paramParser.setLowerCaseNames(true);
                        Map<String, String> params = paramParser.parse(cd, ';');
                        if (params.containsKey("filename")) {
                            name = params.get("filename");
                            if (name != null) {
                                name = new File(name.trim()).getName();
                            } else {
                                name = "";
                            }
                        }
                    }
                }
                if (prefix != null) name = prefix + name;
                else if (newName != null) {
                    name = newName + (name.lastIndexOf(".") > 0 ? name.substring(name.lastIndexOf(".")) : "");
                }
                File file = new File(dir, name);
                if (!dir.exists()) dir.mkdirs();
                else if (!multiple) {
                    File[] files = dir.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.startsWith(prefix);
                        }
                    });
                    if (files!=null && files.length>0) {
                        for (int i=0;i<files.length;i++) {
                            files[i].delete();
                        }
                    }
                }
                part.write(file.getAbsolutePath());
                list.add(file);
                if (!multiple) return list;
            }
        } catch (Exception e) {
            throw IotequException.newInstance(e);
        }
    }

    /**
     * 下载指定的文件
     *
     * @param file        需要下载的文件
     * @param response    response
     * @param browserOpen true: 支持浏览器打开
     * @throws IotequException 异常
     */
    static public void downloadFile(File file, HttpServletResponse response, boolean browserOpen) throws IotequException {
        try {
            if (file == null || !file.exists())
                throw new IotequIOException(IotequThrowable.IO_OBJECT_NOT_EXIST, "File not exists");
            String fileName = file.getName();
            if (browserOpen) {
                int p = fileName.lastIndexOf(".");
                String ct = null;
                if (p >= 0) {
                    ct = MimeMappings.DEFAULT.get(fileName.substring(p + 1).toLowerCase());
                }
                response.setHeader("content-type", ct == null ? "application/octet-stream" : ct);
            } else response.setHeader("content-type", "application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", (browserOpen ? "inline" : "attachment") + ";filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(file.length()));

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                throw new IotequIOException(IotequThrowable.IO_EXCEPTION, e.getMessage());
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e) {
            throw IotequException.newInstance(e);
        }
    }
    public static File getWebappFile(String path) throws IotequException {
        File webapp = new File(SpringContext.getProjectHomePath(),"/webapp");
        if (Util.isEmpty(path)) return webapp;
        else {
            File file=new File(webapp, path);
            if (file.exists()) return file;
            else throw new IotequException(IotequThrowable.IO_OBJECT_NOT_EXIST, file.getAbsolutePath());
        }
    }
    public static File getResourceFile(String path) throws IotequException {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(path);
        try {
            return resource.getFile();
        } catch (IOException e) {
            throw IotequException.newInstance(e);
        }
    }
}
