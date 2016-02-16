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
    <!--[if !IE]><!--><meta http-equiv="X-UA-Compatible" content="IE=edge" /><!--<![endif]-->
    <title>Gestor Microsites</title>

    <!-- CSS -->
    <link type="text/css" rel="stylesheet" href="css/index.css" />
    <link type="text/css" rel="stylesheet" href="css/estils.css" />
    <link type="text/css" rel="stylesheet" href="css/jquery.alerts.css" media="screen" />
    <link type="text/css" rel="stylesheet" href="css/editorCSS.css" />

    <!-- Javascript -->
    <script type="text/javascript" src="js/jquery/jquery.alerts.js"></script>
    <script type="text/javascript" src="js/subMenus.js"></script>
    <script type="text/javascript" src="js/jsListados.js"></script>
</head>
<body>
    <!-- cap -->
    <jsp:include page="cabecera.jsp"/>
    <!-- /cap -->

    <!-- marc lateral -->
    <jsp:include page="menuLateralIzq.jsp"/>
    <!-- /marc lateral -->

	<bean:message key="errors.explorernou" />

    <div id="formulario">
        <!-- titol pagina -->
        <h1>
            <img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.general" />" />
            <bean:message key="menu.frontTemas" />. <span><bean:message key="frontTemas.descripcion" /></span>
        </h1>

        <logic:equal name="parametrosPagina" property="nreg" value="0">
            <!-- botonera -->
            <div id="botonera">
                <span class="grup">
                    <button type="button" title='<bean:message key="micro.boto.tornargestio.titol"/>' onclick='window.parent.location.href="index.do";'>
                        <img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.titol"/>' />
                    </button>
                </span>
                <span class="grup">
                    <button type="submit" title="<bean:message key="frontTemas.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="frontTemas.crear" />" /></button>
                </span>
            </div>

            <jsp:include page="/moduls/mensajes.jsp"/>
            <input type="hidden" name="accion" value=""/>
            <html:form action="/temaFrontAcc.do" styleId="accFormularioLista">
                <html:hidden property="accion" />
            </html:form>
        </logic:equal>

        <logic:notEqual name="parametrosPagina" property="nreg" value="0">
            <!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
            <jsp:include page="/moduls/mensajes.jsp"/>
            <%--<input type="hidden" name="accion" value=""/>--%>

            <html:form action="/temaFront.do"  styleId="accFormSearch">
                <html:hidden property="ordenacion" />
                <!-- botonera -->
                <div id="botonera">
                <span class="grup">
                    <button type="button" title='<bean:message key="micro.boto.tornargestio.titol"/>' onclick='window.parent.location.href="index.do";'>
                        <img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.titol"/>' />
                    </button>
                </span>
                <span class="grup">
                    <button type="button" title="<bean:message key="frontTemas.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="frontTemas.crear" />" /></button>
                    <button type="button" title='<bean:message key="operacion.borrar" />' onclick="borravarios();"><img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' /></button>
                </span>
                <%--<span class="grup">--%>
                        <%--TODO: botones de busqueda y clasificacion--%>
                <%--</span>--%>
                </div>
            </html:form>

            <html:form action="/temaFrontAcc.do" styleId="accFormularioLista">
                <table cellpadding="0" cellspacing="0" class="llistat" style="width:78%;">
                    <thead>
                        <tr>
                            <th width="25%">
                                <bean:message key="frontTemas.nom" />&nbsp;
                                <html:link href="javascript:ordenar('Anombre');">
                                    <logic:equal name="BuscaOrdenaTemaFrontActionForm" property="ordenacion" value="Anombre">
                                        <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                                    </logic:equal>
                                    <logic:notEqual name="BuscaOrdenaTemaFrontActionForm" property="ordenacion" value="Anombre">
                                        <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                                    </logic:notEqual>
                                </html:link>
                                <html:link href="javascript:ordenar('Dnombre');">
                                    <logic:equal name="BuscaOrdenaTemaFrontActionForm" property="ordenacion" value="Dnombre">
                                        <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                                    </logic:equal>
                                    <logic:notEqual name="BuscaOrdenaTemaFrontActionForm" property="ordenacion" value="Dnombre">
                                        <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                                    </logic:notEqual>
                                </html:link>
                            </th>
                            <th width="25%">
                                <bean:message key="frontTemas.padre" />&nbsp;
                            </th>
                            <th width="25%">
                                <bean:message key="frontTemas.plantilla" />&nbsp;
                            </th>
                            <th width="25%">
                                <bean:message key="frontTemas.archivo" />&nbsp;
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                    <logic:iterate id="i" name="temaFronts" indexId="indice">
                        <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
                            <td class="check">
                                <html:multibox property="seleccionados" styleClass="radio">
                                    <bean:write name="i" property="id"/>
                                </html:multibox>
                            </td>
                            <td><bean:write name="i" property="nombre" /></td>
                            <logic:notEmpty name="i" property="temaPadre">
                                <td><bean:write name="i" property="temaPadre.nombre" /></td>
                            </logic:notEmpty>
                            <logic:empty name="i" property="temaPadre">
                                <td></td>
                            </logic:empty>
                            <td><bean:write name="i" property="version.nombre"/></td>
                            <bean:size name="i" property="archivoTemaFronts" id="size" />
                            <td><bean:write name="size" /></td>
                        </tr>
                    </logic:iterate>
                    </tbody>
                </table>

                <html:hidden property="accion" />
            </html:form>

        </logic:notEqual>
    </div>

    <!-- peu -->
    <jsp:include page="peu.jsp"/>
    <!-- /peu -->

</body>
</html>

<script>
    <!--
    var uriEdicion="temaFrontEdita.do?id=";
    var alert1="<bean:message key="frontTemas.alert1"/>";
    var alert2="<bean:message key="frontTemas.alert2"/>";
    -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>