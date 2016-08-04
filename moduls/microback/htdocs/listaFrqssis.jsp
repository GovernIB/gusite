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
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.configuracion" /></li>
		<li class="pagActual"><bean:message key="menu.formulariosqssi" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.formulariosqssi" />" />
	<bean:message key="menu.formulariosqssi" />. <span><bean:message key="frqssi.lista" /></span></h1>


<logic:equal name="parametros_pagina" property="nreg" value="0">

	<!-- No hay ningun componente -->
	
		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="frqssi.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="frqssi.crear" />" /></button> 
			</span>
		</div>

		<p>
			<bean:message key="frqssi.vacio" /><br/>
			<br/>&nbsp;&nbsp;<html:link href="frqssis.do"><bean:message key="frqssi.volver" /></html:link>
		</p>
		<html:form action="/frqssisAcc.do" styleId="accFormularioLista" >
		  <html:hidden property="accion" />
		</html:form>	
	
	
</logic:equal>

<logic:notEqual name="parametros_pagina" property="nreg" value="0">  

	
<!-- HAY algun componente -->
		
		<html:form action="/frqssis.do"  styleId="accFormSearch">
		<html:hidden property="ordenacion" />
		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="frqssi.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="frqssi.crear" />" /></button> 
				<button type="button" title="<bean:message key="op.6" />" onclick="editar();"><img src="imgs/menu/editar.gif" alt="<bean:message key="op.6" />" /></button> 
				<button type="button" title="<bean:message key="op.2" />" onclick="borravarios();"><img src="imgs/botons/borrar.gif" alt="<bean:message key="op.2" />" /></button>
			</span>
			<span class="grup">
				<html:text property="filtro" size="10"/> 
				<button type="button" title="<bean:message key="op.3" />" onclick="submitFormBuscar();"><img src="imgs/botons/cercar.gif" alt="<bean:message key="op.3" />" /></button> 
			</span>
		</div>		
		</html:form>

		<p><bean:message key="frqssi.dobleclic" />.</p>
		<p><bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="frqssi.plural" /></strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.</p>

		<html:form action="/frqssisAcc.do"  styleId="accFormularioLista">
		  <table cellpadding="0" cellspacing="0" class="llistat">
		  <thead>
			<tr>
				<th class="check">&nbsp;</th>
				<th>
		            <bean:message key="frqssi.eticolumna1" />&nbsp;
		            <html:link href="javascript:ordenar('Atrad.nombre');">
		                <logic:equal name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Atrad.nombre">
		                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Atrad.nombre">
		                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
		                </logic:notEqual>
		            </html:link>
		            <html:link href="javascript:ordenar('Dtrad.nombre');">
		                <logic:equal name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Dtrad.nombre">
		                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Dtrad.nombre">
		                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
		                </logic:notEqual>            
		            </html:link>
		        </th>
				<th>
		            <bean:message key="frqssi.eticolumna2" />&nbsp;
		            <html:link href="javascript:ordenar('Afrqssi.centro');">
		                <logic:equal name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Afrqssi.centro">
		                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Afrqssi.centro">
		                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
		                </logic:notEqual>
		            </html:link>
		            <html:link href="javascript:ordenar('Dfrqssi.centro');">
		                <logic:equal name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Dfrqssi.centro">
		                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Dfrqssi.centro">
		                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
		                </logic:notEqual>            
		            </html:link>
		        </th>
				<th>
		            <bean:message key="frqssi.eticolumna3" />&nbsp;
		            <html:link href="javascript:ordenar('Afrqssi.tipoescrito');">
		                <logic:equal name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Afrqssi.tipoescrito">
		                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Afrqssi.tipoescrito">
		                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
		                </logic:notEqual>
		            </html:link>
		            <html:link href="javascript:ordenar('Dfrqssi.tipoescrito');">
		                <logic:equal name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Dfrqssi.tipoescrito">
		                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFrqssiActionForm" property="ordenacion" value="Dfrqssi.tipoescrito">
		                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
		                </logic:notEqual>            
		            </html:link>
		        </th>		        
			</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="5">
					
				        <logic:present name="parametros_pagina" property="inicio">
				            &lt;&lt;<html:link action="/frqssis.do?" paramId="pagina" paramName="parametros_pagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
				        </logic:present>
				        <logic:present name="parametros_pagina" property="anterior">
				            &lt;<html:link action="/frqssis.do" paramId="pagina" paramName="parametros_pagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;      
				        </logic:present>
				        - <bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /> <bean:message key="pagina.de" /> <bean:write name="parametros_pagina" property="nreg" /> -  
				        <logic:present name="parametros_pagina" property="siguiente">
				            <html:link action="/frqssis.do" paramId="pagina" paramName="parametros_pagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;      
				        </logic:present>
				        <logic:present name="parametros_pagina" property="final">
				            <html:link action="/frqssis.do" paramId="pagina" paramName="parametros_pagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;      
				        </logic:present>
					
					</td>
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
				      		<logic:notEmpty name="i" property="traduccion">
								<bean:write name="i" property="traduccion.nombre" />
							</logic:notEmpty>
					    	<logic:empty name="i" property="traduccion">
								[<bean:message key="frqssi.nonombre" />]
							</logic:empty>
				      </td>      
				      <td>
				      		<bean:write name="i" property="centro" />
				      </td> 
				      <td>
				      		<bean:write name="i" property="tipoescrito" />
				      </td> 				           
			      </tr>
		    </logic:iterate>
			</tbody>
		  </table>
		  <html:hidden property="accion" />
		
		</html:form>

</logic:notEqual>

</body>
</html>

<script>
<!--

var uriEdicion="frqssiEdita.do?id=";
var alert1="<bean:message key="frqssi.alert1"/>";
var alert2="<bean:message key="frqssi.alert2"/>";

-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>