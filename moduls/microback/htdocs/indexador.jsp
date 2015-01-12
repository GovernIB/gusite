<%@ page language="java"%>
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
	<script type="text/javascript" src="js/jsListados.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

<body>	
		<!-- molla pa -->
		<ul id="mollapa">
			<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
			<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
			<li><bean:message key="menu.ferramentes" /></li>
			<li class="pagActual"><bean:message key="menu.indexar" /></li>
		</ul>
		<!-- titol pagina -->
		<h1><img src="imgs/titulos/indexar.gif" alt="<bean:message key="menu.indexar" />" />
		<bean:message key="menu.indexar" /> </h1>

		<!-- continguts -->
		
		<div id="botonera">
				<button type="button" name="fichero" title="<bean:message key="menu.indexar" />" onclick="submitURL(<bean:write name="MVS_microsite" property="id"/>);"><img src="imgs/botons/indexar.gif" alt="<bean:message key="menu.indexar" />" /> &nbsp;<bean:message key="menu.indexar" /></button>
		</div>		
		
		<p>
		<strong>
			<bean:message key="indexador.p1" /><br/> 
			<bean:message key="indexador.p2" /><br/>
			<bean:message key="indexador.p3" /><br/>
		</strong>
		</p>		
		
		
		<div style="background:#FFFAF5; margin:40px 50px 40px 50px;">
					<!-- subtitol: cercar -->
					<h1>
						<span>
						    <bean:message key="indexador.cercar" />				
						</span>
					</h1>
					
					<p><bean:message key="indexador.probar.cercador" /></p>
					
					<form action="busca.do" id="accFormSearch">
					<!-- botonera cercar-->
					<div id="botonera">
						
							<input type="hidden" name="micro" value="<bean:write name="MVS_microsite" property="id"/>" >
							&nbsp;&nbsp;<bean:message key="indexador.paraules" />	: <input type=text name="words" >
							&nbsp;&nbsp;<bean:message key="indexador.idioma" />	: 
							<select name="idi" size="1">
								<logic:iterate id="lang" name="org.ibit.rol.sac.microback.LANGS_KEY" indexId="j">
									<option value="<bean:write name="lang"/>"><bean:message name="lang" /></option>
								</logic:iterate>
							</select>
							&nbsp;&nbsp;<button type="button" title="<bean:message key="op.3" />" onclick="submitFormBuscar();"><img src="imgs/botons/cercar.gif" alt="<bean:message key="op.3" />" /></button> 
						
					</div>	
					</form>
		</div>
		

</body>
</html>

<script>
	function submitURL(idsite){
		document.location="indexa.do?espera=si&site="+idsite;
	}
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
