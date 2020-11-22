package com.dragueo.http;

import java.io.File;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.dragueo.Prop;

public class Http {
	private static Properties prop = Prop.loadProp();
	private static Logger log = Logger.getLogger(Http.class);

	SSLConnectionSocketFactory sslsf = null;

	public Http() {
	}
	
	public int sendFile(File file, String code) {		
		String url = prop.getProperty("http.url");
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
				
		url = url + "?code=" + code;
		if("true".equals(Prop.loadProp().getProperty("checkTest"))) {
			url = url + "&test=true";
		}
		HttpPost request = new HttpPost(url);

        request.addHeader("Authorization", "Basic " + prop.getProperty("http.auth", ""));
		request.setHeader("User-Agent", "dragtree");
		request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		CloseableHttpResponse response = null; 
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
			log.error("Http sendFile exception: " + e.toString());
			log.error("Http sendFile exception: " + e.toString());		
			resp = -1;
		} finally {
			response = null;
		}
       
        return resp;
	}
	
	public int sendCommand(String string_query, String code) {	
		String url = prop.getProperty("http.url");
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		url = url + "?" + string_query + "&code=" + code;
		
		if("true".equals(Prop.loadProp().getProperty("checkTest"))) {
			url = url + "&test=true";
		}

		HttpGet request = new HttpGet(url);
        
        request.addHeader("Authorization", "Basic " + prop.getProperty("http.auth", ""));
		request.setHeader("User-Agent", "dragtree");
		request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		CloseableHttpResponse response = null; 
        int resp = 0;
        try {
        	if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) { 
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

			response.close();
		} catch (Exception e) {
			request.abort();
			log.error("Http sendCommand exception: " + e.toString(), e);		
			resp = -2;
		} finally {
			response = null;
		}

		return resp;
	}
}
