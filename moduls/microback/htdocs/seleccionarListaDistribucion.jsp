<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/jsListadosSeleccionLDistrib.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<script type="text/javascript">
		function submitForm(){
			var form = document.getElementById("accFormularioLista");
			form.submit();
		}

		function debug(){
			var trs;
			var str = "";
			var tbodys = document.getElementsByTagName('tbody');
			for(i=0;i<tbodys.length;i++) {
				trs = tbodys[i].getElementsByTagName('tr');
				for(j=0;j<trs.length;j++) {
					var inputcheck = trs[j].getElementsByTagName('input');
					if (inputcheck[0].checked) {
						str = str + " " + inputcheck[0].value;
					}
				}
			}
			return str;
		}
		
	</script>
	
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li><a href="convocatoriaEdita.do?id=<bean:write name="seleccionLDistribucion" property="idConvocatoria"/>"><bean:message key="menu.convocatorias" /></a></li>
		<li class="pagActual"><bean:message key="menu.convocatorias" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/convocatorias.gif" alt="<bean:message key="menu.convocatorias" />" />
	<bean:message key="menu.seleccionar.convocatorias" />. <span><bean:message key="convocatoria.lista" /></span></h1>
	  

	<html:form action="/escogerDistribConvoc.do"  styleId="accFormSearch">
		<div id="botonera">
			<span class="grup">
				<html:text property="filtro" size="10"/> 
				<button type="button" title="<bean:message key="op.3" />" onclick="submitFormBuscar();"><img src="imgs/botons/cercar.gif" alt="<bean:message key="op.3" />" /></button> 
			</span>
			<button type="button" title="<bean:message key="op.3" />" onclick="submitForm();"><img src="imgs/botons/descargar.gif" alt="<bean:message key="convocatoria.confirmarseleccio" />" /></button>
		</div>
	</html:form>	
	
	<p><bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="ldistribucio.plural" /></strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.</p>

	<html:form action="/escogerDistribConvocAcc.do"  styleId="accFormularioLista">
		<html:hidden name="seleccionLDistribucion" property="idConvocatoria"/>
		<html:hidden name="seleccionLDistribucion" property="ordenacion" />
	  	<table cellpadding="0" cellspacing="0" class="llistat" id="seleccion">
			<thead>
				<tr>
					<th  class="check"/>
					<th width="40%">
			            <bean:message key="ldistribucio.eticolumna1" />&nbsp;
			            <html:link href="javascript:ordenar('Anombre');">
			                <logic:equal name="seleccionLDistribucion" property="ordenacion" value="Anombre">
			                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'/>
			                </logic:equal>
			                <logic:notEqual name="seleccionLDistribucion" property="ordenacion" value="Anombre">
			                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'/>
			                </logic:notEqual>
			            </html:link>
			            <html:link href="javascript:ordenar('Dnombre');">
			                <logic:equal name="seleccionLDistribucion" property="ordenacion" value="Dnombre">
			                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'/>
			                </logic:equal>
			                <logic:notEqual name="seleccionLDistribucion" property="ordenacion" value="Dnombre">
			                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'/>
			                </logic:notEqual>            
			            </html:link>
			        </th>
					<th>
			            <bean:message key="ldistribucio.eticolumna2" />&nbsp;
			            <html:link href="javascript:ordenar('Adescripcion');">
			                <logic:equal name="seleccionLDistribucion" property="ordenacion" value="Adescripcion">
			                    <img src="imgs/iconos/orden_ascendente_on.gif" alt='<bean:message key="op.4"/>'/>
			                </logic:equal>
			                <logic:notEqual name="seleccionLDistribucion" property="ordenacion" value="Adescripcion">
			                    <img src="imgs/iconos/orden_ascendente_off.gif" alt='<bean:message key="op.4"/>'/>
			                </logic:notEqual>
			            </html:link>
			            <html:link href="javascript:ordenar('Ddescripcion');">
			                <logic:equal name="seleccionLDistribucion" property="ordenacion" value="Ddescripcion">
			                    <img src="imgs/iconos/orden_descendente_on.gif" alt='<bean:message key="op.5"/>'/>
			                </logic:equal>
			                <logic:notEqual name="seleccionLDistribucion" property="ordenacion" value="Ddescripcion">
			                    <img src="imgs/iconos/orden_descendente_off.gif" alt='<bean:message key="op.5"/>'/>
			                </logic:notEqual>            
			            </html:link>
			        </th>
				</tr>
			</thead>
			<tbody>
			    <logic:iterate id="i" name="seleccionLDistribucion" property="destinatarios" indexId="indice">
					<tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
						<td  class="check">
						  <html:multibox name="seleccionLDistribucion" property="seleccionados" styleClass="radio"><bean:write name="i" property="id"/></html:multibox>						  
						</td>
						<td><bean:write name="i" property="nombre" /></td>
						<td><bean:write name="i" property="descripcion" /></td>
					</tr>
			    </logic:iterate>
			</tbody>
		</table>
	
	</html:form>

	<script>
		var uriEdicion="#";
		var alert1="<bean:message key="ldistribucio.alert1"/>";
		var alert2="<bean:message key="ldistribucio.alert2"/>";
	</script>
</body>
</html>


<jsp:include page="/moduls/pieControl.jsp"/>

		  
