<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!--[if !IE]><!--><meta http-equiv="X-UA-Compatible" content="IE=edge" /><!--<![endif]-->
	<title><bean:message key="micro.listado.microsites" /> - Gestor Microsites</title>
	
	<!-- estils -->
	<link href="css/estils-v.4.1.css" rel="stylesheet" type="text/css" media="screen" />
	<!-- /estils -->
	
	<!-- js -->
	<script type="text/javascript" src="js/jquery-1.2.6.pack.js"></script>
	<script type="text/javascript" src="js/comuns.js"></script>
	<script type="text/javascript" src="js/llistat.js"></script>
    <script type="text/javascript" src="js/subMenus.js"></script>
    <script type="text/javascript" src="js/util.js"></script>
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


	<bean:define id="puedoeditar" value="0" />

     <logic:present name="MVS_rol_sys_adm" >
	     <logic:equal  name="MVS_rol_sys_adm" value="yes">
        	 <bean:define id="puedoeditar" value="1" />
    	 </logic:equal>
     </logic:present>
     
	<!-- contenidor -->
	<div id="contenidor">
	
		<!-- cap -->
		<jsp:include page="cabecera.jsp"/>
		<!-- /cap -->
		
		<!-- marc lateral -->
		<jsp:include page="menuLateralIzq.jsp"/>
		<!-- /marc lateral -->
		
		<bean:message key="errors.explorernou" />	
		
		<div id="errorIE11" style="display: none;"><bean:message key="errors.explorer.sup11" /> </div>
	
				
	<!-- continguts -->
		<div id="continguts">
		
			<h1><img src="imgs/titulos/configuracion.gif" alt="Llistats" /><bean:message key="menu2.gestion.microsites"/></h1>
		
			<bean:size id="tamanyolistado" name="listado"/>
			<!-- total -->
			<p><bean:message key="menu2.microsites.disponibles"/> <strong><bean:write name="tamanyolistado" /> <bean:message key="menu2.microsites.plural"/></strong>.</p>
			<!-- /total -->
			
			<html:form action="/microsites.do"  styleId="accFormSearch">
			<html:hidden property="ordenacion" />
				<!-- botonera -->
				<ul id="botonera">
					<logic:equal name="puedoeditar" value="1">					
						<li><button id="btCrear" type="button" title="<bean:message key="menu.nuevo"/>"><img src="imgs/botons/crear.gif" alt="" /></button></li>
					</logic:equal>
					<li><button id="btSeleccionar" type="button" title="<bean:message key="op.6" />"><img src="imgs/botons/seleccionar.gif" alt="" /></button></li>
					<logic:equal name="puedoeditar" value="1">
						<li class="sep"><button id="btExportar" type="button" title="<bean:message key="menu.exportar" />"><img src="imgs/botons/exportar.gif" alt="" /></button></li>
						<li class="sep"><button id="btEliminar" type="button" title="<bean:message key="op.2" />" ><img src="imgs/botons/eliminar.gif" alt="" /></button></li>
					</logic:equal>
				</ul>
				<!-- /botonera -->
			</html:form>
			
			
			<!-- tabla listado -->
			<table id="llistat">
				<thead>
					<tr>
						<th class="id">Identificador</th>
						<th><bean:message key="micro.titulo" /></th>
						<th><bean:message key="micro.uo" /></th>
						<th><bean:message key="micro.accesibilidad" /></th>						
					</tr>
				</thead>
				<tbody>
					
		 			<logic:iterate id="i" name="listado" indexId="indice">
					      <tr>
						      <td class="id">
						            <bean:write name="i" property="id"/>
						      </td>
						      <td class="nom">
						      		<logic:notEmpty name="i" property="traduccion">
										<bean:write name="i" property="traduccion.titulo" />
									</logic:notEmpty>
							    	<logic:empty name="i" property="traduccion">
										[<bean:message key="micro.noname" />]
									</logic:empty>
						      </td>      
						      <td class="organisme">
			      			      <bean:write name="i" property="nombreUA" />
						      </td>    
								<td>
									<logic:equal name="i" property="nivelAccesibilidad" value="1">
										<img src="imgs/accessibilitat/correcte.gif" alt="Correcte" title="Correcte" class="resultat" />TAW
									</logic:equal>
									<logic:equal name="i" property="nivelAccesibilidad" value="2">
										<img src="imgs/accessibilitat/warning.gif" alt="AdvertÃ¨ncia" title="AdvertÃ¨ncia" class="resultat" />TAW
									</logic:equal>
									<logic:equal name="i" property="nivelAccesibilidad" value="3">
										<img src="imgs/accessibilitat/error.gif" alt="Error" title="Error" class="resultat" />TAW
									</logic:equal>
									<logic:equal name="i" property="nivelAccesibilidad" value="4">
										<img src="imgs/accessibilitat/correcte.gif" alt="Correcte" title="Correcte" class="resultat" />XHTML
									</logic:equal>
									<logic:equal name="i" property="nivelAccesibilidad" value="5">
										<img src="imgs/accessibilitat/warning.gif" alt="AdvertÃ¨ncia" title="AdvertÃ¨ncia" class="resultat" />XHTML
									</logic:equal>
									<logic:equal name="i" property="nivelAccesibilidad" value="6">
										<img src="imgs/accessibilitat/error.gif" alt="Error" title="Error" class="resultat" />XHTML
									</logic:equal>
								</td>									        
					      </tr>
				    </logic:iterate>

				</tbody>
			</table>
			<!-- /tabla listado -->
		
		</div>
		<!-- /continguts -->
		<!-- peu -->
		<jsp:include page="peu.jsp"/>
		<!-- /peu -->		
	</div>
	<!-- contenidor -->

</body>

</html>
<script type="text/javascript">
	getInternetExplorerVersion();
</script>




