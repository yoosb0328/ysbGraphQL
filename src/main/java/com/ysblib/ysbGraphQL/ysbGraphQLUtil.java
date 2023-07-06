package com.ysblib.ysbGraphQL;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysblib.ysbGraphQLException.*;
public class ysbGraphQLUtil {
	QueryStringFactory queries = new QueryStringFactory();
	
	/**
	  * @throws Exception 
	 * @Method Name : getMapFromJsonObject
	  * @작성일 : 2022. 8. 11.
	  * @작성자 : yoosb
	  * @변경이력 : 2022.10.06 예외처리  
	  * @Method 설명 : Convert JSONObject to LinkedHashMap<String, Object> and return.
	  */
	public LinkedHashMap<String, Object> parseJSONObjToMap(JSONObject JSONinput) throws Exception{
    	LinkedHashMap<String, Object> map = null;
        try {
            map = new ObjectMapper().readValue(JSONinput.toJSONString(), LinkedHashMap.class);
            //System.out.println(map);
            //System.out.println(map.toString());
		} catch (Exception e) {throw e;} 
        return map;
    }
	
	/**
	  * @Method Name : getJsonObjetFromMap
	  * @작성일 : 2022. 8. 11.
	  * @작성자 : yoosb
	  * @변경이력 : 2022.10.06 예외처리  
	  * @Method 설명 : Convert Map<String, Object> to JSONObject and return;
	  */
	public JSONObject parseMapToJSONObj(Map<String, Object> map) throws Exception {		    
	    JSONObject json = new JSONObject();
	    String key = "";
	    Object value = null;
	    
	    try {
	    	for(Map.Entry<String, Object> entry : map.entrySet()) {
		        key = entry.getKey();
		        value = entry.getValue();
		        json.put(key, value);
		    }
		} catch (Exception e) {throw e;}	    
	    
	    return json;
	}
	
	/**
	  * @Method Name : getJsonObjetFromEventMap
	  * @작성일 : 2022. 8. 11.
	  * @작성자 : yoosb
	  * @변경이력 : 2022.10.06 예외처리  
	  * @Method 설명 : Convert Map<String, AttributeValue> to JSONObject and return.
	  */
	public JSONObject parseEventMapToJSONObj(Map<String, AttributeValue> eventMap) throws Exception {	    
	    JSONObject json = new JSONObject();
	    String key = "";
	    Object value = null;
	    
	    try {
	    	for(Entry<String, AttributeValue> entry : eventMap.entrySet()){
		        key = entry.getKey();
		        value = entry.getValue();
		        json.put(key, value);
		    }
		} catch (Exception e) {throw e;}
	    return json;
	}
	
	
    
    
    /**
      * @throws Exception 
     * @Method Name : CreateItemGraphQLClient
      * @작성일 : 2022. 8. 11.
      * @작성자 : yoosb
      * @변경이력 : 2022.10.06 예외처리  / 2022.11.7 
      * @Method 설명 : Request GraphQL Create Mutation Query to APIURL
      */
    public void CreateItemGraphQLClient(JSONObject JSONinput, String __typename, String APIURL, String APIKEY) throws Exception{
    		try {
    			String query = queries.mutationCreate(parseJSONObjToMap(JSONinput), __typename);
    			//System.out.println(query);
    			HttpResponse response = httpPostConnect(query, APIURL, APIKEY);
	    		
	    		if(response.getStatusLine().getStatusCode() != 200) {
	    			//System.out.println(response);
	    			throw new Exception("[ysbGraphQLUtil.CreateItemGraphQLClient]\nHttpResponse Error : "+response.getStatusLine().getReasonPhrase()+"\n ErrorCode: "+response.getStatusLine().getStatusCode());
	    		}else {
	    			ResponseHandler<String> handler = new BasicResponseHandler();				
	    			String sResponse = handler.handleResponse(response);				
	    			JSONParser parser = new JSONParser();
	    			Object oResponse = parser.parse(sResponse);
	    			JSONObject responseJSON = (JSONObject) oResponse;
	    			if(responseJSON.containsKey("errors")) {
	    				Object temp = responseJSON.get("errors");
	    				JSONArray errors = (JSONArray) temp;
	    				JSONObject errorJSON = (JSONObject) errors.get(0);
	    				String errorMessage = errorJSON.get("message").toString();
	    				//System.out.println(responseJSON.toJSONString());
		    			throw new ResponseErrorException(errorMessage);
	    			}
	    			//System.out.println(responseJSON.toJSONString());
	    				    		}
			} catch (Exception e) {throw e;}
	 }
    
