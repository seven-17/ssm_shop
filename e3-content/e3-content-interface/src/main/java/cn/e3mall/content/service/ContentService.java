package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {

	public EasyUIDataGridResult getContentListByCategoryId(int page, int rows,long categoryId);
	public E3Result addContent(TbContent content);
	public E3Result deleteContentByIds(String[] ids);
	public E3Result updateContentById(TbContent content);
	public List<TbContent> getContentListByCategoryId(long categoryId);
	
}
