package business.admin.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
public class EasyOpsAPI {
	String ACCESS_KEY, SECRET_KEY;
	String URL;
	Map<String, String> headers;
	
	public EasyOpsAPI(String ACCESS_KEY, String SECRET_KEY, String URL){
		
		Map<String, String> hd = new TreeMap<String, String>();
			hd.put("host", "openapi.easyops-only.com");
			hd.put("Content-Type", "application/json");
		this.ACCESS_KEY = ACCESS_KEY;
		this.SECRET_KEY = SECRET_KEY;
		this.URL = URL;
		this.headers = hd;
	}
	
	private String genSignature(String uri,long request_time, Map<String, String> data){
		
		String body_content="";
		String url_params = "";
		String str_sign = "";
		String signature = "";

		for(Object key: data.keySet()){
			url_params = url_params + key + data.get(key.toString());
		}
		str_sign = "GET" + "\n" + uri + "\n" + url_params + "\n" + 
				this.headers.get("Content-Type") + "\n" + body_content + "\n" + 
				request_time + "\n" + this.ACCESS_KEY;
		signature = HmacSHA1Encrypt(str_sign, this.SECRET_KEY);
		return signature;
	}
	
	private String sendGet(String url, Map<String, String> data) {
        String result = null;
        String param = "";
        BufferedReader in = null;
        int timeout = 60;
        for( Object ks : data.keySet() ){
        	param += "&" + ks + "=" + data.get(ks.toString());
        }
        param = param.substring(1);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        try {
           URL realUrl = new URL(url + "?" + param);
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(timeout);
            for( Object hd : this.headers.keySet() ){
                connection.setRequestProperty(hd.toString(), this.headers.get(hd.toString()));
            }
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            StringBuffer bf = new StringBuffer();
            while ((line = in.readLine()) != null) {
                bf.append(line);
            }
            result = bf.toString();
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！["+e.getMessage()+"]");
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                System.out.println("BufferedReader close error！["+ex.getMessage()+"]");
            }
        }
        return result;
    }
	
    public static String HmacSHA1Encrypt(String encryptText, String encryptKey){
        byte[] data=encryptKey.getBytes();  
        SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
        try{
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey); 
            byte[] text = encryptText.getBytes();
            return Hex.encodeHexString(mac.doFinal(text));
        }catch(Exception ex){
            return null;
        }
    }	
	
	public String doApi(String uri, Map<String, String> data){
		long request_time = System.currentTimeMillis()/1000L;
		String signature = this.genSignature(uri, request_time, data);
		String url = this.URL + uri;
		data.put("accesskey", this.ACCESS_KEY);
		data.put("signature", signature);
		data.put("expires", request_time +"");
		return sendGet(url, data);
	}
}
