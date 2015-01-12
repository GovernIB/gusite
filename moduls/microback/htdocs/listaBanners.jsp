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
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li class="pagActual"><bean:message key="banner.datos" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/banner.gif" alt="<bean:message key="banner.datos" />" />
	<bean:message key="banner.datos" />. <span><bean:message key="banner.lista" /></span></h1>


<logic:equal name="parametros_pagina" property="nreg" value="0">
	<!-- No hay ningun banner -->
	
		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="banner.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="banner.crear" />" /></button> 
			</span>
		</div>

		<p>
			<bean:message key="banner.vacio" /><br/>
			<br/>&nbsp;&nbsp;<html:link href="banners.do"><bean:message key="banner.volver" /></html:link>
		</p>
		<html:form action="/bannersAcc.do" styleId="accFormularioLista" >
		  <html:hidden property="accion" />
		</html:form>



</logic:equal>

<logic:notEqual name="parametros_pagina" property="nreg" value="0">  

<!-- HAY algun banner -->
		
		<html:form action="/banners.do"  styleId="accFormSearch">
		<html:hidden property="ordenacion" />
		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="banner.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="banner.crear" />" /></button> 
				<button type="button" title="<bean:message key="op.6" />" onclick="editar();"><img src="imgs/menu/editar.gif" alt="<bean:message key="op.6" />" /></button> 
				<button type="button" title="<bean:message key="op.2" />" onclick="borravarios();"><img src="imgs/botons/borrar.gif" alt="<bean:message key="op.2" />" /></button>
			</span>
			<span class="grup">
				<html:text property="filtro" size="10"/> 
				<button type="button" title="<bean:message key="op.3" />" onclick="submitFormBuscar();"><img src="imgs/botons/cercar.gif" alt="<bean:message key="op.3" />" /></button> 
			</span>
		</div>		
		</html:form>

		<p><bean:message key="banner.dobleclic" />.</p>
		<p><bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="banner.plural" /></strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.</p>


<html:form action="/bannersAcc.do" styleId="accFormularioLista">
  <table cellpadding="0" cellspacing="0" class="llistat">
  <thead>
	<tr>
		<th class="check">&nbsp;</th>
		<th width="20%">
            <bean:message key="banner.eticolumna2" />&nbsp;
            <html:link href="javascript:ordenar('Aban.fpublicacion');">
                <logic:equal name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Aban.fpublicacion">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Aban.fpublicacion">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dban.fpublicacion');">
                <logic:equal name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Dban.fpublicacion">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Dban.fpublicacion">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th width="20%">
            <bean:message key="banner.eticolumna1" />&nbsp;
            <html:link href="javascript:ordenar('Aban.fcaducidad');">
                <logic:equal name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Aban.fcaducidad">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Aban.fcaducidad">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dban.fcaducidad');">
                <logic:equal name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Dban.fcaducidad">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Dban.fcaducidad">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th width="40%">
            <bean:message key="banner.eticolumna3" />&nbsp;
            <html:link href="javascript:ordenar('Atrad.titulo');">
                <logic:equal name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Atrad.titulo">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Atrad.titulo">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dtrad.titulo');">
                <logic:equal name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Dtrad.titulo">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Dtrad.titulo">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th width="20%">
            <bean:message key="banner.eticolumna4" />&nbsp;
            <html:link href="javascript:ordenar('Atrad.alt');">
                <logic:equal name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Atrad.alt">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Atrad.alt">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dtrad.alt');">
                <logic:equal name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Dtrad.alt">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaBannerActionForm" property="ordenacion" value="Dtrad.alt">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
	</tr>
	</thead>
	<tfoot>
	<tr>
			<td colspan="6">
							
		        <logic:present name="parametros_pagina" property="inicio">
		            &lt;&lt;<html:link action="/banners.do" paramId="pagina" paramName="parametros_pagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
		        </logic:present>
		        <logic:present name="parametros_pagina" property="anterior">
		            &lt;<html:link action="/banners.do" paramId="pagina" paramName="parametros_pagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;      
		        </logic:present>
		        - <bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /> <bean:message key="pagina.de" /> <bean:write name="parametros_pagina" property="nreg" /> -  
		        <logic:present name="parametros_pagina" property="siguiente">
		            <html:link action="/banners.do" paramId="pagina" paramName="parametros_pagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;      
		        </logic:present>
		        <logic:present name="parametros_pagina" property="final">
		            <html:link action="/banners.do" paramId="pagina" paramName="parametros_pagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;      
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
      <td><bean:write name="i" property="fpublicacion" formatKey="date.short.format"/></td>   
      <td><bean:write name="i" property="fcaducidad" formatKey="date.short.format"/></td>      
      <td>
        	<logic:notEmpty name="i" property="traduccion">
				<bean:write name="i" property="traduccion.titulo" />
			</logic:notEmpty>
	    	<logic:empty name="i" property="traduccion">
				[<bean:message key="banner.notitulo" />]
			</logic:empty>
      </td>
      <td>
      <logic:notEmpty name="i" property="traduccion">
	  	<bean:write name="i" property="traduccion.alt" />
	  	</logic:notEmpty>
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

var uriEdicion="bannerEdita.do?id=";
var alert1="<bean:message key="banner.alert1"/>";
var alert2="<bean:message key="banner.alert2"/>";

-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
