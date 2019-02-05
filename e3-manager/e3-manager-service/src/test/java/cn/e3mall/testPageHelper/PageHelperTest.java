package cn.e3mall.testPageHelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

public class PageHelperTest {
	
	@Test
	public void testPageHelper() throws Exception {
		//初始化spring容器
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-dao.xml");
		
		//从容器中获得Mapper代理对象
		TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
		
		//执行sql语句之前设置分页信息使用PageHelper的startPage方法。
		PageHelper.startPage(1, 10);
		
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		
		//取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		System.out.println("总记录数："+pageInfo.getTotal());
		System.out.println("总页数："+pageInfo.getPages());
		System.out.println(list.size());
		
		applicationContext.close();
	}
	
}
