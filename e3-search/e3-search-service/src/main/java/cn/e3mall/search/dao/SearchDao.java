package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

@Repository
public class SearchDao {
	
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult SearchItemBySolrQuery(SolrQuery solrQuery) throws Exception {
		SearchResult searchResult = new SearchResult();
		//执行查询
		QueryResponse queryResponse = solrServer.query(solrQuery);
		//获取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		//获取总记录数
		long totalNum = solrDocumentList.getNumFound();
		searchResult.setRecourdCount(totalNum);
		//获取高亮显示记录
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		ArrayList<SearchItem> list = new ArrayList<SearchItem>();
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setPrice((Long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			String title = "";
			Map<String, List<String>> map = highlighting.get(solrDocument.get("id"));
			List<String> listStr = map.get("item_title");
			if(listStr!=null && listStr.size()>0) {
				title = listStr.get(0);
			}else {
				title = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle(title);
			list.add(searchItem);
		}
		searchResult.setItemList(list);
		return searchResult;
	}
	
}
