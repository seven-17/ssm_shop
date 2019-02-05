package cn.e3mall.search.mapper;

import java.util.List;

import cn.e3mall.common.pojo.SearchItem;

public interface SearchItemMapper {
	
	public List<SearchItem> searchItemList();
	public SearchItem searchItemByItemId(long itemId);
	
}
