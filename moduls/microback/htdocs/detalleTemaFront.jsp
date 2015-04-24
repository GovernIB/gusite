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
    <link href="css/index.css" rel="stylesheet" type="text/css" />
    <link href="css/estils.css" rel="stylesheet" type="text/css" />
    <link type="text/css" rel="stylesheet" href="css/editorCSS.css" />

    <!-- Javascript -->
    <script type="text/javascript" src="js/jquery/jquery-1.3.2.min.js"></script>
    <script type="text/javascript" src="js/funciones.js"></script>
    <script type="text/javascript" src="moduls/funcions.js"></script>
    <script type="text/javascript" src="js/subMenus.js"></script>
    <script type="text/javascript" src="js/jsListados.js"></script>
    <script type="text/javascript" src="js/guardarTemaPlantilla.js"></script>
    <script type="text/javascript" src="js/rArxius.js"></script>
	<script type="text/javascript" src="js/checkUri.js"></script>
    
</head>
<body>
<div id="contenidor">

    <!-- cap -->
    <jsp:include page="cabecera.jsp"/>
    <!-- /cap -->

    <!-- marc lateral -->
    <jsp:include page="menuLateralIzq.jsp"/>
    <!-- /marc lateral -->

    <!-- titol pagina -->
    <h1>
        <img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.general" />" />
        <bean:message key="menu.frontTemas" />. <span><bean:message key="frontTemas.edicion" /></span>
    </h1>

    <html:form action="/temaFrontEdita.do" enctype="multipart/form-data" styleId="accFormularioLista">

    <!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
    <jsp:include page="/moduls/mensajes.jsp"/>

    <input class="accion" type="hidden" name="accion" value=""/>

    <div id="formulario">

        <!-- botonera -->
        <div id="botonera">
            <span class="grup">
                <button type="button" title='<bean:message key="frontTemas.volvermantenimiento"/>' onclick='document.location.href="temaFront.do";'>
                    <img src="imgs/botons/tornar.gif" alt='<bean:message key="frontTemas.volvermantenimiento"/>' />
                </button>
            </span>
            <span class="grup">
				<button type="button" name="fichero1" title="<bean:message key="frontTemas.exportar" />" onclick="submitExportar(<bean:write name="TemaFrontForm" property="id"/>);">
					<img src="imgs/botons/importar.gif" alt="<bean:message key="frontTemas.exportar" />" /> &nbsp;<bean:message key="frontTemas.exportar" />
				</button>
				<button type="button" name="fichero2" title="<bean:message key="frontTemas.importar" />" onclick="submitImportar(<bean:write name="TemaFrontForm" property="id"/>);">
					<img src="imgs/botons/importar.gif" alt="<bean:message key="frontTemas.importar" />" /> &nbsp;<bean:message key="frontTemas.importar" />
				</button>
            </span>
            <span class="grup">
                <button class="btnGuardar" type="submit" title='<bean:message key="operacion.guardar"/>'>
                    <img src="imgs/botons/guardar.gif" alt='<bean:message key="operacion.guardar"/>' /> &nbsp;<bean:message key="operacion.guardar" />
                </button>
            </span>
        </div>

        <input type="hidden" name="id" value='<bean:write name="TemaFrontForm" property="id"/>' />
        <table cellpadding="0" cellspacing="0" class="edicio">
            <tr class="par">
                <td class="etiqueta"><bean:message key="frontTemas.edicion.titulo" /></td>
                <td>
                    <html:text property="nombre" size="40" styleId="tituloTema" maxlength="255" />
                </td>
            </tr>
			<tr>
				<td class="etiqueta"><bean:message key="frontTemas.edicion.uri" /></td>
				<td>
					<html:text property="uri" size="40" maxlength="256"/></td>
				<input type="hidden" name="type" value="ftr_uri" />
			</tr>
            <tr class="par">
                <td class="etiqueta"><bean:message key="frontTemas.edicion.padre" /></td>
                <td>
                    <html:select property="temaPadre">
                        <option value="" selected>Please select an option...</option>
                        <html:options collection="temasFrontPadres" labelProperty="nombre" property="id"/>
                    </html:select>
                </td>
            </tr>
	            <tr>
	                <td class="etiqueta"><bean:message key="micro.css" /> &gt;</td>
	                <td>
	                    <div id="microManagedFileCSS">
	                        <html:hidden property="cssId" />
	                        <logic:notEmpty name="TemaFrontForm" property="cssNom">
	                            <html:text property="cssNom"/>
	                            <button type="button" title="<bean:message key="boton.eliminar" />" onclick="borraFile(this,'css','cssId','cssBor','cssNom');"><img src="imgs/botons/borrar.gif" alt="<bean:message key="boton.eliminar" />" /></button>
	                            <%--<button type="button" title="<bean:message key="op.12" />" onclick="abrirDoc('<bean:write name="TemaFrontForm" property="cssId"/>');"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>--%>
	                        </logic:notEmpty>
	                    </div>
	                    <logic:empty name="TemaFrontForm" property="cssNom">
	                        <html:file property="css" size="30"/>
	                    </logic:empty>
	                </td>
	            </tr>
        </table>

        <logic:notEmpty name="TemaFrontForm" property="id">
	        <div class="subList" style="margin:40px 50px 40px 50px;">
	            <div class="botonera">
	                <span class="grup">
	                    <button type="submit" title="<bean:message key="frontTemas.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="frontTemas.crear" />" /></button>
	                    <button type="submit" title='<bean:message key="operacion.borrar" />' onclick="borravarios();"><img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' /></button>
		        	</span>
	            </div>
	
	            <table cellpadding="0" cellspacing="0" class="llistat" style="width:78%;">
	                <thead>
	                <tr>
	                    <th width="33%">
	                        <bean:message key="frontTemas.edicion.plantillas.titulo" />&nbsp;
	                    </th>
	                    <th width="33%">
	                        <bean:message key="frontTemas.edicion.plantillas.plantill" />&nbsp;
	                    </th>
	                    <th width="33%">
	                        <bean:message key="frontTemas.edicion.plantillas.orden" />&nbsp;
	                    </th>
	                </tr>
	                </thead>
	                <tbody>
	                <logic:iterate id="i" name="TemaFrontForm" property="personalizacionesPlantilla" indexId="indice">
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
	        </div>
	
	        <div class="subList" style="margin:40px 50px 40px 50px;">
	            <div class="botonera">
	                <span class="grup">
	                    <button type="button" title="<bean:message key="frontTemas.crear" />" onclick="mostarGuardarArxiu('divGuardarArxiu','nou');"><img src="imgs/botons/nou.gif" alt="<bean:message key="frontTemas.crear" />" /></button>
	                    <!--  <button type="button" title="<bean:message key="op.6" />" onclick="mostarGuardarArxiu('divGuardarArxiu','modificar');"><img src="imgs/menu/editar.gif" alt="<bean:message key="op.6" />" /></button>  -->
	                    <button type="submit" title='<bean:message key="operacion.borrar" />' onclick="borraVariosArchivos();"><img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' /></button>
	                </span>
	
	                <div id="divGuardarArxiu">
	                    <h2><bean:message key="conte.nuevoarchi"/></h2>
	                    <p><bean:message key="conte.nuevoarchimensa"/></p>
	                        <html:hidden property="idArxiu" value="0"/>
	                    <html:file property="archi" size="50"/>
	                    <div class="botonera">
	                        <button id="guardarArxiu" name="subirficheros" type="button" title='<bean:message key="op.15"/>'><img src="imgs/botons/guardar.gif" alt='<bean:message key="op.15"/>' /> &nbsp;<bean:message key="op.15"/></button>
	                        &nbsp; &nbsp; <button type="button" title='<bean:message key="boton.cerrar"/>' onclick="formEsconder();"><img src="imgs/botons/cerrar.gif" alt='<bean:message key="boton.cerrar"/>' /></button>
	                    </div>
	                    <html:hidden property="operacion" value=""/>
	                </div>
	
	            </div>
	
	            <table cellpadding="0" cellspacing="0" class="llistat" style="width:78%;">
	                <thead>
	                <tr>
	                    <th width="15%">
	                        <bean:message key="frontTemas.edicion.archivos.tipo" />&nbsp;
	                    </th>
	                    <th width="25%">
	                        <bean:message key="frontTemas.edicion.archivos.nombre" />&nbsp;
	                    </th>
	                    <th width="15%">
	                        <bean:message key="frontTemas.edicion.archivos.tamanyo" />&nbsp;
	                    </th>
	                    <th width="45%">
	                        <bean:message key="frontTemas.edicion.archivos.url" />&nbsp;
	                    </th>
	                </tr>
	                </thead>
	                <tbody id="listadoArchivos">
	                <logic:iterate id="i" name="TemaFrontForm" property="archivos" indexId="indice">
	                    <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
	                        <td class="check">
	                            <html:multibox property="seleccionados" styleClass="radio">
	                                <bean:write name="i" property="id"/>
	                            </html:multibox>
	                        </td>
	                        <td><bean:write name="i" property="archivo.mime" /></td>
	                        <td><bean:write name="i" property="archivo.nombre" /></td>
	                        <td><bean:write name="i" property="archivo.peso" /></td>
	                        <td><a href="<bean:write name="i" property="path"/>" target="_blank"><bean:write name="i" property="path"/></a></td>
	                    </tr>
	                </logic:iterate>
	                </tbody>
	            </table>
	        </div>
		</logic:notEmpty>
    </div>
</div>

</html:form>

</body>
</html>
<script>
    var uriEdicion="perPlantillasEdita.do?id=";
    var alert1="<bean:message key="frontTemas.alert1"/>";
    var alert2="<bean:message key="frontTemas.alert2"/>";

    var alert3='<bean:message key="conte.nuevoarchi"/>';
    var alert4='<bean:message key="conte.alertaborrararchivos"/>';
    var mensa1='<bean:message key="conte.modifarchi"/>';
    var mensa2='<bean:message key="conte.alertamodifarchi"/>';
    var mensa3='<bean:message key="conte.nuevoarchimensa"/>';

	function submitExportar(idtema){
		document.location="exportarTema.do?idtema="+idtema;
	}
	function submitImportar(idtema){
		document.location="importarTema.do?idtema="+idtema;
	}
    
</script>
