package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

@Controller
public class CartController {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;
	
	private static final int COOKIE_EXPIRE_TIME=3_600;
	
	@RequestMapping("/cart/add/{itemId}")
	public String addItemToCart(@PathVariable Long itemId, @RequestParam(defaultValue="1")
		Integer num, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		//如果登录
		if(user!=null) {
			//保存到服务端
			cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//设置是否找到商品标志
		boolean flag = false;
		//判断商品在商品列表中是否存在
		for (TbItem tbItem : cartList) {
			//如果存在数量相加
			if(tbItem.getId() == itemId.longValue()) {
				tbItem.setNum(tbItem.getNum() + num);
				//找到商品
				flag = true;
				//跳出循环
				break;
			}
		}
		//如果不存在
		if(!flag) {
			//根据商品id查询商品信息
			TbItem item = itemService.getItemById(itemId);
			//设置商品数量
			item.setNum(num);
			//设置图片
			String image = item.getImage();
			if(StringUtils.isNotBlank(image)) {
				String[] imgs = image.split(",");
				item.setImage(imgs[0]);
			}
			cartList.add(item);
		}
		//写入cookie
		String json = JsonUtils.objectToJson(cartList);
		CookieUtils.setCookie(request, response, "cart", json, COOKIE_EXPIRE_TIME, true);
		return "cartSuccess";
	}
	
	//从cookie中取购物车列表
	private List<TbItem> getCartListFromCookie(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, "cart", true);
		List<TbItem> list = null;
		//判断是否为空
		if(StringUtils.isBlank(json)) {
			return new ArrayList<TbItem>();
		}
		//把json转换成list
		list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, HttpServletResponse response) {
		//从cookie中取购物车信息
		List<TbItem> cartList = getCartListFromCookie(request);
		
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		//如果用户登录
		if(user!=null) {
			//合并购物车
			cartService.mergeCart(user.getId(), cartList);
			//把cookie中的购物车删除
			CookieUtils.deleteCookie(request, response, "cart");
			//从服务端redis 中取购物车列表
			cartList = cartService.getCartList(user.getId());
		}
		request.setAttribute("cartList", cartList);
		return "cart";
	}
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	public @ResponseBody E3Result updateCartNum(@PathVariable Long itemId, @PathVariable
			Integer num, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		//如果登录
		if(user!=null) {
			return cartService.updateCartNum(user.getId(), itemId, num);
		}
		
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//遍历找到对应的商品
		for (TbItem tbItem : cartList) {
			if(tbItem.getId() == itemId.longValue()) {
				tbItem.setNum(num);
				break;
			}
		}
		String json = JsonUtils.objectToJson(cartList);
		CookieUtils.setCookie(request, response, "cart", json, COOKIE_EXPIRE_TIME, true);
		return E3Result.ok();
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		//如果登录
		if(user!=null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//删除购物项
		for (TbItem tbItem : cartList) {
			if(tbItem.getId() == itemId.longValue()) {
				cartList.remove(tbItem);
				//跳出循环
				break;
			}
		}
		//把购物车列表写入cookie
		String json = JsonUtils.objectToJson(cartList);
		CookieUtils.setCookie(request, response, "cart", json, COOKIE_EXPIRE_TIME, true);
		return "redirect:/cart/cart.html";
	}
	
}
