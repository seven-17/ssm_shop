package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor{

	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		//前处理,执行handler之前执行此方法 	 true放行    false不放行
		//1 从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		//2 如果没有token，未登录状态，直接放行
		if(StringUtils.isBlank(token)) {
			return true;
		}
		//3 根据token调用sso的服务，取用户信息
		E3Result e3Result = tokenService.getUserByToken(token);
		if(e3Result.getStatus()!=200) {
			//用户过期
			return false;
		}
		//4 把用户信息放到request中
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute("user", user);
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
