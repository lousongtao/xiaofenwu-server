package com.shuishou.retailer.member.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

public class HttpUtil {

	private final static Logger logger = Logger.getLogger("HttpUtil");
	
	public static HttpClient getHttpClient(){
		HttpParams mHttpParams=new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(mHttpParams, 20*1000);
        HttpConnectionParams.setSoTimeout(mHttpParams, 20*1000);
        HttpConnectionParams.setSocketBufferSize(mHttpParams, 8*1024);
        HttpClientParams.setRedirecting(mHttpParams, true);
          
        HttpClient httpClient=new DefaultHttpClient(mHttpParams);
        return httpClient;
	}
	
    public static String getJSONObjectByGet(String uriString){
//        JSONObject resultJsonObject=null;
    	String result = null;
        if ("".equals(uriString)||uriString==null) {
            return null;
        }
        HttpClient httpClient=getHttpClient();
        StringBuilder urlStringBuilder=new StringBuilder(uriString);
        StringBuilder entityStringBuilder=new StringBuilder();
        HttpGet httpGet=new HttpGet(urlStringBuilder.toString());
        BufferedReader bufferedReader=null;
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpGet); 
        } catch (Exception e) {
        	logger.error(e);
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
//                	resultJsonObject=new JSONObject(entityStringBuilder.toString());
                } else {
                	logger.error("Http Error: URl : "+ uriString 
                			+ "\nhttpcode : "+ httpResponse.getStatusLine().getStatusCode()
                			+ "\nresponse message : " + entityStringBuilder.toString());
                }
                
            } catch (Exception e) {
            	logger.error(e);
            }
        }
        
        return null;
    }
    
    public static String getJSONObjectByPost(String path,Map<String, String> params) {
    	return getJSONObjectByPost(path, params, "UTF-8");
    }
    public static String getJSONObjectByPost(String path,Map<String, String> paramsHashMap, String encoding) {
    	String result = null;
//        JSONObject resultJsonObject = null;
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
            HttpClient httpClient = getHttpClient();
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
//                    	resultJsonObject = new JSONObject(entityStringBuilder.toString());
                    	return entityStringBuilder.toString();
                    } else {
                    	logger.error("Http Error: URl : "+ path + "\nparam : "+ paramsHashMap 
                    			+ "\nhttpcode : "+ httpResponse.getStatusLine().getStatusCode()
                    			+ "\nresponse message : " + entityStringBuilder.toString());
                    }
                    
                } catch (Exception e) {
                    logger.error(e);
                }
            }
            
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    } 
    
    public static String getJSONObjectByPostSerialize(String path,SerializableEntity entity, String encoding) {
    	String result = null;
          
        try {
            HttpPost httpPost = new HttpPost(path);
            httpPost.setEntity(entity);
            HttpClient httpClient = getHttpClient();
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
//                    	resultJsonObject = new JSONObject(entityStringBuilder.toString());
                    	return entityStringBuilder.toString();
                    } else {
                    	logger.error("Http Error: URl : "+ path + "\nentity : "+ entity 
                    			+ "\nhttpcode : "+ httpResponse.getStatusLine().getStatusCode()
                    			+ "\nresponse message : " + entityStringBuilder.toString());
                    }
                    
                } catch (Exception e) {
                    logger.error(e);
                }
            }
            
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    } 
    
	
}
