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
	<script type="text/javascript" src="js/jsListados.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li class="pagActual"><bean:message key="menu.convocatorias" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/convocatorias.gif" alt="<bean:message key="menu.convocatorias" />" />
	<bean:message key="menu.convocatorias" />. <span><bean:message key="convocatoria.lista" /></span></h1>

<logic:equal name="parametros_pagina" property="nreg" value="0">
	<div id="botonera">
		<span class="grup">
			<button type="button" title="<bean:message key="convocatoria.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="convocatoria.crear" />" /></button> 
		</span>
	</div>
	<p>
		<bean:message key="convocatoria.vacio" /><br/>
		<br/>&nbsp;&nbsp;<html:link href="convocatoria.do"><bean:message key="convocatoria.volver" /></html:link>
	</p>
	<html:form action="/convocatoriasAcc.do" styleId="accFormularioLista">
		<html:hidden property="accion" />
	</html:form>
	
</logic:equal>

<logic:notEqual name="parametros_pagina" property="nreg" value="0">  

	<html:form action="/convocatorias.do"  styleId="accFormSearch">
		<html:hidden property="ordenacion" />
		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="convocatoria.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="convocatoria.crear" />" /></button> 
				<button type="button" title="<bean:message key="op.6" />" onclick="editar();"><img src="imgs/menu/editar.gif" alt="<bean:message key="op.6" />" /></button> 
				<button type="button" title="<bean:message key="op.2" />" onclick="borravarios();"><img src="imgs/botons/borrar.gif" alt="<bean:message key="op.2" />" /></button>
			</span>
			<span class="grup">
				<html:text property="filtro" size="10"/> 
				<button type="button" title="<bean:message key="op.3" />" onclick="submitFormBuscar();"><img src="imgs/botons/cercar.gif" alt="<bean:message key="op.3" />" /></button> 
			</span>
		</div>		
	</html:form>

	<p><bean:message key="convocatoria.dobleclic" />.</p>
	<p><bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="convocatoria.plural" /></strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.</p>

	<html:form action="/convocatoriasAcc.do"  styleId="accFormularioLista">
	  	<html:hidden property="accion" />
		<table cellpadding="0" cellspacing="0" class="llistat">
			<thead>
				<tr>
					<th class="check">&nbsp;</th>
					<th width="20%">
			            <bean:message key="convocatoria.eticolumna1" />&nbsp;
			            <html:link href="javascript:ordenar('Anombre');">
			                <logic:equal name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Anombre">
			                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'/>
			                </logic:equal>
			                <logic:notEqual name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Anombre">
			                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'/>
			                </logic:notEqual>
			            </html:link>
			            <html:link href="javascript:ordenar('Dnombre');">
			                <logic:equal name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Dnombre">
			                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'/>
			                </logic:equal>
			                <logic:notEqual name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Dnombre">
			                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'/>
			                </logic:notEqual>            
			            </html:link>
			        </th>
					<th width="40%">
			            <bean:message key="convocatoria.eticolumna2" />&nbsp;
			            <html:link href="javascript:ordenar('Adescripcion');">
			                <logic:equal name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Adescripcion">
			                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'/>
			                </logic:equal>
			                <logic:notEqual name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Adescripcion">
			                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'/>
			                </logic:notEqual>
			            </html:link>
			            <html:link href="javascript:ordenar('Ddescripcion');">
			                <logic:equal name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Ddescripcion">
			                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'/>
			                </logic:equal>
			                <logic:notEqual name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Ddescripcion">
			                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'/>
			                </logic:notEqual>            
			            </html:link>
			        </th>
			        <th width="20%">
			            <bean:message key="convocatoria.eticolumna3" />&nbsp;
			            <html:link href="javascript:ordenar('Adata_envio');">
			                <logic:equal name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Adata_envio">
			                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'/>
			                </logic:equal>
			                <logic:notEqual name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Adata_envio">
			                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'/>
			                </logic:notEqual>
			            </html:link>
			            <html:link href="javascript:ordenar('Ddata_envio');">
			                <logic:equal name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Ddata_envio">
			                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'/>
			                </logic:equal>
			                <logic:notEqual name="BuscaOrdenaConvocatoriaActionForm" property="ordenacion" value="Ddata_envio">
			                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'/>
			                </logic:notEqual>            
			            </html:link>
			        </th>
			        <th width="20%"/>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="5">
						<logic:present name="parametros_pagina" property="inicio">
						    &lt;&lt;<html:link action="/convocatorias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
						</logic:present>
						<logic:present name="parametros_pagina" property="anterior">
						    &lt;<html:link action="/convocatorias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;      
						</logic:present>
						- <bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /> <bean:message key="pagina.de" /> <bean:write name="parametros_pagina" property="nreg" /> -  
						<logic:present name="parametros_pagina" property="siguiente">
						    <html:link action="/convocatorias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;      
						</logic:present>
						<logic:present name="parametros_pagina" property="final">
						    <html:link action="/convocatorias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;      
						</logic:present>					
					</td>
					<td/>
				</tr>
			</tfoot>
		
			<tbody>
			    <logic:iterate id="i" name="listado" indexId="indice">
			      <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
			      <td class="check">
			        <html:multibox property="seleccionados" styleClass="radio"> 
			            <bean:write name="i" property="id"/>
			        </html:multibox>
			      </td>
			      <td>
					<bean:write name="i" property="nombre" />
			      </td>
				  <td>
			      	<bean:write name="i" property="descripcion" />
			      </td>
			      <td>
			      	<bean:write name="i" property="ultimoEnvio" formatKey="date.short.format"/>
			      </td>
			      <td>
					<button type="button" title='<bean:message key="convocatoria.enviar" />' onclick="submitEnvio(<bean:write name="i" property="id"/>);">
			   			<img src="imgs/botons/exportar.gif" alt='<bean:message key="convocatoria.enviar" />' />
			    	</button>
			      </td>               	
			    </tr>
			    </logic:iterate>
			</tbody>
		</table>
	</html:form>
	
	<html:form action="/convocatoriaEdita.do" styleId="enviarMailing" method="get" >
		<input type="hidden" name="id"/>
		<input type="hidden" name="accion"/>
	</html:form>
</logic:notEqual>

	<script>
	
		var uriEdicion="convocatoriaEdita.do?id=";
		var alert1="<bean:message key="convocatoria.alert1"/>";
		var alert2="<bean:message key="convocatoria.alert2"/>";
	
		function submitEnvio(idConvocatoria){
			if (confirm ("<bean:message key="convocatoria.confirmarEnviament" />")){
				var envioForm = document.getElementById('enviarMailing');
				envioForm.id.value = idConvocatoria;
				envioForm.accion.value = 'enviarMailing';
				envioForm.submit();
			}	
		}
	</script>
	<jsp:include page="/moduls/pieControl.jsp"/>
</body>
</html>
