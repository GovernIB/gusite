<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!--[if !IE]><!--><meta http-equiv="X-UA-Compatible" content="IE=edge" /><!--<![endif]-->
	<title>Gestor Microsites</title>
	<link href="css/index.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/index.js"></script>
	<script type="text/javascript" src="js/util.js"></script>
	
</head>

<body>
	<bean:define id="puedoeditar" value="0" />	
	<logic:present name="MVS_rol_sys_adm" >
		<logic:equal  name="MVS_rol_sys_adm" value="yes">
			<bean:define id="puedoeditar" value="1" />
		</logic:equal>
	</logic:present>
	
	<div id="cap">
		<div id="titol"><a href="http://intranet.caib.es"><img src="imgs/logo_caib.gif" alt="Govern de les Illes Balears" /></a><img src="imgs/logo_mini.gif" alt="Microsites" /></div>
		<div id="versio"></div>
		<div id="mTitol">
		<logic:present name="tituloMicro">
			&gt; <bean:write name="tituloMicro" ignore="true"/>
			<form name="formCab" action="">
				<input type=hidden name="idMicro_cab" id="idMicro_cab" value="<bean:write name="MVS_microsite" property="id"/>" >
			</form>
		</logic:present>		
		</div>
		<!-- usuari -->
		<div id="usuari">
			<bean:message key="identificacion.usuario" />
			<bean:write name="MVS_usuario" property="nombre" />			
			(<bean:write name="username" ignore="true" />)
		</div>
		<!-- /usuari -->
	</div>	
	
	<logic:present name="MVS_manteniment">
		<div align="center" style="border:2px solid; border-radius:5px; padding:10px; background:yellow">
			<bean:message key="mantenimiento"/>
		</div>
	</logic:present>
	

		
	<table id="cMenu" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td><a href="#" onmouseup="voreMenu(this, event, 'mConfiguracio');"><bean:message key="menu.configuracion" /></a></td>
			<td><a href="#" onmouseup="voreMenu(this, event, 'mContinguts');"><bean:message key="menu.contenidos" /></a></td>
			<td><a href="#" onmouseup="voreMenu(this, event, 'mRecursos');"><bean:message key="menu.recursos" /></a></td>
            <logic:present name="MVS_microsite">
            <logic:notEqual name="MVS_microsite" property="versio" value="v5">
                <td><a href="#" onmouseup="voreMenu(this, event, 'mEstadistiques');"><bean:message key="menu.estadisticas" /></a></td>
            </logic:notEqual>
            <logic:equal name="MVS_microsite" property="versio" value="v5">
                <logic:notEmpty name="MVS_microsite" property="analytics">
                    <td><a href="#" onmouseup="window.open('https://www.google.com/analytics/', '_blank')"><bean:message key="menu.analytics" /></a></td>
                </logic:notEmpty>
                <logic:empty name="MVS_microsite" property="analytics">
                    <td><a href="#" onmouseup="alert('<bean:message key="menu.noAnalytics" />')"><bean:message key="menu.analytics" /></a></td>
                </logic:empty>
            </logic:equal>
            </logic:present>
			<logic:equal name="puedoeditar" value="1">
				<td><a href="#" onmouseup="voreMenu(this, event, 'mFerramentes');"><bean:message key="menu.ferramentes" /></a></td>
			</logic:equal>
			<td><a href="#" onmouseup="voreMenu(this, event, 'mAjuda');"><bean:message key="menu.ayuda" /></a></td>
		</tr>
	</tbody>
	</table>
	
	<div id="mConfiguracio" class="submenu">
		<p><bean:message key="menu.configuracion" /></p>
		<logic:present name="MVS_microsite">
			<a href="microEdita.do?accion=general&idsite=<bean:write name="MVS_microsite" property="id"/>" target="escritori"><bean:message key="menu.general" /></a>
			<a href="cabeceraPieEdita.do?accion=cabpie&idsite=<bean:write name="MVS_microsite" property="id"/>" target="escritori"><bean:message key="menu.cabecerapie" /></a>
            <logic:equal name="MVS_microsite" property="versio" value="v5">
                <a href="perPlantillas.do?idsite=<bean:write name="MVS_microsite" property="id"/>" target="escritori"><bean:message key="menu.plantillas" /></a>
            </logic:equal>
            <logic:iterate id="i" name="MVS_menugenerico" indexId="indice">
                <logic:equal name="i" property="value" value="Llistats">
                    <a href="tipos.do" target="escritori"><bean:message key="menu.listados" /></a>
                    <a href="componentes.do" target="escritori"><bean:message key="menu.componentes" /></a>
                </logic:equal>
            </logic:iterate>
			<a href="paginaInicioEdita.do?idsite=<bean:write name="MVS_microsite" property="id"/>" target="escritori"><bean:message key="menu.home" /></a>
			<logic:equal name="puedoeditar" value="1">
				<a href="microUsuarios.do?idsite=<bean:write name="MVS_microsite" property="id"/>" target="escritori"><bean:message key="menu.usuarios" /></a>
			</logic:equal>
		</logic:present>
	</div>
	<div id="mContinguts" class="submenu">
		<p><bean:message key="menu.contenidos" /></p>
		<logic:present name="MVS_microsite">			
				<a href="menuEdita.do" target="escritori"><bean:message key="menu.arbol" /></a>
				<a href="recursosEdita.do" target="escritori"><bean:message key="menu.archivos" /></a>
		</logic:present>
	</div>
	<div id="mRecursos" class="submenu">
		<p><bean:message key="menu.recursos" /></p>
		<logic:present name="MVS_microsite">			
					<logic:iterate id="i" name="MVS_menugenerico" indexId="indice">
						<a href="<bean:write name="i" property="key"/>" target="escritori"><bean:write name="i" property="value" filter="false"/></a>
					</logic:iterate>
		</logic:present>
	</div>
    <div id="mEstadistiques" class="submenu">
        <p><bean:message key="menu.estadisticas" /></p>
        <logic:present name="MVS_microsite">
            <a href="estadisticagen.do" target="escritori"><bean:message key="menu.estadisticas.gen" /></a>
            <a href="estadisticaind.do" target="escritori"><bean:message key="menu.estadisticas.ind" /></a>
            <logic:iterate id="i" name="MVS_menugenerico" indexId="indice">
                <logic:equal name="i" property="value" value="Encuestas">
                    <a href="estadisticaenc.do" target="escritori"><bean:message key="index.inicio.statenc" /></a>
                </logic:equal>
            </logic:iterate>
        </logic:present>
    </div>
	<div id="mFerramentes" class="submenu">
		<logic:present name="MVS_microsite">
			<logic:equal name="puedoeditar" value="1">
					<p><bean:message key="menu.ferramentes" /></p>
					<logic:present name="MVS_microsite">			
							<a href="exportador.do" target="escritori"><bean:message key="menu.exportar" /></a>
							<a href="indexador.do" target="escritori"><bean:message key="menu.indexar" /></a>
							<a href="desindexa.do" target="escritori"><bean:message key="menu.desindexar" /></a>
					</logic:present>
			</logic:equal>	
			<a href="procesow3c.do?idsite=<bean:write name="MVS_microsite" property="id"/>" target="escritori"><bean:message key="menu.accesibilitat" /> </a>			
		</logic:present>	
	</div>	
	<div id="mAjuda" class="submenu">
		<p><bean:message key="menu.ayuda" /></p>
			<logic:present name="MVS_microsite">
				<a href="manual.do?tipo=manual" target="escritori"><bean:message key="menu.manual" /></a>
				<a href="manual.do?tipo=pildora" target="escritori"><bean:message key="menu.pildoras" /></a>
			</logic:present>
	</div>
	
	<bean:message key="errors.explorernou" />	
	
	<div id="errorIE11" style="display: none;"><bean:message key="errors.explorer.sup11" /> </div>
			
	<logic:present name="MVS_microsite">
	<logic:present name="microGeneral">
		<iframe id="escritori" name="escritori" src="microGeneral.jsp" frameborder="0" scrolling="auto"></iframe>
	</logic:present>
	<logic:present name="microCabPie">
		<iframe id="escritori" name="escritori" src="microCabpie.jsp" frameborder="0" scrolling="auto"></iframe>
	</logic:present>
	</logic:present>

	<logic:notPresent name="microGeneral">
	<logic:notPresent name="microCabPie">
	<logic:notPresent name="MVS_index_con_info">
		<logic:present name="MVS_pagina_inicio">
			<iframe id="escritori" name="escritori" src="<bean:write name="MVS_pagina_inicio" />" frameborder="0" scrolling="auto"></iframe>
		</logic:present>
		<logic:notPresent name="MVS_pagina_inicio">
			<iframe id="escritori" name="escritori" src="index_inicio.do" frameborder="0" scrolling="auto"></iframe>
		</logic:notPresent>
	</logic:notPresent>
	</logic:notPresent>
	</logic:notPresent>

	<logic:present name="MVS_index_con_info">
		<br/>
		<br/>
		<div class="encabezado"><bean:message key="menu.mensaje" />. </div>
	
		<div class="alerta" style="color:#FF1111;">
			<html:messages id="message" message="true">
			<%= message %><br/>
			</html:messages>		
		</div>
		<iframe id="escritori" name="escritori" frameborder="0" scrolling="auto"></iframe>
	</logic:present>

</body>
</html>

<script type="text/javascript">

	getInternetExplorerVersion();

</script>