package cn.e3mall.iteminfo.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 **生成静态页面 
 * @author wpc
 */
@Controller
public class HTMLGenController {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception {
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//加载模板
		Template template = configuration.getTemplate("hello.ftl");
		//准备数据
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("hello", 123456);
		//输出文件
		Writer out = new FileWriter(new File("E:\\freemarker\\hello.html"));
		//创造模板
		template.process(map, out);
		return "OK";
	}
	
}
