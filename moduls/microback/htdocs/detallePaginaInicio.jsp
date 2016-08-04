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


<%session.setAttribute("action_path_key",null);%>
<html:form action="/paginaInicioEdita.do" enctype="multipart/form-data" styleId="accFormulario">

		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	

		<input type="hidden" name="accion" value=""/>
		<input type="hidden" name="espera" value="si" id="espera" />
		<!-- botonera -->	
		<div id="botonera">
				<span class="grup">
			   	<button type="button" title='<bean:message key="micro.boto.tornargestio.titol"/>' onclick='window.parent.location.href="index.do?idsite=<bean:write name="MVS_microsite" property="id"/>";'>
			   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.titol"/>' />
			   </button> 
				</span>
				<span class="grup">
					<button type="button" title="<bean:message key="op.12" />" onclick="previsualizar();"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
				</span>
				<button type="submit"  title='<bean:message key="operacion.guardar"/>' onclick="submitForm('Guardar');">
			   		<img src="imgs/botons/guardar.gif" alt='<bean:message key="operacion.guardar"/>' /> &nbsp;<bean:message key="operacion.guardar" />
			   	</button>
		</div>

		<html:hidden property="id" />
		
		<div id="formulario">
			<!-- las tablas están entre divs por un bug del FireFox -->
			<div id="capa_tablaHome">
				<table cellpadding="0" cellspacing="0" class="edicio" width="90%">
					<tr>
						<td><html:radio property="plantilla" value="1" /></td>
						<td class="desc"><bean:message key="micro.pl1" /></td>
						<td><img src="imgs/general/micro_pl01.gif"/></td>
					</tr>
					<tr class="par">
						<td><html:radio property="plantilla" value="4" /></td>
						<td class="desc"><bean:message key="micro.pl4" /></td>
						<td>
							<html:text property="urlhome" size="45" maxlength="512" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('urlhome','microManamegedURL');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button>
							<br/><div id="microManamegedURL" style="color:#8a4700; font-weight:bold;"><bean:write name="MVS_URL_migapan" filter="false" ignore="true"/></div>
						</td>
					</tr>
				</table>
			</div>
		</div>
</html:form>

<br/>
<br/>

<script type="text/javascript">
<!--

	<logic:present name="MVS_microsite">
	function previsualizar() {
		abrirWindow('<bean:message key="url.aplicacion" />index.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&stat=no');
	}

	function submitForm(nom_accio){
		var accForm = document.getElementById('accFormulario');
		accForm.accion.value= nom_accio;
		 if (nom_accio== "Traduir") {
			 accForm.accion.value="<bean:message key='operacion.traducir'/>";
		 }else if (nom_accio== "Guardar"){
			 accForm.accion.value="<bean:message key='operacion.guardar'/>";
		}
		accForm.submit();
	}

	</logic:present>
	

    var Rcajatemp;
    var divContenedor;
    function Rpopupurl(obj,divcontenedor) {
    	divContenedor=document.getElementById(divcontenedor);
    	Rcajatemp=document.microForm[obj];
    	window.open('recursos.do?homedesactivado=true','recursos','scrollbars=yes,width=700,height=400');
    }
	
	function Rmeterurl(laurl) {
		divContenedor.innerHTML="";
		Rcajatemp.value=laurl;
	}
	
// -->
</script>

</body>
</html>

<jsp:include page="/moduls/pieControl.jsp"/>