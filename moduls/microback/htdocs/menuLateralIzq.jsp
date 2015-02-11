<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

	<bean:define id="puedoeditar" value="0" />

     <logic:present name="MVS_rol_sys_adm" >
	     <logic:equal  name="MVS_rol_sys_adm" value="yes">
        	 <bean:define id="puedoeditar" value="1" />
    	 </logic:equal>
     </logic:present>
          
	<!-- menu -->
	<ul id="menu">
		<li><a href="index.do"><bean:message key="menu2.gestion.microsites"/></a></li>
		<logic:equal name="puedoeditar" value="1">
			<li><a href="usuarios.do"><bean:message key="menu2.gestion.usuarios"/></a></li>
			<li><a href="consola.do?accion=consola"><bean:message key="log.consola" /></a></li>
			<li><a href="importar.do?accion=importar"><bean:message key="boton.importar" /></a></li>
			<li><a href="archivo.do?accion=exportarArchivosMicrosites">Exportar archivos de todos los Microsites</a></li>
			<li><a href="auditorias.do?accion=listarAuditorias"><bean:message key="auditoria.consultar" /></a></li>
		</logic:equal>
	</ul>
	<!-- /menu -->
			