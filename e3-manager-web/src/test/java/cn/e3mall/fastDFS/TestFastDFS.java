package cn.e3mall.fastDFS;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class TestFastDFS {
	
	@Test
	public void testUploadFile() throws Exception {
		//创建一个配置文件。文件名任意。内容就是tracker服务器的地址。
		//tracker_server=192.168.25.133:22122
		//使用全局对象加载配置文件。
		ClientGlobal.init("D:\\eclipse_workspaceKJ\\ssm_shop\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
		//创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//通过TrackClient获得一个TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个StrorageServer的引用，可以是null
		StorageServer storageServer = null;
		//创建一个StorageClient，参数需要TrackerServer和StrorageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//使用StorageClient上传文件。
		String[] strings = storageClient.upload_file("C:\\Users\\wpc\\Desktop\\study\\strlen.gif", "gif", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}
	
}
