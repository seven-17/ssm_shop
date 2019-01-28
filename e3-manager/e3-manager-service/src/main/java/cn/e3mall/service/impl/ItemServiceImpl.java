package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	public TbItem getItemById(long itemId) {
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		
		TbItemExample example = new TbItemExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) return list.get(0);
		return null;
	}

	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//获取详细信息
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		EasyUIDataGridResult dataGridResult = new EasyUIDataGridResult();
		dataGridResult.setRows(list);
		dataGridResult.setTotal((int) pageInfo.getTotal());
		return dataGridResult;
	}

	public E3Result addItem(TbItem item, String desc) {
		long id = IDUtils.genItemId();
		item.setId(id);
		item.setCreated(new Date());
		item.setStatus((byte)1);
		item.setUpdated(new Date());
		itemMapper.insert(item);
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(id);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		itemDescMapper.insert(tbItemDesc);
		return E3Result.ok();
	}

	public E3Result updateItemStatus(String[] ids, byte status) {
		for (String str : ids) {
			TbItem record = new TbItem();
			record.setId(new Long(str));
			record.setStatus(status);
			itemMapper.updateByPrimaryKeySelective(record);
		}
		return E3Result.ok();
	}

	public E3Result getItemDescById(Long id) {
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(id);
		return E3Result.ok(itemDesc);
	}

	public E3Result updateItem(TbItem item, String desc) {
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		return E3Result.ok();
	}

}
