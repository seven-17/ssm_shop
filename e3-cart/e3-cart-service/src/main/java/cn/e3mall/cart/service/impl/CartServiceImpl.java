package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemMapper itemMapper;
	
	//向redis中添加购物车
	@Override
	public E3Result addCart(long userId, long itemId, int num) {
		//数据类型是hash  key：用户id  filed：商品id  value：商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists("CART:"+userId, itemId+"");
		//如果存在数量相加
		if(hexists) {
			String json = jedisClient.hget("CART:"+userId, itemId+"");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+num);
			//写入redis
			jedisClient.hset("CART:"+userId, itemId+"", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		//如果不存在,根据商品id取商品信息
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		//设置商品数量
		tbItem.setNum(num);
		//设置图片
		String image = tbItem.getImage();
		if(StringUtils.isNotBlank(image)) {
			String[] imgs = image.split(",");
			tbItem.setImage(imgs[0]);
		}
		//添加到redis
		jedisClient.hset("CART:"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		//根据用户id查询购物车列表
		List<String> jsonList = jedisClient.hvals("CART:"+userId);
		List<TbItem> itemList = new ArrayList<TbItem>();
		//添加到列表
		for (String str : jsonList) {
			TbItem item = JsonUtils.jsonToPojo(str, TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}

	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		//取商品信息
		String json = jedisClient.hget("CART:"+userId, itemId+"");
		//更新商品数量
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		//写入redis
		jedisClient.hset("CART:"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		//删除购物车商品
		jedisClient.hdel("CART:"+userId, itemId+"");
		return E3Result.ok();
	}

	@Override
	public E3Result clearCart(long userId) {
		//删除购物车信息
		jedisClient.del("CART:"+userId);
		return E3Result.ok();
	}

}