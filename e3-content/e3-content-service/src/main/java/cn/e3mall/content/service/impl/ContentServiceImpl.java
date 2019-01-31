package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${Redis_ContentList_Key}")
	private String Redis_ContentList_Key;
	
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
		//同步缓存
		jedisClient.hdel(Redis_ContentList_Key, content.getCategoryId().toString());
		return E3Result.ok();
	}

	public E3Result deleteContentByIds(String[] ids) {
		Long cid = contentMapper.selectByPrimaryKey(new Long(ids[0])).getCategoryId();
		for (String id : ids) {
			contentMapper.deleteByPrimaryKey(new Long(id));
		}
		//同步缓存
		jedisClient.hdel(Redis_ContentList_Key, cid.toString());
		return E3Result.ok();
	}

	public E3Result updateContentById(TbContent content) {
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(content);
		//同步缓存
		jedisClient.hdel(Redis_ContentList_Key, content.getCategoryId().toString());
		return E3Result.ok();
	}

	public List<TbContent> getContentListByCategoryId(long categoryId) {
		//查询缓存数据
		try {
			String json = jedisClient.hget(Redis_ContentList_Key, categoryId+"");
			if(StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有,查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//将数据放入缓存
		try {
			String json = JsonUtils.objectToJson(list);
			jedisClient.hset(Redis_ContentList_Key, categoryId+"", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
