package util;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Http2IdentificationPhone {
	private CloseableHttpClient httpclient;
	private HttpClientContext httpClientContext;
	private CloseableHttpResponse cl;
	private String phoneNo;
	private String code;
	private String sno;
	private String token;
	public Http2IdentificationPhone(CloseableHttpClient httpclient, HttpClientContext httpClientContext, String phoneNo,
			String code, String sno) {
		super();
		this.httpclient = httpclient;
		this.httpClientContext = httpClientContext;
		this.phoneNo = phoneNo;
		this.code = code;
		this.sno = sno;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	private String MD5it(String pre) {
		String str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(pre.getBytes());
			str = new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public void execute()throws Exception {
		HttpGet get = new HttpGet("https://mail-mgmt.tongji.edu.cn:8081/email/identificationPhone?phoneNo="+phoneNo+"&code="+code+"&sno="+sno);
		
		get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		get.setHeader("Accept-Encoding", "gzip, deflate");
		get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");

		cl = httpclient.execute(get, httpClientContext);
		
		if(cl.getStatusLine().getStatusCode()==200) {
			JSONObject jb = JSON.parseObject(EntityUtils.toString(this.cl.getEntity()));
			String msgStatus = (String)jb.get("msgStatus");
			String msgCode = (String)jb.get("msgCode");
			if("3".equals(msgStatus)) {
				token = MD5it(msgCode);
			}
			else {
				System.out.println("msgStatus:"+msgStatus+"|msgCode:"+msgCode);
			}
		}
	}
}
