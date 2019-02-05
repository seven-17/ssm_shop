package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class SearchResult implements Serializable {
	
	private long recourdCount;
	private List<SearchItem> itemList; 
	private int totalPages;
	
	public long getRecourdCount() {
		return recourdCount;
	}
	public void setRecourdCount(long recourdCount) {
		this.recourdCount = recourdCount;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
}
