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
	<script type="text/javascript" src="moduls/funcions.js"></script>
<!-- tinyMCE -->
	<script language="javascript" type="text/javascript" src="tinymce/jscripts/tiny_mce/tiny_mce.js"></script>
	<script language="javascript" type="text/javascript">
	
		tinyMCE.init({
			mode : "textareas",
			theme : "advanced",
			plugins : "advlink",
			theme_advanced_buttons1 : "bold,italic,underline,separator,justifyleft,justifycenter,justifyright,justifyfull,bullist,numlist,separator,outdent,indent,separator,link,unlink,forecolor,removeformat,cleanup,code",
			theme_advanced_buttons2 : "",		
			theme_advanced_buttons3 : "",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_path_location : "bottom",
			file_browser_callback : "fileBrowserCallBack",		
			verify_html : true,
			theme_advanced_resizing : false,	
			accessibility_warnings : true,		
			language: "ca"	
		});
	
	   	var Rcajatemp_tiny;
	   	var Rwin_tiny;
		
		function Rmeterurl_tiny(laurl) {
			Rwin_tiny.document.forms[0].elements[Rcajatemp_tiny].value = laurl;
		}	
	
		// Método que recoge las variables de la ventana que la llama y abre un popup con el listado de recursos url.
		// este método es la implementación personalizada del tiny
		function fileBrowserCallBack(field_name, url, type, win) {
			Rcajatemp_tiny=field_name;
			Rwin_tiny=win;
			
			window.open('/sacmicroback/recursos.do?tiny=true','recursos','scrollbars=yes,width=700,height=400');
	
		}
	
	</script>
	<!-- /tinyMCE -->
</head>

