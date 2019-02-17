package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		//从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		//判断token是否存在
		if(StringUtils.isBlank(token)) {
			//不存在  未登录状态  跳转到登录页面，登录成功后跳转到当前请求的url
			response.sendRedirect("http://localhost:8088/page/login?redirect="+request.getRequestURL());
			return false;
		}
		//根据token取用户信息
		E3Result e3Result = tokenService.getUserByToken(token);
		//判断用户是否过期
		if(e3Result.getStatus()!=200) {
			//未登录状态  跳转到登录页面，登录成功后跳转到当前请求的url
			response.sendRedirect("http://localhost:8088/page/login?redirect="+request.getRequestURL());
			return false;
		}
		//得到用户信息
		TbUser user = (TbUser) e3Result.getData();
		//把用户信息放到request中
		request.setAttribute("user",user);
		//判断cookie中是否有购物车数据
		String json = CookieUtils.getCookieValue(request, "cart", true);
		//cookie中的购物车有数据
		if(StringUtils.isNotBlank(json)) {
			//合并购物车
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(json, TbItem.class));
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub

	}

}
