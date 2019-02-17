package cn.e3mall.iteminfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.iteminfo.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

@Controller
public class ItemInfoController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String getItemInfoByitemId(@PathVariable long itemId, Model model) {
		TbItem tbItem = itemService.getItemById(itemId);
		E3Result e3Result = itemService.getItemDescById(itemId);
		Item item = new Item(tbItem);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", e3Result.getData());
		return "item";
	}
}
