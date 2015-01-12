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

<body>	
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.configuracion" /></li>
		<li class="pagActual"><bean:message key="menu.home" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.home" />" />
	<bean:message key="menu.home" />. <span><bean:message key="micro.homemensa" /></span></h1>
	<!-- botonera -->
	<div id="botonera">
		<span class="grup">
			<button type="button" title="<bean:message key="op.12" />" onclick="previsualizar();"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
		</span>
		<button type="button" title="<bean:message key="op.15" />" onclick="submitForm();"><img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" /></button>
	</div>
	

		<div style="font-weight:bold; color:#FF4400;">
		<html:errors/>
		</div>

<html:form action="/microEdita.do" enctype="multipart/form-data" styleId="accFormulario">

		<!--  Mensajes de Información -->
		<logic:equal name="mensajes" value="si">
		<div class="alerta" style="color:#FF1111;">
				<html:messages id="message" message="true">
				<%= message %><br/>
				</html:messages>	
		</div>
		</logic:equal>

		<html:hidden property="id" />
		<input type="hidden" name="grabahome" value="Grabar">
		
		<div id="formulario">
			<!-- las tablas están entre divs por un bug del FireFox -->
			<div id="capa_tablaHome">
			<table cellpadding="0" cellspacing="0" class="edicio" width="90%">

			<tr>
				<td><html:radio property="plantilla" value="1" /></td>
				<td class="desc"><bean:message key="micro.pl1" /></td>
				<td><img src="imgs/general/micro_pl01.gif"/></td>
			</tr>
			<!-- 			
			<tr class="par">
				<td><html:radio property="plantilla" value="2" /></td>
				<td class="desc"><bean:message key="micro.pl2" /></td>
				<td><img src="imgs/general/micro_pl02.gif"/></td>
			</tr>
			<tr>
				<td><html:radio property="plantilla" value="3" /></td>
				<td class="desc"><bean:message key="micro.pl3" /></td>
				<td>
					<logic:notEmpty name="microForm" property="imagenPrincipalid">
						<img src='archivo.do?id=<bean:write name="microForm" property="imagenPrincipalid"/>'/>
					</logic:notEmpty>
					<logic:empty name="microForm" property="imagenPrincipalid">
						<bean:message key="micro.noimagen" />
					</logic:empty>
				</td>
			</tr>
			--> 
			<tr class="par">
				<td><html:radio property="plantilla" value="4" /></td>
				<td class="desc"><bean:message key="micro.pl4" /></td>
				<td>
					<html:text property="urlhome" size="45" maxlength="512" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('urlhome','microManamegedURL');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button>
					<br/><div id="microManamegedURL" style="color:#8a4700; font-weight:bold;"><bean:write name="MVS_URL_migapan" filter="false" ignore="true"/></div>
				</td>
			</tr>
			<!-- 
			<tr>
				<td><html:radio property="plantilla" value="5" /></td>
				<td class="desc"><bean:message key="micro.pl5" /></td>
				<td>
					<logic:notEmpty name="servicios">
				    <html:select property="planti5serv" multiple="true" size="8" onclick="javascript:marcatodos();">
	                	<html:option value=""></html:option>
		                <html:options collection="servicios" labelProperty="traduccion.nombre" property="id"/>
	    	        </html:select>
					</logic:notEmpty>
					<logic:empty name="servicios">
						<bean:message key="micro.noservicios" />
					</logic:empty>
				</td>
			</tr>
			 -->
			</table>
		</div>
	
	</div>
	

</html:form>

<br/>
<br/>

</body>
</html>

<script type="text/javascript">
<!--

	<logic:present name="MVS_microsite">
	function previsualizar() {
		abrirWindow('<bean:message key="url.aplicacion" />index.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&stat=no');
	}

	
	function submitForm(){
		var accForm = document.getElementById('accFormulario');
		accForm.submit();
	}
	</logic:present>

    var Rcajatemp;
    var divContenedor;
    function Rpopupurl(obj,divcontenedor) {
    	divContenedor=document.getElementById(divcontenedor);
    	Rcajatemp=document.microForm[obj];
		window.open('recursos.do','recursos','scrollbars=yes,width=700,height=400');
    }
	
	function Rmeterurl(laurl) {
		divContenedor.innerHTML="";
		Rcajatemp.value=laurl;
	}
	
// -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>