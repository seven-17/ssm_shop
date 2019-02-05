package cn.e3mall.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.SearchItemMapper;

public class AddItemMessageListener implements MessageListener {

	@Autowired
	private SolrServer solrServer;
	@Autowired
	private SearchItemMapper itemMapper;
	
	public void onMessage(Message message) {
		try {
			Thread.sleep(100);//防止商品未提交，就查数据库
			TextMessage textMessage = (TextMessage) message;
			String itemId = textMessage.getText();
			SearchItem searchItem = itemMapper.searchItemByItemId(new Long(itemId));
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			solrServer.add(document);
			//提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
