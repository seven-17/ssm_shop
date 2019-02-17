package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

public interface TokenService {
	
	public E3Result getUserByToken(String token);
	
}
