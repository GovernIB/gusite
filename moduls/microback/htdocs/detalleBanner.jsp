<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li><a href="banners.do"><bean:message key="banner.banners" /></a></li>
	    <logic:present name="bannerForm" property="id">
         		<li class="pagActual"><bean:message key="banner.modificacion" /></li>
	    </logic:present>		
	    <logic:notPresent name="bannerForm" property="id">
	         <li class="pagActual"><bean:message key="banner.alta" /></li>
	    </logic:notPresent>				
	</ul>

	<!-- titol pagina -->
	<h1><img src="imgs/titulos/banner.gif" alt="<bean:message key="banner.banners" />" />
	<bean:message key="banner.banners" />. 
		<span>
		    <logic:present name="bannerForm" property="id">
	         		<bean:message key="banner.modificacion" />
		    </logic:present>		
		    <logic:notPresent name="bannerForm" property="id">
		         <bean:message key="banner.alta" />
		    </logic:notPresent>					
		</span>
	</h1>


	<div style="font-weight:bold; color:#FF4400;">
	<html:errors/>
	</div>


	<div id="botonera">
		<button type="button" title="<bean:message key="op.15" />" onclick="submitForm();"><img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" /></button>
	</div>


<html:form action="/bannerEdita.do" enctype="multipart/form-data" styleId="accFormulario">

		<logic:present name="bannerForm" property="id">
			<input type="hidden" name="modifica">
			<html:hidden property="id" />
		</logic:present>
		<logic:notPresent name="bannerForm" property="id">
			<input type="hidden" name="anyade">
		</logic:notPresent>  

<div id="formulario">
	<!-- las tablas están entre divs por un bug del FireFox -->
		<table cellpadding="0" cellspacing="0" class="edicio">

		<tr class="par">
			<td class="etiqueta"><bean:message key="banner.fpublicacion" /></td>
			<td>
				<html:text property="fpublicacion" readonly="readonly" maxlength="16" />
			</td>
			<td class="etiqueta"><bean:message key="banner.fcaducidad" /></td>
			<td>
				<html:text property="fcaducidad" readonly="readonly" maxlength="16" />
			</td>
		</tr>
		<tr>
			<td class="etiqueta"><bean:message key="banner.visible" /></td>
			<td colspan="3"><html:radio property="visible" value="S" />&nbsp;Sí<html:radio property="visible" value="N" />&nbsp;No</td>
		</tr>

		<tr>
		<td colspan="4">

		<ul id="submenu">
			<logic:iterate id="lang" name="org.ibit.rol.sac.microback.LANGS_KEY" indexId="j">
				<li<%=(j.intValue()==0?" class='selec'":"")%>><a href="#" onclick="mostrarForm(this);"><bean:message name="lang" /></a></li>
        	</logic:iterate>
		</ul>    

	    <logic:iterate id="traducciones" name="bannerForm" property="traducciones" indexId="i" >
		<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
	
			<table cellpadding="0" cellspacing="0" class="tablaEdicio">
			<tr>
				<td class="etiqueta"><bean:message key="banner.titulo" />:</td>
				<td><html:text property="titulo" name="traducciones" size="50" maxlength="50" indexed="true" /></td>
			</tr>
			<tr>
				<td class="etiqueta"><bean:message key="banner.alt" />:</td>
				<td><html:text property="alt" name="traducciones" size="50" maxlength="100" indexed="true" /></td>
			</tr>
			<tr>
				<td class="etiqueta"><bean:message key="banner.url" />:</td>
				<td><html:text property="url" name="traducciones" size="50" maxlength="1024" indexed="true" />
				&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].url');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
			</tr>
			<tr>
				<td class="etiqueta"><bean:message key="banner.imagen" /></td>
				<td colspan="3">
				<div style="text-align:left" id="microManagedFile<%=i%>">
					<html:hidden property="<%="ficherosid["+i+"]"%>" />
					<logic:notEmpty name="bannerForm" property="<%="ficherosnom["+i+"]"%>">
	                	<html:text property="<%="ficherosnom["+i+"]"%>"/>
						<button type="button" title="<bean:message key="boton.eliminar" />" onclick="borraFile(this,'<%="ficheros["+i+"]"%>','<%="ficherosid["+i+"]"%>','<%="ficherosbor["+i+"]"%>','<%="ficherosnom["+i+"]"%>');"><img src="imgs/botons/borrar.gif" alt="<bean:message key="boton.eliminar" />" /></button>
						<button type="button" title="<bean:message key="op.12" />" onclick="abrirDoc('<bean:write name="bannerForm" property="<%="ficherosid["+i+"]"%>"/>');"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
				    </logic:notEmpty>
				    <logic:empty name="bannerForm" property="<%="ficherosnom["+i+"]"%>">
					    <html:file property="<%="ficheros["+i+"]"%>" size="30"/>
	   			    </logic:empty>
   			    </div>    	
   			    <br/><bean:message key="mensa.bannertamano" />
   			    
				</td>
			</tr>

			</table>
		</div>
    	</logic:iterate>
		</td></tr>
		</table>
</div>

</html:form>


</body>
</html>

<script type="text/javascript">
<!--

	function submitForm(){
		var accForm = document.getElementById('accFormulario');
		accForm.submit();
	}

    var Rcajatemp;
    function Rpopupurl(obj) {
    	Rcajatemp=document.bannerForm[obj];
		window.open('recursos.do','recursos','scrollbars=yes,width=700,height=400');
    }
	
	function Rmeterurl(laurl) {
		Rcajatemp.value=laurl;
	}


// -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
