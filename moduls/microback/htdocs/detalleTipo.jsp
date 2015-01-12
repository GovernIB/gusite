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
		<li><a href="tipos.do"><bean:message key="menu.listados" /></a></li>
	    <logic:present name="tiponotForm" property="id">
         		<li class="pagActual"><bean:write name="tiponotForm" property="traducciones[0].nombre" ignore="true" /></li>
	    </logic:present>		
	    <logic:notPresent name="tiponotForm" property="id">
	         <li class="pagActual"><bean:message key="tipo.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.listados" />" />
	<bean:message key="menu.listados" />. <span><bean:message key="tipo.propietats" /></span></h1>
	

<%session.setAttribute("action_path_key",null);%>
<html:form action="/tipoEdita.do" method="POST" enctype="multipart/form-data" styleId="accFormulario">

		
		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>

		<input type="hidden" name="accion" value=""/>
		
		<!-- botonera -->
		<div id="botonera">
			<span class="grup">
		   	<button type="button" title='<bean:message key="tipo.volverconfiguracion"/>' onclick='document.location.href="tipos.do";'>
		   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="tipo.volverconfiguracion"/>' />
		    </button> 
			</span>
			<logic:present name="tiponotForm" property="id">
			<logic:equal name="tiponotForm" property="tipoelemento"  value="3">
				<span class="grup">
					<button type="button" title="<bean:message  key="op.12" />" onclick="previsualizar();"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message  key="op.12" />" /></button>
				</span>	
				</logic:equal>
			<logic:equal name="MVS_microsite" property="funcionalidadTraduccion" value="true">
			   <span class="grup">
			   <button type="submit" title='<bean:message key="operacion.traducir"/>' onclick="submitForm('Traduir');">
			   		<img src="imgs/botons/clonar.gif" alt='<bean:message key="operacion.traducir"/>' /> &nbsp;<bean:message key="operacion.traducir" />
			   </button>
			   </span> 
			</logic:equal>
			</logic:present>
		   <button type="submit" title='<bean:message key="operacion.guardar"/>' onclick="submitForm('Guardar');">
		   		<img src="imgs/botons/guardar.gif" alt='<bean:message key="operacion.guardar"/>' /> &nbsp;<bean:message key="operacion.guardar" />
		   	</button>
		</div>
		
		<input type="hidden" name="espera" value="si" id="espera" />

		     <logic:present name="tiponotForm" property="id">
			     <input type="hidden" name="modifica" value="Grabar" />
		         <html:hidden property="id" />
		     </logic:present>
			 <logic:notPresent name="tiponotForm" property="id">
			  	<input type="hidden" name="anyade" value="Crear" />
			 </logic:notPresent> 
	
	
	<div id="formulario">
		<!-- las tablas están entre divs por un bug del FireFox -->
			<table cellpadding="0" cellspacing="0" class="edicio">
			<tr>
				<td class="etiqueta"><bean:message key="tipo.tpelem" /></td>
				<td>
					<html:select property="tipoelemento" onchange="javascript:controlaTipo();">
		   			    <html:option value="0"><bean:message key="tipo.cod0" /></html:option>
		    	        <html:option value="1"><bean:message key="tipo.cod1" /></html:option>
	   	    	        <html:option value="2"><bean:message key="tipo.cod2" /></html:option>
	   	    	        <html:option value="3"><bean:message key="tipo.cod3" /></html:option>
	   	    	        <html:option value="4"><bean:message key="tipo.cod4" /></html:option>
			        </html:select>
				</td>
				<td class="etiqueta">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			</table>

			<div id="fotosporfila">
			<table cellpadding="0" cellspacing="0" class="edicio">
			<tr>
				<td class="etiqueta">Numero de fotos per fila</td>
				<td>
					<html:select property="fotosporfila">
	    			    <html:option value="1">1</html:option>
		    	        <html:option value="2">2</html:option>
						<html:option value="3">3</html:option>
						<html:option value="4">4</html:option>
						<html:option value="5">5</html:option>
						<html:option value="6">6</html:option>
			        </html:select>
				</td>
			</tr>
			</table>
			</div>
			
			
			<div id="diferenteExterno">
			<table cellpadding="0" cellspacing="0" class="edicio">
			<tr class="par">
				<td class="etiqueta"><bean:message key="tipo.tippag" /></td>
				<td>
					<html:select property="tipopagina">
	    			    <html:option value="0"><bean:message key="tipo.pag.cod0" /></html:option>
		    	        <html:option value="1"><bean:message key="tipo.pag.cod1" /></html:option>
			        </html:select>
				</td>
				<td class="etiqueta">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			
			<tr>
				<td class="etiqueta"><bean:message key="tipo.tampag" /></td>
				<td>
					<html:text property="tampagina" maxlength="8" size="10"/>
				</td>
				<td class="etiqueta">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
	
			<tr class="par">
				<td class="etiqueta"><bean:message key="tipo.buscador" /></td>
				<td>
					<label><html:radio property="buscador" value="S" />&nbsp;Sí­&nbsp;&nbsp;&nbsp;</label><label><html:radio property="buscador" value="N" />&nbsp;No</label>
				</td>
				<td class="etiqueta">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			
			<tr>
				<td class="etiqueta"><bean:message key="tipo.orden" /></td>
				<td>
					<html:select property="orden">
	   	    	        <html:option value="0"><bean:message key="tipo.orden0" /></html:option>
	   	    	        <html:option value="1"><bean:message key="tipo.orden1" /></html:option>
	   	    	        <html:option value="2"><bean:message key="tipo.orden2" /></html:option>
	   	    	        <html:option value="3"><bean:message key="tipo.orden3" /></html:option>
			        </html:select>
				</td>
				<td class="etiqueta">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>

			<tr class="par">
				<td class="etiqueta"><bean:message key="tipo.clasificacion" /></td>
				<td colspan="3">
					<span>
					<logic:empty name="listaClasificacion">										
						<html:text styleId="txt_clasif" property="clasificacion" maxlength="100" size="20"/>
					</logic:empty>
					<logic:notEmpty name="listaClasificacion">										
						<bean:define id="valorclasificacion">null</bean:define>						
						<logic:notEmpty name="tiponotForm" property="clasificacion">						
							<bean:define id="valorclasificacion"><bean:write name="tiponotForm" property="clasificacion"/></bean:define>						
						</logic:notEmpty>
				        <span id="listadoClasificacion_0">						
							<html:select property="clasificacion_txt" value="<%=valorclasificacion%>" onchange="if (this.value.length>0) document.getElementById('txt_clasif').value=this.options[this.selectedIndex].value;">
				                <html:option value="" key="" />			                
			           			<html:options collection="listaClasificacion" property="clasificacion" labelProperty="clasificacion"/>
				            </html:select>
				        	<button name="editarClasificacion" type="button" title='Editar classificació' onclick="editClasificacion_display('0');"><img src="imgs/menu/editar.gif" alt='Editar' /></button>				        				            
				        </span>			            
				        <span id="editClasificacion_0">
					        <html:text styleId="txt_clasif" property="clasificacion" maxlength="100" size="20"/>
							<button name="listarClasificacion" type="button" title='Llistat de classificació' onclick="editClasificacion_display('0');"><img src="imgs/menu/pagListado.gif" alt='Listar' /></button>				        				            					        
				        </span>
				  		<script type="text/javascript">editClasificacion_display("0");</script>					
			        </logic:notEmpty>
		            </span>
		            <p><bean:message key="tipo.clasif.mensa" /></p>
				</td>
			</tr>


			</table>
			</div>
			<div id="Externo">
			<table cellpadding="0" cellspacing="0" class="edicio">
			<tr class="par">
				<td class="etiqueta"><bean:message key="tipo.url" /></td>
				<td>
					<html:hidden property="xjndi" value="x" />
					<html:hidden property="xusr"  value="x" />
					<html:hidden property="xpwd"  value="x" />
					<html:hidden property="xid" value="x"  />
					<html:text property="xurl" maxlength="1000" size="50"/>
				</td>
				<td class="etiqueta">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="etiqueta"><bean:message key="tipo.clasificacion" /></td>
				<td colspan="3">
					<span>
					<logic:empty name="listaClasificacion">										
						<html:text styleId="txt_clasif1" property="clasificacion_ext" maxlength="100" size="20"/>
					</logic:empty>
					<logic:notEmpty name="listaClasificacion">										
						<bean:define id="valorclasificacion">null</bean:define>						
						<logic:notEmpty name="tiponotForm" property="clasificacion_ext">						
							<bean:define id="valorclasificacion"><bean:write name="tiponotForm" property="clasificacion_ext"/></bean:define>						
						</logic:notEmpty>
				        <span id="listadoClasificacion_1">						
							<html:select property="clasificacion_txt_ext" value="<%=valorclasificacion%>" onchange="if (this.value.length>0) document.getElementById('txt_clasif1').value=this.options[this.selectedIndex].value;">
				                <html:option value="" key="" />			                
			           			<html:options collection="listaClasificacion" property="clasificacion" labelProperty="clasificacion"/>
				            </html:select>
				        	<button name="editarClasificacion" type="button" title='Editar classificació' onclick="editClasificacion_display('1');"><img src="imgs/menu/editar.gif" alt='Editar' /></button>				        				            
				        </span>			            
				        <span id="editClasificacion_1">
					        <html:text styleId="txt_clasif1" property="clasificacion_ext" maxlength="100" size="20"/>
							<button name="listarClasificacion" type="button" title='Llistat de classificació' onclick="editClasificacion_display('1');"><img src="imgs/menu/pagListado.gif" alt='Listar' /></button>				        				            					        
				        </span>
				  		<script type="text/javascript">editClasificacion_display("1");</script>					
			        </logic:notEmpty>
		            </span>
		            <p><bean:message key="tipo.clasif.mensa" /></p>
				</td>
			</tr>			
			</table>
			
			
			</div>
			<table cellpadding="0" cellspacing="0" class="edicio">

			<tr>
			<td colspan="4">
			
	
		<ul id="submenu">
			<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
				<li<%=(j.intValue()==0?" class='selec'":"")%>><a href="#" onclick="mostrarForm(this);"><bean:message name="lang" /></a></li>
	        </logic:iterate>
		</ul>    
	
	    <logic:iterate id="traducciones" name="tiponotForm" property="traducciones" indexId="i" >
		<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
			<table cellpadding="0" cellspacing="0" class="tablaEdicio">
			<tr>
				<td class="etiqueta"><bean:message key="tema.nombre" />:</td>
				<td><html:text property="nombre" name="traducciones" size="40" maxlength="100" indexed="true" /></td>
			</tr>
			</table>
		</div>
	    </logic:iterate>
	    
	    </td>
	    </tr>
	    </table>
	    
	    </div>

