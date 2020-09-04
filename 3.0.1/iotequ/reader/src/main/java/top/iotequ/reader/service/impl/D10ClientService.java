package top.iotequ.reader.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.svein.D10Client.D10Client;
import com.svein.sdkUtils.Constant;
import top.iotequ.framework.util.Util;


@Service
public class D10ClientService implements ApplicationRunner {

	@Autowired
	private Environment env; // 设置环境变量

	private static D10Client d10client;// D10客户端操作句柄

	private String BASEURL;// 服务器基础URL
	public final static String Callback_Auth = "common/D10/authDevice";
	public final static String Callback_Record = "common/D10/deviceRecord";
	public final static String Callback_Disconnect = "common/D10/disconnect";
	public final static String Callback_ServerAuth = "common/D10/serverAuthRequest";// 服务端验证请求

	private static final Logger logger = Util.getInfoLogger(D10ClientService.class);

	public static D10Client getD10client() {
		return d10client;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		BASEURL = env.getProperty("config.serverurl");// 配置基础URL

		if (BASEURL == null || BASEURL.isEmpty()) {

			String keyStore = env.getProperty("server.ssl.key-store");
			if (keyStore == null || keyStore.isEmpty()) {
				BASEURL = "http://localhost";
			} else {
				BASEURL = "https://localhost";
			}

			String port = env.getProperty("server.port");// 配置端口
			if (port != null && !port.isEmpty()) {
				BASEURL = BASEURL + ":" + port;
			}

			String contextpath = env.getProperty("server.servlet.context-path");// 配置路径
			if (contextpath == null || contextpath.isEmpty()) {
				contextpath = "/";
			}

			BASEURL += contextpath;
		}
		
		if (!BASEURL.endsWith("/")) {
			BASEURL += "/";
		}
		d10client = new D10Client(BASEURL); // D10客户端

		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, String> RspMap = new HashMap<String, String>();
				RspMap.put(Constant.D10_DEVICE_CONTENT_AUTH, BASEURL + Callback_Auth);
				RspMap.put(Constant.D10_DEVICE_DATA_CHANGED, BASEURL + Callback_Record);
				RspMap.put(Constant.D10_DEVICE_DISCONNECT, BASEURL + Callback_Disconnect);
				RspMap.put(Constant.D10_DEVICE_SERVERAUTH, BASEURL + Callback_ServerAuth);
				if (d10client.RegistNotify(RspMap)) {
					logger.info("RegistNotify ok");
				}else {
					logger.error("RegistNotify failed , url = {}",BASEURL);
				}
			}
		}).start();
	}
}
