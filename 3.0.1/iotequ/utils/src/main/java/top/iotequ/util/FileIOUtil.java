package top.iotequ.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileIOUtil {
    /************************
     * 将字符串写入文件,utf-8 编码
     * @param s : 字符串
     * @param file : 文件
     * @throws IOException 出错抛出异常
     */
    static public void writeToFile(String s, File file) throws IOException {
        writeToFile(s,file,"utf-8");
    }

    /************************
     * 将字符串写入文件
     * @param s : 字符串
     * @param file : 文件
     * @param fmt: 编码格式
     * @throws IOException 出错抛出异常
     */
    static public void writeToFile(String s, File file, String fmt) throws IOException {
        if (file.exists()) {
            file.delete();
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) dir.mkdirs();
        }
        PrintWriter fw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), fmt)));
        fw.write(s);
        fw.close();
    }
    /************************
     * 将字符串添加到文件，utf-8编码
     * @param s ： 字符
     * @param file : 文件
     * @throws IOException 出错抛出异常
     */
    static public void appendToFile(String s, File file) throws IOException {
        appendToFile(s, file, "utf-8");
    }
    /************************
     * 将字符串添加到文件
     * @param s ： 字符
     * @param file : 文件
     * @param fmt: 编码格式
     * @throws IOException 出错抛出异常
     */
    static public void appendToFile(String s, File file, String fmt) throws IOException {
        if (!file.exists()) {
            File dir = file.getParentFile();
            if (!dir.exists()) dir.mkdirs();
        }
        PrintWriter fw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), fmt)));
        fw.write(s);
        fw.close();
    }
    /************************
     * 将缓冲区写入文件，二进制
     * @param buf : 写入内容
     * @param file : 文件
     * @throws IOException 出错抛出异常
     */
    static public void writeToFile(byte[] buf,File file) throws IOException {
        if (file.exists()) {
            file.delete();
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) dir.mkdirs();
        }
        FileOutputStream outs = new FileOutputStream(file);
        outs.write(buf);
        outs.close();
    }

    /**
     * 从文件读入多行字符串，自动判断编码格式
     * @param file 文件
     * @return 读入字符串,\n换行
     * @throws IOException 异常
     */
    public static String readFromFile(File file) throws IOException {
        String fmt = "utf-8";
        BufferedInputStream bin = null;
        bin = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte [1024];
        int len = bin.read(buf);
        if (len==1024) fmt = getCharsetOf(buf).name();
        else if (len>0) fmt = getCharsetOf(Arrays.copyOf(buf,len)).name();
        else fmt = StandardCharsets.UTF_16.name();
        bin.close();
        return readFromFile(file,fmt);
    }

    /**
     * 从文件读入多行字符串
     * @param file 文件
     * @param fmt 编码格式
     * @return 读入字符串,\n换行
     * @throws IOException 异常
     */
    public static String readFromFile(File file,String fmt) throws IOException {
        FileInputStream s = new FileInputStream(file);
        InputStreamReader r = new InputStreamReader(s,fmt );
        BufferedReader in = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        String str;
        boolean firstLine = true;
        while ((str = in.readLine()) != null) {
            if (firstLine)
                firstLine = false;
            else
                sb.append("\n");
            sb.append(str);
        }
        return sb.toString();
    }

    public static String getFileExt(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos == -1)
            return "";
        return fileName.substring(pos + 1);
    }

    public static String getFileExt(File file) {
        return getFileExt(file.getName());
    }

    public static String removeExt(String s) {
        int index = s.lastIndexOf(".");
        if (index == -1)
            index = s.length();
        return s.substring(0, index);
    }

    public static boolean isUTF8(byte [] buf) {
        /*
        1字节：0xxxxxxx
        2字节：110xxxxx 10xxxxxx
        3字节：1110xxxx 10xxxxxx 10xxxxxx
        4字节：11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
         */
        if (buf==null || buf.length<3) return false;
        int i = 0;
        int length = buf.length;
        int v;
        while (i < length) {
            v = buf[i] & 0xff;
            if (v < 128) {
                i++;
            } else if (v >> 5 == 0b110){
                if (i+1 >= length) return true; // 最后位数不足忽略
                v = buf[i+1] & 0xff;
                if (v >> 6 != 0b10) return false;
                i+=2;
            } else if (v >>4 == 0b1110) {
                if (i+2 >= length) return true;
                v = buf[i+1] & 0xff;
                if (v >> 6 != 0b10) return false;
                v = buf[i+2] & 0xff;
                if (v >> 6 != 0b10) return false;
                i+=3;
            } else if (v >>3 == 0b11110) {
                if (i+3 >= length) return true;
                v = buf[i+1] & 0xff;
                if (v >> 6 != 0b10) return false;
                v = buf[i+2] & 0xff;
                if (v >> 6 != 0b10) return false;
                v = buf[i+3] & 0xff;
                if (v >> 6 != 0b10) return false;
                i+=4;
            } else return false;
        }
        return true;
    }
    public static boolean isGBK(byte [] buf) {
        /*
        1字节：0xxxxxxx
        2字节：1xxxxxxx 1xxxxxxx
         */
        if (buf==null || buf.length<3) return false;
        int i = 0;
        int length = buf.length;
        int v;
        while (i < length) {
            v = buf[i] & 0xff;
            if (v < 128) {
                i++;
            } else {
                if (i+1 >= length) return true; // 最后位数不足忽略
                v = buf[i+1] & 0xff;
                if (v < 128) return false;
                i+=2;
            }
        }
        return true;
    }
    public static boolean isUnicode(byte [] buf) {
        /*
        1字节：0xxxxxxx 0
        2字节：1xxxxxxx 1xxxxxxx
         */
        if (buf==null || buf.length<2) return false;
        int i = 0;
        int length = buf.length;
        int v;
        while (i < length) {
            v = buf[i] & 0xff;
            if (v < 128 ) {
                if (i+1 >= length) return true; // 最后位数不足忽略
                if (buf[i+1] !=0 ) return false;
                i+=2;
            } else {
                if (i+1 >= length) return true; // 最后位数不足忽略
                v = buf[i+1] & 0xff;
                if (v < 128) return false;
                i+=2;
            }
        }
        return true;
    }

    /** 自动判断编码格式，不能判断返回 utf-8
     * utf-16, utf-16le, unicode 为同一个
     * @param buf 缓冲字节
     * @return 编码格式
     */
    public static Charset getCharsetOf(byte [] buf) {
        if (buf==null) return null;
        if (buf.length < 2) return StandardCharsets.UTF_8;
        if (buf[0]==(byte)0xff && buf[1]==(byte)0xfe) return StandardCharsets.UTF_16;
        if (buf[0]==(byte)0xfe && buf[1]==(byte)0xff) return StandardCharsets.UTF_16BE;
        if (buf.length < 3) return StandardCharsets.UTF_8;
        if (buf[0]==(byte)0xef && buf[1]==(byte)0xbb && buf[2]==(byte)0xbf) return StandardCharsets.UTF_8;
        if (isGBK(buf)) return Charset.forName("gbk");
        if (isUTF8(buf)) return StandardCharsets.UTF_8;
        if (isUnicode(buf)) return StandardCharsets.UTF_16;
        return StandardCharsets.UTF_8;
    }
}
