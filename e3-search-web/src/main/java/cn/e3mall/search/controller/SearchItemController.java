package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchItemService;

@Controller
public class SearchItemController {
	
	@Autowired
	private SearchItemService searchItemService;
	@Value("${SearchItem_Rows}")
	private int rows;
	
	@RequestMapping("/search")
	public String SearchItemByKeyword(String keyword, Model model,
			@RequestParam(defaultValue="1") Integer page) throws Exception {
		keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		SearchResult searchResult=searchItemService.searchItem(keyword,page,rows);
		model.addAttribute("query", keyword);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("recourdCount", searchResult.getRecourdCount());
		model.addAttribute("itemList", searchResult.getItemList());
		return "search";
	}
	
}
