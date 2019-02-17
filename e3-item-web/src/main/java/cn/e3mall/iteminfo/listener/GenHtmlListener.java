package cn.e3mall.iteminfo.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.iteminfo.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 **生成商品详情页面 
 * @author wpc
 */
public class GenHtmlListener implements MessageListener {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("html_gen_path")
	private String html_gen_path;
	
	public void onMessage(Message message) {
		try {
			//从消息中去商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待商品提交事务完成
			Thread.sleep(200);
			//根据商品Id取商品信息（基本信息和商品描述）
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			//根据商品id取商品描述
			TbItemDesc itemDesc = (TbItemDesc) itemService.getItemDescById(itemId).getData();
			//加载模板
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//准备数据
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("item", item);
			map.put("itemDesc", itemDesc);
			//生成页面输出目录
			Writer out = new FileWriter(new File(html_gen_path+itemId+".html"));
			//生成页面
			template.process(map, out);
			//关闭流
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
