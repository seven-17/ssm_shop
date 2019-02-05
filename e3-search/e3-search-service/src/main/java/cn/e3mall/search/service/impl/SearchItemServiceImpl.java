package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchDao searchDao;
	
	public SearchResult searchItem(String keyword,int page,int rows) throws Exception {
		//封装查询条件
		SolrQuery query = new SolrQuery();
		query.setQuery(keyword);
		query.setStart((page-1)*rows);
		query.setRows(rows);
		query.set("df", "item_title");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<span style=\"color:red\">");
		query.setHighlightSimplePost("</span>");
		//调用dao查询索引库
		SearchResult searchResult = searchDao.SearchItemBySolrQuery(query);
		int totalPages = (int) Math.ceil(searchResult.getRecourdCount()*1.0/rows);
		searchResult.setTotalPages(totalPages);
		return searchResult;
	}

}
