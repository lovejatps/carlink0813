package com.uniits.carlink.elasticsearch;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.engine.Engine.Searcher;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;



public class ElasticSearchHandler {
	
	private static final String HOST = "192.168.20.79";
	
	private static final int PORT = 9300;
	
	private static final String CLUSTER_NAME = "elas1";
	
	private static Client client;
	
	static Map<String, String> m = new HashMap<String, String>();
    // 设置client.transport.sniff为true来使客户端去嗅探整个集群的状态，把集群中其它机器的ip地址加到客户端中，
    static Settings settings = ImmutableSettings.settingsBuilder().put(m).put("cluster.name",CLUSTER_NAME).put("client.transport.sniff", true).build();
 
    // 创建私有对象
//    private static TransportClient client2;
 
    static {
        try {
            Class<?> clazz = Class.forName(TransportClient.class.getName());
            Constructor<?> constructor = clazz.getDeclaredConstructor(Settings.class);
            constructor.setAccessible(true);
            client = ((TransportClient) constructor.newInstance(settings)).addTransportAddress(new InetSocketTransportAddress(HOST, PORT));
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
 
    // 取得实例
    public static synchronized Client getTransportClient() {
        return client;
    }
    

//	public ElasticSearchHandler() {
//		this(HOST);
//	}
//
//	@SuppressWarnings("resource")
//	public ElasticSearchHandler(String ipAddress) {
//		Settings settings = ImmutableSettings.settingsBuilder()
//		        .put("cluster.name", "es_steven").put("client.transport.sniff", true).build();
//		// 集群连接超时设置
//		client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(ipAddress, PORT));
//	}

	/**
	 * 创建索引
	 * @param indexname
	 * @param type
	 * @param id
	 * @param jsondata
	 * @return
	 */
    public IndexResponse createIndexResponse(String indexname, String type,String id,String jsondata){
    	IndexResponse response = client.prepareIndex(indexname, type, id)
    	        .setSource(jsondata)
    	        .execute()
    	        .actionGet();
	        return response;
	}
    
    /**
     * 获取单条记录
     * @param indexname
     * @param type
     * @param id
     * @return
     */
    public GetResponse getIndexResponse(String indexname, String type,String id){
    	 GetResponse response = client.prepareGet(indexname, type, id)
    	            .setOperationThreaded(false)
    	            .execute()
    	            .actionGet();
    	 return response;
  	}
    
    /**
     * 删除单条记录
     * @param indexname
     * @param type
     * @param id
     * @return
     */
    public DeleteResponse delIndexResponse(String indexname, String type,String id){
    	DeleteResponse response = client.prepareDelete(indexname, type, id)
    	        .setOperationThreaded(false)
    	        .execute()
    	        .actionGet();
   	 return response;

 	}
    
    /**
     * 更新记录
     * @param indexname
     * @param type
     * @param id
     * @param json
     */
    public void updateIndexResponse(String indexname, String type,String id,String json){
    	UpdateRequest updateRequest = new UpdateRequest(indexname, type, id)
        .doc(json);
    		try {
				client.update(updateRequest).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

 	}
    
    
    public static void searcher() {
    	Long start = System.currentTimeMillis();
    	SearchResponse response = client.prepareSearch("index")
		        .setTypes("fulltext")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.prefixQuery("name", "测试"))
//		        .setQuery(QueryBuilders.fuzzyQuery("message", "el~"))
//		        .setQuery(QueryBuilders.termQuery("rate", "128"))             // Query
//		        .setPostFilter(FilterBuilders.rangeFilter("age").from(1).to(18))   // Filter
		        .setFrom(0).setSize(20).setExplain(true)
		        .execute()
		        .actionGet();
    	SearchHit[] shl = response.getHits().getHits();
    	if(shl != null && shl.length > 0) {
    		for(int i =0; i < shl.length;i ++) {
    			SearchHit s = shl[i];
    			System.out.println(s.getSourceAsString());
    		}
    	}
    	Long end = System.currentTimeMillis();
    	System.out.println("times: " + (end-start));
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
//		System.out.println(response.toString());
    }
    
    public static void main(String[] args) {
//    	ElasticSearchHandler e = new ElasticSearchHandler("192.168.20.196");
//    	GetResponse ess = new ElasticSearchHandler().getIndexResponse("a", "b", "14467879");
////    	
//    	System.out.println(ess.getSourceAsString());
    	
    	
    	IndexResponse resp  =new ElasticSearchHandler().createIndexResponse("B", "a", "10", "empname:emp21");
		
		System.out.println(resp.getIndex());
	}
    
    /**
     * 清除ES_INDEX下的所有数据 
     */
    public void clearAllDatas() {
        client.prepareDeleteByQuery("").setTypes("").setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
    }
    
  

}
