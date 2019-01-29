package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	public EasyUIDataGridResult getContentListByCategoryId(int page, int rows, long categoryId) {
		//设置分页
		PageHelper.startPage(page, rows);
		//执行查询
		TbContentExample example = new TbContentExample();
		cn.e3mall.pojo.TbContentExample.Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//获取详细信息
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		EasyUIDataGridResult dataGridResult = new EasyUIDataGridResult();
		dataGridResult.setRows(list);
		dataGridResult.setTotal((int) pageInfo.getTotal());
		return dataGridResult;
	}

	public E3Result addContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		return E3Result.ok();
	}

	public E3Result deleteContentByIds(String[] ids) {
		for (String id : ids) {
			contentMapper.deleteByPrimaryKey(new Long(id));
		}
		return E3Result.ok();
	}

	public E3Result updateContentById(TbContent content) {
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(content);
		return E3Result.ok();
	}

	public List<TbContent> getContentListByCategoryId(long categoryId) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		return contentMapper.selectByExampleWithBLOBs(example);
	}
	
}