    /**
     * @throws Exception 
    * @Method Name : CreateItemGraphQLClient
     * @작성일 : 2022. 11. 11.
     * @작성자 : yoosb
     * @변경이력 : 
     * @Method 설명 : Request GraphQL Create Mutation Query to APIURL
     */
   public void CreateItemGraphQLClient2(JSONObject JSONinput, String __typename, String APIURL, String APIKEY) throws Exception{
   		try {
   			String query = queries.mutationCreateByJSON(JSONinput, __typename);
   			//System.out.println(query);
   			HttpResponse response = httpPostConnect(query, APIURL, APIKEY);
	    		
	    		if(response.getStatusLine().getStatusCode() != 200) {
	    			//System.out.println(response);
	    			throw new Exception("[ysbGraphQLUtil.CreateItemGraphQLClient]\nHttpResponse Error : "+response.getStatusLine().getReasonPhrase()+"\n ErrorCode: "+response.getStatusLine().getStatusCode());
	    		}else {
	    			ResponseHandler<String> handler = new BasicResponseHandler();				
	    			String sResponse = handler.handleResponse(response);				
	    			JSONParser parser = new JSONParser();
	    			Object oResponse = parser.parse(sResponse);
	    			JSONObject responseJSON = (JSONObject) oResponse;
	    			if(responseJSON.containsKey("errors")) {
	    				Object temp = responseJSON.get("errors");
	    				JSONArray errors = (JSONArray) temp;
	    				JSONObject errorJSON = (JSONObject) errors.get(0);
	    				String errorMessage = errorJSON.get("message").toString();
	    				//System.out.println(responseJSON.toJSONString());
		    			throw new ResponseErrorException(errorMessage);
	    			}
	    			//System.out.println(responseJSON.toJSONString());
	    				    		}
			} catch (Exception e) {throw e;}
	 }
    /**
      * @throws Exception 
     * @Method Name : UpdateItemGraphQLClient
      * @작성일 : 2022. 8. 11.
      * @작성자 : yoosb
      * @변경이력 : 2022.10.06 예외처리  
      * @Method 설명 : Request GraphQL Update Mutation Query to APIURL
      */
    public void UpdateItemGraphQLClient(JSONObject JSONinput, String __typename, String APIURL, String APIKEY) throws Exception {
    		try {
    			String query = queries.mutationUpdate(parseJSONObjToMap(JSONinput), __typename);
    			//System.out.println(query);
    			HttpResponse response = httpPostConnect(query, APIURL, APIKEY);
    			if(response.getStatusLine().getStatusCode() != 200) {
	    			throw new Exception("[ysbGraphQLUtil.UpdateItemGraphQLClient]\\\nHttpResponse Error : "+response.getStatusLine().getReasonPhrase()+"\\\n ErrorCode: "+response.getStatusLine().getStatusCode());
	    		}else {
	    			ResponseHandler<String> handler = new BasicResponseHandler();				
	    			String body = handler.handleResponse(response);				
	    			//System.out.println(body);
	    		}
			} catch (Exception e) {throw e;}
	 }
    
    /**
     * @throws Exception 
    * @Method Name : QueryListGraphQLClient
     * @작성일 : 2022. 10. 01.
     * @작성자 : yoosb
     * @변경이력 : 2022.10.06 예외처리 / 2022.10.24 items에서 _deleted = true인 경우는 제거함. * _deleted devicetoken이 많을 수록 속도 느려질 수 있음. devicetoken TTL 짧게 설정할필요 있음. 
     * @Method 설명 : equal 조건의 filter로 List query를 요청합니다. 검색 목표 item이 1개라는 가정하에 사용. 
     */
    public JSONObject QueryListGraphQLClient(JSONObject filterParams, List<String> itemsList, String __typename, String APIURL, String APIKEY) throws Exception{
		JSONObject result = new JSONObject();
		
    	try {
    		String query = queries.queryListEqual(parseJSONObjToMap(filterParams), itemsList, __typename);    		
    		HttpResponse response = httpPostConnect(query, APIURL, APIKEY);
    		
    		if(response.getStatusLine().getStatusCode() == 200) {				
    			ResponseHandler<String> handler = new BasicResponseHandler();				
    			String body = handler.handleResponse(response);
    			//System.out.println("[ysbGraphQLUtil.QueryListGraphQLClient] query response body : \n" + body);
    			JSONParser parser = new JSONParser();        
    			JSONObject obj = (JSONObject) parser.parse(body);
    			JSONObject jsonObj = (JSONObject) obj;
    			JSONObject data = (JSONObject)jsonObj.get("data");
    			JSONObject list = (JSONObject) data.get("list"+__typename);
    			JSONArray itemsArray = (JSONArray)list.get("items");
    			//JSONObject items = (itemsArray.size() == 0) ? new JSONObject() : (JSONObject) itemsArray.get(0);
    			JSONArray resultArray = new JSONArray();
    			JSONObject item;    			
    			for(int i=0; i<itemsArray.size(); i++) {
    				item = (JSONObject) itemsArray.get(i);   				
    				//if((boolean)item.get("_deleted") == true) itemsArray.remove(i);
    				//if(item.get("_deleted") == null) result.put("deviceToken", item.get("deviceToken"));
    				if(item.get("_deleted") == null) {
    					resultArray.add(itemsArray.get(i));
    				}
    			}
    			//System.out.println("[ysbGraphQLUtil.QueryListGraphQLClient] : List Query resultArray Size : " + resultArray.size());
    			JSONObject items = (resultArray.size() == 0) ? new JSONObject() : (JSONObject) resultArray.get(0);

				result = items;
    		} else {				
    			throw new Exception("[ysbGraphQLUtil.QueryListGraphQLClient]\\\nHttpResponse Error : "+response.getStatusLine().getReasonPhrase()+"\\\n ErrorCode: "+response.getStatusLine().getStatusCode());
    		}    		
		} catch (Exception e) {throw e;}
    	
		return result;
    }
    
