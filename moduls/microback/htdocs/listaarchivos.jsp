<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title><bean:message key="menu.archivos" /></title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/rArxius.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

<body>

	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.contenidos" /></li>
		<li class="pagActual"><bean:message key="menu.archivos" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/archivos.gif" alt="<bean:message key="menu.archivos" />" />
	<bean:message key="menu.archivos" /></h1>

	<html:form action="/docsEdita.do" enctype="multipart/form-data" >

	<!-- botonera -->
	<div id="botonera">
		<span class="grup">
			<button type="button" title='<bean:message key="conte.nuevoarchi"/>' onclick="mostarGuardarArxiu('divGuardarArxiu','nou');"><img src="imgs/botons/nou.gif" alt='<bean:message key="conte.nuevoarchi"/>' /></button> 
			<button type="button" title='<bean:message key="boton.modif"/>' onclick="mostarGuardarArxiu('divGuardarArxiu','modificar');"><img src="imgs/botons/modificar.gif" alt='<bean:message key="boton.modif"/>' /></button>
		</span>
		<button type="button" title='<bean:message key="boton.ver"/>' onclick="verArchivo();"><img src="imgs/botons/ver.gif" alt='<bean:message key="boton.ver"/>' /></button> 
		<button name="esborrar" type="button" title='<bean:message key="op.2"/>' onclick=" borrar();" ><img src="imgs/botons/borrar.gif" alt='<bean:message key="op.2"/>' /></button>
		
		<div id="divGuardarArxiu">
			<h2><bean:message key="conte.nuevoarchi"/></h2>
			<p><bean:message key="conte.nuevoarchimensa"/></p>
			<html:hidden property="id" value="0"/>
			<html:file property="archi" size="50"/>
			<div class="botonera">
				<button name="subirficheros" type="button" title='<bean:message key="op.15"/>' onclick="guardar()"><img src="imgs/botons/guardar.gif" alt='<bean:message key="op.15"/>' /> &nbsp;<bean:message key="op.15"/></button> 
				&nbsp; &nbsp; <button type="button" title='<bean:message key="boton.cerrar"/>' onclick="formEsconder();"><img src="imgs/botons/cerrar.gif" alt='<bean:message key="boton.cerrar"/>' /></button>
			</div>
			<html:hidden property="operacion" value=""/>
		</div>

	</div>

	<logic:notEmpty name="listaDocs">
	<table cellpadding="0" cellspacing="0" class="llistat">
	<thead>
		<tr>
			<th class="check"></th>
			<th><bean:message key="conte.tipoarchi" /></th>
			<th><bean:message key="conte.nombrearchi" /></th>
			<th><bean:message key="conte.pesoarchi" /></th>
			<th><bean:message key="conte.url" /></th>
		</tr>
	</thead>	
	<tbody id="listadoArchivos">
	
	<logic:iterate id="i" name="listaDocs" indexId="indice">
	    <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
	    <td class="check"><input name="seleccionados" id="c<bean:write name="i" property="id"/>" type="checkbox" value="<bean:write name="i" property="id"/>" /></td>
		<bean:define id="icono" value="stream.gif"/>
		
		<logic:match name="i" property="mime" value="EXCEL"><bean:define id="icono" value="xls.gif"/></logic:match>
		<logic:match name="i" property="mime" value="POWERPOINT"><bean:define id="icono" value="powerpoint.gif"/></logic:match>							    
		<logic:match name="i" property="mime" value="ZIP"><bean:define id="icono" value="zip.gif"/></logic:match>							    							    
		<logic:match name="i" property="mime" value="WORD"><bean:define id="icono" value="word.gif"/></logic:match>							    							    
	    <logic:match name="i" property="mime" value="RTF"><bean:define id="icono" value="word.gif"/></logic:match>							    							      							    
	    <logic:match name="i" property="mime" value="PDF"><bean:define id="icono" value="pdf.gif"/></logic:match>							    							    
	    <logic:match name="i" property="mime" value="PLAIN"><bean:define id="icono" value="txt.gif"/></logic:match>							    							    
	    <logic:match name="i" property="mime" value="HTML"><bean:define id="icono" value="html.gif"/></logic:match>
	    <logic:match name="i" property="mime" value="CSS"><bean:define id="icono" value="css.gif"/></logic:match>
	    <logic:match name="i" property="mime" value="AUDIO"><bean:define id="icono" value="media.gif"/></logic:match>
		<logic:match name="i" property="mime" value="MEDIA"><bean:define id="icono" value="media.gif"/></logic:match>
	    <logic:match name="i" property="mime" value="VIDEO"><bean:define id="icono" value="media.gif"/></logic:match>
	    <logic:match name="i" property="mime" value="IMAGE"><bean:define id="icono" value="image.gif"/></logic:match>							    
		<logic:match name="i" property="mime" value="FLASH"><bean:define id="icono" value="flash.gif"/></logic:match>		
		<logic:match name="i" property="mime" value="OPENDOCUMENT"><bean:define id="icono" value="odt.gif"/></logic:match>		
		<logic:match name="i" property="mime" value="SUN.XML.WRITER"><bean:define id="icono" value="odt.gif"/></logic:match>		
		<logic:match name="i" property="mime" value="STARDIVISION"><bean:define id="icono" value="odt.gif"/></logic:match>				

		<td><img src='imgs/arxius/<bean:write name="icono"/>' alt='<bean:write name="i" property="mime"/>' /></td>
		<td><bean:write name="i" property="nombre"/></td>
		<td><bean:write name="i" property="peso"/>&nbsp;Kb</td>
		<td>archivopub.do?ctrl=MCRST<bean:write name="MVS_microsite" property="id"/>ZI<bean:write name="i" property="id"/>&id=<bean:write name="i" property="id"/></td>
		</tr>
	</logic:iterate>
	</tbody>
    </table>
	</logic:notEmpty>
	
	</html:form>
	
</body>
</html>

<script>

<!--

var alert1='<bean:message key="conte.alertamasdeuno"/>';
var alert2='<bean:message key="conte.alertaelegirarchi"/>';
var alert3='<bean:message key="conte.nuevoarchi"/>';
var alert4='<bean:message key="conte.alertaborrararchivos"/>';
var mensa1='<bean:message key="conte.modifarchi"/>';
var mensa2='<bean:message key="conte.alertamodifarchi"/>';
var mensa3='<bean:message key="conte.nuevoarchimensa"/>';

addEvent(window,'load',iniciarMenu,true);
addEvent(document,'keydown',esborrarArxiuTecla,true);

function guardar() {

if (document.docsForm.id.value=='0')  document.docsForm.operacion.value='crear';
else  document.docsForm.operacion.value='modificar';
document.docsForm.submit();

}

function borrar() {

if (!esborrarArxiu()) return;
document.docsForm.operacion.value='borrar';
document.docsForm.submit();

}

-->

</script>
<jsp:include page="/moduls/pieControl.jsp"/>
