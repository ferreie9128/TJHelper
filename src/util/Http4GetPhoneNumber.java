package util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Http4GetPhoneNumber {
	private CloseableHttpClient httpclient;
	private HttpClientContext httpClientContext;
	private String url;
	private CloseableHttpResponse cl;
	private String randomCode = "";
	
	public String getRandomCode() {
		return randomCode;
	}
	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}
	public Http4GetPhoneNumber(CloseableHttpClient httpclient, HttpClientContext httpClientContext,String url) {
		super();
		this.httpclient = httpclient;
		this.httpClientContext = httpClientContext;
		this.url = url;
	}

	public void execute() throws Exception{
		HttpGet get = new HttpGet(url);
		
		get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		get.setHeader("Accept-Encoding", "gzip, deflate");
		get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		
		//wait 10s to get the random code
		Thread.sleep(10000);
		
		cl = httpclient.execute(get, httpClientContext);
		
		if(cl.getStatusLine().getStatusCode()==200) {
			Document doc = Jsoup.parse(EntityUtils.toString(this.cl.getEntity()));
			//select table which class=layui-table, then find child element tbody, then tr
			Elements trs = doc.select(".layui-table>tbody>tr");
			for (Element element : trs) {
				Elements tds = element.select("td");
				if(tds!=null&&tds.size()==3) {
					String from = tds.get(0).text();
					String msg = tds.get(1).text();
					if ("同济大学".equals(from)) {
						String pattern = "(\\d){6}";
						Pattern r = Pattern.compile(pattern);
						Matcher m = r.matcher(msg);
						if (m.find()) {
							//once find the code end the loop
							randomCode = m.group(0);
							break;
						}
					}
				}
			}
		}
	}
}	
