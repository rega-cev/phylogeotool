package be.kuleuven.rega.phylogeotool.trash;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("serial")
public class PhyloGeoService extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doPost(req, resp);
		System.out.println("Sequence: " + req.getParameter("sequence"));
		System.out.println("Gender: " + req.getParameter("gender"));
		System.out.println("Ethnicity: " + req.getParameter("ethnic"));
		System.out.println("Origin: " + req.getParameter("origin"));
		System.out.println("Infection: " + req.getParameter("infection"));
		System.out.println("Birth: " + req.getParameter("birth"));
		System.out.println("Risk: " + req.getParameter("risk"));
//		super.doPost(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doPost(req, resp);
		
		System.out.println("GET");
		System.out.println(req.getAttribute("sequence"));
		super.doGet(req, resp);
	}
	
	public String getPPlacedTree(ByteArrayOutputStream tar) {
	    CloseableHttpClient httpclient = HttpClientBuilder.create().setConnectionTimeToLive(20, TimeUnit.MINUTES).setDefaultSocketConfig(SocketConfig.custom().setSoKeepAlive(true).setSoReuseAddress(true).build()).build();
	    URIBuilder builder = null;
	    HttpPost httppost = null;
		try {
			builder = new URIBuilder("http://regatools.med.kuleuven.be/test/test2.php");
//			builder.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1.toString());
			httppost = new HttpPost(builder.build());
		} catch (URISyntaxException e) {
			System.err.println("Wrong URI syntax was used.");
			e.printStackTrace();
		}
//	    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

//	    File file = new File("/Users/ewout/Desktop/10408711_1014123358620567_8448991517677829292_n.jpg");
//	    File file = new File("/Users/ewout/Documents/TDRDetector/temp/test.tar.gz");

	    
//	    HttpEntity httpEntity = MultipartEntityBuilder.create().addBinaryBody("war", file, ContentType.create("image/jpeg"), file.getName()).build();
//	    HttpEntity httpEntity = MultipartEntityBuilder.create().addBinaryBody("war", file, ContentType.create("application/x-gzip"), file.getName()).build();
	    HttpEntity httpEntity = MultipartEntityBuilder.create().addBinaryBody("war", tar.toByteArray(), ContentType.create("application/x-gzip"), "pplacer.tar.gz").build();
	    
	    httppost.setEntity(httpEntity);
	    System.out.println("executing request " + httppost.getRequestLine());
	    HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    HttpEntity resEntity = response.getEntity();

//	    System.out.println("Response: " + response.getStatusLine());
	    String result = null;
	    if (resEntity != null) {
	    	try {
	    		result = EntityUtils.toString(resEntity);
	    		EntityUtils.consume(resEntity);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

	    try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return result;
	}
	
}