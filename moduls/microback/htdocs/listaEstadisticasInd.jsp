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
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.estadisticas" /></li>
		<li class="pagActual"><bean:message key="menu.estadisticas.ind" /></li>
	</ul>

	<!-- titol pagina -->
	<h1><img src="imgs/titulos/estadisticas.gif" alt="<bean:message key="menu.estadisticas.ind" />" />
	<bean:message key="menu.estadisticas.ind" /></h1>
	<!-- continguts -->

		
		<html:form action="/estadisticaind.do">
		<html:hidden property="ordenacion" />
				
		<div id="botonera">
			<bean:message key="estadisticas.filtre.temporal" />
			<html:select property="filtro" onchange="lanzaFiltros(this.form)" style="margin-right:20px; ">
				<html:option value="ALL"><bean:message key="estadisticas.filtre.temporal.value.all" /></html:option>
          		<html:option value="NOW"><bean:message key="estadisticas.filtre.temporal.value.now" /></html:option>
	            <optgroup label="Per Any">
		            <html:options  name="filtro" collection="MVA_listaanyos" labelProperty="key" property="value"/>
	            </optgroup>
	          </html:select>
	        &nbsp;<bean:message key="estadisticas.filtre.zonal" /> 
			<html:select property="filtro2" onchange="lanzaFiltros(this.form)" style="margin-right:20px; ">
				<html:option value="ALL"><bean:message key="estadisticas.filtre.zonal.value.all" /></html:option>
          		<html:option value="PUB"><bean:message key="estadisticas.filtre.zonal.value.pub" /></html:option>
          		<html:option value="PRV"><bean:message key="estadisticas.filtre.zonal.value.prv" /></html:option>        		
          	</html:select>
        </div>
		
				
				<p><bean:message key="estadisticas.titulo.visitas.total" /> <strong><bean:write name="MVA_statmicrositeind" property="accesos"/> <bean:message key="estadisticas.titulo.visitas" /> </strong>.</p>
				<table cellpadding="0" cellspacing="0" class="llistat noPointer">
					<tr>
						<th width="20%"><bean:message key="estadisticas.titulo.tipo" />
							<html:link href="javascript:ordenar('Aref');">
				                <logic:equal name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Aref">
				                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='Ordenar ascendentemente'>
				                </logic:equal>
				                <logic:notEqual name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Aref">
				                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='Ordenar ascendentemente'>
				                </logic:notEqual>
				            </html:link>
				            <html:link href="javascript:ordenar('Dref');">
				                <logic:equal name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Dref">
				                    <img src="imgs/iconos/orden_descendente_on.gif" alt='Ordenar descendentemente'>
				                </logic:equal>
				                <logic:notEqual name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Dref">
				                    <img src="imgs/iconos/orden_descendente_off.gif" alt='Ordenar descendentemente'>
				                </logic:notEqual>            
				            </html:link>
						</th>
						<th width="65%"><bean:message key="estadisticas.titulo.elemento" />
							<html:link href="javascript:ordenar('Atitulo');">
				                <logic:equal name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Atitulo">
				                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='Ordenar ascendentemente'>
				                </logic:equal>
				                <logic:notEqual name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Atitulo">
				                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='Ordenar ascendentemente'>
				                </logic:notEqual>
				            </html:link>
				            <html:link href="javascript:ordenar('Dtitulo');">
				                <logic:equal name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Dtitulo">
				                    <img src="imgs/iconos/orden_descendente_on.gif" alt='Ordenar descendentemente'>
				                </logic:equal>
				                <logic:notEqual name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Dtitulo">
				                    <img src="imgs/iconos/orden_descendente_off.gif" alt='Ordenar descendentemente'>
				                </logic:notEqual>            
				            </html:link>
						</th>
						<th><bean:message key="estadisticas.titulo.accesos" />
							<html:link href="javascript:ordenar('Aaccesos');">
				                <logic:equal name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Aaccesos">
				                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='Ordenar ascendentemente'>
				                </logic:equal>
				                <logic:notEqual name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Aaccesos">
				                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='Ordenar ascendentemente'>
				                </logic:notEqual>
				            </html:link>
				            <html:link href="javascript:ordenar('Daccesos');">
				                <logic:equal name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Daccesos">
				                    <img src="imgs/iconos/orden_descendente_on.gif" alt='Ordenar descendentemente'>
				                </logic:equal>
				                <logic:notEqual name="BuscaOrdenaEstadisticaIndActionForm" property="ordenacion" value="Daccesos">
				                    <img src="imgs/iconos/orden_descendente_off.gif" alt='Ordenar descendentemente'>
				                </logic:notEqual>            
				            </html:link>
						</th>
					</tr>
					<logic:iterate id="i" name="MVA_listaestadisticaind" indexId="indice">
						<tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
							<td><bean:write name="i" property="nombreservicio" /></td>
							<td><bean:write name="i" property="tituloitem" /></td>
							<td><bean:write name="i" property="accesos" /></td>
						</tr>
					</logic:iterate>
				</table>
				
				
		</html:form>
		<br/>
</body>
</html>

<script type="text/javascript">
<!--


	function ordenar(campo) {
	    document.BuscaOrdenaEstadisticaIndActionForm.ordenacion.value=campo;
	    document.BuscaOrdenaEstadisticaIndActionForm.submit();
	}

	
	function lanzaFiltros(formulario) {
	    formulario.submit();
	}


-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>