package top.iotequ.codegenerator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.iotequ.codegenerator.pojo.CgTable;
import top.iotequ.codegenerator.service.impl.DtoService;
import top.iotequ.codegenerator.service.impl.GenService;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CodeUtil {
    public static final String CODE_BY_USER = "//^_^ Your code";
    private static final Logger log = LoggerFactory.getLogger(CodeUtil.class);

    public static String getUserCode(String path) {
        File file = new File(path);
        if (!file.exists()) return null;
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String s = "";
            boolean findBegin = false, findEnd = false, needCF = false;
            while (!findEnd && (s = bReader.readLine()) != null) {
                if (!findBegin) {
                    if (s.trim().startsWith(CODE_BY_USER)) findBegin = true;
                } else {
                    if (s.trim().startsWith(CODE_BY_USER)) findEnd = true;
                    else {
                        if (needCF) sb.append("\n");
                        sb.append(s);
                        needCF = true;
                    }
                }
            }
            bReader.close();
            if (findBegin && findEnd) {
                return sb.toString();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static int scanCode2Cg(String codePath, String projectId, String code) throws CgException {
        int scanFiles = 0;
        DtoService dtoService= Util.getBean(DtoService.class);
        CgTable t = dtoService.getTableByProjectIdAndCode(projectId,code);
        if (codePath == null) throw new CgException(IotequThrowable.ERROR,"请配置代码扫描文件路径 generator.codepath");
        if (t != null) {
            boolean changed = false;
            String filePath = NameUtil.controllerPath(codePath, t, true);
            String userCode = getUserCode(filePath);
            if (userCode != null) {
                log.info("scan :" + filePath);
                scanFiles++;
                changed = true;
            }

            filePath = NameUtil.baseJavaPath(codePath, t, true) + "/" + GenService.POJO + "/" + t.getEntity() + ".java";
            userCode = getUserCode(filePath);
            if (userCode != null) {
                log.info("scan :" + filePath);
                t.setPojoJavaCode(userCode);
                scanFiles++;
                changed = true;
            }
/*
            filePath = NameUtil.baseResourcePath(codePath, t, "templates", true) + NameUtil.updateHtml(t) + ".html";
            ;
            userCode = getUserCode(filePath);
            if (userCode != null) {
                log.info("scan :" + filePath);
                t.setUpdateJs(userCode);
                scanFiles++;
                changed = true;
            }
            filePath = NameUtil.baseResourcePath(codePath, t, "templates", true) + NameUtil.listHtml(t) + ".html";
            userCode = getUserCode(filePath);
            if (userCode != null) {
                log.info("scan :" + filePath);
                t.setListJs(userCode);
                scanFiles++;
                changed = true;
            }

 */
            if (changed) dtoService.update(t);
        }
        return scanFiles;
    }
}
