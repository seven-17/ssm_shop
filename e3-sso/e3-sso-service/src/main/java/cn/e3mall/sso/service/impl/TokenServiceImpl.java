package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public E3Result getUserByToken(String token) {
		//根据token从redis中取用户信息
		String json = jedisClient.get("SESSION:" + token);
		//用户过期
		if(StringUtils.isBlank(json)) {
			return E3Result.build(400, "用户信息已经过期");
		}
		//更新用户过期时间
		jedisClient.expire("SESSION:"+token, 1800);
		//返回用户信息
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return E3Result.ok(user);
	}

}
