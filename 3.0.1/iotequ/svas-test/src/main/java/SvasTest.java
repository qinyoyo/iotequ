import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class SvasTest {
    static String serverUrl = "http://192.168.1.162:30080";
    static String clientId= "svas";
    static String clientSecret = "e10adc3949ba59abbe56e057f20f883e";
    static String getToken() throws Exception {
        return null;
//        String urlFmt = "%s/oauth/token?grant_type=client_credentials&client_id=%s&client_secret=%s&scope=api";
//        HttpResponse ack = HttpUtils.doPost(String.format(urlFmt, serverUrl, clientId, clientSecret));
//        JSONObject json = HttpUtils.getHttpEntity(ack);
//        String token = json.getString("access_token");
//        return token;
    }
    static JSONObject auth(String token,String template,int thresh) throws Exception {
        String urlFmt = "%s/res/svastest/auth";
        Map<String,Object> h = new HashMap<String,Object>();
        Map<String,Object> q = new HashMap<String,Object>();
        if (token!=null) h.put("Authorization",token);
        q.put("template",template);
        q.put("thresh",thresh);
        String b=null;
        HttpResponse ack = HttpUtils.doPost(String.format(urlFmt, serverUrl),h,q,b);
        JSONObject json = HttpUtils.getHttpEntity(ack);

        return json;
    }
    static void wait4Start() throws Exception {
       String  urlFmt = "%s/res/svastest/start";
       HttpResponse ack = HttpUtils.doPost(String.format(urlFmt, serverUrl));
       JSONObject json = HttpUtils.getHttpEntity(ack);
       long tm = json.getLong("sleep");
       if (tm>0) {
           System.out.println("Wait for sync test("+(tm/1000)+")s...");
           Thread.sleep(tm);
       }
    }
    static void loopAuth(String token,int dictNumber,int beginPos) {
        try {
            wait4Start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Start test...");
        int i = 0; // new Random().nextInt(1000);
        int tmException = 0;
        int tmSuccess = 0;
        int tmFound = 0;
        int tmNotFound =0;
        int tmMultiFound = 0;
        long usMatch = 0,usMinMatch=1000000000, usMaxMatch=0;
        long usUsed = 0,usMinUsed=1000000000, usMaxUsed=0;
        int count;
        while (i<dictNumber) {
            try {
                long tmBegin = new Date().getTime();
                int index = (beginPos + i) % Templates.veins.length;
                JSONObject json = auth(token, Templates.veins[index], 400);
                tmSuccess ++;
                long match = json.getLong("matchUsed");
                long used = json.getLong("usUsed");
                usMatch += match;
                usUsed += used;
                if (match>usMaxMatch) usMaxMatch = match;
                if (used>usMaxUsed)   usMaxUsed = used;
                if (match<usMinMatch) usMinMatch = match;
                if (used<usMinUsed)   usMinUsed = used;
                count = json.getInt("count");
                if (count==0) tmNotFound ++;
                else {
                    tmFound ++;
                    if (count>1) tmMultiFound++;
                }
                long tmEnd = new Date().getTime();
                if ((tmEnd - tmBegin) < 1000) Thread.sleep(1000-(tmEnd - tmBegin));
            } catch (Exception e) {
                tmException++;
            }
            i++;
        }
        String  urlFmt = "%s/res/svastest/end";
        Map<String,Object> h = null;
        Map<String,Object> q = new HashMap<String,Object>();
        q.put("usMatch",usMatch);
        q.put("usMinMatch",usMinMatch);
        q.put("usMaxMatch",usMaxMatch);
        q.put("usUsed",usUsed);
        q.put("usMinUsed",usMinUsed);
        q.put("usMaxUsed",usMaxUsed);
        q.put("tmSuccess",tmSuccess);
        q.put("tmException",tmException);
        q.put("tmFound",tmFound);
        q.put("tmNotFound",tmNotFound);
        q.put("tmMultiFound",tmMultiFound);
        try {
            String b = null;
            HttpResponse ack = HttpUtils.doPost(String.format(urlFmt, serverUrl), h, q, b);
            JSONObject json = HttpUtils.getHttpEntity(ack);
            System.out.println(String.format("insert result : rows = %d",json.getInt("rows")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(String.format("time used : match = %dus total = %dus",usMatch,usUsed));
        System.out.println(String.format("times: success = %d failed = %d found = %d not found = %d multi found = %d",
                tmSuccess,tmException,tmFound,tmNotFound, tmMultiFound));
    }
    public static void main(String args[]) {
        int count = 100 ,thread=10;
        for (int i=0;i<args.length;i++) {
            try {
                String [] ss = args[i].split("=");
                if (ss.length > 1) {
                    char ch = ss[0].toLowerCase().charAt(0);
                    if (ch=='u') serverUrl = ss[1];
                    else if (ch=='c') count = Integer.parseInt(ss[1]);
                    else if (ch=='t') thread = Integer.parseInt(ss[1]);
                }
            } catch (Exception e) {}
        }
        if (count<=0 || count>100) count = 100;
        if (thread<=0 || thread>100) thread = 100;
        if (!serverUrl.toLowerCase().startsWith("http:")) serverUrl = "http://"+serverUrl;
        System.out.println("Svas test. server = " + serverUrl + ", dict number = "+count + ", thresh number = "+thread);
        try {
            final String token = getToken();
            final int dicts = count;
            if (token!=null) System.out.println("token = "+token);
            final CountDownLatch lock = new CountDownLatch(thread);
            int dataL = Templates.veins.length / thread;
            for (int i=0;i<thread;i++) {
                final int index=i;
                final int beginPos = i * dataL;
                System.out.println("thread "+i+" running...");
                new Thread(){
                    @Override
                    public void run() {
                        loopAuth(token,dicts,beginPos);
                        lock.countDown();
                    }
                }.start();
            }
            lock.await();
            System.out.println("Svas end.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
