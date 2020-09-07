package util;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Http3RegiterInfo {
	private CloseableHttpClient httpclient;
	private HttpClientContext httpClientContext;
	private CloseableHttpResponse cl;
	private String personName;
	private String identityNo;
	private String phoneNo;
	private String personNo;
	private String token;
	private boolean found = false;
	
	public Http3RegiterInfo(CloseableHttpClient httpclient, HttpClientContext httpClientContext, String personName, String identityNo,
			String phoneNo, String personNo, String token) {
		super();
		this.httpclient = httpclient;
		this.httpClientContext = httpClientContext;
		this.personName = personName;
		this.identityNo = identityNo;
		this.phoneNo = phoneNo;
		this.personNo = personNo;
		this.token = token;
	}
	public boolean isFound() {
		return found;
	}
	public void execute() throws Exception {
		HttpPost post = new HttpPost("https://mail-mgmt.tongji.edu.cn:8081/email/regiterInfo");
		post.setConfig(Brower.getRequestConfig());
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();

		list.add(new BasicNameValuePair("personName", personName));
		list.add(new BasicNameValuePair("identityNo", identityNo));
		list.add(new BasicNameValuePair("phoneNo", phoneNo));
		list.add(new BasicNameValuePair("personNo", personNo));
		list.add(new BasicNameValuePair("token", token));

		UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
		post.setEntity(uefEntity);

		post.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		post.setHeader("Accept-Encoding", "gzip, deflate");
		post.setHeader("Accept-Language", "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4");
		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");

		this.cl = this.httpclient.execute(post, this.httpClientContext);

		if(cl.getStatusLine().getStatusCode()==200) {
			JSONObject jb = JSON.parseObject(EntityUtils.toString(this.cl.getEntity()));
			String msgCode = (String)jb.get("msgCode");
			String msgStatus = (String)jb.get("msgStatus");
			if(!"5".equals(msgStatus)) {
				System.out.println("msgStatus:"+msgStatus+"|msgCode:"+msgCode);
				found = true;
			}
			else {
				System.out.println("msgStatus:"+msgStatus+"|msgCode:"+msgCode);
			}
		}
		this.cl.close();
	}
}