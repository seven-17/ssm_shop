package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;

@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryServic;
	
	@RequestMapping("/content/category/list")
	public @ResponseBody List<EasyUITreeNode> getContentCategoryListByParentid(
			@RequestParam(name="id",defaultValue="0") Long parentId){
		return contentCategoryServic.getContentCategoryListByParentid(parentId);
	}
	
	@RequestMapping("/content/category/create")
	public @ResponseBody E3Result addContentCategory(long parentId, String name) {
		return contentCategoryServic.addContentCategory(parentId, name);
	}
	
	@RequestMapping("/content/category/update")
	public @ResponseBody E3Result updateContentCategory(long id, String name) {
		return contentCategoryServic.updateContentCategory(id, name);
	}
	
	@RequestMapping("/content/category/delete")
	public @ResponseBody E3Result deleteContentCategoryById(long id) {
		return contentCategoryServic.deleteContentCategoryById(id);
	}
}
