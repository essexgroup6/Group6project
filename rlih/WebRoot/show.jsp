<%@ page language="java" import="java.util.*,com.mongodb.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    <% 
    	MongoClient mongo = new MongoClient("localhost", 27017);
		List<String> dbs = mongo.getDatabaseNames();
		System.out.println(dbs);
		
		DB db = mongo.getDB("rlih");
        
		Set<String> collections = db.getCollectionNames();
		System.out.println(collections); // [datas, names, system.indexes, users]
		
		DBCollection col = db.getCollection("news");
		DBCursor cursor = col.find();
        while(cursor.hasNext()){
        %>
            News:<%=cursor.next() %><br>
        <%
        }
		System.out.println();
    %>
    <img alt="" src="http://ichef-1.bbci.co.uk/news/660/cpsprodpb/A36B/production/_88753814_88753813.jpg">
  </body>
</html>
