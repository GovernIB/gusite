<%@ page language="java"%>
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
	<script type="text/javascript" src="js/coLlistats.js"></script>
	<script type="text/javascript">
	<!--
		function lanzaFiltros(formulario) {
			formulario.submit();
		}
		//
		var uriEdicion = "visorw3c.do?id=";
	-->
	</script>
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
			<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
			<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
			<li><bean:message key="menu.ferramentes" /></li>
			<li class="pagActual">Test Accessibilitat W3C</li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/taww3c.gif" alt="Accesibilidad" />Test Accessibilitat</h1>
	<!-- continguts -->
	

			<div id="botonera">
				<span class="grup">
					<button type="button" name="fichero" title="començament  de test" onclick="submitURL(<bean:write name="MVS_microsite" property="id"/>);"><img src="imgs/botons/taww3cButton.gif" alt="Comienzo testeo" /> &nbsp;començament  de test</button> 
				</span>
			</div>		

			
			<p>
			<strong>
				&nbsp;Aquest procés pot trigar diversos minuts depenent  de la grandaria del microsite.<br/> 
				&nbsp;una vegada premut el botó de <em>començament  de test</em>, encara que canvie de pantalla, el procés continuarà .<br/>
			</strong>
			</p>		
		
			<logic:present name="finalizado">
			 	
					<logic:equal name="finalizado" value="ok">
						<p><strong>&nbsp;Process finalitzat amb Ã¨xit <br/> </strong></p>
					</logic:equal>
					<logic:equal name="finalizado" value="error">
						<p><strong>S`ha produit un error <br/> </strong></p>
					</logic:equal>		
			</logic:present>
		
			<table cellpadding="0" cellspacing="0" class="llistat">
				<thead>
					<tr>
						<th class="check">&nbsp;</th>
						<th>Tipus pàgina</th>
						<th>Títol</th>
						<th>Idioma</th>
						<th class="resultat">Accessibilitat</th>
						<th class="resultat">XHTML</th>						
					</tr>
					</thead>
					<tbody>
					
						<logic:iterate id="i" name="mapalistados" property="kContenidos">
							<tr>
								<td class="check">
								<input type="checkbox" name="seleccionados" value="<bean:write name="i" property="id"/>" class="radio" />
								</td>
								<td>Contingut</td>
								<td><bean:write name="i" property="servicio"/></td>
								<td>
									<logic:equal name="i" property="idioma" value="ca">Català</logic:equal>
									<logic:equal name="i" property="idioma" value="es">Castellà</logic:equal>
									<logic:equal name="i" property="idioma" value="en">Anglés</logic:equal>
									<logic:equal name="i" property="idioma" value="de">Alemá</logic:equal>
								</td>
								<td>
									<logic:equal name="i" property="tawresultado" value="1">
										<img src="imgs/accessibilitat/correcte.gif" alt="Correcte" title="Correcte" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="tawresultado" value="2">
										<img src="imgs/accessibilitat/warning.gif" alt="AdvertÃ¨ncia" title="AdvertÃ¨ncia" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="tawresultado" value="3">
										<img src="imgs/accessibilitat/error.gif" alt="Error" title="Error" class="resultat" />
									</logic:equal>
								</td>
								<td>
									<logic:equal name="i" property="resultado" value="1">
										<img src="imgs/accessibilitat/correcte.gif" alt="Correcte" title="Correcte" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="resultado" value="2">
										<img src="imgs/accessibilitat/warning.gif" alt="AdvertÃ¨ncia" title="AdvertÃ¨ncia" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="resultado" value="3">
										<img src="imgs/accessibilitat/error.gif" alt="Error" title="Error" class="resultat" />
									</logic:equal>
								</td>								
							</tr>
						</logic:iterate>
						<logic:iterate id="i" name="mapalistados" property="kNoticias">
							<tr>
								<td class="check">
								<input type="checkbox" name="seleccionados" value="<bean:write name="i" property="id"/>" class="radio" />
								</td>
								<td>Element de llistat</td>
								<td><bean:write name="i" property="servicio"/></td>
								<td>
									<logic:equal name="i" property="idioma" value="ca">Català</logic:equal>
									<logic:equal name="i" property="idioma" value="es">Castellà</logic:equal>
									<logic:equal name="i" property="idioma" value="en">Anglés</logic:equal>
									<logic:equal name="i" property="idioma" value="de">Alemá</logic:equal>
								</td>
								<td>
									<logic:equal name="i" property="tawresultado" value="1">
										<img src="imgs/accessibilitat/correcte.gif" alt="Correcte" title="Correcte" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="tawresultado" value="2">
										<img src="imgs/accessibilitat/warning.gif" alt="AdvertÃ¨ncia" title="AdvertÃ¨ncia" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="tawresultado" value="3">
										<img src="imgs/accessibilitat/error.gif" alt="Error" title="Error" class="resultat" />
									</logic:equal>
								</td>
								<td>
									<logic:equal name="i" property="resultado" value="1">
										<img src="imgs/accessibilitat/correcte.gif" alt="Correcte" title="Correcte" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="resultado" value="2">
										<img src="imgs/accessibilitat/warning.gif" alt="AdvertÃ¨ncia" title="AdvertÃ¨ncia" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="resultado" value="3">
										<img src="imgs/accessibilitat/error.gif" alt="Error" title="Error" class="resultat" />
									</logic:equal>
								</td>								
							</tr>
						</logic:iterate>
						<logic:iterate id="i" name="mapalistados" property="kAgendas">
							<tr>
								<td class="check">
								<input type="checkbox" name="seleccionados" value="<bean:write name="i" property="id"/>" class="radio" />
								</td>
								<td>Event</td>
								<td><bean:write name="i" property="servicio"/></td>
								<td>
									<logic:equal name="i" property="idioma" value="ca">Català</logic:equal>
									<logic:equal name="i" property="idioma" value="es">Castellà</logic:equal>
									<logic:equal name="i" property="idioma" value="en">Anglés</logic:equal>
									<logic:equal name="i" property="idioma" value="de">Alemá</logic:equal>
								</td>
								<td>
									<logic:equal name="i" property="tawresultado" value="1">
										<img src="imgs/accessibilitat/correcte.gif" alt="Correcte" title="Correcte" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="tawresultado" value="2">
										<img src="imgs/accessibilitat/warning.gif" alt="AdvertÃ¨ncia" title="AdvertÃ¨ncia" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="tawresultado" value="3">
										<img src="imgs/accessibilitat/error.gif" alt="Error" title="Error" class="resultat" />
									</logic:equal>
								</td>
								<td>
									<logic:equal name="i" property="resultado" value="1">
										<img src="imgs/accessibilitat/correcte.gif" alt="Correcte" title="Correcte" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="resultado" value="2">
										<img src="imgs/accessibilitat/warning.gif" alt="AdvertÃ¨ncia" title="AdvertÃ¨ncia" class="resultat" />
									</logic:equal>
									<logic:equal name="i" property="resultado" value="3">
										<img src="imgs/accessibilitat/error.gif" alt="Error" title="Error" class="resultat" />
									</logic:equal>
								</td>								
							</tr>
						</logic:iterate>
						

				</tbody>
		  </table>
			
</body>
</html>

<script>
	function submitURL(idsite){
		document.location="procesow3c.do?lanzar=yes&site="+idsite;
	}
</script>
<jsp:include page="/moduls/pieControl.jsp"/>



