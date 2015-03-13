<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title><bean:message key="micro.listado.microsites" /> - Gestor Microsites</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />	
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	
    <link href="css/estils.css" rel="stylesheet" type="text/css" />
    <link href="css/estils-v.4.1.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="css/jquery-ui-1.10.4.custom.css" rel="stylesheet" type="text/css" media="screen" />
    
	<script type="text/javascript" src="js/jsListados.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.10.4/jquery-1.10.2.js"></script>
 	<script type="text/javascript" src="js/llistat.js"></script>
 	<script type="text/javascript" src="js/jquery-ui-1.10.4/jquery-ui-1.10.4.custom.min.js"></script>
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
		
		var alert1 = "<bean:message key="micro.alert1"/>";
		var alert2 = "<bean:message key="micro.alert2"/>";
		var alert3 = "<bean:message key="micro.alert3"/>";
		var alert4 = "<bean:message key="micro.alert4"/>";

		<%--
			Obtenemos una vez en el controlador todas las UAs (para sólo hacer una llamada al WS)
			y las incluimos en esta variable como la fuente de datos para el autocompletar. De otro
			modo habría que hacer una petición vía WS por cada tecla que apretamos para mostrar los
			nuevos resultados disponibles.
		--%>
		var nombresIdsMicrosites = [
		<logic:iterate id="i" name="listatodos">
			{'label': '<bean:write name="i" property="traduccion.titulo" filter="true" />', 'value': '<bean:write name="i" property="id" />'},
		</logic:iterate>
		];

		$(document).ready(function() {
						
			$("#nombremicro").autocomplete({
				source: nombresIdsMicrosites,
				minLength: 1,
				select: function(event, ui) {
					<%--
						Al seleccionar un elemento del listado que se despliega, rellenamos el input con el id del
						microsite y mostramos de nuevo el label, ya que el value es un ID y no le interesa al usuario
						ver eso en el input autocompletable.
					--%>
					$("#idmicro").val(ui.item.value);
					$("#nombremicro").val(ui.item.label);
					
					<%--
						Prevenimos el comportamiento por defecto del evento, ya que sólo queremos que se ejecute
						el código que hay anteriormente.
					--%>
					event.preventDefault();
				}
			});
						
		});
			
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
			<ul id="mollapa">
				 <li><a href="usuarios.do" /><bean:message key="usuari.gestion" /></a></li>			
				 <li><a href="usuarioEdita.do?id=<bean:write name='elusu'/>"</a><bean:message key="usuari.datos"/></a></li>
				 <li class="pagActual"><bean:message key="micro.usuario.gestion"/></li> 
		  	</ul>
		  				
			<h1><img src="imgs/titulos/configuracion.gif" alt="Llistats" /><bean:message key="micro.usuario.gestion"/> ( <bean:write name='nomusu'/> )</h1>
			
			<bean:size id="tamanyolistado" name="listado"/>
			<!-- total -->
			<p><bean:message key="menu2.microsites.disponibles"/> <strong><bean:write name="tamanyolistado" /> <bean:message key="menu2.microsites.plural"/></strong>.</p>
			<!-- /total -->

		<html:form action="/microUsuarios.do"  styleId="accFormSearch">
			<html:hidden property="ordenacion" />
			<input type="hidden" id="iduser" name="iduser" value="<bean:write name="elusu" />">
			<div id="botonera">
			
				<span class="grup">
					<button type="button" title="<bean:message key="op.2" />" onclick="borravariosmicro();"><img src="imgs/botons/borrar.gif" alt="<bean:message key="op.2" />" /></button>
				</span>
				
				<input type="hidden" id="idmicro" name="idmicro" />
				<input type="text" id="nombremicro" name="nombremicro" style="width: 550px" />
     
			    &nbsp;&nbsp;
				<button type="button" title="<bean:message key="micro.usuario.crear" />" onclick="nuevomicro();"><img src="imgs/botons/nou.gif" alt="<bean:message key="menu.nuevo" />" /></button>
			
			</div>		
		</html:form>
		  	 
			 <html:form action="/microusuariosAcc.do" styleId="accFormularioLista">
			 <input type="hidden" id="iduser" name="iduser" value="<bean:write name="elusu" />">
		   	 <table cellpadding="0" cellspacing="0" class="llistat">
			    <thead>
				<tr>
					<th class="check">&nbsp;</th>
					<th><bean:message key="micro.titulo" /></th>
					<th><bean:message key="micro.uo" /></th>
					<th><bean:message key="micro.accesibilidad" /></th>
				</tr>
				</thead>
				<tbody>		 
					    <logic:iterate id="i" name="listado" indexId="indice">
					      <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
					      <td class="check">
					        <html:multibox property="seleccionados" styleClass="radio"> 
					            <bean:write name="i" property="id"/>
					            
					        </html:multibox>
					      </td>
					      <td><logic:notEmpty name="i" property="traduccion">
										<bean:write name="i" property="traduccion.titulo" />
									</logic:notEmpty>
							    	<logic:empty name="i" property="traduccion">
										[<bean:message key="micro.noname" />]
									</logic:empty></td>
					      <td><bean:write name="i" property="nombreUA" /> </td>
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
 		    
		  <html:hidden property="accion" />
		
		</html:form>
			  
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

	var uriEdicion = "#";
	var alert1 = "<bean:message key="tipo.alert1"/>";
	var alert2 = "<bean:message key="tipo.alert2"/>";

	function nuevomicro() {
		
		var accidmicro = document.getElementById('idmicro');
		var acciduser = document.getElementById('iduser').value;
	    var url = 'microUsuarios.do?accion=nuevomicro&idmicro=' + accidmicro.value + '&iduser=' + acciduser;

        if (accidmicro.value !== "") {
            document.location = url;
        }
	}
	
	function borravariosmicro() {
		 
		var acciduser = document.getElementById('iduser').value;
		var accFormLista = document.getElementById('accFormularioLista');
		var nselec = 0;
	
	    if (accFormLista.seleccionados.length == undefined) {
	        if (accFormLista.seleccionados.checked)
		        nselec = 1;
	    } else {
	        for (var i = 0; i < accFormLista.seleccionados.length; i++)
	            if (accFormLista.seleccionados[i].checked)
		            nselec++;
	    }
	    
	    if (nselec == 0) {
            alert(alert1);
            return;
	    }
	    
	    if (!confirm(alert2))
            return;
	
	    accFormLista.accion.value='borrarmicro';
	    accFormLista.submit();	    	 
	   
	}
	
-->
</script>