<body>


	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li><a href="faqs.do"><bean:message key="faq.faqs" /></a></li>
	    <logic:present name="faqForm" property="id">
         		<li class="pagActual"><bean:message key="faq.modificacion" /></li>
	    </logic:present>		
	    <logic:notPresent name="faqForm" property="id">
	         <li class="pagActual"><bean:message key="faq.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/faqs.gif" alt="<bean:message key="faq.faqs" />" />
	<bean:message key="faq.faqs" />. 
		<span>
		    <logic:present name="faqForm" property="id">
	         		<bean:message key="faq.modificacion" />
		    </logic:present>		
		    <logic:notPresent name="faqForm" property="id">
		         <bean:message key="faq.alta" />
		    </logic:notPresent>					
		</span>
	</h1>



	<bean:size id="tamano" name="temasCombo"/>
	<logic:equal name="tamano" value="0">
		<p>
			<div class="alerta" style="font-weight:bold; color:#FF1111;">
				<em><strong><bean:message key="faq.tema.nohay" />.</strong> <bean:message key="faq.tema.alerta" />.&nbsp;&nbsp;&nbsp;<button type="button" title="<bean:message key="tema.crear" />" onclick="document.location='temasAcc.do?accion=crear';"><img src="imgs/botons/nou.gif" alt="<bean:message key="tema.crear" />" /></button> </em><br/>
				<br/>
			</div>
		</p>					
	</logic:equal>
	
	<logic:notEqual name="tamano" value="0">
	<!-- botonera -->
	<div id="botonera">
		<button type="button" title="<bean:message key="op.15" />" onclick="submitForm();"><img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" /></button>
	</div>


	<div style="font-weight:bold; color:#FF4400;">
	<html:errors/>
	</div>
	
		<html:form action="/faqEdita.do" enctype="multipart/form-data"  styleId="accFormulario">
		
				     <logic:present name="faqForm" property="id">
					     <input type="hidden" name="modifica" value="Grabar">
				         <html:hidden property="id" />
				     </logic:present>
					 <logic:notPresent name="faqForm" property="id">
					  	<input type="hidden" name="anyade" value="Crear">
					 </logic:notPresent>    
		
	
				
				<div id="formulario">
					<!-- las tablas están entre divs por un bug del FireFox -->
						<table cellpadding="0" cellspacing="0" class="edicio">
				
								<tr class="par">
										<td class="etiqueta"><bean:message key="faq.fecha" /></td>
										<td>
											<html:text property="fecha" readonly="readonly" maxlength="16" />
										</td>
										<td class="etiqueta"><bean:message key="faq.tema" /></td>
										<td>
							                <html:select property="idTema">
							                	<html:option value=""><bean:message key="faq.selectema" /></html:option>
								                <html:options collection="temasCombo" labelProperty="traduccion.nombre" property="id"/>
							    	        </html:select>
										</td>
								</tr>
								<tr>
										<td class="etiqueta"><bean:message key="faq.visible" /></td>
										<td colspan="3"><html:radio property="visible" value="S" />&nbsp;Sí<html:radio property="visible" value="N" />&nbsp;No</td>
								</tr>
						
								<tr>
										<td colspan="4">
								
												<ul id="submenu">
													<logic:iterate id="lang" name="org.ibit.rol.sac.microback.LANGS_KEY" indexId="j">
														<li<%=(j.intValue()==0?" class='selec'":"")%>><a href="#" onclick="mostrarForm(this);"><bean:message name="lang" /></a></li>
											        </logic:iterate>
												</ul>    
								
											    <logic:iterate id="traducciones" name="faqForm" property="traducciones" indexId="i" >
											     	     <bean:define id="idiomaahora" value="Catalan" type="java.lang.String" />
												            <logic:iterate id="lang" name="org.ibit.rol.sac.microback.LANGS_KEY" indexId="j">
												            		<%if(j.intValue()==i.intValue()){%>
															  	<bean:define id="idiomaahora" name="lang" type="java.lang.String" />  
															<%}%>   	
															 
												            </logic:iterate>
												<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
												
													<table cellpadding="0" cellspacing="0" class="edicio">
													<tr>
														<td class="etiqueta"><bean:message key="faq.pregunta" />:</td>
														<td><html:textarea property="pregunta" name="traducciones" rows="5" cols="50" indexed="true" /></td>
													</tr>
													<tr><td colspan="2">&nbsp;</td></tr>
													<tr>
														<td class="etiqueta"><bean:message key="faq.respuesta" />:</td>
														<td><html:textarea property="respuesta" name="traducciones" rows="5" cols="50" indexed="true" /></td>
													</tr>
													<tr>
														<td class="etiqueta"><bean:message key="url.adicional" />:</td>
														<td></td>
													</tr>
													<tr>																
														<td class="etiqueta"><bean:message key="faq.url" />:</td>
														<td><html:text property="url" name="traducciones"  size="60" maxlength="1024" indexed="true" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].url', 'traducciones[<%=i%>].urlnom','<bean:write name="idiomaahora"/>');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
													</tr>
													<tr>														
														<td class="etiqueta"><bean:message key="faq.urlnom" />:</td>
														<td><html:text property="urlnom" name="traducciones" size="114" maxlength="512" indexed="true" /></td>
													</tr>
													</table>
											
												</div>
											    </logic:iterate>
								
										</td>
								</tr>
						</table>
				
		
				</div>
		
		
		</html:form>
	
	
	</logic:notEqual>


</body>
</html>

<script type="text/javascript">
<!--
	
	function submitForm(){
		var accForm = document.getElementById('accFormulario');
		accForm.submit();
	}
	
    var RcajatempUrl;
	var RcajatempDesc;

    function Rpopupurl(objurl, objdesc, idioma ) 
    {

      RcajatempUrl =document.faqForm[objurl];
      RcajatempDesc =document.faqForm[objdesc];
      window.open('recursos.do?lang='+idioma,'recursos','scrollbars=yes,width=700,height=400');

    }

      function Rmeterurl(laurl, descr) 
    {
            RcajatempUrl.value=laurl;
			RcajatempDesc.value=descr;

    }
	
// -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>