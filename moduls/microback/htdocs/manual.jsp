<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

<body>	
		<!-- molla pa -->
		<ul id="mollapa">
			<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
			<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
			<li class="pagActual"><bean:message key="manual.titul.mollapa" /></li>
		</ul>
		<!-- titol pagina -->
		<h1><bean:message key="manual.titul" /> <span> <bean:message key="manual.titul.subtitul" /></span></h1>

		<!-- continguts -->
		
		<p><bean:message key="manual.contingut" /></p>
		
		
		<table cellpadding="0" cellspacing="0" class="llistat llistatMini noPointer" style="width:50%;">
	  	<thead>
			<tr>
				<th>&nbsp;</th>
				<th><bean:message key="manual.quadre" /></th>
			</tr>
		</thead>
		<tbody>
		      <tr>
				  <td>
				       <button type="button" title="<bean:message key="manual.contingut.manual" />" onclick="abrirWindow('http://intranet.caib.es/info/DGTIC-PORSIT-MAN-001-v1.pdf');"><img src="imgs/botons/pdfbig.gif" alt="<bean:message key="manual.contingut.manual" />" /></button> 
			      </td>      
			      <td>
				      	<bean:message key="manual.contingut.manual" />
			      </td>
			  </tr>
	    	  <tr>
				  <td>
				     <button type="button" title="<bean:message key="manual.contingut.fullaestil.corporativa" />" onclick="abrirWindow('/sacmicrofront/v4/css/estils.css');"><img src="imgs/botons/cssbig.gif" alt="<bean:message key="manual.contingut.fullaestil.corporativa" />" /></button> 
			      </td>      
			      <td>
				     <bean:message key="manual.contingut.fullaestil.corporativa" />
			      </td>
			  </tr>
			  <tr>
				  <td>
				     <button type="button" title="<bean:message key="manual.contingut.fullaestil.corporativa.blau" />" onclick="abrirWindow('/sacmicrofront/v4/css/estils_blau.css');"><img src="imgs/botons/cssbig.gif" alt="<bean:message key="manual.contingut.fullaestil.corporativa.blau" />" /></button> 
			      </td>      
			      <td>
				     <bean:message key="manual.contingut.fullaestil.corporativa.blau" />
			      </td>
			  </tr>
			  <tr>
				  <td>
				     <button type="button" title="<bean:message key="manual.contingut.fullaestil.corporativa.vermell" />" onclick="abrirWindow('/sacmicrofront/v4/css/estils_roig.css');"><img src="imgs/botons/cssbig.gif" alt="<bean:message key="manual.contingut.fullaestil.corporativa.vermell" />" /></button> 
			      </td>      
			      <td>
				     <bean:message key="manual.contingut.fullaestil.corporativa.vermell" />
			      </td>
			  </tr>
	    	  <tr>
				  <td>
				     <button type="button" title="<bean:message key="manual.contingut.fullaestil.corporativa.verd" />" onclick="abrirWindow('/sacmicrofront/v4/css/estils_verd.css');"><img src="imgs/botons/cssbig.gif" alt="<bean:message key="manual.contingut.fullaestil.corporativa.verd" />" /></button> 
			      </td>      
			      <td>
				     <bean:message key="manual.contingut.fullaestil.corporativa.verd" />
			      </td>
			  </tr>
			  <tr>
				  <td>
				     <button type="button" title="<bean:message key="manual.contingut.fullaestil.corporativa.groc" />" onclick="abrirWindow('/sacmicrofront/v4/css/estils_groc.css');"><img src="imgs/botons/cssbig.gif" alt="<bean:message key="manual.contingut.fullaestil.corporativa.groc" />" /></button> 
			      </td>      
			      <td>
				     <bean:message key="manual.contingut.fullaestil.corporativa.groc" />
			      </td>
			  </tr>
			  <tr>
				  <td>
				     <button type="button" title="<bean:message key="manual.contingut.fullaestil.corporativa.magenta" />" onclick="abrirWindow('/sacmicrofront/v4/css/estils_morat.css');"><img src="imgs/botons/cssbig.gif" alt="<bean:message key="manual.contingut.fullaestil.corporativa.magenta" />" /></button> 
			      </td>      
			      <td>
				     <bean:message key="manual.contingut.fullaestil.corporativa.magenta" />
			      </td>
			  </tr>
	      	  <tr>
				  <td>
				     <button type="button" title="<bean:message key="manual.contingut.fullaestil" />" onclick="abrirWindow('/sacmicrofront/css/estilos01_blau.css');"><img src="imgs/botons/cssbig.gif" alt="<bean:message key="manual.contingut.fullaestil" />" /></button> 
			      </td>      
			      <td>
				     <bean:message key="manual.contingut.fullaestil" />
			      </td>
			  </tr>
    	</tbody>
    	</table>
		
		

</body>
</html>
<jsp:include page="/moduls/pieControl.jsp"/>