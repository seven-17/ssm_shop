package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.SearchIndexService;

@Controller
public class SearchIndexController {
	
	@Autowired
	private SearchIndexService indexService;
	
	@RequestMapping("/index/item/import")
	public @ResponseBody E3Result importAllItemIndex() {
		return indexService.importAllItemIndx();
	}
	
}