    /**
     * @throws Exception 
    * @Method Name : QuerGetGraphQLClient
     * @작성일 : 2022. 10. 14.
     * @작성자 : yoosb
     * @변경이력 : 2022.10.06 예외처리  
     * @Method 설명 : DataStore item의 id를 조건으로 하여 Get query를 요청합니다.
     */
    public JSONObject QuerGetGraphQLClient(String id, List<String> itemsList, String __typename, String APIURL, String APIKEY) throws Exception{
		JSONObject result = new JSONObject();
		//System.out.println("QuerGetGraphQLClient TEST@@@");
    	try {
    		String query = queries.queryGetById(id, itemsList, __typename);    		
    		HttpResponse response = httpPostConnect(query, APIURL, APIKEY);
    		
    		if(response.getStatusLine().getStatusCode() == 200) {				
    			ResponseHandler<String> handler = new BasicResponseHandler();				
    			String body = handler.handleResponse(response);
				//System.out.println("QuerGetGraphQLClient : \n" + body);

    			JSONParser parser = new JSONParser();        
    			JSONObject obj = (JSONObject) parser.parse(body);
    			JSONObject jsonObj = (JSONObject) obj;
    			JSONObject data = (JSONObject)jsonObj.get("data");
    			JSONObject get = (JSONObject) data.get("get"+__typename);
    			//JSONArray itemsArray = (JSONArray)list.get("items");
    			//JSONObject items = (get.size() == 0) ? new JSONObject() : (JSONObject) get.get(0);
				result = get;
    		} else {				
    			throw new Exception("[ysbGraphQLUtil.QuerGetGraphQLClient]\\\nHttpResponse Error : "+response.getStatusLine().getReasonPhrase()+"\\\n ErrorCode: "+response.getStatusLine().getStatusCode());
    		}    		
		} catch (Exception e) {throw e;}
    	
		return result;
    }
    
    /**
     * @throws Exception 
    * @Method Name : QueryListGraphQLClient
     * @작성일 : 2022. 10. 06.
     * @작성자 : yoosb
     * @변경이력 : 
     * @Method 설명 : http post 방식으로 query request를 실행하고 response를 return 합니다.
     */
    public HttpResponse httpPostConnect(String query, String APIURL, String APIKEY) throws Exception{
    	int timeout = 5; // timeout 설정 단위 시간. 
    	HttpResponse response = null;
    	try {
    		RequestConfig requestConfig = RequestConfig.custom()
	    	        .setSocketTimeout(timeout * 1000)//Connection은 되었으나 I/O 작업이 길어지거나 요청이 처리되지 못하는 경우. 
	    	        .setConnectTimeout(timeout * 1000)//ConnectinTimepout 서버의 장애로 connection이 맺어지지 않음.
	    	        .setConnectionRequestTimeout(timeout * 1000)//Connection Pool에서 커넥션을 꺼내오는 시간. 
	    	        .build();
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost postRequest = new HttpPost(APIURL);
			postRequest.setHeader("Content-Type", "application/json");
			postRequest.setHeader("X-API-KEY", APIKEY);
			postRequest.setEntity(new StringEntity(query));
			postRequest.setConfig(requestConfig);
			
			response = client.execute(postRequest);
		} catch (Exception e) {throw e;}

		return response;
    }
}//class
