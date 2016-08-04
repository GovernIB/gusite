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
		<li class="pagActual"><bean:message key="menu.contactos" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><bean:message key="menu.contactos" />. <span><bean:message key="formu.lista" /></span></h1>



<logic:equal name="parametros_pagina" property="nreg" value="0">
	
		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	
		
		<div id="botonera">
			<span class="grup">
				<button type="button" title='<bean:message key="micro.boto.tornargestio.tag"/>' onclick='document.location.href="index_inicio.do";'>
			   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.tag"/>' />
			   	</button> 
			</span>
				<button type="button" title="<bean:message key="formu.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="formu.crear" />" /></button> 
		</div>
	
		<html:form action="/formulariosAcc.do" styleId="accFormularioLista">
		  <html:hidden property="accion" />
		</html:form>
	
	
</logic:equal>

<logic:notEqual name="parametros_pagina" property="nreg" value="0">  
	
		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	

	<html:form action="/formularios.do"  styleId="accFormSearch">
		<html:hidden property="ordenacion" />
		<div id="botonera">
			<span class="grup">
				<button type="button" title='<bean:message key="micro.boto.tornargestio.tag"/>' onclick='document.location.href="index_inicio.do";'>
			   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.tag"/>' />
			   	</button> 
			</span>
			<span class="grup">
				<button type="button" title="<bean:message key="formu.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="formu.crear" />" /></button> 
				<button type="button" title="<bean:message key="op.6" />" onclick="editar();"><img src="imgs/menu/editar.gif" alt="<bean:message key="op.6" />" /></button> 
				<button type="button" title="<bean:message key="op.2" />" onclick="borravarios();"><img src="imgs/botons/borrar.gif" alt="<bean:message key="op.2" />" /></button>
			</span>
			<span class="grup">
				<button type="button" title="<bean:message key="op.12" />" onclick="previsualizar();"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
			</span>
			<span class="grup">
				<html:text property="filtro" size="10"/> 
				<button type="button" title="<bean:message key="op.3" />" onclick="submitFormBuscar();"><img src="imgs/botons/cercar.gif" alt="<bean:message key="op.3" />" /></button> 
			</span>
		</div>		
		</html:form>

		<p><bean:message key="formu.dobleclic" />.</p>
		<p><bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="formu.plural" /></strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.</p>

<html:form action="/formulariosAcc.do" styleId="accFormularioLista">
   <table cellpadding="0" cellspacing="0" class="llistat">
  <thead>
	<tr>
		<th class="check">&nbsp;</th>
		<th>
            <bean:message key="formu.eticolumna1" />&nbsp;
            <html:link href="javascript:ordenar('Acontacto.email');">
                <logic:equal name="BuscaOrdenaFormularioActionForm" property="ordenacion" value="Acontacto.email">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaFormularioActionForm" property="ordenacion" value="Acontacto.email">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dcontacto.email');">
                <logic:equal name="BuscaOrdenaFormularioActionForm" property="ordenacion" value="Dcontacto.email">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaFormularioActionForm" property="ordenacion" value="Dcontacto.email">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th>
            <bean:message key="formu.eticolumna2" />&nbsp;
            <html:link href="javascript:ordenar('Acontacto.visible');">
                <logic:equal name="BuscaOrdenaFormularioActionForm" property="ordenacion" value="Acontacto.visible">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaFormularioActionForm" property="ordenacion" value="Acontacto.visible">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dcontacto.visible');">
                <logic:equal name="BuscaOrdenaFormularioActionForm" property="ordenacion" value="Dcontacto.visible">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaFormularioActionForm" property="ordenacion" value="Dcontacto.visible">
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
            &lt;&lt;<html:link action="/formularios.do" paramId="pagina" paramName="parametros_pagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
        </logic:present>
        <logic:present name="parametros_pagina" property="anterior">
            &lt;<html:link action="/formularios.do" paramId="pagina" paramName="parametros_pagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;      
        </logic:present>
        - <bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /> <bean:message key="pagina.de" /> <bean:write name="parametros_pagina" property="nreg" /> -  
        <logic:present name="parametros_pagina" property="siguiente">
            <html:link action="/formularios.do" paramId="pagina" paramName="parametros_pagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;      
        </logic:present>
        <logic:present name="parametros_pagina" property="final">
            <html:link action="/formularios.do" paramId="pagina" paramName="parametros_pagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;      
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
       	<logic:notEmpty name="i" property="email">
			<bean:write name="i" property="email" />
		</logic:notEmpty>
    	<logic:empty name="i" property="email">
			[<bean:message key="formu.nocorre" />]
		</logic:empty>
      </td>
		<td>
			<logic:match name="i" property="visible" value="S">Si</logic:match>
			<logic:notMatch name="i" property="visible" value="S">No</logic:notMatch>
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

var uriEdicion="formularioEdita.do?accion="+"<bean:message key='operacion.editar'/>&id=";
var alert1="<bean:message key="formu.alert1"/>";
var alert2="<bean:message key="formu.alert2"/>";

function previsualizar() {
	abrirWindow('<bean:message key="url.aplicacion" />contactos.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&stat=no');
}

-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
