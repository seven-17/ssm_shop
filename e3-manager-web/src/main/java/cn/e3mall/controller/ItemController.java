package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
/**
 **商品管理
 * @author wpc
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public @ResponseBody TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(691300L);
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	public @ResponseBody EasyUIDataGridResult getItemList(int page, int rows) {
		return itemService.getItemList(page, rows);
	}
}
