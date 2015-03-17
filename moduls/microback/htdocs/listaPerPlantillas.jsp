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
    <link type="text/css" rel="stylesheet" href="css/estils.css" />
    <link type="text/css" rel="stylesheet" href="css/jquery.alerts.css" media="screen" />

    <!-- Core files -->
    <script type="text/javascript" src="js/jquery/jquery-1.3.2.min.js"></script>
    <script type="text/javascript" src="js/jquery/jquery.alerts.js" ></script>
    <script type="text/javascript" src="js/jsListados.js"></script>
</head>
<body>
    <!-- molla pa -->
    <ul id="mollapa">
        <li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
        <li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
        <li><bean:message key="menu.configuracion" /></li>
        <li class="pagActual"><bean:message key="menu.plantillas" /></li>
    </ul>

    <!-- titol pagina -->
    <h1>
        <img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.general" />" />
        <bean:message key="menu.plantilla" />. <span><bean:message key="micro.descripcion.plantillas" /></span>
    </h1>

    <logic:equal name="parametrosPagina" property="nreg" value="0">
        <!-- botonera -->
        <div id="botonera">
            <logic:present name="MVS_microsite" property="id">
            <span class="grup">
                <button type="button" title='<bean:message key="micro.boto.tornargestio.titol"/>' onclick='window.parent.location.href="index.do?idsite=<bean:write name="MVS_microsite" property="id"/>";'>
                    <img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.titol"/>' />
                </button>
            </span>
			<span class="grup">
                <button type="submit" title="<bean:message key="plantilla.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="plantilla.crear" />" /></button>
			</span>
            </logic:present>
        </div>

        <input type="hidden" name="accion" value=""/>
        <jsp:include page="/moduls/mensajes.jsp"/>
        <html:form action="/perPlantillasAcc.do" styleId="accFormularioLista">
            <html:hidden property="accion" />
        </html:form>
    </logic:equal>

    <logic:notEqual name="parametrosPagina" property="nreg" value="0">
        <!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
        <jsp:include page="/moduls/mensajes.jsp"/>
        <input type="hidden" name="accion" value=""/>

        <html:form action="/perPlantillas.do"  styleId="accFormSearch">
            <html:hidden property="ordenacion" />
            <!-- botonera -->
            <div id="botonera">
                <logic:present name="MVS_microsite" property="id">
                    <span class="grup">
                        <button type="button" title='<bean:message key="micro.boto.tornargestio.titol"/>' onclick='window.parent.location.href="index.do?idsite=<bean:write name="MVS_microsite" property="id"/>";'>
                            <img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.titol"/>' />
                        </button>
                    </span>
	        		<span class="grup">
                        <button type="button" title="<bean:message key="plantilla.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="plantilla.crear" />" /></button>
                        <button type="button" title="<bean:message key="op.6" />" onclick="editar();"><img src="imgs/menu/editar.gif" alt="<bean:message key="op.6" />" /></button>
                        <button type="button" title='<bean:message key="operacion.borrar" />' onclick="borravarios();"><img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' /></button>
    			    </span>
                </logic:present>
            </div>
        </html:form>

        <div id="formulario">
            <html:form action="/perPlantillasAcc.do" styleId="accFormularioLista">
                <table cellpadding="0" cellspacing="0" class="llistat" style="width:78%;">
                    <thead>
                        <tr>
                            <th width="40%">
                                <bean:message key="plantilla.titol" />&nbsp;
                                <html:link href="javascript:ordenar('Atitulo');">
                                    <logic:equal name="BuscaOrdenaPerPlantillasActionForm" property="ordenacion" value="Atitulo">
                                        <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                                    </logic:equal>
                                    <logic:notEqual name="BuscaOrdenaPerPlantillasActionForm" property="ordenacion" value="Atitulo">
                                        <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                                    </logic:notEqual>
                                </html:link>
                                <html:link href="javascript:ordenar('Dtitulo');">
                                    <logic:equal name="BuscaOrdenaPerPlantillasActionForm" property="ordenacion" value="Dtitulo">
                                        <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                                    </logic:equal>
                                    <logic:notEqual name="BuscaOrdenaPerPlantillasActionForm" property="ordenacion" value="Dtitulo">
                                        <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                                    </logic:notEqual>
                                </html:link>
                            </th>
                            <th width="40%">
                                <bean:message key="plantilla.nom" />&nbsp;
                            </th>
                            <th width="20%">
                                <bean:message key="plantilla.ordre" />&nbsp;
                                <html:link href="javascript:ordenar('Aorden');">
                                    <logic:equal name="BuscaOrdenaPerPlantillasActionForm" property="ordenacion" value="Aorden">
                                        <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                                    </logic:equal>
                                    <logic:notEqual name="BuscaOrdenaPerPlantillasActionForm" property="ordenacion" value="Aorden">
                                        <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                                    </logic:notEqual>
                                </html:link>
                                <html:link href="javascript:ordenar('Dorden');">
                                    <logic:equal name="BuscaOrdenaPerPlantillasActionForm" property="ordenacion" value="Dorden">
                                        <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                                    </logic:equal>
                                    <logic:notEqual name="BuscaOrdenaPerPlantillasActionForm" property="ordenacion" value="Dorden">
                                        <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                                    </logic:notEqual>
                                </html:link>
                            </th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <td colspan="3">
                                <logic:present name="parametrosPagina" property="inicio">
                                    &lt;&lt;<html:link action="/perPlantillas.do" paramId="pagina" paramName="parametrosPagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
                                </logic:present>
                                <logic:present name="parametrosPagina" property="anterior">
                                    &lt;<html:link action="/perPlantillas.do" paramId="pagina" paramName="parametrosPagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;
                                </logic:present>
                                - <bean:write name="parametrosPagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametrosPagina" property="cursorFinal" /> <bean:message key="pagina.de" /> <bean:write name="parametrosPagina" property="nreg" /> -
                                <logic:present name="parametrosPagina" property="siguiente">
                                    <html:link action="/perPlantillas.do" paramId="pagina" paramName="parametrosPagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;
                                </logic:present>
                                <logic:present name="parametrosPagina" property="final">
                                    <html:link action="/perPlantillas.do" paramId="pagina" paramName="parametrosPagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;
                                </logic:present>
                            </td>
                        </tr>
                    </tfoot>
                    <tbody>
                        <logic:iterate id="i" name="FR_PERPLA" indexId="indice">
                            <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
                                <td class="check">
                                    <html:multibox property="seleccionados" styleClass="radio">
                                        <bean:write name="i" property="id"/>
                                    </html:multibox>
                                </td>
                                <td><bean:write name="i" property="titulo" /></td>
                                <td><bean:write name="i" property="plantilla.nombre" /></td>
                                <td><bean:write name="i" property="orden"/></td>
                            </tr>
                        </logic:iterate>
                    </tbody>
                </table>

                <html:hidden property="accion" />
            </html:form>
        </div>
    </logic:notEqual>

</body>
</html>
<script>
    <!--
    var uriEdicion="perPlantillasEdita.do?id=";
    var alert1="<bean:message key="plantilla.alert1"/>";
    var alert2="<bean:message key="plantilla.alert2"/>";
    -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>