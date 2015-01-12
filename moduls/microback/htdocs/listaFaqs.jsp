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
		<li class="pagActual"><bean:message key="faq.faqs" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/faqs.gif" alt="<bean:message key="faq.faqs" />" />
	<bean:message key="faq.faqs" />. <span><bean:message key="faq.lista" /></span></h1>

<logic:equal name="parametros_pagina" property="nreg" value="0">
		<!-- No hay ninguna faq -->


	
		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="faq.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="faq.crear" />" /></button> 
			</span>
		</div>

		<p>
			<em><strong><bean:message key="faq.vacio" /><strong></em><br/><br/>

			<div class="alerta" style="font-weight:bold; color:#FF1111;">	
				<h3><bean:message key="faq.tema.temas" /></h3><br/>
				<logic:notPresent name="MVS_lista_temasfaqs">
					<em><strong><bean:message key="faq.tema.nohay" />.</strong> <bean:message key="faq.tema.alerta" />.&nbsp;&nbsp;&nbsp;</em><br/>
				</logic:notPresent>
				<logic:present name="MVS_lista_temasfaqs">
				<ul>
					<logic:iterate id="i" name="MVS_lista_temasfaqs" indexId="indice">
						<li><bean:write name="i" property="traduce.nombre"/></li>	
					</logic:iterate>					
				</ul>
				</logic:present>
				<br/>
				<button type="button" title="<bean:message key="tema.crear" />" onclick="document.location='temasAcc.do?accion=crear';"><img src="imgs/botons/nou.gif" alt="<bean:message key="tema.crear" />" /></button> 
				<button type="button" title="<bean:message key="faq.tema.mante" />" onclick="document.location='temas.do';"><img src="imgs/menu/editarElements.gif" alt="<bean:message key="faq.tema.mante" />" /></button>				
			</div>			
			
			<br/>&nbsp;&nbsp;<html:link href="faqs.do"><bean:message key="faq.volver" /></html:link>
		</p>
		<html:form action="/faqsAcc.do" styleId="accFormularioLista" >
		  <html:hidden property="accion" />
		</html:form>
</logic:equal>

<logic:notEqual name="parametros_pagina" property="nreg" value="0">  


<!-- HAY alguna faq -->
		
		<html:form action="/faqs.do"  styleId="accFormSearch">
		<html:hidden property="ordenacion" />
		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="faq.crear" />" onclick="crear();"><img src="imgs/botons/nou.gif" alt="<bean:message key="faq.crear" />" /></button> 
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

		<p><bean:message key="faq.dobleclic" />.</p>
		<p><bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="faq.plural" /></strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.</p>


		<!-- ficha resumen -->
		<div id="llistatResumen">
			<h3><bean:message key="faq.tema.temas" /></h3>
			<br/>
			<logic:present name="MVS_lista_temasfaqs">
				<ul>
					<logic:iterate id="i" name="MVS_lista_temasfaqs" indexId="indice">
						<li><bean:write name="i" property="traduce.nombre"/></li>	
					</logic:iterate>					
				</ul>
			</logic:present>
			<p>
					<button type="button" title="<bean:message key="tema.crear" />" onclick="document.location='temasAcc.do?accion=crear';"><img src="imgs/botons/nou.gif" alt="<bean:message key="tema.crear" />" /></button> 
					<button type="button" title="<bean:message key="faq.tema.mante" />" onclick="document.location='temas.do';"><img src="imgs/menu/editarElements.gif" alt="<bean:message key="faq.tema.mante" />" /></button>				
			</p>
			
		</div>

		<html:form action="/faqsAcc.do" styleId="accFormularioLista">
		  <table cellpadding="0" cellspacing="0" class="llistat" style="width:78%;">
		  <thead>
			<tr>
				<th class="check">&nbsp;</th>
				<th width="30%">
		            <bean:message key="faq.eticolumna1" />&nbsp;
		            <html:link href="javascript:ordenar('Afaq.fecha');">
		                <logic:equal name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Afaq.fecha">
		                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Afaq.fecha">
		                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
		                </logic:notEqual>
		            </html:link>
		            <html:link href="javascript:ordenar('Dfaq.fecha');">
		                <logic:equal name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Dfaq.fecha">
		                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Dfaq.fecha">
		                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
		                </logic:notEqual>            
		            </html:link>
		        </th>
				<th width="30%">
		            <bean:message key="faq.eticolumna2" />&nbsp;
		            <html:link href="javascript:ordenar('Afaq.tema');">
		                <logic:equal name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Afaq.tema">
		                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Afaq.tema">
		                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
		                </logic:notEqual>
		            </html:link>
		            <html:link href="javascript:ordenar('Dfaq.tema');">
		                <logic:equal name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Dfaq.tema">
		                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Dfaq.tema">
		                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
		                </logic:notEqual>            
		            </html:link>
		        </th>
				<th width="40%">
		            <bean:message key="faq.eticolumna3" />&nbsp;
		            <html:link href="javascript:ordenar('Atrad.pregunta');">
		                <logic:equal name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Atrad.pregunta">
		                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Atrad.pregunta">
		                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'>
		                </logic:notEqual>
		            </html:link>
		            <html:link href="javascript:ordenar('Dtrad.pregunta');">
		                <logic:equal name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Dtrad.pregunta">
		                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'>
		                </logic:equal>
		                <logic:notEqual name="BuscaOrdenaFaqActionForm" property="ordenacion" value="Dtrad.pregunta">
		                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'>
		                </logic:notEqual>            
		            </html:link>
		        </th>
			</tr>
			</thead>
			<tfoot>
			<tr>
					<td colspan="4">
									
			        <logic:present name="parametros_pagina" property="inicio">
			            &lt;&lt;<html:link action="/faqs.do" paramId="pagina" paramName="parametros_pagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
			        </logic:present>
			        <logic:present name="parametros_pagina" property="anterior">
			            &lt;<html:link action="/faqs.do" paramId="pagina" paramName="parametros_pagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;      
			        </logic:present>
			        - <bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /> <bean:message key="pagina.de" /> <bean:write name="parametros_pagina" property="nreg" /> -  
			        <logic:present name="parametros_pagina" property="siguiente">
			            <html:link action="/faqs.do" paramId="pagina" paramName="parametros_pagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;      
			        </logic:present>
			        <logic:present name="parametros_pagina" property="final">
			            <html:link action="/faqs.do" paramId="pagina" paramName="parametros_pagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;      
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
		      <td><bean:write name="i" property="fecha" formatKey="date.short.format"/></td>   
		      <td><bean:write name="i" property="tema.traduccion.nombre"/></td>      
		      <td>
		        	<logic:notEmpty name="i" property="traduccion">
						<bean:write name="i" property="traduccion.pregunta" filter="false"/>
					</logic:notEmpty>
			    	<logic:empty name="i" property="traduccion">
						[<bean:message key="faq.notitulo" />]
					</logic:empty>
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

var uriEdicion="faqEdita.do?id=";
var alert1="<bean:message key="noticia.alert1"/>";
var alert2="<bean:message key="noticia.alert2"/>";

	function previsualizar() {
		abrirWindow('<bean:message key="url.aplicacion" />faqs.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&stat=no');
	}


-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>





