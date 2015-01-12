<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
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
			<li class="pagActual"><bean:message key="pindoles.titul.mollapa" /></li>
		</ul>
		<!-- titol pagina -->
		<h1><bean:message key="pindoles.titul" /> <span><bean:message key="pindoles.titul.subtitul" /></span></h1>

		<!-- continguts -->
		
		<p><bean:message key="pindoles.contingut" /></p>
		
		
		<table cellpadding="0" cellspacing="0" class="llistat llistatMini noPointer" style="width:50%;">
	  	<thead>
			<tr>
				<th>&nbsp;</th>
				<th><bean:message key="pindoles.quadre" /></th>
			</tr>
		</thead>
		<tbody>
		      <tr>
				  <td>
				       <button type="button" title="<bean:message key="pindoles.contingut.arbre" />" onclick="abrirWindow('http://intranet.caib.es/info/arbol1.html');"><img src="imgs/botons/htmlbig.gif" alt="<bean:message key="pindoles.contingut.arbre" />" /></button> 
			      </td>      
			      <td>
				      	<bean:message key="pindoles.contingut.arbre" />
			      </td>
			  </tr>
	    
		      <tr>
				  <td>
				       <button type="button" title="<bean:message key="pindoles.contingut.component" />" onclick="abrirWindow('http://intranet.caib.es/info/componentes2.html');"><img src="imgs/botons/htmlbig.gif" alt="<bean:message key="pindoles.contingut.component" />" /></button> 
			      </td>      
			      <td>
				      	<bean:message key="pindoles.contingut.component" />
			      </td>
			  </tr>
			  
		      <tr>
				  <td>
				       <button type="button" title="<bean:message key="pindoles.contingut.enquesta" />" onclick="abrirWindow('http://intranet.caib.es/info/encuesta1.html');"><img src="imgs/botons/htmlbig.gif" alt="<bean:message key="pindoles.contingut.enquesta" />" /></button> 
			      </td>      
			      <td>
				      	<bean:message key="pindoles.contingut.enquesta" />
			      </td>
			  </tr>			  
    	</tbody>
    	</table>

</body>
</html>
<jsp:include page="/moduls/pieControl.jsp"/>