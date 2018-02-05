package com.shuishou.retailer.member.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.SecureRandom;

import org.apache.log4j.Logger;

public class HttpsUtil {
	private final static Logger logger = Logger.getLogger("HttpsUtil");

	private static CloseableHttpClient client;

	public static HttpClient getHttpsClient() throws Exception {

		if (client != null) {
			return client;
		}
		//example from web, https://prasans.info/2014/06/making-https-call-using-apache-httpclient/
//		SSLContext sslcontext = SSLContexts.custom().useSSL().build();
//		sslcontext.init(null, new X509TrustManager[] { new HttpsTrustManager() }, new SecureRandom());
//		SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
//				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//		client = HttpClients.custom().setSSLSocketFactory(factory).build();

		//HttpClient 4.5 example exists compile wrong
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		InputStream fis = null;
	    try {
	        fis = logger.getClass().getClassLoader().getResourceAsStream("/JerryLou.keystore");
	        ks.load(fis, "lou1980".toCharArray());
	    } catch(Exception e){
	    	e.printStackTrace();
	    } finally {
	        if (fis != null) {
	            fis.close();
	        }
	    }
		SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(ks,new TrustSelfSignedStrategy()).build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
                new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        client = HttpClients.custom().setSSLSocketFactory(factory).build();
		
		
        
		return client;
	}

	public static void releaseInstance() {
		client = null;
	}
	
	public static String getJSONObjectByGet(String uriString){
//      JSONObject resultJsonObject=null;
  	String result = null;
      if ("".equals(uriString)||uriString==null) {
          return null;
      }
      HttpClient httpClient = null;
	try {
		httpClient = getHttpsClient();
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
      StringBuilder urlStringBuilder=new StringBuilder(uriString);
      StringBuilder entityStringBuilder=new StringBuilder();
      HttpGet httpGet=new HttpGet(urlStringBuilder.toString());
      BufferedReader bufferedReader=null;
      HttpResponse httpResponse=null;
      try {
          httpResponse=httpClient.execute(httpGet); 
      } catch (Exception e) {
      	logger.error("", e);
      }
      if (httpResponse == null)
      	return null;
      int statusCode=httpResponse.getStatusLine().getStatusCode();
      HttpEntity httpEntity=httpResponse.getEntity();
      if (httpEntity!=null) {
          try {
              bufferedReader=new BufferedReader(new InputStreamReader(httpEntity.getContent(), "UTF-8"), 8*1024);
              String line=null;
              while ((line=bufferedReader.readLine())!=null) {
                  entityStringBuilder.append(line+"\n");
              }
              if (statusCode==HttpStatus.SC_OK) {
              	return entityStringBuilder.toString();
//              	resultJsonObject=new JSONObject(entityStringBuilder.toString());
              } else {
              	logger.error("Http Error: URl : "+ uriString 
              			+ "\nhttpcode : "+ httpResponse.getStatusLine().getStatusCode()
              			+ "\nresponse message : " + entityStringBuilder.toString());
              }
              
          } catch (Exception e) {
          	logger.error("", e);
          }
      }
      
      return null;
  }
  
  public static String getJSONObjectByPost(String path,Map<String, String> params) {
  	return getJSONObjectByPost(path, params, "UTF-8");
  }
  public static String getJSONObjectByPost(String path,Map<String, String> paramsHashMap, String encoding) {
  	String result = null;
//      JSONObject resultJsonObject = null;
      List<NameValuePair> nameValuePairArrayList = new ArrayList<NameValuePair>();
      if (paramsHashMap != null && !paramsHashMap.isEmpty()) {
          for (Map.Entry<String, String> entry : paramsHashMap.entrySet()) {
              nameValuePairArrayList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
          }
      }
        
      UrlEncodedFormEntity entity = null;
      try {
          entity = new UrlEncodedFormEntity(nameValuePairArrayList, encoding);
          HttpPost httpPost = new HttpPost(path);
          httpPost.setEntity(entity);
          HttpClient httpClient = getHttpsClient();
          HttpResponse httpResponse = httpClient.execute(httpPost);
          HttpEntity httpEntity = httpResponse.getEntity();
          if (httpEntity != null) {
              try {
                  BufferedReader bufferedReader = new BufferedReader(  
                  new InputStreamReader(httpEntity.getContent(),"UTF-8"), 8 * 1024);
                  StringBuilder entityStringBuilder = new StringBuilder();
                  String line = null;
                  while ((line = bufferedReader.readLine()) != null) {
                      entityStringBuilder.append(line + "\n");
                  }
                  if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                  	resultJsonObject = new JSONObject(entityStringBuilder.toString());
                  	return entityStringBuilder.toString();
                  } else {
                  	logger.error("Http Error: URl : "+ path + "\nparam : "+ paramsHashMap 
                  			+ "\nhttpcode : "+ httpResponse.getStatusLine().getStatusCode()
                  			+ "\nresponse message : " + entityStringBuilder.toString());
                  }
                  
              } catch (Exception e) {
                  logger.error("", e);
              }
          }
          
      } catch (Exception e) {
          logger.error("", e);
      }
      return null;
  } 
  
  public static String getJSONObjectByPostSerialize(String path,SerializableEntity entity, String encoding) {
  	String result = null;
        
      try {
          HttpPost httpPost = new HttpPost(path);
          httpPost.setEntity(entity);
          HttpClient httpClient = getHttpsClient();
          HttpResponse httpResponse = httpClient.execute(httpPost);
          HttpEntity httpEntity = httpResponse.getEntity();
          if (httpEntity != null) {
              try {
                  BufferedReader bufferedReader = new BufferedReader(  
                  new InputStreamReader(httpEntity.getContent(),"UTF-8"), 8 * 1024);
                  StringBuilder entityStringBuilder = new StringBuilder();
                  String line = null;
                  while ((line = bufferedReader.readLine()) != null) {
                      entityStringBuilder.append(line + "\n");
                  }
                  if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                  	resultJsonObject = new JSONObject(entityStringBuilder.toString());
                  	return entityStringBuilder.toString();
                  } else {
                  	logger.error("Http Error: URl : "+ path + "\nentity : "+ entity 
                  			+ "\nhttpcode : "+ httpResponse.getStatusLine().getStatusCode()
                  			+ "\nresponse message : " + entityStringBuilder.toString());
                  }
                  
              } catch (Exception e) {
                  logger.error("", e);
              }
          }
          
      } catch (Exception e) {
          logger.error("", e);
      }
      return null;
  } 
}
