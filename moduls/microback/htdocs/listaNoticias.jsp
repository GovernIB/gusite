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
		<li><a href="tipos.do?mntnmnt=yes"><bean:message key="menu.listados" /></a></li>		
		<li class="pagActual"><bean:write name="MVS_tipo_elemento" property="traduccion.nombre"/></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/fichas.gif" alt="<bean:write name="MVS_tipo_elemento" property="traduccion.nombre"/>" />
	<bean:write name="MVS_tipo_elemento" property="traduccion.nombre"/>. <span><bean:message key="noticia.mantenimiento" /></span>
	</h1>

<logic:equal name="parametros_pagina" property="nreg" value="0">
	<!-- No hay ningun elemento -->
	    <p>
		
		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>		
		
		<div id="botonera">
			<span class="grup">
		   	<button type="button" title='<bean:message key="tipo.volvermantenimiento"/>' onclick='document.location.href="tipos.do?mntnmnt=yes";'>
		   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="tipo.volvermantenimiento"/>' />
		    </button> 
			</span>
			<span class="grup">
				<button type="button" title="<bean:message key="tipo.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="tipo.crear" />" /></button> 
			</span>
		</div>
		
		</p>

		<html:form action="/noticiasAcc.do" styleId="accFormularioLista" >
		  <html:hidden property="accion" />
		</html:form>	
</logic:equal>


