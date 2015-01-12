<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
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
		<li class="pagActual"><bean:message key="menu.encuestas" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/encuestas.gif" alt="<bean:message key="menu.encuestas" />" />
	<bean:message key="menu.encuestas" />. <span><bean:message key="encuesta.lista" /></span></h1>

<logic:equal name="parametros_pagina" property="nreg" value="0">

		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="encuesta.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="encuesta.crear" />" /></button> 
			</span>
		</div>

		<p>
			<bean:message key="encuesta.vacio" /><br/>
			<br/>&nbsp;&nbsp;<html:link href="encuestas.do"><bean:message key="encuesta.volver" /></html:link>
		</p>
		<html:form action="/encuestasAcc.do" styleId="accFormularioLista">
		  <html:hidden property="accion" />
		</html:form>
		
</logic:equal>

<logic:notEqual name="parametros_pagina" property="nreg" value="0">  

	<html:form action="/encuestas.do"  styleId="accFormSearch">
		<html:hidden property="ordenacion" />
		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="encuesta.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="encuesta.crear" />" /></button> 
				<button type="button" title="<bean:message key="op.6" />" onclick="editar();"><img src="imgs/menu/editar.gif" alt="<bean:message key="op.6" />" /></button> 
				<button type="button" title="<bean:message key="op.2" />" onclick="borravarios();"><img src="imgs/botons/borrar.gif" alt="<bean:message key="op.2" />" /></button>
			</span>
			<span class="grup">
				<html:text property="filtro" size="10"/> 
				<button type="button" title="<bean:message key="op.3" />" onclick="submitFormBuscar();"><img src="imgs/botons/cercar.gif" alt="<bean:message key="op.3" />" /></button> 
			</span>
		</div>		
		</html:form>

		<p><bean:message key="encuesta.dobleclic" />.</p>
		<p><bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="encuesta.plural" /></strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.</p>

<html:form action="/encuestasAcc.do"  styleId="accFormularioLista">
  <table cellpadding="0" cellspacing="0" class="llistat">
  <thead>
	<tr>
		<th class="check">&nbsp;</th>
		<th width="40%">
            <bean:message key="encuesta.eticolumna1" />&nbsp;
            <html:link href="javascript:ordenar('Atrad.titulo');">
                <logic:equal name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Atrad.titulo">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Atrad.titulo">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dtrad.titulo');">
                <logic:equal name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Dtrad.titulo">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Dtrad.titulo">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th>
            <bean:message key="encuesta.eticolumna2" />&nbsp;
            <html:link href="javascript:ordenar('Aenc.visible');">
                <logic:equal name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Aenc.visible">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Aenc.visible">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Denc.visible');">
                <logic:equal name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Denc.visible">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Denc.visible">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th>
            <bean:message key="encuesta.eticolumna3" />&nbsp;
            <html:link href="javascript:ordenar('Aenc.fpublicacion');">
                <logic:equal name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Aenc.fpublicacion">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Aenc.fpublicacion">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Denc.fpublicacion');">
                <logic:equal name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Denc.fpublicacion">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Denc.fpublicacion">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th>
            <bean:message key="encuesta.eticolumna4" />&nbsp;
            <html:link href="javascript:ordenar('Aenc.fcaducidad');">
                <logic:equal name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Aenc.fcaducidad">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Aenc.fcaducidad">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Denc.fcaducidad');">
                <logic:equal name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Denc.fcaducidad">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaEncuestaActionForm" property="ordenacion" value="Denc.fcaducidad">
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
            &lt;&lt;<html:link action="/encuestas.do" paramId="pagina" paramName="parametros_pagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
        </logic:present>
        <logic:present name="parametros_pagina" property="anterior">
            &lt;<html:link action="/encuestas.do" paramId="pagina" paramName="parametros_pagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;      
        </logic:present>
        - <bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /> <bean:message key="pagina.de" /> <bean:write name="parametros_pagina" property="nreg" /> -  
        <logic:present name="parametros_pagina" property="siguiente">
            <html:link action="/encuestas.do" paramId="pagina" paramName="parametros_pagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;      
        </logic:present>
        <logic:present name="parametros_pagina" property="final">
            <html:link action="/encuestas.do" paramId="pagina" paramName="parametros_pagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;      
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
		<bean:write name="i" property="traduccion.titulo" />
      </td>
	  <td>
      	<logic:equal name="i" property="visible" value="S">Si</logic:equal>
      	<logic:notEqual name="i" property="visible" value="S">No</logic:notEqual>
      </td>
      <td><bean:write name="i" property="fpublicacion" formatKey="date.short.format"/></td>   
      <td><bean:write name="i" property="fcaducidad" formatKey="date.short.format"/></td>      

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

var uriEdicion="encuestaEdita.do?id=";
var alert1="<bean:message key="encuesta.alert1"/>";
var alert2="<bean:message key="encuesta.alert2"/>";

function previsualizar() {
	abrirWindow('<bean:message key="url.aplicacion" />encuestas.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>');
}

-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>

