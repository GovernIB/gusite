<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>
<html:html xhtml="true" >
<head>
    <title><bean:message key="calendario.title"/></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta http-equiv="Content-type" content='text/html; charset="UTF-8"' />
    <link rel="stylesheet" href='<html:rewrite page="/css/styleA.css"/>' type="text/css" />

    <script language="JavaScript">
    <!--
        function marca(dia) {

          mes = <bean:write name='dataForm' property='mes' />+1;
          if (mes < 10){
            mes = "0" + mes;
          }

          if (dia < 10){
            dia = "0" + dia;
          }

          data = dia;
          data += "/";
          data += mes;
          data += "/";
          data += "<bean:write name='dataForm' property='any'/>";
          opener.seleccionaDia(data, "<bean:write name='dataForm' property='field'/>");
          window.close();
    }
    //-->
    </script>
</head>
<body class="calendario" leftmargin="0" rigthmargin="0" topmargin="12" marginwidth="0" marginheight="0">
<html:errors />
<table class="simple" width="100%">
   <tr><td align="center">
         <table class="simple" width="210">
            <tr>
               <html:form action="/moduls/calendario">
               <td align="center" valign="middle">
                  <html:hidden property="field"/>
                  <html:select property="mes" onchange="this.form.submit()" style="width: 100px">
                     <html:option value="0"  key="mes.0"  />
                     <html:option value="1"  key="mes.1"  />
                     <html:option value="2"  key="mes.2"  />
                     <html:option value="3"  key="mes.3"  />
                     <html:option value="4"  key="mes.4"  />
                     <html:option value="5"  key="mes.5"  />
                     <html:option value="6"  key="mes.6"  />
                     <html:option value="7"  key="mes.7"  />
                     <html:option value="8"  key="mes.8"  />
                     <html:option value="9"  key="mes.9"  />
                     <html:option value="10" key="mes.10" />
                     <html:option value="11" key="mes.11" />
                  </html:select>&nbsp;&nbsp;
                  <html:select property="any" onchange="this.form.submit()" style="width: 60px">
                     <html:options name="anys" />
                  </html:select>
               </td>
               </html:form>
            </tr>
            <tr>
               <td width="210">
                  <bean:define id="firstDayPos" name="dataForm" property="firstDayPos" type="java.lang.Integer"/>
                  <bean:define id="lastDay" name="dataForm" property="lastDay" type="java.lang.Integer" />
                  <table class="simple" width="210" border="0" cellpadding="4" cellspacing="0">
                        <tr bgcolor="#d8dfe7">
                        <td align="center" width="30"><bean:message key="dia.1" /></td>
                        <td align="center" width="30"><bean:message key="dia.2" /></td>
                        <td align="center" width="30"><bean:message key="dia.3" /></td>
                        <td align="center" width="30"><bean:message key="dia.4" /></td>
                        <td align="center" width="30"><bean:message key="dia.5" /></td>
                        <td align="center" width="30"><bean:message key="dia.6" /></td>
                        <td align="center" width="30"><bean:message key="dia.7" /></td>
                     </tr>
                  </table>
                  <table class="simple" cellpadding="0" cellspacing="0" width="210">
                     <!-- <tr><td colspan="7">&nbsp;</td></tr> -->
                     <% for (int i=0; i<6; i++) { %>
                     <tr>
                        <% for (int j=0; j<7; j++) { %>
                           <% int dia = ((i*7) + j + 1) - firstDayPos.intValue() + 1;%>
                           <% if (dia > 0 && dia <= lastDay.intValue()) { %>
                              <td>
                                 <table class="dia" border="0" cellspacing="0" width="100%">
                                    <tr><td align="center" style="border: 1px solid #000000" onmouseover="style.backgroundColor='#d8dfe7'" onmouseout="style.backgroundColor='#ffffff'">
                                      <a href="javascript:void(0)" class="boto" style="text-decoration: none; color: #000000" onclick="marca(<%=dia%>)"><%=dia%></a>
                                    </td></tr>
                                 </table>
                              </td>
                           <% } else { %>
                              <td>
                                 <table class="dia" border="0" cellspacing="0" width="100%">
                                    <tr><td style="border: 1px solid #000000">&nbsp;</td></tr>
                                 </table>
                              </td>
                           <% } %>
                        <% } %>
                     </tr>
                     <% } %>
                  </table>
               </td>
            </tr>
         </table>
      </td>
   </tr>
</table>
</body>
</html:html>