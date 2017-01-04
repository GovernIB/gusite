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
</head>

<body>	
 
    
	<logic:present name="MVS_microsite">
	   	<iframe src="../../sacmicrofront/previsualiza.do?idsite=<bean:write name="MVS_microsite" property="id"/>" width="0" height="0"></iframe>
		<!-- molla pa -->
		<ul id="mollapa">
			<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
			<li class="pagActual"><bean:message key="op.7" /></li>
		</ul>
		<!-- titol pagina -->
		<h1><bean:message key="op.7" />. <span><bean:message key="index.inicio.hoy" /></span></h1>

		<!-- continguts -->
		<div id="pagInici">
			<div class="columnaUM">
				<h2><bean:message key="index.inicio.ultimos" /></h2>
				<ul>
				<bean:size id="tamano" name="MVS_ultimsmodificats"/>
					<logic:notEqual name="tamano" value="0">
						<logic:iterate id="i" name="MVS_ultimsmodificats" indexId="indice">
							<li><a href="contenidoEdita.do?id=<bean:write name="i" property="key"/>"><bean:write name="i" property="value"/></a></li>
						</logic:iterate>
					</logic:notEqual>
					<logic:equal name="tamano" value="0">
							<li><bean:message key="index.inicio.ultimos.vacio" /></li>
					</logic:equal>
				</ul>
			</div>
			<div class="columna">
				<h2><bean:message key="menu.configuracion" /></h2>
				<ul>
					<li><a href="microEdita.do?accion=general&idsite=<bean:write name="MVS_microsite" property="id"/>"><bean:message key="index.inicio.general" /></a></li>
					<li><a href="cabeceraPieEdita.do?accion=cabpie&idsite=<bean:write name="MVS_microsite" property="id"/>"><bean:message key="index.inicio.cappeu" /></a></li>
                    <li><a href="perPlantillas.do?idsite=<bean:write name="MVS_microsite" property="id"/>" target="escritori"><bean:message key="menu.plantillas" /></a></li>
						<logic:iterate id="i" name="MVS_menugenerico" indexId="indice">
							<logic:equal name="i" property="value" value="Listados">
								<li><a href="tipos.do"><bean:message key="index.inicio.listados" /></a></li>
								<li><a href="componentes.do"><bean:message key="index.inicio.componentes" /></a></li>
							</logic:equal>
						</logic:iterate>
					<li><a href="paginaInicioEdita.do?idsite=<bean:write name="MVS_microsite" property="id"/>"><bean:message key="index.inicio.home" /></a></li>
					<logic:equal name="puedoeditar" value="1">
					<li><a href="microUsuarios.do?idsite=<bean:write name="MVS_microsite" property="id"/>" target="escritori"><bean:message key="menu.usuarios" /></a></li>
					</logic:equal>
				</ul>
				
			</div>
			<div class="columna">
			<h2><bean:message key="menu.contenidos" /></h2>
				<ul>
					<li><a href="menuEdita.do"><bean:message key="index.inicio.arbol" /></a></li>
					<li><a href="recursosEdita.do"><bean:message key="index.inicio.archivo" /></a></li>
				</ul>
			</div>
			<div class="columna">
				<h2><bean:message key="menu.recursos" /></h2>
				<ul>
					<logic:iterate id="i" name="MVS_menugenerico" indexId="indice">
						<li><a href="<bean:write name="i" property="key"/>"><bean:write name="i" property="value" filter="false"/></a></li>
					</logic:iterate>				
				</ul>
			</div>
			<div id="estAju">
				<div class="columna2 estadistiques">
					<h2><bean:message key="menu.estadisticas" /></h2>
					<ul>
						<logic:notEqual name="MVS_microsite" property="versio" value="v5">
							<li><a href="estadisticagen.do"><bean:message key="index.inicio.statgen" /></a></li>
							<li><a href="estadisticaind.do"><bean:message key="index.inicio.statind" /></a></li>
						</logic:notEqual>
						
						<!-- Si tiene analytics realiza lo mismo que el botón del menú -->
						<logic:equal name="MVS_microsite" property="versio" value="v5">
							<logic:notEmpty name="MVS_microsite" property="analytics">
	                    		<li><a href="#" onmouseup="window.open('https://www.google.com/analytics/', '_blank')"><bean:message key="menu.veure.analytics" /></a></li>
	                		</logic:notEmpty>
	                		<logic:empty name="MVS_microsite" property="analytics">
								<li><a href="estadisticagen.do"><bean:message key="index.inicio.statgen" /></a></li>
								<li><a href="estadisticaind.do"><bean:message key="index.inicio.statind" /></a></li>
							</logic:empty>
                		</logic:equal>
						
						<logic:iterate id="i" name="MVS_menugenerico" indexId="indice">
							<logic:equal name="i" property="value" value="Encuestas">
								<li><a href="estadisticaenc.do"><bean:message key="index.inicio.statenc" /></a></li>
							</logic:equal>
						</logic:iterate>					
					</ul>
				</div>
				<div class="columna2 ajuda">
					<h2><bean:message key="menu.ayuda" /></h2>
					<ul>
						<li><a href="manual.do?tipo=manual"><bean:message key="index.inicio.manual" /></a></li>
						<li><a href="manual.do?tipo=pildora"><bean:message key="index.inicio.pildora" /></a></li>
					</ul>
				</div>
			</div>
			<div id="estUrl">
				&nbsp;URL pública: <a href="<bean:write name="MVS_urlpublica"/>" target="_blank"><bean:write name="MVS_urlpublica"/></a>
			</div>
			<div id="version"><bean:write name="MVS_einaversion" ignore="true"/><a href="<bean:write name="MVS_urlrevision" ignore="true"/>" target="_blank"><bean:write name="MVS_einarevision" ignore="true"/></a>
			</div>
		</div>
	</logic:present>
	
	<logic:notPresent name="MVS_microsite">
		<!-- molla pa -->
		<ul id="mollapa">
			<li class="pagActual"><bean:message key="op.7" /></a></li>
		</ul>
		<!-- titol pagina -->
		<h1><bean:message key="menu.mensaje" />. </h1>
			<div class="alerta" style="font-weight:bold; color:#FF1111;">
				<bean:message key="index.inicio.nomicrosite" /> <br/><br/>
				<em><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></em>
			</div>
	</logic:notPresent>	
	
	
</body>
</html>
