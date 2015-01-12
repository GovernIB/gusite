<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/jsProcedimientos.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li class="pagActual"><bean:message key="mprocedimiento.procedimientos" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/procediments.gif" alt="<bean:message key="mprocedimiento.procedimientos" />" />
	<bean:message key="mprocedimiento.procedimientos" />. <span><bean:message key="mprocedimiento.datos" /></span></h1>


<bean:size id="tamano" name="procedimientosUA"/>

<logic:equal name="tamano" value="0">
		<p>
			<strong><bean:message key="mprocedimiento.vacio" /></strong>
		</p>
	
		<p>
			<bean:message key="mprocedimiento.aviso1" /><br/> 
			<bean:message key="mprocedimiento.aviso2" /><br/>
			<bean:message key="mprocedimiento.funcion" />	
		</p>
		
</logic:equal>

<logic:notEqual name="tamano" value="0">  

		<!-- botonera -->
		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="op.12" />" onclick="previsualizar();"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
			</span>
			<button type="button" title="<bean:message key="op.15" />" onclick="submitForm();"><img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" /></button>
		</div>


		<p>
			<bean:message key="mprocedimiento.aviso1" /><br/> 
			<bean:message key="mprocedimiento.aviso2" /><br/>
			<bean:message key="mprocedimiento.funcion" />
		</p>

		<p><bean:message key="encontrados" /> <strong><bean:write name="tamano" /> <bean:message key="mprocedimiento.procedimientos" /></strong>. </p>

		<html:form action="/procedimientos.do" styleId="accFormularioLista">
		<input type="hidden" name="Grabar" value="Grabar">
		
		     <logic:present name="MProcedimientoActionForm" property="id">
		         <html:hidden property="id" />
		     </logic:present>
		     <logic:present name="MProcedimientoActionForm" property="idmicrosite">
		         <html:hidden property="idmicrosite" />
		     </logic:present>     
		  <table cellpadding="0" cellspacing="0" class="llistat">
		  <thead>
			<tr>
				<th class="check">&nbsp;</th>			
				<th><html:checkbox property="todonada" onclick="selec_deselec()"/></th>
				<th width="95%">
		            <bean:message key="mprocedimiento.procedimientos" />&nbsp;
		        </th>
			</tr>
	       </thead>
	       <tbody>
		    <logic:iterate id="i" name="procedimientosUA" indexId="indice">
		      <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
		      <td class="check">
		        <html:multibox name="MProcedimientoActionForm" property="procedimientos" styleClass="radio"> 
		            <bean:write name="i" property="id"/>
		        </html:multibox>
		      </td>
		      <td>&nbsp;</td>
		      <td>
			      <logic:notEmpty name="i" property="traduccion">
				      <bean:write name="i" property="traduccion.nombre" ignore="true"/>
			      </logic:notEmpty>
		      </td>   
		    </tr>
		    </logic:iterate>
			</tbody>		
		  </table>
		
		</html:form>

</logic:notEqual>


</body>
</html>

<script type="text/javascript">
<!--


	function previsualizar() {
		abrirWindow('<bean:message key="url.aplicacion" />procedimientos.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&stat=no');
	}

	
	function submitForm(){
		var accForm = document.getElementById('accFormularioLista');
		accForm.submit();
	}


//-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>

