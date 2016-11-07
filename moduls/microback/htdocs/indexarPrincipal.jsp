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
	<script type="text/javascript" src="js/jsListados.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

<body>	
		<!-- molla pa -->
		<ul id="mollapa">
			<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
			<li><a href="index.do"><bean:message key="op.7" /></a></li>
			<li><bean:message key="menu.ferramentes" /></li>
			<li class="pagActual"><bean:message key="menu.indexar.principal" /></li>
		</ul>
		<!-- titol pagina -->
		<h1><img src="imgs/titulos/indexar.gif" alt="<bean:message key="menu.indexar.principal" />" />
		<bean:message key="menu.indexar.principal" /> </h1>

		<!-- continguts -->
		
		<div id="botonera">
				<button type="button" name="fichero" title="<bean:message key="menu.indexar.todo" />" onclick='indexarTodo()'><img src="imgs/botons/indexar.gif" alt="<bean:message key="menu.indexar.todo" />" /> &nbsp;<bean:message key="menu.indexar.todo" /></button>
		</div>
		
		<div id="botonera">
				<button type="button" name="fichero" title="<bean:message key="menu.indexar.pendientes" />" onclick='indexarPendientes()'><img src="imgs/botons/indexar.gif" alt="<bean:message key="menu.indexar.pendientes" />" /> &nbsp;<bean:message key="menu.indexar.pendientes" /></button>
				<logic:empty name="listado">
					<button type="button" name="fichero" title="<bean:message key="menu.indexar.verpendientes" />" onclick='indexarVerPendientes()'><img src="imgs/botons/indexar.gif" alt="<bean:message key="menu.indexar.verpendientes" />" /> &nbsp;<bean:message key="menu.indexar.verpendientes" /></button>
				</logic:empty>
				<logic:notEmpty name="listado">
					<p></p><p></p>
					<table cellpadding="0" cellspacing="0" class="llistat" style="width:78%;">
					 <thead>
						<tr>
							<th width="15%"><bean:message key="menu.indexar.cab.id" /></th>
							<th width="15%"><bean:message key="menu.indexar.cab.tipo" /></th>
							<th width="15%"><bean:message key="menu.indexar.cab.idElem" /></th>
							<th width="15%"><bean:message key="menu.indexar.cab.idArchivo" /></th>
							<th width="15%"><bean:message key="menu.indexar.cab.accion" /></th>
							<th width="15%"><bean:message key="menu.indexar.cab.mensajeError" /></th>
							<th width="15%"><bean:message key="menu.indexar.cab.fechaCreacion" /></th>							
						</tr>
					</thead>
					<tbody>
						<logic:iterate id="i" name="listado" indexId="indice">
						       <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
						       		 <td><bean:write name="i" property="id" 			ignore="true"/></td>
								     <!-- <td><bean:write name="i" property="tipo" 			ignore="true"/></td> -->
								     <td>
								     	<logic:equal name="i" property="tipo" value="CON">
								     		<bean:message key="menu.indexar.cab.tipo.CON" />
								     	</logic:equal>
								     	<logic:equal name="i" property="tipo" value="ENC">
								     		<bean:message key="menu.indexar.cab.tipo.ENC" />
								     	</logic:equal>
								     	<logic:equal name="i" property="tipo" value="MIC">
								     		<bean:message key="menu.indexar.cab.tipo.MIC" />
								     	</logic:equal>
								     	<logic:equal name="i" property="tipo" value="FAQ">
								     		<bean:message key="menu.indexar.cab.tipo.FAQ" />
								     	</logic:equal>
								     	<logic:equal name="i" property="tipo" value="NTC">
								     		<bean:message key="menu.indexar.cab.tipo.NTC" />
								     	</logic:equal>
								     	<logic:equal name="i" property="tipo" value="AGE">
								     		<bean:message key="menu.indexar.cab.tipo.AGE" />
								     	</logic:equal>
								     	<logic:equal name="i" property="tipo" value="ARC">
								     		<bean:message key="menu.indexar.cab.tipo.ARC" />
								     	</logic:equal>
								     </td>
								     <td><bean:write name="i" property="idElem" 		ignore="true"/></td>
								     <td><bean:write name="i" property="idArchivo" 		ignore="true"/></td>
								     <logic:equal name="i" property="accion" value="0">
								     	<td><bean:message key="menu.indexar.cab.desindexar" /></td>
								     </logic:equal>
								     <logic:equal name="i" property="accion" value="1">
								     	<td><bean:message key="menu.indexar.cab.indexar" /></td>
								     </logic:equal>
								     <td><bean:write name="i" property="mensajeError" 	ignore="true"/></td>
								     <td><bean:write name="i" property="fechaCreacion" formatKey="date.short.format"/></td>
						       </tr>					
					    </logic:iterate>
				    </tbody>
				    </table>
			    </logic:notEmpty>
		</div>		
		
		<div id="botonera">
				<button type="button" name="fichero" title="<bean:message key="menu.indexar.unidad.administrativa" />" onclick='indexarByUA()'><img src="imgs/botons/indexar.gif" alt="<bean:message key="menu.indexar.unidad.administrativa" />" /> &nbsp;<bean:message key="menu.indexar.unidad.administrativa" /></button>  								
                <select id="unidadAdministrativa" >				       	
			       	<logic:iterate id="i" name="listaUA">
			       		<option value='<bean:write name="i" property="idUnidad"/>'><bean:write name="i" property="nombre"/></option>
					</logic:iterate>
			    </select>	
								
				
		</div>	
		
		<p>
		<strong>
			<bean:message key="indexador.p1" /><br/> 
			<bean:message key="indexador.p2" /><br/>
			<bean:message key="indexador.p3" /><br/>
		</strong>
		</p>	
		<logic:notEmpty name="ok">			
		<div class="alerta" style="font-weight:bold; color:#FF1111;">
			<html:messages id="message" message="true">
			<%= message %><br/>
			</html:messages>	
			
		</div>
		</logic:notEmpty>
		
		<logic:notEmpty name="nok">			
		<div class="alerta" style="font-weight:bold; color:#FF1111;">
			<html:messages id="message" message="true">
				<bean:message key="menu.indexar.error.ejecutandose" />
			</html:messages>	
			
		</div>
		</logic:notEmpty>
</body>
</html>

<script>
	function indexarTodo(){
		document.location="indexarPrincipal.do?indexar=todo";
		
	}

	function indexarByUA(){
		
		// Plain old JavaScript
		var sel = document.getElementById('unidadAdministrativa');
		var selected = sel.options[sel.selectedIndex];
		var idUA = selected.value;
		if (idUA == null){
			return;
		}
		
		document.location.href="indexarPrincipal.do?indexar=byUA&uaId="+idUA;
		
	
		
	}
    function indexarPendientes(){
		
		document.location.href="indexarPrincipal.do?indexar=pendientes";
		
	}
    
    function indexarVerPendientes(){
		
		document.location.href="indexarPrincipal.do?indexar=verpendientes";
		
	}
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
