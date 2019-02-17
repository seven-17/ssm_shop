package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {
	
	@Autowired
	private TbUserMapper userMapper;
	
	@Override
	public E3Result checkData(String param, int type) {
		//创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//封装查询条件
		if(type == 1) {//用户名
			criteria.andUsernameEqualTo(param);
		}else if(type == 2) {//手机号
			criteria.andPhoneEqualTo(param);
		}else if(type == 3) {//邮箱
			criteria.andEmailEqualTo(param);
		}else {
			return E3Result.build(400, "数据类型错误", false);
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		//数据库中有数据返回false
		if(list!=null&&list.size()>0) {
			return E3Result.ok(false);
		}
		//数据库中没有数据返回true
		return E3Result.ok(true);
	}

	@Override
	public E3Result register(TbUser user) {
		//数据有效性
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
				|| StringUtils.isBlank(user.getPhone())) {
			return E3Result.build(400, "用户数据不完整，注册失败");
		}
		//1 用户名  2 手机号 3 邮箱
		E3Result e3Result = checkData(user.getUsername(), 1);
		if(!(boolean) e3Result.getData()) {
			return E3Result.build(400, "此用户名已经被占用");
		}
		e3Result = checkData(user.getPhone(), 2);
		if(!(boolean) e3Result.getData()) {
			return E3Result.build(400, "此手机号已经被占用");
		}
		//补全pojo
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//对密码进行md5加密
		String md5pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5pwd);
		//插入数据
		userMapper.insert(user);
		return E3Result.ok();
	}

}
