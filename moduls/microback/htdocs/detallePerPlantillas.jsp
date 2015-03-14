<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

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
    <link type="text/css" rel="stylesheet" href="css/editorCSS.css" />

    <!-- Javascript -->
    <script type="text/javascript" src="js/jquery/jquery-1.3.2.min.js"></script>
    <script type="text/javascript" src="js/subMenus.js"></script>
    <script type="text/javascript" src="js/guardarTemaPlantilla.js"></script>
    <script type="text/javascript" src="js/ace/ace.js"></script>
    <script type="text/javascript" src="js/ace/ext-language_tools.js"></script>
    <script type="text/javascript" src="js/ace/editorConfig.js"></script>
    <script type="text/javascript" src="js/ace/ext-textarea.js"></script>
    <script type="text/javascript" src="js/ace/mode-css.js"></script>
    <script type="text/javascript" src="js/ace/mode-html.js"></script>
    <script type="text/javascript" src="js/ace/worker-css.js"></script>
    <script type="text/javascript" src="js/ace/worker-html.js"></script>
    <script type="text/javascript" src="js/ace/theme-chrome.js"></script>
</head>
<body>
<div id="contenidor">

    <!-- cap -->
    <logic:notPresent name="MVS_microsite">
        <jsp:include page="cabecera.jsp"/>
        <jsp:include page="menuLateralIzq.jsp"/>
    </logic:notPresent>
    <!-- /cap -->

    <!-- molla pa -->
    <logic:present name="MVS_microsite">
        <ul id="mollapa">
            <li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
            <li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
            <li><bean:message key="menu.configuracion" /></li>
            <li class="pagActual"><bean:message key="menu.plantillas" /></li>
        </ul>
    </logic:present>

    <div id="formulario">

        <!-- titol pagina -->
        <h1>
            <img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.general" />" />
            <bean:message key="menu.plantilla" />. <span><bean:message key="plantilla.edicion" /></span>
        </h1>

        <!-- continguts pestanyes / formulario -->
        <%session.setAttribute("action_path_key",null);%>

        <!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
        <jsp:include page="/moduls/mensajes.jsp"/>

        <html:form action="/perPlantillasEdita.do" method="POST" enctype="multipart/form-data"  styleId="accFormulario">

        <input class="accion" type="hidden" name="accion" value=""/>

        <!-- botonera -->
        <div id="botonera">
            <span class="grup">
                <logic:notPresent name="MVS_microsite">
                <button type="button" title='<bean:message key="frontTemas.volvermantenimiento"/>' onclick='document.location.href="temaFrontEdita.do?id=<bean:write name="TemaFrontForm" property="id"/>";'>
                </logic:notPresent>
                <logic:present name="MVS_microsite">
                <button type="button" title='<bean:message key="frontTemas.volvermantenimiento"/>' onclick='document.location.href="perPlantillas.do?idsite=<bean:write name="MVS_microsite" property="id"/>";'>
                </logic:present>
                    <img src="imgs/botons/tornar.gif" alt='<bean:message key="plantilla.volvermantenimiento"/>' />
                </button>
            </span>
            <span class="grup">
                <button class="btnGuardar" type="button" title='<bean:message key="operacion.guardar"/>'>
                    <img src="imgs/botons/guardar.gif" alt='<bean:message key="operacion.guardar"/>' /> &nbsp;<bean:message key="operacion.guardar" />
                </button>
            </span>
        </div>

        <table cellpadding="0" cellspacing="0" class="edicio">
            <tr>
                <td class="etiqueta"><bean:message key="plantilla.edicion.titulo" /></td>
                <td>
                    <html:text property="titulo" maxlength="16" />
                </td>
            </tr>
            <tr class="par">
                <td class="etiqueta"><bean:message key="plantilla.edicion.tipo" /></td>
                <td>
                    <input type="hidden" value='<bean:write name="PerPlantillasForm" property="idPlantilla"/>' />
                    <html:text property="plantilla" maxlength="16" readonly="true" />
                </td>
            </tr>
            <tr>
                <td class="etiqueta"><bean:message key="plantilla.edicion.contenido"/></td>
                <td>
                    <button type="button" class="btnFullScreen"><bean:message key="perPlantillaFull" /></button>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <html:textarea property="contenido"/>
                    <div id="description"/>
                </td>
            </tr>
        </table>
        </html:form>
    </div>
</div>

</body>
</html>