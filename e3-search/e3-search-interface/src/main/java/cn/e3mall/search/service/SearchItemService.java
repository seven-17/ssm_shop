package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

public interface SearchItemService {

	public SearchResult searchItem(String keyword,int page,int rows) throws Exception;
	
}
