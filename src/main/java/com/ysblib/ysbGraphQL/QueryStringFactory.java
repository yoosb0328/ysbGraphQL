package com.ysblib.ysbGraphQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class QueryStringFactory {
	final static String UpdateMetaFields = "_lastChangedAt, createdAt, updatedAt";
	final static String CreateMetaFields = "_lastChangedAt, createdAt, updatedAt, _version, id";
	
	/**
	  * @Method Name : mutationCreate
	  * @작성일 : 2022. 8. 11.
	  * @작성자 : yoosb
	  * @변경이력 : 2022.10.06 예외처리  
	  * @Method 설명 : Return GraphQL Create Mutation Query.
	  */
	String mutationCreate(LinkedHashMap<String, Object> params, String __typename) throws Exception {
	JSONParser parser = new JSONParser();
   	String query = "{\"query\":\"mutation{create"+__typename+"(input: {";
   	
		try {
			Set<String> keySet = params.keySet();
	    	Iterator<String> iter = keySet.iterator();
	    	int MapSize = params.size();
			int count = 1;
			String keys = "";
			System.out.println("params: " +params);
	    	while(iter.hasNext()) {    		
	    		String key = ((String)iter.next());
	    		Object value = params.get(key);
	    		if (count < MapSize) {
	    			if(value instanceof String) {
	    				//System.out.println("String : " + value.getClass());
	        			value = "\\\""+value+"\\\"";
	        			query += key + ": " + value + ", ";
	        		}
	    			else {
	    				//System.out.println("else : " + value.getClass());
	        			query += key + ": " + value + ", ";
	        		}
	    		} else if(count == MapSize) {
	    			if(value instanceof String) {
	        			value = "\\\""+value+"\\\"";
	        			query += key + ": " + value;
	        		} 
	    			else {
	        			query += key + ": " + value;
	        		}
	    		}   		
	    		keys += key + ", ";
	    		count++;
	    	}
	    	
			query += "}){ "+keys+CreateMetaFields+"}}\\n\"}";
			//query += "}){ id }}\\n\"}";

		} catch (Exception e) {throw e;}
		
		return query;
   }
	/**
	  * @Method Name : mutationCreateByJSON
	  * @작성일 : 2022. 11. 11.
	  * @작성자 : yoosb
	  * @변경이력 :   
	  * @Method 설명 : Return GraphQL Create Mutation Query. JSONObject를 parameter로 받습니다.
	  */
	String mutationCreateByJSON(JSONObject params, String __typename) throws Exception {
	   	String query = "{\"query\":\"mutation{create"+__typename+"(input: {";
	   		System.out.println("mutationCreateByJSON!!");
	   		//System.out.println(params.getClass());
	   		//System.out.println(params);

			try {
				Set<String> keySet = params.keySet();
		    	Iterator<String> iter = keySet.iterator();
		    	int paramJsonSize = params.size();
				int count = 1;
				String keys = "";
				//System.out.println("listHasrshTurn Class : "  + params.get("listHarshTurn").getClass());
		    	while(iter.hasNext()) {    		
		    		String key = ((String)iter.next());
		    		Object value = params.get(key);
		    		if (count < paramJsonSize) {
		    			if(value instanceof String) {
		        			value = "\\\""+value+"\\\"";
		        			query += key + ": " + value + ", ";
		        			keys += key + ", ";
		        		}
		    			else if(value instanceof JSONArray) {
		    				query += key + ": [";
		    				keys += key + "{";

		    				JSONArray array = (JSONArray) value;
//		    				System.out.println("JSONArray key : " + key + " JSONArray size : " + array.size());
//		    				System.out.println("JSONArray : " + array);
		    				for(int i=0; i<array.size(); i++) {
		    					query += "{";
		    					JSONObject json = (JSONObject) array.get(i);
//		    					System.out.println("JSONArray for loop index : " + i);
//	    						System.out.println("JSONArray.get(i) JSONObject : " + json.toJSONString());
	    						
		    					Set<String> jsonKeySet = json.keySet();
		    					int jsonKeySetSize = jsonKeySet.size();
		    					int jsonKeySetCount = 1;
		    					for(String jKeys : jsonKeySet) {
		    						if(jsonKeySetCount < jsonKeySetSize) keys += jKeys + ", ";
		    						else if(jsonKeySetCount == jsonKeySetSize) keys += jKeys;
		    					}
		    					Iterator<String> jsonIter = jsonKeySet.iterator();
		    					int jSize = json.size();
		    					int jCount = 1;
		    					while(jsonIter.hasNext()) {
		    						
		    						if(jCount > jSize) break;
		    						String jKey = ((String)jsonIter.next());
			    					Object jValue = json.get(jKey);
			    					if(jCount < jSize) {
			    						if(jValue instanceof String) {
			    							jValue = "\\\""+jValue+"\\\"";
			    							query += jKey + ": " + jValue + ", ";
			    						} else {
			    							query += jKey + ": " + jValue + ", ";
			    						}		    						
			    					} else if(jCount == jSize) {
			    						if(jValue instanceof String) {
			    							jValue = "\\\""+jValue+"\\\"";
			    							query += jKey + ": " + jValue;
			    						} else {
			    							query += jKey + ": " + jValue;
			    						}		    						
			    					}	    					
			    					jCount++;
		    					}
		    					if(i==array.size()-1) query += "}" ;
		    					else  query += "}, ";
		    				}
		    				query += "], ";
		    				keys += "}, ";
		    			}
		    			else {
		        			query += key + ": " + value + ", ";
		        			keys += key + ", ";
		        		}
		    		}//if if (count < paramJsonSize)
		    		else if(count == paramJsonSize) {
		    			if(value instanceof String) {
		        			value = "\\\""+value+"\\\"";
		        			query += key + ": " + value;
		        			keys += key + ", ";
		        		}
		    			else if(value instanceof JSONArray) {
		    				query += key + ": [";
		    				keys += key + "{";
		    				
		    				JSONArray array = (JSONArray) value;
		    				for(int i=0; i<array.size(); i++) {
		    					JSONObject json = (JSONObject) array.get(i);
		    					Set<String> jsonKeySet = json.keySet();
		    					int jsonKeySetSize = jsonKeySet.size();
		    					int jsonKeySetCount = 1;
		    					for(String jKeys : jsonKeySet) {
		    						if(jsonKeySetCount < jsonKeySetSize) keys += jKeys + ", ";
		    						else if(jsonKeySetCount == jsonKeySetSize) keys += jKeys;
		    					}
		    					Iterator<String> jsonIter = jsonKeySet.iterator();
		    					int jSize = json.size();
		    					int jCount = 1;
		    					while(jsonIter.hasNext()) {
		    						String jKey = ((String)jsonIter.next());
			    					Object jValue = json.get(jKey);
			    					if(jCount < jSize) {
			    						if(jValue instanceof String) {
			    							jValue = "\\\""+jValue+"\\\"";
			    							query += jKey + ": " + jValue + ", ";
			    						} else {
			    							query += jKey + ": " + jValue + ", ";
			    						}		    						
			    					} else if(jCount == jSize) {
			    						if(jValue instanceof String) {
			    							jValue = "\\\""+jValue+"\\\"";
			    							query += jKey + ": " + jValue;
			    						} else {
			    							query += jKey + ": " + jValue;
			    						}		    						
			    					}
			    					jCount++;
		    					}
		    					if(i==array.size()-1) query += "}";
		    					else  query += "}, ";
		    				}
		    				query += "]";
		    				keys += "}, ";
		    			}
		    			else {
		        			query += key + ": " + value;
		        			keys += key + ", ";
		        		}
		    		}   		
		    		//keys += key + ", ";
		    		count++;
		    	}//while
		    	
				query += "}){ "+keys+CreateMetaFields+"}}\\n\"}";
				//query += "}){ id }}\\n\"}";

			} catch (Exception e) {throw e;}
			
			return query;
	   }
   /**
     * @Method Name : getMutationUpdate
     * @작성일 : 2022. 8. 11.
     * @작성자 : yoosb
     * @변경이력 : 2022.10.06 예외처리  
     * @Method 설명 : Return GraphQL Update Mutation Query.
     */
   String mutationUpdate(LinkedHashMap<String, Object> params, String __typename) throws Exception {
   	String query = "{\"query\":\"mutation{update"+__typename+"(input: {";
		
		try {
			Set<String> keySet = params.keySet();
	    	Iterator<String> iter = keySet.iterator();
	    	int MapSize = params.size();
			int count = 1;
			String keys = "";

			while(iter.hasNext()) {    		
	    		String key = ((String)iter.next());
	    		Object value = params.get(key);
	    		if (count < MapSize) {
	    			if(value instanceof String) {
	        			value = "\\\""+value+"\\\"";
	        			query += key + ": " + value + ", ";
	        		} else {
	        			query += key + ": " + value + ", ";
	        		}
	    		} else if(count == MapSize) {
	    			if(value instanceof String) {
	        			value = "\\\""+value+"\\\"";
	        			query += key + ": " + value;
	        		} else {
	        			query += key + ": " + value;
	        		}
	    		}
	    		keys += key + ", ";
	    		count++;
	    	}
			
			query += "}){ "+keys+UpdateMetaFields+"}}\\n\"}";
			
		} catch (Exception e) {throw e;}
		
		return query;
   }
   
   /**
     * @throws Exception 
    * @Method Name : getListEqualQuery
     * @작성일 : 2022. 9. 29.
     * @작성자 : yoosb
     * @변경이력 : 2022.10.06 예외처리 / 2022.10.24 _deleted = true 아이템 필터링 위해 items에 _deleted 추가.
     * @Method 설명 : Return GraphQL list Query (filter: equal) ** list query문의 경우 __typename이 복수형이어야 함. 
     */
   String queryListEqual(LinkedHashMap<String, Object> params, List<String> itemsList, String __typename) throws Exception{
   	String query = "{\"query\":\"query{list"+__typename+"(filter: {";
   	
   	try {
   		Set<String> keySet = params.keySet();
       	Iterator<String> iter = keySet.iterator();
       	int MapSize = params.size();
       	int ListSize = itemsList.size();       	
   		int count = 1;
   		String items = "";
   		while(iter.hasNext()) {    		
       		String key = ((String)iter.next());
       		Object value = params.get(key);
       		if (count < MapSize) {
       			if(value instanceof String) {
           			value = "{eq: \\\""+value+"\\\"}";
           			query += key + ": " + value + ", ";
           		} else {
           			value = "{eq: "+value+"}";
           			query += key + ": " + value + ", ";
           		}
       		} else if(count == MapSize) {
       			if(value instanceof String) {
           			value = "{eq: \\\""+value+"\\\"}";
           			query += key + ": " + value;
           		} else {
           			value = "{eq: "+value+"}";
           			query += key + ": " + value;
           		}
       		}
       		count++;
       	}//while
//   		count = 1;
//       	for(String item : itemsList){
//       		items = (count < ListSize) ? items+item+", " : items+item;
//       		count++;
//       	}
   		//2022.10.24 _deleted 추가 
   		for(String item : itemsList){
       		items += item+", ";
       	}
   		items += "_deleted";
   		
   		query += "}){ items { "+items+" }}}\\n\"}";
   		
		} catch (Exception e) {throw e;}
   	
       return query;
   }
   
   /**
    * @throws Exception 
   * @Method Name : getListEqualQuery
    * @작성일 : 2022. 9. 29.
    * @작성자 : yoosb
    * @변경이력 : 2022.10.06 예외처리  
    * @Method 설명 : Return GraphQL list Query (filter: equal)
    */
  String queryGetById(String id, List<String> itemsList, String __typename) throws Exception{
	  id = "\\\""+id+"\\\"";
  	String query = "{\"query\":\"query{get"+__typename+"(id: "+id+"){";
  	
  	try {
      	int ListSize = itemsList.size();       	
  		String items = "";
  		int count = 1;
      	for(String item : itemsList){
      		items = (count < ListSize) ? items+item+", " : items+item;
      		count++;
      	}
      	
  		query += items+" }}\\n\"}";
  		
		} catch (Exception e) {throw e;}
  	
  	System.out.println("queryGetById-------\n" + query);
      return query;
  }
}
