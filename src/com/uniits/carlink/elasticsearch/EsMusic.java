/**
 * 
 */
package com.uniits.carlink.elasticsearch;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;

import com.uniits.carlink.domain.BaiduMusicResultInfo;

/**
 * @author 成卫
 *
 */
public class EsMusic {
	
	@Test
	public void test() {
		findEsMusic("i");
	}
	
	public static void main(String[] args) {
		new EsMusic().findEsMusic("i");
	}
	
	public List<?> findEsMusic(String song) {
		Client client = ElasticSearchHandler.getTransportClient();
		Long start = System.currentTimeMillis();
    	SearchResponse response = client.prepareSearch("local")
		        .setTypes("music")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.fuzzyQuery("songName", song))
//		        .setQuery(QueryBuilders.termQuery("rate", "128"))             // Query
		        //.setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
		        .setFrom(0).setSize(20).setExplain(true)
		        .execute()
		        .actionGet();
    	SearchHit[] shl = response.getHits().getHits();
    	List<BaiduMusicResultInfo> list = null;
    	if(shl != null && shl.length > 0) {
    		list = new ArrayList<BaiduMusicResultInfo>();
    		for(int i =0; i < shl.length;i ++) {
    			SearchHit s = shl[i];
    			System.out.println(s.getSourceAsString());
    			BaiduMusicResultInfo e = new BaiduMusicResultInfo();
    			e.setAlbum("");
    			e.setFormat("mp3");
    			e.setLrclink("");
    			e.setSinger("");
    			e.setSongname("" + s.getSource().get("songName"));
    			e.setSize(Long.parseLong("" + s.getSource().get("size")));
    			e.setSongid("" + s.getSource().get("songId"));
    			e.setTime(0L);
    			list.add(e);
    		}
    	}
    	Long end = System.currentTimeMillis();
    	System.out.println("times: " + (end-start));
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
		return list;
	}
}
