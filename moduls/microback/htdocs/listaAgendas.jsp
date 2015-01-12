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
		<li class="pagActual"><bean:message key="agenda.agenda" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/agenda.gif" alt="<bean:message key="agenda.agenda" />" />
	<bean:message key="agenda.agenda" />. <span><bean:message key="agenda.lista" /></span></h1>
	

<logic:equal name="parametros_pagina" property="nreg" value="0">

		<p>
			<logic:notPresent name="MVS_lista_actividades">
			<div class="alerta" style="font-weight:bold; color:#FF1111;">	
					<h3><bean:message key="agenda.actividad.temas" /></h3><br/>
					<em><strong><bean:message key="agenda.actividad.nohay" />.</strong> <bean:message key="agenda.actividad.alerta" />.&nbsp;&nbsp;&nbsp;</em> 
					<button type="button" title="<bean:message key="actividad.crear" />" onclick="document.location='actividadesAcc.do?accion=crear';"><img src="imgs/botons/nou.gif" alt="<bean:message key="actividad.crear" />" /></button>
					<button type="button" title="<bean:message key="agenda.actividad.mante" />" onclick="document.location='actividades.do';"><img src="imgs/menu/editarElements.gif" alt="<bean:message key="agenda.actividad.mante" />" /></button> 
					<br/>
			</div>
			</logic:notPresent>
				
			<logic:present name="MVS_lista_actividades">
					<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
					<jsp:include page="/moduls/mensajes.jsp"/>	
			</logic:present>

		</p>
		
		<div id="botonera">
			<span class="grup">
				<button type="button" title='<bean:message key="micro.boto.tornargestio.tag"/>' onclick='document.location.href="index_inicio.do";'>
			   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.tag"/>' />
			   	</button> 
			</span>
			<button type="button" title="<bean:message key="agenda.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="agenda.crear" />" /></button> 
		</div>
		
		<logic:notPresent name="MVS_lista_actividades">
			<em><strong><bean:message key="agenda.vacio" /></strong></em><br/><br/>		
		</logic:notPresent>
		
		
		<logic:present name="MVS_lista_actividades">
			<!-- ficha resumen -->
			<div id="llistatResumenLeft">
				<h3><bean:message key="agenda.actividad.temas" /></h3>
				<br/>
					<ul>
						<logic:iterate id="i" name="MVS_lista_actividades" indexId="indice">
							<li><bean:write name="i" property="traduce.nombre"/></li>	
						</logic:iterate>					
					</ul>
				<p>
						<button type="button" title="<bean:message key="actividad.crear" />" onclick="document.location='actividadesAcc.do?accion=crear';"><img src="imgs/botons/nou.gif" alt="<bean:message key="actividad.crear" />" /></button> 
						<button type="button" title="<bean:message key="agenda.actividad.mante" />" onclick="document.location='actividades.do';"><img src="imgs/menu/editarElements.gif" alt="<bean:message key="agenda.actividad.mante" />" /></button>				
				</p>
				
			</div>
		</logic:present>
		
		<html:form action="/agendasAcc.do" styleId="accFormularioLista">
		  <html:hidden property="accion" />
		</html:form>

</logic:equal>

<logic:notEqual name="parametros_pagina" property="nreg" value="0">  

		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	
	
	<html:form action="/agendas.do"  styleId="accFormSearch">
		<html:hidden property="ordenacion" />
		<div id="botonera">
			<span class="grup">
				<button type="button" title='<bean:message key="micro.boto.tornargestio.tag"/>' onclick='document.location.href="index_inicio.do";'>
			   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.tag"/>' />
			   	</button> 
			</span>
			<span class="grup">
				<button type="button" title="<bean:message key="agenda.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="agenda.crear" />" /></button> 
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

		<p><bean:message key="agenda.dobleclic" />.</p>
		<p><bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="agenda.plural" /></strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.</p>


		<!-- ficha resumen -->
		<div id="llistatResumen">
			<h3><bean:message key="agenda.actividad.temas" /></h3>
			<br/>
			<logic:present name="MVS_lista_actividades">
				<ul>
					<logic:iterate id="i" name="MVS_lista_actividades" indexId="indice">
						<li><bean:write name="i" property="traduce.nombre"/></li>	
					</logic:iterate>					
				</ul>
			</logic:present>
			<p>
					<button type="button" title="<bean:message key="actividad.crear" />" onclick="document.location='actividadesAcc.do?accion=crear';"><img src="imgs/botons/nou.gif" alt="<bean:message key="actividad.crear" />" /></button> 
					<button type="button" title="<bean:message key="agenda.actividad.mante" />" onclick="document.location='actividades.do';"><img src="imgs/menu/editarElements.gif" alt="<bean:message key="agenda.actividad.mante" />" /></button>				
			</p>
			
		</div>