</html:form>

</body>
</html>

<script type="text/javascript">
<!--
	
	function submitForm(){
		var accForm = document.getElementById('accFormulario');
		accForm.submit();
	}

	function submitForm(nom_accio){
		var accForm = document.getElementById('accFormulario');
		accForm.accion.value= nom_accio;
		 if (nom_accio== "Traduir") {
			 accForm.accion.value="<bean:message key='operacion.traducir'/>";
		 }else if (nom_accio== "Guardar"){
			 accForm.accion.value="<bean:message key='operacion.guardar'/>";
		} else if (nom_accio== "Borrar"){
			if(confirm("<bean:message key='conte.alert6' />"))
			 accForm.accion.value="<bean:message key='operacion.borrar' />";
			 else return false;
		}
		accForm.submit();
	}
	
	function previsualizar() {
		abrirWindow('<bean:message key="url.aplicacion" />noticias.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&tipo=<bean:write name="tiponotForm" property="id" />&stat=no');
	}
	
		//muestra/esconde el editor
	function controlaTipo() {
		var obj;var valor;var obj2;var obj3;
		obj = document.getElementsByName("tipoelemento")[0];
		if(obj.options[obj.selectedIndex].value == '3')
		{
	 		obj = document.getElementById("Externo");
	 		obj.style.display="block";
	 		obj = document.getElementById("diferenteExterno");
	 		obj.style.display="none";
	 	}
		else if(obj.options[obj.selectedIndex].value == '4') { //4 = galeria de fotos 
			obj = document.getElementById("fotosporfila");
	 		obj.style.display="block";
	 		obj = document.getElementById("Externo");
			obj.style.display="none";
			obj = document.getElementById("diferenteExterno");
			obj.style.display="block";
		}
	 	else
	 	{
	 		obj = document.getElementById("Externo");
	 		obj.style.display="none";
	 		obj = document.getElementById("diferenteExterno");
	 		obj.style.display="block";
			obj = document.getElementById("fotosporfila");
	 		obj.style.display="none";
	 	}
		
	}
	
	
	controlaTipo();

	
// -->
</script>

<jsp:include page="/moduls/pieControl.jsp"/>
