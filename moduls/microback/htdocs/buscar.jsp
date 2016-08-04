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
</head>

<style>
.resultatsRecercaInteligent { list-style:circle; margin:2em 0 1em 2em; padding:0; }
.resultatsRecercaInteligent li { margin-bottom:1em; }
.resultatsRecercaInteligent li span { color:#999; font-size:.9em; margin-left:.5em; }
.resultatsRecercaInteligent li span.detall { display:block; color:#494949; margin:.3em 0 0 1em; }

</style>


<body>	
	<!-- molla pa -->
	<ul id="mollapa">
			<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
			<li><bean:message key="menu.ferramentes" /></li>
			<li><a href="indexador.do"><bean:message key="menu.indexar" /></a></li>	
			<li class="pagActual"><bean:message key="buscar.resultados" />	</li>			
	</ul>
	
	<h1><img src="imgs/titulos/indexar.gif" alt="<bean:message key="menu.indexar" />" /> <bean:message key="buscar.resultados" /></h1>
	
	

	 

	<logic:present name="listado" property="lista">
		
		<p>
		<strong><bean:message key="buscar.encontrados" /> <bean:write name="listado" property="numEncontrados"/> <bean:message key="buscar.resultados.en" /> <bean:write name="listado" property="duracionBusqueda"/> 
		<bean:message key="buscar.resultados.para" /> "<bean:write name="listado" property="consultaOriginal"/>"</strong>
		 </p>
		
		<logic:notEqual name="listado" property="saltos" value="">
			<p><i><bean:message key="buscar.primer.intento" /></i></p>
		</logic:notEqual>
		
		<logic:notEqual name="listado" property="consultaSugerida" value="">
			<span style="font-size:2; color:#ff0000"><bean:message key="buscar.quisodecir" />:</span>&nbsp;<a href="javascript:document.buscador.submit();"><bean:write name="listado" property="consultaSugerida"/><a>
			<form name="buscador" action="busca.do">
				<input type=hidden name="words" value="<bean:write name="listado" property="consultaSugerida"/>" >
				<input type=hidden name="micro" value="<bean:write name="micro"/>" >
				<input type=hidden name="idi" value="<bean:write name="idi"/>" >
			</form>
		</logic:notEqual>	
		
		<bean:define id="docus" name="listado" property="lista"/>

   		<logic:iterate id="i" name="docus">
   		
			<ul class="resultatsRecercaInteligent">
				<li>
					<a href="<bean:write name="i" property="url"/>"><bean:write name="i" property="titulo"/></a> 
					<span>(<bean:write name="i" property="score"/>%)</span> 
					<br/>
					<span><bean:write name="i" property="id" filter="yes"/> Site: <bean:write name="i" property="site"/></span> 
					<span class="detall"><bean:write name="i" property="descripcion" filter="yes"/></span>
				</li>
			</ul>   		

   		</logic:iterate>

		<br/><br/>
		<strong><a href="indexador.do"><bean:message key="buscar.volver" /></a></strong>
   	</logic:present>
    	
    <logic:notPresent name="listado" property="lista">

		<logic:notEqual name="listado" property="consultaSugerida" value="">
			<span style="font-size:2; color:#ff0000"><bean:message key="buscar.quisodecir" />:</span>&nbsp;<a href="javascript:document.buscador.submit();"><bean:write name="listado" property="consultaSugerida"/><a>

			<form name="buscador" action="busca.do">
				<input type=hidden name="words" value="<bean:write name="listado" property="consultaSugerida"/>" >
				<input type=hidden name="micro" value="<bean:write name="micro"/>" >
				<input type=hidden name="idi" value="<bean:write name="idi"/>" >
			</form>
		</logic:notEqual>

		<logic:equal name="listado" property="consultaSugerida" value="">
			<bean:message key="buscar.subusqueda" /> - <strong><bean:write name="listado" property="consultaOriginal"/></strong> - <bean:message key="buscar.no.resultados" />
		</logic:equal>
    	    	    	
    </logic:notPresent>

	<logic:present name="diccionario">
		<bean:size id="tamano" name="diccionario"/>
		<br/><br/><b>DICCIONARIO</b>&nbsp;(Contiene <bean:write name="tamano"/> palabras)<br/>
	
    	<logic:iterate id="i" name="diccionario">
	    	<bean:write name="i" filter="yes"/>
    	</logic:iterate>
    	
    </logic:present>



</body>
</html>