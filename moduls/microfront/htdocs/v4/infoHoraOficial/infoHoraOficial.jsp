<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.SimpleDateFormat"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.png" type="image/x-ico"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Hora Oficial</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
</head>

<body>	
	<%String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	  Calendar cal = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);%>
	<div id="infoNoMenu"><h2><%=sdf.format(cal.getTime())%></h2></div>
</body>
</html>