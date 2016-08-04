<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<html:html>
<head>
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">
  <META http-equiv="CACHE-CONTROL" content="NO-CACHE">  <!-- For HTTP 1.1 -->
  <META http-equiv="PRAGMA" content="NO-CACHE">         <!-- For HTTP 1.0 -->
  <META http-equiv="refresh" content="1; URL=<%= request.getContextPath() %>/intranet/home.do?idsite=<bean:write name="MVS_idsite" />&amp;lang=<bean:write name="MVS_idioma" />">
  <title><bean:message key="pleasewait.favor"/></title>
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>  
</head>

<body onload="javascript:window.focus();">
	<br />
	<br />
  <table cellpadding="10">
        <tr>
           <td align="center">
           <h4><bean:message key="pleasewait.peticion"/>...</h4>
           <h4><bean:message key="pleasewait.favor"/>...</h4>
           </td>     
        </tr>
  </table>
</body>

</html:html>