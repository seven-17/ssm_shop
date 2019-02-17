package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTest {
	
	@Test
	public void testFreeMarker() throws Exception {
		//1、创建一个模板文件（hello.ftl）
		//2、创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3、设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("D:\\eclipse_workspaceKJ\\ssm_shop\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		//4、模板文件的编码格式，一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//5、加载一个模板文件，创建一个模板对象。
		//Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("student.ftl");
		//6、创建一个数据集。可以是pojo也可以是map。推荐使用map
		Map<String,Object> data = new HashMap<String,Object>();
	//添加普通字符串
		data.put("hello", "hello freemarker!");
	//添加一个pojo
		data.put("student", new Student(1,"李明",22,"河南"));
	//添加list集合
		ArrayList<Student> stuList = new ArrayList<Student>();
		stuList.add(new Student(1,"小明1",20,"河南"));
		stuList.add(new Student(2,"小明2",21,"河南"));
		stuList.add(new Student(3,"小明3",22,"河南"));
		stuList.add(new Student(4,"小明4",23,"河南"));
		data.put("stuList", stuList);
	//date日期
		data.put("date", new Date());
	//null值测试
		data.put("val", null);
		//7、创建一个Writer对象，指定输出文件的路径及文件名。
		Writer out = new FileWriter(new File("E:\\freemarker\\student.html"));
		//8、生成静态页面
		template.process(data, out);
		//9、关闭流
		out.close();
	}
	
}
