package top.iotequ.codegenerator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import top.iotequ.codegenerator.IotequModule;
import top.iotequ.codegenerator.pojo.CgProject;
import top.iotequ.codegenerator.util.CgException;
import top.iotequ.codegenerator.util.NameUtil;
import top.iotequ.framework.exception.IotequException;

import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.util.RestJson;
import top.iotequ.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class CgProjectService extends CgCgProjectService {
    @Autowired
    private GenService genService;
    @Autowired
    private Environment env;
    @Autowired
    private IotequModule iotequModule;
    public static void toZip(File sourceFile, OutputStream out, boolean KeepDirStructure) throws Exception {
        ZipOutputStream zos = new ZipOutputStream(out);
        compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
        zos.close();
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception 出错抛出异常
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[4096];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (KeepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                }
            }
        }
    }

    @Override
    public void beforeSave(CgProject gp0, CgProject gp, boolean updateSelective, HttpServletRequest request) throws IotequException {
        String creator = Util.getUser().getName();
        if ("admin".equals(creator)) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"不允许admin用户进行代码定义操作");
        gp.setCreator(creator);
    }

    @Override
    public String listFilter(String path) {
        return "creator = '" + Util.getUser().getName() + "'";
    }

    @Override
    public void setPrimaryKey(CgProject obj) throws IotequException {
        if (Objects.nonNull(obj)) {
            obj.setId(obj.getCreator() + "." + obj.getCode());
        }
    }

    @Override
    public RestJson doAction(String action, String id, HttpServletRequest request) throws IotequException {
        RestJson j=new RestJson();
        if ("build".equals(action)) {
            String[] ids = id.split(",");
            for (String s : ids) genService.project(s);
        } else if ("down".equals(action)) {
            String generatorPath = env.getProperty("generator.path");
            if (generatorPath == null) throw new CgException("请配置代码生成文件路径 generator.path");
            generatorPath = new File(generatorPath,iotequModule.getVersion()).getAbsolutePath();
            String path = NameUtil.userPath(generatorPath, false);
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                OutputStream out = j.getDownloadOutputStream(Util.getResponse(), "application/x-zip-compressed", "cg.zip");
                if (out != null) {
                    try {
                        toZip(dir, out, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else throw new CgException("请先配置和生成代码");
            Util.setProgress(100);
        }
        return j;
    }
}
