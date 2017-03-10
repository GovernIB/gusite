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
	<script type="text/javascript" src="js/jsListados.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<script type="text/javascript"	src="js/jquery/jquery-1.3.2.min.js"></script>
</head>

<body>	
		<!-- molla pa -->
		<ul id="mollapa">
			<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
			<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
			<li><bean:message key="menu.ferramentes" /></li>
			<li class="pagActual"><bean:message key="menu.indexar" /></li>
		</ul>
		<!-- titol pagina -->
		<h1><img src="imgs/titulos/indexar.gif" alt="<bean:message key="menu.indexar" />" />
		<bean:message key="menu.indexar" /> </h1>

		<!-- continguts -->
		
		<div id="botonera">
				<button type="button" name="fichero" title="<bean:message key="menu.indexar" />" onclick='submitURL(<bean:write name="MVS_microsite" property="id"/>);'><img src="imgs/botons/indexar.gif" alt="<bean:message key="menu.indexar" />" /> &nbsp;<bean:message key="menu.indexar" /></button>
				<button type="button" name="fichero" title="<bean:message key="menu.indexar.verinfo" />" onclick='indexarVerInfo(<bean:write name="MVS_microsite" property="id"/>);'><img src="imgs/botons/indexar.gif" alt="<bean:message key="menu.indexar.verinfo" />" /> &nbsp;<bean:message key="menu.indexar.verinfo" /></button>
		</div>		
		
		<p>
		<strong>
			<bean:message key="indexador.p1" /><br/> 
			<bean:message key="indexador.p2" /><br/>
			<bean:message key="indexador.p3" /><br/>
		</strong>
		</p>	
		<logic:notEmpty name="ok">			
		<div class="alerta" style="font-weight:bold; color:#FF1111;">
			<html:messages id="message" message="true">
			<%= message %><br/>
			</html:messages>	
			
		</div>
		</logic:notEmpty>
		
		<logic:notEmpty name="nok">			
		<div class="alerta" style="font-weight:bold; color:#FF1111;">
			<html:messages id="message" message="true">
				<bean:message key="menu.indexar.error.ejecutandose" />
			</html:messages>	
		</div>
		</logic:notEmpty>
		
		<!-- Se muestra la tabla con mas info de los registros job -->
		<logic:notEmpty name="mostrarinfo">		
		<div style="font-weight:bold;">
			<logic:notEmpty name="listado">
					<p></p><p></p>
					<table cellpadding="0" cellspacing="0" class="llistat" style="width:78%;">
					 <thead>
						<tr>
							<th width="15%"><bean:message key="menu.indexar.cab.fechaIni" /></th>
							<th width="15%"><bean:message key="menu.indexar.cab.fechaFin" /></th>	
							<th width="15%"></th>					
						</tr>
					</thead>
					<tbody>
						<logic:iterate id="i" name="listado" indexId="indice">
						       <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
						       		 <td><bean:write name="i" property="fechaIni" formatKey="date.short.format"/></td>
								     <td><bean:write name="i" property="fechaFin" formatKey="date.short.format"/></td>
								     <td>
									 	<button type="button" name="fichero" title="<bean:message key="menu.indexar.verinfo" />" onclick='pintarPopUp(limpiarTexto("<bean:write name="i" property="info" />  "))'><img src="imgs/botons/indexar.gif" alt="<bean:message key="menu.indexar.verinfo" />" /> &nbsp;<bean:message key="menu.indexar.verinfo" /></button>
								     </td>
								   
						       </tr>					
					    </logic:iterate>
				    </tbody>
				    </table>
			    </logic:notEmpty>	
			
		</div>
		</logic:notEmpty>
		
		
		<div id="popup" class="popup" style="display: none;">
    		<div class="content-popup">
        		<div class="close"><a href="#" onclick="cerrarPopUp();" id="close"><img src="imgs/botons/cerrar.gif"/></a></div>
        		<div>
            		<textarea id="item_texto" name="item_observacions" cols="70" rows="15" class="nou"></textarea>     
        		</div>
    		</div>
		</div>
</body>
</html>

<script>
	function submitURL(idsite){
		document.location="indexarMicrosite.do?indexar=si&site="+idsite;
	}
	
	function indexarVerInfo(idsite){
		document.location.href="indexarMicrosite.do?indexar=verinfo&site="+idsite;
	}
	
	function limpiarTexto(descripcion) {
		while (descripcion.indexOf("'") != -1) {
			 descripcion = descripcion.replace("'","");
		}
		while (descripcion.indexOf("<br/>") != -1) {
			 descripcion = descripcion.replace("<br/>","\n");
		}
		while (descripcion.indexOf("<br />") != -1) {
			 descripcion = descripcion.replace("<br />","\n");
		}
		return descripcion;			
	}
	function pintarPopUp(descripcion){
		 $('.popup').fadeIn('slow');
	     $('.popup-overlay').fadeIn('slow');
	     $('.popup-overlay').height($(window).height());
		 
	     $("#item_texto").html(descripcion);
	     return false;
	}
	
	function cerrarPopUp(){
		 $('.popup').fadeOut('slow');
	     $('.popup-overlay').fadeOut('slow');
	     $("#item_texto").html("");
	     return false;
	}
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
