package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/query/list")
	public @ResponseBody EasyUIDataGridResult getContentListByCategoryId(int page, int rows, long categoryId) {
		return contentService.getContentListByCategoryId(page, rows, categoryId);
	}
	
	@RequestMapping("/content/save")
	public @ResponseBody E3Result addContent(TbContent content) {
		return contentService.addContent(content);
	}
	
	@RequestMapping("/content/delete")
	public @ResponseBody E3Result deleteContentByIds(String ids) {
		return contentService.deleteContentByIds(ids.split(","));
	}
	
	@RequestMapping("/content/edit")
	public @ResponseBody E3Result updateContentById(TbContent content) {
		return contentService.updateContentById(content);
	}
	
}
