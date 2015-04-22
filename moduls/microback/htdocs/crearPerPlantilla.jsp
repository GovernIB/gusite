<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="es.caib.gusite.micromodel.Microsite" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="es.caib.gusite.micromodel.Accesibilidad"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>Gestor Microsites</title>

    <!-- CSS -->
    <link type="text/css" rel="stylesheet" href="css/index.css" />
    <link type="text/css" rel="stylesheet" href="css/estils.css" />
    <link type="text/css" rel="stylesheet" href="css/jquery.alerts.css" media="screen" />
    <link type="text/css" rel="stylesheet" href="css/editorCSS.css" />

    <!-- Core files -->
    <script type="text/javascript" src="js/jquery/jquery-1.3.2.min.js"></script>
    <script type="text/javascript" src="js/jquery/jquery.alerts.js" ></script>
    <script type="text/javascript" src="js/guardarTemaPlantilla.js" ></script>
</head>
<body>
    <!-- cap -->
    <logic:notPresent name="MVS_microsite">
        <jsp:include page="cabecera.jsp"/>
        <jsp:include page="menuLateralIzq.jsp"/>
    </logic:notPresent>
    <!-- /cap -->

    <div id="formulario">

        <!-- molla pa -->
        <logic:present name="MVS_microsite">
            <ul id="mollapa">
                <li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
                <li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
                <li><bean:message key="menu.configuracion" /></li>
                <li class="pagActual"><bean:message key="menu.plantillas" /></li>
            </ul>
        </logic:present>

        <!-- titol pagina -->
        <h1>
            <img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.general" />" />
            <bean:message key="menu.plantilla" />. <span><bean:message key="plantilla.creacion" /></span>
        </h1>

        <!-- botonera -->
        <div id="botonera">
            <span class="grup">
                <logic:notPresent name="MVS_microsite">
                <button type="button" title='<bean:message key="frontTemas.volvermantenimiento"/>' onclick='document.location.href="temaFrontEdita.do?id=<bean:write name="TemaFrontForm" property="id"/>";'>
                </logic:notPresent>
                <logic:present name="MVS_microsite">
                <button type="button" title='<bean:message key="micro.boto.tornargestio.titol"/>' onclick='window.parent.location.href="index.do?idsite=<bean:write name="MVS_microsite" property="id"/>";'>
                </logic:present>
                    <img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.titol"/>' />
                </button>
            </span>
        </div>

        <div id="capaCreacion" class="capaFormIdioma">
            <table cellpadding="0" cellspacing="0" class="edicio">
                <tr>
                    <td class="etiqueta"><bean:message key="plantilla.creacion.tipo" /> &gt;</td>
                    <td>
                        <select id="tipus" size="1">
                            <option disabled selected> -- select an option -- </option>
                            <logic:iterate id="plan" name="plantillas" indexId="i">
                                <option value="<bean:write name="plan" property="id"/>"><bean:write name="plan" property="nombre"/>: <bean:write name="plan" property="titulo"/></option>
                            </logic:iterate>
                        </select>
                    </td>
                </tr>
                <tr></tr>
            </table>
        </div>
    </div>
</body>