<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title></title>
</head>
<body>
	学生信息：<br>
	学号：${student.id}&nbsp;&nbsp;&nbsp;&nbsp;
	姓名：${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
	年龄：${student.age}&nbsp;&nbsp;&nbsp;&nbsp;
	住址：${student.address}&nbsp;&nbsp;&nbsp;&nbsp;
	学生列表：
	<table border="1">
		<tr>
			<th>序号</th>
			<th>学号</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>住址</th>
		</tr>
		<#list stuList as stu>
			<#if stu_index % 2 ==0>
				<tr bgcolor="red">
			<#else>
				<tr bgcolor="green">
			</#if>
					<td>${stu_index}</td>
					<td>${stu.id}</td>
					<td>${stu.name}</td>
					<td>${stu.age}</td>
					<td>${stu.address}</td>
				</tr>
		</#list>
	</table>
	<br>
	年月日：${date?date} <br>
	时间：${date?time} <br>
	日期：${date?datetime} <br>
	自定义格式：${date?string("dd-MM-yyyy HH:mm:ss")} <br>
	null值的处理：${val!""} <br>
	判断是否为null值：
	<#if val??>
		val不为null
	<#else>
		val为null
	</#if>
	<br>
	引用模板测试：<#include "hello.ftl">
</body>
</html>