package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
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
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	public @ResponseBody EasyUIDataGridResult getItemList(int page, int rows) {
		return itemService.getItemList(page, rows);
	}
	
	@RequestMapping("/item/save")
	public @ResponseBody E3Result addItem(TbItem item,String desc) {
		return itemService.addItem(item, desc);
	}
	
	//1-正常，2-下架，3-删除
	@RequestMapping("/item/delete")
	public @ResponseBody E3Result deleteItemById(String ids) {
		String[] strings = ids.split(",");
		return itemService.updateItemStatus(strings,(byte)3);
	}
	
	@RequestMapping("/item/instock")
	public @ResponseBody E3Result instockItem(String ids) {
		String[] strings = ids.split(",");
		return itemService.updateItemStatus(strings,(byte)2);
	}
	
	@RequestMapping("/item/reshelf")
	public @ResponseBody E3Result reshelfItem(String ids) {
		return itemService.updateItemStatus(ids.split(","),(byte)1);
	}
	
	@RequestMapping("/item/desc/{id}")
	public @ResponseBody E3Result getItemDescById(@PathVariable long id) {
		return itemService.getItemDescById(id);
	}
	
	@RequestMapping("/item/update")
	public @ResponseBody E3Result updateItem(TbItem item,String desc) {
		return itemService.updateItem(item, desc);
	}
}
