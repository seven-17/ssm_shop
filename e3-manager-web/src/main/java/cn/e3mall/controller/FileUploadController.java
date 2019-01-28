package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

/**
 * 图片上传Controller
 * @author wpc
 */
@Controller
public class FileUploadController {
	
	@Value("${FastDFS_Client_Adress}")
	private String FastDFS_Client_Adress;
	
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	public @ResponseBody String itemImageUpload(MultipartFile uploadFile) {
		try {
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			//原始文件名
			String name = uploadFile.getOriginalFilename();
			//文件后缀名
			String extName = name.substring(name.lastIndexOf(".")+1);
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			url = FastDFS_Client_Adress + url;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("error", 0);
			map.put("url", url);
			return JsonUtils.objectToJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("error", 1);
			map.put("message", "图片上传失败");
			return JsonUtils.objectToJson(map);
		} 
	}
	
}
