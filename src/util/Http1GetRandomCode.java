package util;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class Http1GetRandomCode {
	private CloseableHttpClient httpclient;
	private HttpClientContext httpClientContext;
	private CloseableHttpResponse cl;
	private String phoneNo;
	
	public Http1GetRandomCode(CloseableHttpClient httpclient, HttpClientContext httpClientContext, String phoneNo) {
		super();
		this.httpclient = httpclient;
		this.httpClientContext = httpClientContext;
		this.phoneNo = phoneNo;
	}
	public void execute() throws Exception{
		HttpPost post = new HttpPost("https://mail-mgmt.tongji.edu.cn:8081/email/getRandomCode?phoneNo="+phoneNo);
		//HttpPost post = new HttpPost("https://www.baidu.com");
		post.setConfig(Brower.getRequestConfig());
		
		post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		post.setHeader("Accept-Encoding", "gzip, deflate");
		post.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
		post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");

	    cl = httpclient.execute(post, this.httpClientContext);
	    
	    if(cl.getStatusLine().getStatusCode()==200) {
	    	System.out.println("random code send successfully");
	    }
	    else {
	    	System.out.println("fail to send random code:"+EntityUtils.toString(this.cl.getEntity()));
	    }
	    EntityUtils.toString(this.cl.getEntity());
	    
	}
}
