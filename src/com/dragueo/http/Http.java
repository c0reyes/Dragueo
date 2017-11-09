package com.dragueo.http;

import java.io.File;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.log4j.Logger;

import com.dragueo.Prop;

public class Http {
	private static HttpParams params = null;
	private static SchemeRegistry schemeRegistry = null;
	private static ClientConnectionManager cm = null;
	private static ConnPerRouteBean connPerRoute = null;
	private static Properties prop = new Properties();
	private static Logger log = Logger.getLogger(Http.class);
	
	public Http() {
		prop = Prop.loadProp();

		if(params == null) {
			params = new BasicHttpParams();
			ConnManagerParams.setMaxTotalConnections(params, Integer.parseInt(prop.getProperty("max", "2"))); /* Max conexion */
        	HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        	HttpProtocolParams.setUseExpectContinue(params, true);
		}
		if(connPerRoute == null) {
			ConnPerRouteBean connPerRoute = new ConnPerRouteBean(Integer.parseInt(prop.getProperty("maxroute", "20")));
			ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
			ConnManagerParams.setTimeout(params, Integer.parseInt(prop.getProperty("managertimeout", "2")));
		}
		if(schemeRegistry == null) {
			schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme(prop.getProperty("proto", "http"), PlainSocketFactory.getSocketFactory(), Integer.parseInt(prop.getProperty("port", "80"))));
		}
		if(cm == null) {
			cm = new ThreadSafeClientConnManager(params, schemeRegistry); 
		}
	}
	
	public void shutdown() {
		cm.shutdown();
	}
	
	public void cleanPool() {
		cm = null;
	}

	public int sendFile(File file, String code) {
		HttpClient httpclient = new DefaultHttpClient(cm, params);
		httpclient.getParams().setParameter("http.socket.timeout", new Integer(Integer.parseInt(prop.getProperty("httptimeout", "2000"))));
		
		String url = prop.getProperty("url","http://www.dragueo.com/dragtree/putTimeV2.php");
		url = url + "?code=" + code;
		if("true".equals(Prop.loadProp().getProperty("checkTest"))) {
			url = url + "&test=true";
		}
		HttpPost request = new HttpPost(url);
		
		request.setHeader("User-Agent", "dragtree");
		request.setHeader("Accept", 
	             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        HttpResponse response = null; 
        int resp = 0;

        FileEntity entity = new FileEntity(file, "text/plain");
		request.setEntity(entity);
        
        try {
        	if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) 
        		log.info("Executing request: " + request.getRequestLine());
				
			response = httpclient.execute(request);
			
			if(response.getStatusLine().getStatusCode() != 200) {
				log.info("response: " + response.getStatusLine());
			}
			
			resp = response.getStatusLine().getStatusCode();
			
			if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null) {
					log.info("Response content length: " + resEntity.getContentLength());
					log.info("Content: " + resEntity.getContent().toString());
				}
			}

			response.getEntity().consumeContent();
        } catch (Exception e) {
			request.abort();
			log.info("Http sendFile exception: " + e.toString());
			log.info("Http sendFile exception: " + e.toString());		
			resp = -1;
		} finally {
			response = null;
		}

        cm.closeExpiredConnections();        
        return resp;
	}
	
	public int sendCommand(String string_query, String code) {	
		HttpClient httpclient = new DefaultHttpClient(cm, params);
		httpclient.getParams().setParameter("http.socket.timeout", new Integer(Integer.parseInt(prop.getProperty("httptimeout", "2000"))));
		
		String url = prop.getProperty("url");
		url = url + "?" + string_query + "&code=" + code;
		
		if("true".equals(Prop.loadProp().getProperty("checkTest"))) {
			url = url + "&test=true";
		}
		HttpGet request = new HttpGet(url);
		
		request.setHeader("User-Agent", "dragtree");
		request.setHeader("Accept", 
	             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        HttpResponse response = null; 
        int resp = 0;
        try {
        	if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) { 
        		log.info("Executing request: " + request.getRequestLine());
				log.info("Content request: " + string_query);
        	}
				
			response = httpclient.execute(request);
			
			if(response.getStatusLine().getStatusCode() != 200) {
				log.info("response: " + response.getStatusLine());
			}
			
			resp = response.getStatusLine().getStatusCode();
			
			if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null) {
					log.info("Response content length: " + resEntity.getContentLength());
					log.info("Content: " + resEntity.getContent().toString());
				}
			}

			response.getEntity().consumeContent();
		} catch (Exception e) {
			request.abort();
			log.info("Http sendCommand exception: " + e.toString());		
			resp = -2;
		} finally {
			response = null;
		}

        cm.closeExpiredConnections();
		return resp;
	}
}