<html:form action="/agendasAcc.do" styleId="accFormularioLista">
  <table cellpadding="0" cellspacing="0" class="llistat" style="width:78%;">
  <thead>
	<tr>
		<th class="check">&nbsp;</th>
		<th width="15%">
            <bean:message key="agenda.eticolumna1" />&nbsp;
            <html:link href="javascript:ordenar('Aagenda.finicio');">
                <logic:equal name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Aagenda.finicio">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Aagenda.finicio">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dagenda.finicio');">
                <logic:equal name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Dagenda.finicio">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Dagenda.finicio">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th width="15%">
            <bean:message key="agenda.eticolumna2" />&nbsp;
            <html:link href="javascript:ordenar('Aagenda.ffin');">
                <logic:equal name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Aagenda.ffin">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Aagenda.ffin">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dagenda.ffin');">
                <logic:equal name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Dagenda.ffin">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Dagenda.ffin">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th width="20%">
            <bean:message key="agenda.eticolumna3" />&nbsp;
            <html:link href="javascript:ordenar('Aagenda.actividad');">
                <logic:equal name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Aagenda.actividad">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Aagenda.actividad">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dagenda.actividad');">
                <logic:equal name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Dagenda.actividad">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Dagenda.actividad">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th width="20%">
            <bean:message key="agenda.eticolumna4" />&nbsp;
            <html:link href="javascript:ordenar('Aagenda.organizador');">
                <logic:equal name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Aagenda.organizador">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Aagenda.organizador">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dagenda.organizador');">
                <logic:equal name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Dagenda.organizador">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Dagenda.organizador">
                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
                </logic:notEqual>            
            </html:link>
        </th>
		<th width="30%">
            <bean:message key="agenda.eticolumna5" />&nbsp;
            <html:link href="javascript:ordenar('Atrad.titulo');">
                <logic:equal name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Atrad.titulo">
                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Atrad.titulo">
                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
                </logic:notEqual>
            </html:link>
            <html:link href="javascript:ordenar('Dtrad.titulo');">
                <logic:equal name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Dtrad.titulo">
                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
                </logic:equal>
                <logic:notEqual name="BuscaOrdenaAgendaActionForm" property="ordenacion" value="Dtrad.titulo">
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
            &lt;&lt;<html:link action="/agendas.do" paramId="pagina" paramName="parametros_pagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
        </logic:present>
        <logic:present name="parametros_pagina" property="anterior">
            &lt;<html:link action="/agendas.do" paramId="pagina" paramName="parametros_pagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;      
        </logic:present>
        - <bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /> <bean:message key="pagina.de" /> <bean:write name="parametros_pagina" property="nreg" /> -  
        <logic:present name="parametros_pagina" property="siguiente">
            <html:link action="/agendas.do" paramId="pagina" paramName="parametros_pagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;      
        </logic:present>
        <logic:present name="parametros_pagina" property="final">
            <html:link action="/agendas.do" paramId="pagina" paramName="parametros_pagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;      
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
      <td><bean:write name="i" property="finicio" formatKey="date.short.format"/></td>   
      <td><bean:write name="i" property="ffin" formatKey="date.short.format"/></td>      
      <td><bean:write name="i" property="actividad.traduccion.nombre"/></td>      
      <td><bean:write name="i" property="organizador"/></td>      
      <td>
      	<logic:present name="i" property="traduccion">
			<bean:write name="i" property="traduccion.titulo" ignore="true" />
		</logic:present>
    	<logic:notPresent name="i" property="traduccion">
			[<bean:message key="agenda.notitulo" />]
		</logic:notPresent>
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

var uriEdicion="agendaEdita.do?id=";
var alert1="<bean:message key="agenda.alert1"/>";
var alert2="<bean:message key="agenda.alert2"/>";


function previsualizar() {
	abrirWindow('<bean:message key="url.aplicacion" />agendas.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&stat=no');
}

-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>


