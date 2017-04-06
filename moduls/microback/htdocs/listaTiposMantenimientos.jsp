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
	<script type="text/javascript" src="js/jsListadosTipos.js"></script>
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li class="pagActual"><bean:message key="menu.listados" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/fichas.gif" alt="<bean:message key="menu.listados" />" />
	<bean:message key="menu.listados" />. <span><bean:message key="tipo.lista.mantenimiento" /></span>
	</h1>


<logic:equal name="parametros_pagina" property="nreg" value="0">

	<!-- No hay ningun componente -->
	
		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>
	
		<div id="botonera">
			<span class="grup">
			   	<button type="button" title='<bean:message key="micro.boto.tornargestio.titol"/>' onclick='window.parent.location.href="index.do?idsite=<bean:write name="MVS_microsite" property="id"/>";'>
			   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.titol"/>' />
			   </button> 
			</span>
			<span class="grup">
				<button type="button" title="<bean:message key="tipo.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="tipo.crear" />" /></button> 
			</span>
		</div>

		<html:form action="/tiposAcc.do" styleId="accFormularioLista" >
		  <html:hidden property="accion" />
		</html:form>	

</logic:equal>

<logic:notEqual name="parametros_pagina" property="nreg" value="0">  

	
<!-- HAY algun tipo listado -->
		
		<html:form action="/tipos.do"  styleId="accFormSearch">
		<html:hidden property="ordenacion" />
		<input type="hidden" name="mntnmnt" value="yes" />
		
		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	

		<div id="botonera">
			<span class="grup">
		   	<button type="button" title='<bean:message key="micro.boto.tornargestio.titol"/>' onclick='window.parent.location.href="index.do?idsite=<bean:write name="MVS_microsite" property="id"/>";'>
		   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.titol"/>' />
		   </button> 
			</span>
			<span class="grup">
				<input type="text" name="filtro" id="filtro" size="10" value="">
				<logic:notEmpty name="listaClasificacion">
				     <bean:message key="tipo.clasificacion"/>&nbsp;
				     <select name="clasificacion_txt" onchange="if (this.value.length>0) document.getElementById('filtro').value=this.options[this.selectedIndex].value;">
				       	<option value="" />
				       	<logic:iterate id="i" name="listaClasificacion">
				       		<option value='<bean:write name="i" property="clasificacion"/>'><bean:write name="i" property="clasificacion"/></option>
						</logic:iterate>
				     </select>				
				 </logic:notEmpty>				
				<button type="button" title="<bean:message key="op.3" />" onclick="submitFormBuscar();"><img src="imgs/botons/seleccionar.gif" alt="<bean:message key="op.3" />" /></button> 
			</span>
		</div>		
		</html:form>

		<p><bean:message key="tipo.dobleclic.mante" />.</p>
		<p><bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="tipo.plural" /></strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.</p>

	<html:form action="/tiposAcc.do"  styleId="accFormularioLista">
	<input type="hidden" name="mntnmnt" value="yes">
  	<table cellpadding="0" cellspacing="0" class="llistat">
  	<thead>
	<tr>
		<th class="check">&nbsp;</th>
		<th>
            <bean:message key="tipo.eticolumna1" />&nbsp;
            <html:link href="javascript:ordenar('Atrad.nombre');">
                <logic:equal name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Atrad.nombre">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Atrad.nombre">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dtrad.nombre');">
                <logic:equal name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Dtrad.nombre">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Dtrad.nombre">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th>
            <bean:message key="tipo.eticolumna2" />&nbsp;
            <html:link href="javascript:ordenar('Atipo.tipoelemento');">
                <logic:equal name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Atipo.tipoelemento">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Atipo.tipoelemento">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dtipo.tipoelemento');">
                <logic:equal name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Dtipo.tipoelemento">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Dtipo.tipoelemento">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th>
            <bean:message key="tipo.eticolumna3" />&nbsp;
            <html:link href="javascript:ordenar('Atipo.clasificacion');">
                <logic:equal name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Atipo.clasificacion">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Atipo.clasificacion">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dtipo.clasificacion');">
                <logic:equal name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Dtipo.clasificacion">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaTipoActionForm" property="ordenacion" value="Dtipo.clasificacion">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>		                
	</tr>
	</thead>
	<tfoot>
				<tr>
					<td colspan="3">
					
				        <logic:present name="parametros_pagina" property="inicio">
				            &lt;&lt;<html:link action="/tipos.do?mntnmnt=yes" paramId="pagina" paramName="parametros_pagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
				        </logic:present>
				        <logic:present name="parametros_pagina" property="anterior">
				            &lt;<html:link action="/tipos.do?mntnmnt=yes" paramId="pagina" paramName="parametros_pagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;      
				        </logic:present>
				        - <bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /> <bean:message key="pagina.de" /> <bean:write name="parametros_pagina" property="nreg" /> -  
				        <logic:present name="parametros_pagina" property="siguiente">
				            <html:link action="/tipos.do?mntnmnt=yes" paramId="pagina" paramName="parametros_pagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;      
				        </logic:present>
				        <logic:present name="parametros_pagina" property="final">
				            <html:link action="/tipos.do?mntnmnt=yes" paramId="pagina" paramName="parametros_pagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;      
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
		        <html:multibox property="seleccionados" styleClass="radio"> 
		            <bean:write name="i" property="tipoelemento"/>
		        </html:multibox>
	      </td>      
	      <td>
		      		<logic:notEmpty name="i" property="traduccion">
						<bean:write name="i" property="traduccion.nombre" />
					</logic:notEmpty>
			    	<logic:empty name="i" property="traduccion">
						[<bean:message key="tipo.nonombre" />]
					</logic:empty>
	      </td>
	      <td>
		      <logic:equal name="i" property="tipoelemento" value="0"><bean:message key="tipo.cod0" /></logic:equal>
		      <logic:equal name="i" property="tipoelemento" value="1"><bean:message key="tipo.cod1" /></logic:equal>
		      <logic:equal name="i" property="tipoelemento" value="2"><bean:message key="tipo.cod2" /></logic:equal>
		      <logic:equal name="i" property="tipoelemento" value="3"><bean:message key="tipo.cod3" /></logic:equal>
   		      <logic:equal name="i" property="tipoelemento" value="4"><bean:message key="tipo.cod4" /></logic:equal>
   		      <logic:equal name="i" property="tipoelemento" value="5"><bean:message key="tipo.cod5" /></logic:equal>
	      </td>
	      <td>
    		   <bean:write name="i" property="clasificacion" />
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
var uriEdicion="noticias.do?tipo=";
var uriEdicionClase="&clase=";
var uriEdicionNoticias="noticias.do?tipo=";
var uriEdicionTipo="tipoEdita.do?id=";
var alert1="<bean:message key="tipo.alert1"/>";
var alert2="<bean:message key="tipo.alert2"/>";
var alert3="<bean:message key="tipo.alert3"/>";

<logic:present name="alert">
	alert("<bean:write name='alert' />");
</logic:present>

-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>