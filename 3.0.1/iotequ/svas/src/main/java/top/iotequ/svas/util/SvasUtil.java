package top.iotequ.svas.util;

import java.io.*;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.system.ApplicationHome;

public class SvasUtil {
	static public Map<String,Object> fromJson(String json) {
		return new GsonBuilder()
				.registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new MapTypeAdapter())
				.registerTypeAdapter(Boolean.TYPE,new GsonBooleanTypeAdapter())
				.create()
				.fromJson(json,new TypeToken<Map<String, Object>>() {}.getType());
	}
	static public boolean isEmpty(Object o) {
		if (o==null) return true;
		else if (o instanceof String) {
			return ((String) o).trim().isEmpty();
		} else {
			return o.toString().trim().isEmpty();
		}
	}
	static public String getPath(Class<?> clazz) {
		ApplicationHome home = new ApplicationHome(clazz == null ? File.class : clazz);
		File file = home.getDir();
		if (file != null) return file.getPath();
		else return null;
	}
	public static String getHomePath() {
		Class<?> clazz = SvasUtil.class;
		if ("file".equals(clazz.getResource("").getProtocol())) {  // ide模式，使用上級目錄
			String path = getPath(clazz);
			if (path.endsWith("\\target\\classes") || path.endsWith("/target/classes")) {
				File hd = new File(path).getParentFile().getParentFile().getParentFile();
				return hd.getAbsolutePath();
			} else {
				return getPath(null);
			}
		} else {
			return getPath(null);
		}
	}

	static public String dllFile() {
        String appPath = getHomePath();
		String os = System.getProperty("os.name");  
		String dll="";
		if(os.toLowerCase().indexOf("windows")>=0){  
			if (appPath==null) appPath="C:\\iotequ\\";
			else if (!appPath.endsWith("\\")) appPath=appPath+"\\";
			dll ="svas.dll";
		}  else {
			if (appPath==null) appPath="/usr/lib/";
			else	if (!appPath.endsWith("/"))appPath=appPath+"/";
			dll="svas.so";
		}
		if (new File(appPath+dll).exists()) 	return appPath+dll;
		else return dll;
	}
	public static String readFileContent(File file) {
		BufferedReader reader = null;
		StringBuffer sbf = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempStr;
			while ((tempStr = reader.readLine()) != null) {
				sbf.append(tempStr);
			}
			reader.close();
			return sbf.toString();
		} catch (Exception e) {
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e1) {
				}
			}
		}
		return null;
	}
	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}
	private static String getMainBordId_windows() throws Exception {
		String result = "";
		File file = File.createTempFile("realhowto", ".vbs");
		file.deleteOnExit();
		FileWriter fw = new java.io.FileWriter(file);

		String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
				+ "Set colItems = objWMIService.ExecQuery _ \n"
				+ "   (\"SELECT * FROM Win32_ComputerSystemProduct\") \n"
				+ "For Each objItem in colItems \n"
				+ "    Wscript.Echo objItem.UUID \n"
				+ "    exit for  ' do the first cpu only! \n" + "Next \n";

		fw.write(vbs);
		fw.close();
		Process p = Runtime.getRuntime().exec(
				"cscript //NoLogo " + file.getPath());
		BufferedReader input = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while ((line = input.readLine()) != null) {
			result += line;
		}
		input.close();
		return result;
	}
	private static String getMainBordId_linux() throws Exception {
			String result = "";
			String maniBord_cmd = "cat /sys/class/dmi/id/product_uuid 2>&1";
			Process p;
			p = Runtime.getRuntime().exec(
					new String[]{"sh", "-c", maniBord_cmd});// 管道
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				result += line;
				break;
			}
			br.close();
			return result;
	}
	public static String getMainBordId() throws Exception {
		String os = getOSName();
		String mainBordId = "";
		if (os.startsWith("windows")) {
			mainBordId = getMainBordId_windows();
		} else if (os.startsWith("linux")) {
			mainBordId = getMainBordId_linux();
		}
		return mainBordId;
	}
	private static byte[] hexToByte(String hex,int maxlen){
		hex=hex.replace("-", "").replaceAll(" ", "");
		int m = 0, n = 0;
		int byteLen = hex.length() / 2;
		byte[] ret = new byte[Math.min(byteLen,maxlen)];
		for (int i = 0; i < ret.length; i++) {
			m = i * 2 + 1;
			n = m + 1;
			int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
			ret[i] = Byte.valueOf((byte)intVal);
		}
		return ret;
	}
	private static String  byte2hex(byte[] s, int byteLen, int spLen) {
		char [] tab = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		StringBuilder sb=new StringBuilder();
		if (byteLen>s.length)  byteLen = s.length;
		for (int i = 0; i < byteLen; i++) {
			sb.append(tab[(s[i] & 0xf0) >> 4]);
			sb.append(tab[s[i] & 0xf]);
			if (spLen>0 && i < byteLen - 1 && i%spLen == spLen - 1) sb.append('-');
		}
		return sb.toString();
	}
	private static int unsigned(byte b) {
		int u = (int)b;
		u=u&0xff;
		return u;
	}
	public static String getSetupCode() throws Exception {
		String sn = getMainBordId();
		byte[] uuid = hexToByte(sn, 16);
		byte[] key = "AoTang1996-08-02".getBytes();
		int xk = 0;
		for (int i = 0; i < 16; i++) xk = xk + unsigned(uuid[i]);
		for (int i = 0; i < 16; i++) uuid[i] = (byte) (uuid[i] ^ key[(xk + i) % 16]);
		sn = byte2hex(uuid, 16, 2);
		return sn;
	}
}
