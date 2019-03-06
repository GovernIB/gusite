<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
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
	
	<style type="text/css">
		#detalleLog { 
			display: block; 
			height: 300px; 
			background-color: #FFF; 
			width: 90%;
		}
	</style>
	
	<script type="text/javascript">
        
		function onMouseDown() {
			var accLog = document.getElementById('detalleLog');
			accLog.src = "detallelogexport.do";
		}

		function onMouseUp() {
			var accForm = document.getElementById('accFormulario');
			accForm.submit();
		}

		window.onload = function() {
			document.getElementById('btnExport').onmousedown = onMouseDown;
			document.getElementById('btnExport').onmouseup = onMouseUp;
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
			<h1><img src="imgs/titulos/configuracion.gif" alt="Llistats" /><bean:message key="micro.exportar.todos" />.</h1>
	
			</br>
	
			<logic:notPresent name="mensaje">
			
				<form action="archivo.do" method="get" id="accFormulario">
				
					<input type="hidden" name="accion" value="exportarArchivosDeTodosLosMicrosites"/>
								
					<!-- tabla listado -->
					<table id="llistatimportar">
						<thead>
							<tr>
								<th><bean:message key="micro.exportar.todos.cap" /></th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<td>
									<div id="botonera">										
										<button id="btnExport" name="fichero" title="<bean:message key="boton.exportar" />">
											<img src="imgs/botons/exportar.gif" alt="<bean:message key="boton.exportar" />" /> &nbsp;<bean:message key="boton.exportar" />
										</button>
										<span>Numero Máximo de elementos a tratar: </Span>
										<select id="numMaxElemTratar" name="numMaxElemTratar">
											<option value="1000"   >1.000</option>
											<option value="5000"   >5.000</option>
											<option value="10000" selected >10.000</option>
											<option value="30000"  >30.000</option>
											<option value="50000"  >50.000</option>
											<option value="100000" >100.000</option>
											<option value="150000" >150.000</option>
											<option value="200000" >200.000</option>
											<option value="250000" >250.000</option>
											<option value="300000" >300.000</option>
											<option value="350000" >350.000</option>
											<option value="400000" >400.000</option>
											<option value="0"      >Todos</option>															
										</select>
										
										<span>Desea omitir los ficheros ya tratados marcados con error?: </Span>
										<select id="omitirEnError" name="omitirEnError">
											<option value="0"   >NO</option>
											<option value="1"   >SI</option>																				
										</select>

									</div>					
								</td>
							</tr>
						</tfoot>
						<tbody>
							<tr>
								<td>
									<p>Los archivos presentes en base de datos se exportarán a la siguiente ruta: <strong><bean:write name="rutaArchivosEnFileSystem" ignore="true" /></strong></p>
								</td>
							</tr>
						</tbody>
					</table>
					<!-- /tabla listado -->
						
					</br>
				  	
				</form>
				
				</br>
				
				<center>
					<iframe id="detalleLog" name="detalleLog" src="" frameborder="0" scrolling="auto"></iframe>
				</center>			
				
			</logic:notPresent>
		
			<logic:present name="mensaje">
			
		  		<br/>
		  		
				<div class="alerta" style="font-weight: bold; color: #FF1111;">
					<strong><bean:message key="micro.exportar.todos.log" /> <bean:write name="MVS_fechaexportprocessor" format="dd/MM/yyyy HH:mm:ss"/>:</strong><br/>
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
 