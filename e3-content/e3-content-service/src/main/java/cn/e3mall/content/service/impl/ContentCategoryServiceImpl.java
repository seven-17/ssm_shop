package cn.e3mall.content.service.impl;
/**
 **内容菜单管理
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategoryListByParentid(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		criteria.andStatusEqualTo(1);
		List<TbContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> list = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory contentCategory : contentCategoryList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(contentCategory.getId());
			node.setText(contentCategory.getName());
			node.setState(contentCategory.getIsParent()?"closed":"open");
			list.add(node);
		}
		return list;
	}

	public E3Result addContentCategory(long parentId, String name) {
		//初始化各种数据
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setCreated(new Date());
		contentCategory.setIsParent(false);
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		contentCategory.setSortOrder(1);
		//可选值:1(正常),2(删除)
		contentCategory.setStatus(1);
		contentCategory.setUpdated(new Date());
		contentCategoryMapper.insert(contentCategory);
		
		//判断父节点的is_parent属性是否需要改为1
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
		boolean isParent = parentNode.getIsParent();
		if(isParent==false) {
			parentNode.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(parentNode);
		}
		return E3Result.ok(contentCategory.getId());
	}

	public E3Result updateContentCategory(long id, String name) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		contentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		return E3Result.ok();
	}

	public E3Result deleteContentCategoryById(long id) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		Boolean isParent = contentCategory.getIsParent();
		if(isParent) {
			return E3Result.build(304, "");
		}else {
			contentCategory.setStatus(0);
		}
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(contentCategory.getParentId());
		criteria.andStatusEqualTo(1);
		int count = contentCategoryMapper.countByExample(example);
		if(count == 1) {
			TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
			parentNode.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parentNode);
		}
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		return E3Result.ok();
	}

}
