package cn.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.SearchItemMapper;
import cn.e3mall.search.service.SearchIndexService;

@Service
public class SearchIndexServiceImpl implements SearchIndexService {

	@Autowired
	private SolrServer solrServer;
	@Autowired
	private SearchItemMapper searchItemMapper;
	
	public E3Result importAllItemIndx() {
		try {
			//从数据库查询所有商品
			List<SearchItem> list = searchItemMapper.searchItemList();
			for (SearchItem item : list) {
				//创建一个文档对象SolrInputDocument
				SolrInputDocument document = new SolrInputDocument();
				//向文档对象中添加域。文档中必须包含一个id域，所有的域的名称必须在schema.xml中定义。
				document.addField("id", item.getId());
				document.addField("item_title", item.getTitle());
				document.addField("item_sell_point", item.getSell_point());
				document.addField("item_price", item.getPrice());
				document.addField("item_image", item.getImage());
				document.addField("item_category_name", item.getCategory_name());
				//把文档写入索引库
				solrServer.add(document);
			}
			//提交
			solrServer.commit();
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(500, "导入数据失败");
		}
	}

}