<logic:notEqual name="parametros_pagina" property="nreg" value="0">  


		<html:form action="/noticias.do"  styleId="accFormSearch">
		<html:hidden property="ordenacion" />
		
		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>
		
		<div id="botonera">
			<span class="grup">
		   	<button type="button" title='<bean:message key="tipo.volvermantenimiento"/>' onclick='document.location.href="tipos.do?mntnmnt=yes";'>
		   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="tipo.volvermantenimiento"/>' />
		    </button> 
			</span>	
			<span class="grup">
				<button type="button" title="<bean:message key="noticia.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="noticia.crear" />" /></button> 
				<button type="button" title="<bean:message key="op.6" />" onclick="editar();"><img src="imgs/menu/editar.gif" alt="<bean:message key="op.6" />" /></button> 
				<button type="button" title="<bean:message key="op.2" />" onclick="borravarios();"><img src="imgs/botons/borrar.gif" alt="<bean:message key="op.2" />" /></button>
				<button type="button" title="<bean:message key="op.16" />" onclick="clonar();"><img src="imgs/botons/clonar.gif" alt="<bean:message key="op.16" />" /></button>
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

		<p><bean:message key="noticia.dobleclic" />.</p>
		<p><bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="noticia.plural" /> <logic:present name="MVS_tipo_elemento"> '<bean:write name="MVS_tipo_elemento" property="traduccion.nombre"/>'</logic:present></strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.</p>

		<!-- ficha resumen -->
		<div id="llistatResumen">
			<dl>
				<dt><bean:message key="noticia.resumen.clase" />:</dt>
				<dd>
					<logic:equal name="MVS_tipo_elemento" property="tipoelemento" value="0"><bean:message key="tipo.cod0" /></logic:equal>
					<logic:equal name="MVS_tipo_elemento" property="tipoelemento" value="1"><bean:message key="tipo.cod1" /></logic:equal>
					<logic:equal name="MVS_tipo_elemento" property="tipoelemento" value="2"><bean:message key="tipo.cod2" /></logic:equal>
					<logic:equal name="MVS_tipo_elemento" property="tipoelemento" value="4"><bean:message key="tipo.cod4" /></logic:equal>
					<logic:equal name="MVS_tipo_elemento" property="tipoelemento" value="5"><bean:message key="tipo.cod5" /></logic:equal>
				</dd>
				<dt><bean:message key="noticia.resumen.paginacion" />:</dt>
				<dd>
					<logic:equal name="MVS_tipo_elemento" property="tipopagina" value="0"><bean:message key="tipo.pag.cod0" /></logic:equal>
					<logic:equal name="MVS_tipo_elemento" property="tipopagina" value="1"><bean:message key="tipo.pag.cod1" /></logic:equal>				
				</dd>
				<logic:equal name="MVS_tipo_elemento" property="tipopagina" value="0">
					<dt><bean:message key="noticia.resumen.tamanyo" />:</dt>
					<dd><bean:write name="MVS_tipo_elemento" property="tampagina"/></dd>
				</logic:equal>
				<dt><bean:message key="noticia.resumen.buscador" />:</dt>
				<dd>
					<logic:equal name="MVS_tipo_elemento" property="buscador" value="S">S&iacute;</logic:equal>
					<logic:notEqual name="MVS_tipo_elemento" property="buscador" value="S">No</logic:notEqual>					
				</dd>
				<dt><bean:message key="noticia.resumen.orden" />:</dt>
				<dd>
					<logic:equal name="MVS_tipo_elemento" property="orden" value="0"><bean:message key="tipo.orden0" /></logic:equal>
					<logic:equal name="MVS_tipo_elemento" property="orden" value="1"><bean:message key="tipo.orden1" /></logic:equal>
					<logic:equal name="MVS_tipo_elemento" property="orden" value="2"><bean:message key="tipo.orden2" /></logic:equal>
					<logic:equal name="MVS_tipo_elemento" property="orden" value="3"><bean:message key="tipo.orden3" /></logic:equal>					
				</dd>
			</dl>
			<p><button type="button" title="<bean:message key="noticia.resumen.editalistado" />" onclick="document.location='tipoEdita.do?id=<bean:write name="MVS_tipo_elemento" property="id"/>';"><img src="imgs/menu/editar.gif" alt="<bean:message key="noticia.resumen.editalistado" />" /></button></p>
			<logic:present name="MVS_lista_componentes">
				<h2><bean:message key="noticia.resumen.comporel" /></h2>
				<ul>
					<logic:iterate id="i" name="MVS_lista_componentes" indexId="indice">
						<li><a href="componenteEdita.do?id=<bean:write name="i" property="id"/>" title="<bean:message key="noticia.resumen.editacompo" />"><bean:write name="i" property="nombre"/></a></li>	
					</logic:iterate>					
				</ul>
			</logic:present>
			<logic:notPresent name="MVS_lista_componentes">
				<p><em><bean:message key="noticia.resumen.nocompo" /></em>.</p>
			</logic:notPresent>
		</div>

		<html:form action="/noticiasAcc.do" styleId="accFormularioLista">
		  <table cellpadding="0" cellspacing="0" class="llistat" style="width:78%;">
		  <thead>
			<tr>
				<th class="check">&nbsp;</th>
				<th width="20%">
		            <bean:message key="noticia.eticolumna2" />&nbsp;
		            <html:link href="javascript:ordenar('Anoti.fpublicacion');">
		                <logic:equal name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Anoti.fpublicacion">
		                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Anoti.fpublicacion">
		                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
		                </logic:notEqual>
		            </html:link>
		            <html:link href="javascript:ordenar('Dnoti.fpublicacion');">
		                <logic:equal name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Dnoti.fpublicacion">
		                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Dnoti.fpublicacion">
		                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
		                </logic:notEqual>            
		            </html:link>
		        </th>
				<th width="20%">
		            <bean:message key="noticia.eticolumna1" />&nbsp;
		            <html:link href="javascript:ordenar('Anoti.fcaducidad');">
		                <logic:equal name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Anoti.fcaducidad">
		                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Anoti.fcaducidad">
		                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
		                </logic:notEqual>
		            </html:link>
		            <html:link href="javascript:ordenar('Dnoti.fcaducidad');">
		                <logic:equal name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Dnoti.fcaducidad">
		                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Dnoti.fcaducidad">
		                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
		                </logic:notEqual>            
		            </html:link>
		        </th>
				<th>
		            <bean:message key="noticia.eticolumna3" />&nbsp;
		            <html:link href="javascript:ordenar('Atrad.titulo');">
		                <logic:equal name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Atrad.titulo">
		                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Atrad.titulo">
		                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
		                </logic:notEqual>
		            </html:link>
		            <html:link href="javascript:ordenar('Dtrad.titulo');">
		                <logic:equal name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Dtrad.titulo">
		                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Dtrad.titulo">
		                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
		                </logic:notEqual>            
		            </html:link>
		        </th>
				<logic:equal name="MVS_tipo_elemento" property="tipoelemento" value="0">		        
					<th>
			            <bean:message key="noticia.eticolumna4" />&nbsp;
			            <html:link href="javascript:ordenar('Atrad.subtitulo');">
			                <logic:equal name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Atrad.subtitulo">
			                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
			                </logic:equal>
			                <logic:notEqual name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Atrad.subtitulo">
			                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
			                </logic:notEqual>
			            </html:link>
			            <html:link href="javascript:ordenar('Dtrad.subtitulo');">
			                <logic:equal name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Dtrad.subtitulo">
			                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
			                </logic:equal>
			                <logic:notEqual name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Dtrad.subtitulo">
			                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
			                </logic:notEqual>            
			            </html:link>
			        </th>
		        </logic:equal>
				<th>
		            <bean:message key="noticia.orden" />&nbsp;
		            <html:link href="javascript:ordenar('Anoti.orden');">
		                <logic:equal name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Anoti.orden">
		                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Anoti.orden">
		                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
		                </logic:notEqual>
		            </html:link>
		            <html:link href="javascript:ordenar('Dnoti.orden');">
		                <logic:equal name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Dnoti.orden">
		                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaNoticiaActionForm" property="ordenacion" value="Dnoti.orden">
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
					            &lt;&lt;<html:link action="/noticias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
					        </logic:present>
					        <logic:present name="parametros_pagina" property="anterior">
					            &lt;<html:link action="/noticias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;      
					        </logic:present>
					        - <bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /> <bean:message key="pagina.de" /> <bean:write name="parametros_pagina" property="nreg" /> -  
					        <logic:present name="parametros_pagina" property="siguiente">
					            <html:link action="/noticias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;      
					        </logic:present>
					        <logic:present name="parametros_pagina" property="final">
					            <html:link action="/noticias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;      
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
								<bean:write name="i" property="traduccion.titulo" ignore="true"/>
							</logic:notEmpty>
					    	<logic:empty name="i" property="traduccion">
								[<bean:message key="noticia.notitulo" />]
							</logic:empty>
				      </td>
   				      <logic:equal name="MVS_tipo_elemento" property="tipoelemento" value="0">
					      <td>
						      <logic:notEmpty name="i" property="traduccion">
							  	<bean:write name="i" property="traduccion.subtitulo" />
						  	  </logic:notEmpty>
					      </td>
				      </logic:equal>
   				      <td><bean:write name="i" property="orden" ignore="true"/></td>
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

var uriEdicion="noticiaEdita.do?id=";
var alert1="<bean:message key="noticia.alert1"/>";
var alert2="<bean:message key="noticia.alert2"/>";
var alert3="<bean:message key="noticia.alert3"/>";
var alert4="<bean:message key="noticia.alert4"/>";

	function previsualizar() {
		abrirWindow('<bean:message key="url.aplicacion" />noticias.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&tipo=<bean:write name="MVS_idtipo" />&stat=no');
	}


-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>

