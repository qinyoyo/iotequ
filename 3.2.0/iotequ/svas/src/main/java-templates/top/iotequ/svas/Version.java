package top.iotequ.svas;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.*;

public class Version {
    private static final String groupId="${project.groupId}";
    private static final String module="${project.artifactId}";
    private static final String version="${project.version}";
    private static final String buildTime="${project.timestamp}";

    public static String getGroupId() {
//        if (groupId==null) readVersionInfo();
        return groupId;
    }

    public static String getModule() {
//        if (groupId==null) readVersionInfo();
        return module;
    }

    public static String getVersion() {
//        if (groupId==null) readVersionInfo();
        return version;
    }

    public static String getBuildTime() {
//        if (groupId==null) readVersionInfo();
        return buildTime;
    }
/*
    private static void readVersionInfo() {
        ApplicationHome home = new ApplicationHome(Version.class);
        String path = home.getSource().getAbsolutePath();
        if (path.endsWith("\\target\\classes") || path.endsWith("/target/classes")) {
            File hd = new File(path).getParentFile();
            File[] jarFiels = hd.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        return false;
                    }
                    if (file.getName().toLowerCase().endsWith(".jar")) {
                        return true;
                    }
                    return false;
                }
            });
            if (jarFiels.length>0) path = jarFiels[0].getAbsolutePath();
            else return;
        }
        System.out.println("jar path = "+path);
        try {
            JarFile jarfile =  new JarFile(path);
            Manifest manifest = jarfile.getManifest();
            if (manifest==null) return;
            for (Enumeration<JarEntry> e = jarfile .entries(); e.hasMoreElements(); ) {
                JarEntry entry = e.nextElement();
                String name = entry.getName();
                if (name.endsWith(".jar") && name.startsWith("BOOT-INF/lib/")) {
                    JarInputStream ji = new JarInputStream(jarfile.getInputStream(entry));
                    manifest = ji.getManifest();
                    if (Objects.nonNull(manifest)) {
                        Attributes attrs = manifest.getMainAttributes();
                        if (attrs==null) return;
                        groupId = attrs.getValue("Group-Id");
                        module = attrs.getValue("Artifact-Id");
                        version = attrs.getValue("Version");
                        String dt = attrs.getValue("Build-Time");
                        if (dt!=null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                buildTime = sdf.parse(dt);
                            } catch (Exception e1) {
                                buildTime = null;
                            }
                        }
                        if (buildTime==null) buildTime=new Date();
                        else buildTime.setTime(buildTime.getTime()+8L*3600000L);
                    }
                }
            }
        } catch (Exception e) {
            return;
        }
        if (groupId==null) groupId = "top.iotequ";
    }
 */
}
