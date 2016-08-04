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
    <table id="cMenu" cellpadding="0" cellspacing="0">
        <tbody><tr>
		<td><a href="index.do"><bean:message key="menu2.gestion.microsites"/></a></td>
		<logic:equal name="puedoeditar" value="1">
			<td><a href="usuarios.do"><bean:message key="menu2.gestion.usuarios"/></a></td>
            <td><a href="temaFront.do"><bean:message key="menu2.gestion.temes"/></a></td>
            <td><a href="#" onmouseup="voreMenu(this, event, 'mFerramentes');"><bean:message key="menu.ferramentes" /></a></td>
		</logic:equal>
        </tr></tbody>
    </table>
	<!-- /menu -->
    <!-- submenu -->
    <div id="mFerramentes" class="submenu">
        <p><bean:message key="menu.ferramentes" /></p>
        <a href="importar.do?accion=importar"><bean:message key="boton.importar" /></a>
        <a href="archivo.do?accion=exportarArchivosMicrosites">Exportar archivos de todos los Microsites</a>
        <a href="auditorias.do?accion=listarAuditorias"><bean:message key="auditoria.consultar" /></a>
    </div>
    <!-- /submenu -->

			