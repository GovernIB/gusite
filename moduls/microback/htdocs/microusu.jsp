<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />	
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <link href="css/estils.css" rel="stylesheet" type="text/css" />
        <link href="css/estils-v.4.1.css" rel="stylesheet" type="text/css" media="screen" />
	<script type="text/javascript" src="js/jsListados.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<title><bean:message key="micro.listado.microsites" /> - Gestor Microsites</title>
	<script type="text/javascript" src="js/jquery-1.2.6.pack.js"></script>
 	<script type="text/javascript" src="js/llistat.js"></script>
    <script type="text/javascript" src="js/subMenus.js"></script>
	<script type="text/javascript">
	<!--
		// enllaços
		var pagCrear = "index.do?accion=alta";
		var pagSeleccionar = "index.do?idsite=";
		var pagExportar = "index.do?accion=exportar&idsite=";
		var pagEliminar = "index.do?accion=eliminar&idsite=";
		
		// textos
		var txtNoSeleccionat = '<bean:message key="micro.home.selecciona"/>';
		var txtAlertEliminar = '<bean:message key="menu2.microsites.general.borrar.alert"/>';
		
		var alert1="<bean:message key="micro.alert1"/>";
		var alert2="<bean:message key="micro.alert2"/>";
		var alert3="<bean:message key="micro.alert3"/>";
		var alert4="<bean:message key="micro.alert4"/>";

		
	-->
	</script>
	<!-- /js -->
	
	<!--[if lt IE 7]>
		<link rel="stylesheet" type="text/css" href="css/estils-v.4.1_ie6.css" media="screen" />
	<![endif]-->
</head>

<body>
 
	<!-- contenidor -->
	<div id="contenidor">
	
		<!-- cap -->
		<jsp:include page="cabecera.jsp"/>
		<!-- /cap -->
		
		<!-- marc lateral -->
		<jsp:include page="menuLateralIzq.jsp"/>
		<!-- /marc lateral -->
		
		<!-- continguts -->
		<div id="continguts">
			
			<h1><img src="imgs/titulos/usuari.gif" alt="Detall" /><bean:message key="micro.usuario.gestion"/></h1>		
			
			 <br/>
			<!-- text info -->
			<div id="textInfo">
			<bean:write name="destino" filter="false"/> 
			<br/><br/>
				<html:messages id="message" message="true">
				<%= message %> 
				</html:messages>
				 
			</div>
			  <br/>
		</div>
		<!-- /continguts -->
		<!-- peu -->
		<jsp:include page="peu.jsp"/>
		<!-- /peu -->	
	</div>
	<!-- contenidor -->

</body>

</html>
<script>
<!--

var uriEdicion="#";
var alert1="<bean:message key="tipo.alert1"/>";
var alert2="<bean:message key="tipo.alert2"/>";

	function nuevomicro() {
		var accidmicro = document.getElementById('idmicro');
		var acciduser = document.getElementById('iduser').value;
		 
	    	var url = 'microUsuarios.do?accion=nuevomicro&idmicro='+accidmicro.value+'&iduser='+acciduser;
	    document.location = url;
	}
	
	function borravariosmicro() {
		 
		var acciduser = document.getElementById('iduser').value;
		var accFormLista = document.getElementById('accFormularioLista');
		var nselec=0;
	
	    if (accFormLista.seleccionados.length==undefined) {
	        if (accFormLista.seleccionados.checked) nselec=1;
	    } else {
	        for (var i=0;i<accFormLista.seleccionados.length;i++)
	            if (accFormLista.seleccionados[i].checked) nselec++;
	    }
	    
	    if (nselec==0) {
	            alert (alert1);
	            return;
	    }
	    
	    if (!confirm(alert2))
	            return;
	
	    accFormLista.accion.value='borrarmicro';
	    accFormLista.submit();
		 
	    	 
	   
	}
	
	
-->
</script>





