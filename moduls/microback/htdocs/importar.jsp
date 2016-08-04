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
	<link href="css/estils-v.4.1.css" rel="stylesheet" type="text/css" media="screen" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<script type="text/javascript" src="js/rArxius.js"></script>
    <script type="text/javascript" src="js/subMenus.js"></script>
	 <!--[if lt IE 7]>
		<link rel="stylesheet" type="text/css" href="css/estils-v.4.1_ie6.css" media="screen" />
	<![endif]-->
	<style>
	#detalleLog { display:block; height:300px; background-color:#FFF; width:90%; }
	</style>
	<script type="text/javascript">
	
            function onMouseDown() {
            	if (document.getElementById('detalleLog')) {
					var accLog = document.getElementById('detalleLog');
					accLog.src = "detallelogimport.do";
            	}
            }

            function onMouseUp() {
            	if (document.getElementById('accFormulario')) {
            		var accForm = document.getElementById('accFormulario');
					accForm.submit();
            	}
            }

            window.onload = function() {
            	if (document.getElementById('bntImport')) {
	            	document.getElementById('bntImport').onmousedown = onMouseDown;
            		document.getElementById('bntImport').onmouseup = onMouseUp;
            	}
	        }
 
	</script>		
</head>

<body>
<!-- contenidor -->
	<div id="contenidor">
	
		<!-- cap -->
		<jsp:include page="cabecera.jsp"/>
		<!-- /cap -->
		
		<!-- marc lateral -->
		<jsp:include page="menuLateralIzq.jsp"/>
		<!-- /marc lateral -->
		
		<!-- continguts -->
		<div id="continguts">
	 
	
	<!-- titol pagina -->
			<h1><img src="imgs/titulos/configuracion.gif" alt="Llistats" /><bean:message key="micro.importar" />.</h1>

	</br>

	<logic:notPresent name="mensaje">
		<html:form action="importar.do" enctype="multipart/form-data"   styleId="accFormulario">
		<!-- tabla listado -->
		<table id="llistatimportar">
			<thead>
				<tr>
					<th><bean:message key="micro.importar.cap" /></th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td>
						<div id="botonera">
							<button type="button" id="bntImport" name="fichero" title="<bean:message key="boton.importar" />" ><img src="imgs/botons/importar.gif" alt="<bean:message key="boton.importar" />" /> &nbsp;<bean:message key="boton.importar" /></button>
						</div>					
					</td>
				</tr>
			</tfoot>
			<tbody>
				<tr>
					<td>
					<table cellpadding="0" cellspacing="0" class="edicio">
						<tr class="par">
							<td class="etiqueta"><bean:message key="micro.seleccimport" /></td>
							<td><html:file property="archi" size="40" /></td>
						</tr>
						<tr>
							<td class="etiqueta"><bean:message key="micro.nombrenuevo" /></td>
							<td><html:text property="nuevonombre" size="40" /></td>
						</tr>
					</table>
					</br>
					<bean:message key="micro.importar.tarea" /> </br>
					</br>
					<table cellpadding="0" cellspacing="0" class="edicio">
						<tr class="par">
							<td><label><html:radio property="tarea" value="R" />&nbsp;<bean:message
								key="micro.importar.reemplazar" /></label><br />
							<label><html:radio property="tarea" value="C" />&nbsp;<bean:message
								key="micro.importar.crear" /></label></td>
						</tr>

					</table>
					</br>
					<bean:message key="micro.importar.indexar" /> </br>
					</br>
					<table cellpadding="0" cellspacing="0" class="edicio">
						<tr class="par">
							<td><label><html:radio property="indexar" value="S" />&nbsp;S&iacute;&nbsp;&nbsp;&nbsp;</label><label><html:radio
								property="indexar" value="N" />&nbsp;No</label>
							&nbsp;&nbsp;&nbsp;&nbsp;<i><bean:message
								key="micro.importar.lento" /></i></td>
						</tr>
					</table>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- /tabla listado -->		
		</br>
		  	
		</html:form>
	</br>		
					<center>
						<iframe id="detalleLog" name="detalleLog" src="" frameborder="0" scrolling="auto"></iframe>
					</center>			
		
	</logic:notPresent>
	
	<logic:present name="mensaje">
  		<br/>
		<div class="alerta" style="font-weight:bold; color:#FF1111;">
			<bean:write name="mensaje" filter="false"/>	
			<br/><br/>
			<strong><bean:message key="micro.importar.log" />:</strong><br/>
			<bean:write name="MVS_importprocessor" ignore="true" filter="false"/>
		</div>	  
	  
	</logic:present>
	</div>
		<!-- /continguts -->
		<!-- peu -->
		<jsp:include page="peu.jsp"/>
		<!-- /peu -->			
	</div>
	<!-- contenidor -->
</body>
</html>
 