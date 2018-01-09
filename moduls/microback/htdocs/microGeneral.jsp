<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="es.caib.gusite.micromodel.Microsite" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="es.caib.gusite.micromodel.Accesibilidad"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<script type="text/javascript"	src="js/jquery/jquery-1.3.2.min.js"></script>
	<script type="text/javascript"	src="js/jquery/jquery-ui-1.7.3.custom.min.js"></script>
	<script type="text/javascript"	src="js/jquery/ui.draggable.min.js"></script>
	<script type="text/javascript" src="js/editarMicro.js"></script>
	<script type="text/javascript" src="js/checkUri.js"></script>

	<!-- Core files -->
	<script src="js/jquery/jquery.alerts.js" type="text/javascript"></script>
	<link href="css/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
	<style type="text/css">
		.rolRowM {
			display:table-cell;
		}
		.rolRowP, .rolRowR, .rolRow {
			display:none;
		}
	</style>
</head>
<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.configuracion" /></li>
		<li class="pagActual"><bean:message key="menu.general" /></li>
	</ul>

	<!-- titol pagina -->
	<h1><img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.general" />" />
	<bean:message key="menu.general" />. <span><bean:message key="micro.descripcion.general" /></span></h1>

	<!-- continguts pestanyes / formulario -->
	<%session.setAttribute("action_path_key",null);%>
	<html:form action="/microEdita.do" method="POST" enctype="multipart/form-data" styleId="accFormulario">

		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>
		<input type="hidden" name="accion" value=""/>

		<!-- botonera -->
		<div id="botonera">
			<logic:present name="MVS_microsite" property="id">
				<span class="grup">
					<button type="button" title='<bean:message key="micro.boto.tornargestio.titol"/>' onclick='window.parent.location.href="index.do?idsite=<bean:write name="MVS_microsite" property="id"/>";'>
						<img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.titol"/>' />
					</button>
				</span>
				<span class="grup">
					<button type="button" title="<bean:message key="op.12" />" onclick="previsualizar();"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
				</span>
				<logic:equal name="MVS_microsite" property="funcionalidadTraduccion" value="true">
					<span class="grup">
						<button type="submit" title='<bean:message key="operacion.traducir"/>' onclick="submitForm('Traduir');">
							<img src="imgs/botons/clonar.gif" alt='<bean:message key="operacion.traducir"/>' /> &nbsp;<bean:message key="operacion.traducir" />
						</button>
					</span>
				</logic:equal>
			</logic:present>
			<button type="submit"  title='<bean:message key="operacion.guardar"/>' onclick="submitForm('Guardar');">
				<img src="imgs/botons/guardar.gif" alt='<bean:message key="operacion.guardar"/>' /> &nbsp;<bean:message key="operacion.guardar" />
			</button>
		</div>

		<input type="hidden" name="espera" value="si" id="espera" />
		<bean:define id="puedoeditar" value="0" />
		<bean:define id="superpuedoeditar" value="0" />

		<logic:present name="MVS_rol_sys_adm">
			<logic:equal  name="MVS_rol_sys_adm" value="yes">
				<bean:define id="puedoeditar" value="1" />
			</logic:equal>
		</logic:present>
		<logic:present name="MVS_rol_super">
			<logic:equal  name="MVS_rol_super" value="yes">
				<bean:define id="superpuedoeditar" value="1" />
			</logic:equal>
		</logic:present>

		<!-- submenu pestanyes -->
		<ul id="submenu">
			<li class="selec"><a href="#" onclick="mostrarForm(this);"><bean:message key="micro.pestgeneral" /></a></li>
			<li><a href="#" onclick="mostrarForm(this);"><bean:message key="micro.campanya" /></a></li>
			<li><a href="#" onclick="mostrarForm(this);"><bean:message key="micro.pestservicios" /></a></li>
            <li><a href="#" onclick="mostrarForm(this);"><bean:message key="micro.pestaparen" /></a></li>
		</ul>

		<div id="formulario">
			<html:hidden property="numeronoticias" value="5" />
			<html:hidden property="imagenPrincipalid" />
			<!-- <input type="hidden" name="graba" value="Grabar"/>	-->

			<!-- las tablas están entre divs por un bug del FireFox -->
			<div id="capa_tablaGeneral" class="capaFormIdioma">
				<!-- test -->
				<table cellpadding="0" cellspacing="0" class="edicio">
					<tr><td>
						<ul id="submenu">
							<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
								<li<%=(j.intValue()==0?" class='selec2'":"")%>>
									<a href="#" onclick="mostrarForm2(this);">
										<bean:message name="lang" />
										
									</a>
								</li>
							</logic:iterate>
						</ul>

						<div id="formulario2">
							<logic:iterate id="traducciones" name="microForm" property="traducciones" indexId="i" >
								<bean:define id="idiomaahora" value="Catalan" type="java.lang.String" />

								<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
									<%if(j.intValue()==i.intValue()){%>
										<bean:define id="idiomaahora" name="lang" type="java.lang.String" />
									<%}%>
								</logic:iterate>

								<div id="capa_tabla<%=i%>" class="capaFormIdioma2" style="<%=(i.intValue()==0?"display:block;":"display:none;")%>">
									<table cellpadding="0" cellspacing="0" class="edicio">

										<tr class="par">
											<td class="etiqueta"><bean:message key="micro.titulo" /> &gt;</td>
											<td>
												<logic:present name="microForm" property="id">
													<html:hidden property="id" />
													<html:hidden property="claveunica" />
												</logic:present>
												<html:text property="titulo" name="traducciones" size="100" maxlength="256" indexed="true"/>
											</td>
										</tr>

										<!-- TEST -->
										<tr>
											<td class="etiqueta"><bean:message key="micro.descripcion" /> &gt;</td>
											<td><html:textarea property="description" name="traducciones" rows="5" cols="50" indexed="true"/></td>
										</tr>

										<tr class="par">
											<td class="etiqueta"><bean:message key="micro.keywords" /> &gt;</td>
											<td><html:text property="keywords" name="traducciones" size="100" maxlength="256" indexed="true"/></td>
										</tr>

									</table>
								</div>
							</logic:iterate>

							<table cellpadding="0" cellspacing="0" class="edicio">

								<logic:present name="MVS_microsite">
									<tr>
										<td class="etiqueta"><bean:message key="micro.claveunica" /> &gt;</td>
										<td><strong><bean:write name="MVS_microsite" property="claveunica" /></strong></td>
									</tr>
								</logic:present>

								<!-- TEST -->
								<tr class="par">
									<td class="etiqueta"><bean:message key="micro.uo" /> &gt;</td>
									<td>
										<logic:equal name="puedoeditar" value="1">
											<html:hidden name="microForm" property="idUA" />
							            		<html:text name="microForm" property="nombreUA" size="50" maxlength="256" readonly="true" />
												<button type="button" title="<bean:message key="boton.seleccionar" />" onclick="abrirUA();"><img src="imgs/botons/cercar.gif" alt="<bean:message key="boton.seleccionar" />" /></button>
										</logic:equal>
										<logic:equal name="puedoeditar" value="0">
											<html:hidden name="microForm" property="idUA" />
							            		<html:text name="microForm" property="nombreUA" size="50" maxlength="256" disabled="true" readonly="true" />
							            </logic:equal>
									
									<%--
										<logic:equal name="puedoeditar" value="1">
											<html:select name="microForm" property="idUA" styleId="idUA">
												<html:option value=""><bean:message key="micro.uo" /></html:option>
												<html:optionsCollection name="microForm" property="listaUAs" label="nombre" value="id" />
											</html:select>
										</logic:equal>
										<logic:equal name="puedoeditar" value="0">
											<html:select disabled="true" name="microForm" property="idUA" styleId="idUA">
												<html:option value=""><bean:message key="micro.uo" /></html:option>
												<html:optionsCollection name="microForm" property="listaUAs" label="nombre" value="id" />
											</html:select>
										</logic:equal>
									 --%>
									</td>
								</tr>
								<tr>
									<td class="etiqueta"><bean:message key="micro.visible" /> &gt;</td>
									<td><label><html:radio property="visible" value="S" />&nbsp;S&iacute;&nbsp;&nbsp;&nbsp;</label><label><html:radio property="visible" value="N" />&nbsp;No</label></td>
								</tr>
								<tr class="par">
									<td class="etiqueta"><bean:message key="micro.idiomas" /> &gt;</td>
									<td>
										<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="x">
											<label>
												<html:multibox name="microForm" property="idiomas" onclick="checkIdioma(this);" >
													<bean:write name="lang" />
												</html:multibox>
												<bean:message name="lang" />
											</label>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										</logic:iterate>
									</td>
								</tr>
								<logic:equal name="puedoeditar" value="1">
									<tr>
										<td class="etiqueta"><bean:message key="micro.domini" /> &gt;</td>
										<td><html:text property="domini" size="30" maxlength="150" /></td>
									</tr>
									<tr class="par">
										<td class="etiqueta"><bean:message key="micro.version2" /> &gt;</td>
										<td>
											<html:select property="versio">
												<html:option value="IN"><bean:message key="micro.version5.intranet" /></html:option>
												<html:option value="v1"><bean:message key="micro.version5.publico.1" /></html:option>
												<html:option value="v4"><bean:message key="micro.version5.publico.2" /></html:option>
												<html:option value="v5"><bean:message key="micro.version5.publico.5" /></html:option>
											</html:select>
										</td>
									</tr>
									<tr>
										<td class="etiqueta"><bean:message key="micro.orientacionmenu" /> &gt;</td>
										<td>
											<html:select property="orientacionMenu">
												<html:option value="C"><bean:message key="micro.orientacion.clasico" /></html:option>
												<html:option value="H"><bean:message key="micro.orientacion.horizontal" /></html:option>
											</html:select>
										</td>
									</tr>
									<tr class="par">
										<td class="etiqueta"><bean:message key="micro.acceso" /> &gt;</td>
										<td>
											<table cellpadding="0" cellspacing="0">
											<tr>
												<td>
													<html:select property="acceso">
														<html:option value="R"><bean:message key="micro.version5.acceso.restringit" /></html:option>
														<html:option value="P"><bean:message key="micro.version5.acceso.public" /></html:option>
		                                                <html:option value="M"><bean:message key="micro.version5.acceso.rol" /></html:option>
													</html:select>
												</td>
												<td id="rolRowLabel" class="etiqueta rolRow<bean:write name="MVS_microsite" property="acceso"/>" style="width: 20em;">
													<bean:message key="micro.indique.rol" /> &gt;
												</td>
												<td id="rolRowInput" class="etiqueta rolRow<bean:write name="MVS_microsite" property="acceso"/>">
													<html:text property="rol" size="30" maxlength="150" />
												</td>
											</tr>
											</table>
										</td>
									</tr>
									<!-- TEST -->
									<tr>
										<td class="etiqueta"><bean:message key="micro.analytics" /> &gt;</td>
										<td><html:text property="analytics" name="microForm" size="100" maxlength="256"/></td>
									</tr>
									<tr class="par">
										<td class="etiqueta"><bean:message key="micro.uri" /> &gt;</td>
										<td><html:text property="uri" name="microForm"  size="100" maxlength="256"/></td>
										<input type="hidden" name="type" value="mic_uri" />
									</tr>

									<!-- TEST -->
								</logic:equal>

								<logic:equal name="puedoeditar" value="0">
									<tr>
										<td>
											<html:hidden name="microForm" property="rol" />
											<html:hidden name="microForm" property="restringido" />											
											<html:hidden name="microForm" property="buscador" />
										</td>
									</tr>
								</logic:equal>

								<tr>
									<td colspan="2">
										<logic:iterate id="traducciones" name="microForm" property="traducciones" indexId="z" >
											<html:hidden property="txtop1" name="traducciones" indexed="true" /><html:hidden property="urlop1" name="traducciones" indexed="true" />
											<html:hidden property="txtop2" name="traducciones" indexed="true" /><html:hidden property="urlop2" name="traducciones" indexed="true" />
											<html:hidden property="txtop3" name="traducciones" indexed="true" /><html:hidden property="urlop3" name="traducciones" indexed="true" />
											<html:hidden property="txtop4" name="traducciones" indexed="true" /><html:hidden property="urlop4" name="traducciones" indexed="true" />
											<html:hidden property="txtop5" name="traducciones" indexed="true" /><html:hidden property="urlop5" name="traducciones" indexed="true" />
											<html:hidden property="txtop6" name="traducciones" indexed="true" /><html:hidden property="urlop6" name="traducciones" indexed="true" />
											<html:hidden property="txtop7" name="traducciones" indexed="true" /><html:hidden property="urlop7" name="traducciones" indexed="true" />
											<html:hidden property="cabecerapersonal" name="traducciones" indexed="true" />
											<html:hidden property="piepersonal" name="traducciones" indexed="true" />
										</logic:iterate>
									</td>
								</tr>
							</table>
						</div>
					</td></tr>
				</table>
			<!-- test -->
			</div>

			<div id="capa_Campanya" class="capaFormIdioma" style="display:none;">
				<table cellpadding="0" cellspacing="0" class="edicio">
					<tr>
						<td class="etiqueta"><bean:message key="micro.imagencam" /> &gt;</td>
						<td>
							<div style="text-align:left" id="microManagedFileCam">
								<html:hidden property="imagenCampanyaid" />
								<logic:notEmpty name="microForm" property="imagenCampanyanom">
									<html:text property="imagenCampanyanom"/>
									<button type="button" title="<bean:message key="boton.eliminar" />" onclick="borraFile(this,'imagenCampanya','imagenCampanyaid','imagenCampanyabor','imagenCampanyanom');"><img src="imgs/botons/borrar.gif" alt="<bean:message key="boton.eliminar" />" /></button>
									<button type="button" title="<bean:message key="op.12" />" onclick="abrirDoc('<bean:write name="microForm" property="imagenCampanyaid"/>');"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
								</logic:notEmpty>
								<logic:empty name="microForm" property="imagenCampanyanom">
									<html:file property="imagenCampanya" size="30"/>
								</logic:empty>
							</div>
						</td>
					</tr>

					<tr class="par">
						<td class="etiqueta">
							<bean:message key="micro.urlcam" /> &gt;
						</td>
						<td>
							<html:text property="urlcampanya" size="40" maxlength="512" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('urlcampanya','microManamegedURL');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button>
							<br/><div id="microManamegedURL" style="font-weight:bold; color:#8a4700; "><bean:write name="MVS_microsite" property="mvsUrlMigapan" filter="false" ignore="true"/></div>
						</td>
					</tr>

					<tr>
						<td class="etiqueta">
							<bean:message key="micro.titcampa" /> &gt;
						</td>
						<td>
							<ul class="xIdioma">
								<logic:iterate id="traducciones" name="microForm" property="traducciones" indexId="i" >
									<li><label>
										<span>
											<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
												<bean:message key='<%=(i.intValue()==j.intValue()?""+lang:"vacio")%>' />
											</logic:iterate>
										:</span>
										<html:text property="titulocampanya" name="traducciones" size="35" maxlength="256" indexed="true" />
									</label></li>
								</logic:iterate>
							</ul>
						</td>
					</tr>

					<tr class="par">
						<td class="etiqueta">
							<bean:message key="micro.subtitcampa" /> &gt;
						</td>
						<td>
							<ul class="xIdioma">
								<logic:iterate id="traducciones" name="microForm" property="traducciones" indexId="i" >
									<li><label>
										<span>
											<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
												<bean:message key='<%=(i.intValue()==j.intValue()?""+lang:"vacio")%>' />
											</logic:iterate>
										:</span>
										<html:text property="subtitulocampanya" name="traducciones" size="35" maxlength="256" indexed="true" />
									</label></li>
								</logic:iterate>
							</ul>
						</td>
					</tr>

				</table>
			</div>

			<div id="capa_ServOfrecidos" class="capaFormIdioma" style="display:none;">
				<table cellpadding="0" cellspacing="0" class="edicio">
					<logic:iterate id="serv" name="MVS_microsite" property="tiposServicios" indexId="i">
						<logic:notEqual name="serv" property="id" value="100">
							<tr class="<%=((i.intValue()%2==0) ? "par" : "")%>">
								<td class="etiqueta"><bean:write name="serv" property="nombre" /></td>
								<td>
									<html:multibox name="microForm" property="servofr">
										<bean:write name="serv" property="id"/>
									</html:multibox>
								</td>
							</tr>
						</logic:notEqual>
					</logic:iterate>
				</table>
				<input type="hidden" name="servofr" value="100" />
			</div>

            <div id="capa_Aparen" class="capaFormIdioma" style="display:none;">
                <table cellpadding="0" cellspacing="0" class="edicio">
					<logic:equal name="puedoeditar" value="1">
	                    <tr>
	                        <td class="etiqueta"><bean:message key="micro.desarrollo" /> &gt;</td>
	                        <td><html:checkbox property="desarrollo" value="S" disabled="false" /></td>
	                    </tr>
                    </logic:equal>
                    <tr class="par">
                        <td class="etiqueta"><bean:message key="micro.menu" /> &gt;</td>
                        <td>
                            <html:select property="tipomenu">
                                <html:option value="0"><bean:message key="micro.menu.nohay" /></html:option>
                                <html:option value="1"><bean:message key="micro.menu.siniconos" /></html:option>
                                <html:option value="2"><bean:message key="micro.menu.coniconos" /></html:option>
                            </html:select>
                        </td>
                    </tr>
                                        				
                    <tr>
                        <td class="etiqueta"><bean:message key="micro.menucorporativo" /> &gt;</td>
                        <td><label><html:radio property="menucorporativo" value="S" />&nbsp;S&iacute;&nbsp;&nbsp;&nbsp;</label><label><html:radio property="menucorporativo" value="N" />&nbsp;No</label></td>
                    </tr>  
                    
                <logic:equal name="puedoeditar" value="1">                
					<tr class="par">
						<td class="etiqueta"><bean:message key="micro.buscador" /> &gt;</td>
						<td><label><html:radio property="buscador" value="S" />&nbsp;S&iacute;&nbsp;&nbsp;&nbsp;</label><label><html:radio property="buscador" value="N" />&nbsp;No</label></td>
					</tr>
			    </logic:equal>
		
                    <logic:equal name="MVS_microsite" property="versio" value="v5">
                    <tr>
                        <td class="etiqueta"><bean:message key="micro.tema" /> &gt;</td>
                        <td>
                            <html:select property="tema">
                                <html:option value=""> </html:option>
                                <html:options collection="MVS_temas" labelProperty="nombre" property="id"/>
                            </html:select>
                        </td>
                    </tr>
                    </logic:equal>
                    <tr class="par">
                        <td class="etiqueta"><bean:message key="micro.css" /> &gt;</td>
                        <td><span><bean:message key="micro.csspatron" />:</span>
                            <label class="N"><html:radio property="estiloCSSPatron" value="N" /><bean:message key="micro.negre" />&nbsp;&nbsp;&nbsp;</label>&nbsp;&nbsp;
                            <label class="A"><html:radio property="estiloCSSPatron" value="A" /><bean:message key="micro.blau" />&nbsp;&nbsp;&nbsp;</label>&nbsp;&nbsp;
                            <label class="R"><html:radio property="estiloCSSPatron" value="R" /><bean:message key="micro.vermell" />&nbsp;&nbsp;&nbsp;</label>&nbsp;&nbsp;
                            <label class="V"><html:radio property="estiloCSSPatron" value="V" /><bean:message key="micro.verd" />&nbsp;&nbsp;&nbsp;</label>&nbsp;&nbsp;
                            <label class="G"><html:radio property="estiloCSSPatron" value="G" /><bean:message key="micro.groc" />&nbsp;&nbsp;&nbsp;</label>&nbsp;&nbsp;
                            <label class="M"><html:radio property="estiloCSSPatron" value="M" /><bean:message key="micro.magenta" />&nbsp;&nbsp;&nbsp;</label>&nbsp;&nbsp;
                        </td>
                    </tr>
                    <tr class="par">
                        <td class="etiqueta"></td>
                        <td>
                            <div id="microManagedFileCSS">
                                <span><bean:message key="micro.csspropio" />:</span>
                                <html:hidden property="estiloCSSid" />
                                <logic:notEmpty name="microForm" property="estiloCSSnom">
                                    <html:text property="estiloCSSnom"/>
                                    <button type="button" title="<bean:message key="boton.eliminar" />" onclick="borraFile(this,'estiloCSS','estiloCSSid','estiloCSSbor','estiloCSSnom');"><img src="imgs/botons/borrar.gif" alt="<bean:message key="boton.eliminar" />" /></button>
                                    <button type="button" title="<bean:message key="op.12" />" onclick="abrirDoc('<bean:write name="microForm" property="estiloCSSid"/>');"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
                                </logic:notEmpty>
                            </div>
                            <logic:empty name="microForm" property="estiloCSSnom">
                                <html:file property="estiloCSS" size="30"/>
                                &nbsp; - &nbsp;<button type="button" title="<bean:message key="micro.descargaestilo" />" onclick="abrirWindow('/sacmicrofront/css/estilos01_blau.css');"><img src="imgs/botons/css_descargar.gif" alt="" /></button>
                            </logic:empty>
                        </td>
                    </tr>
                </table>
            </div>

		</div>
	</html:form>

<%Microsite micro = (Microsite)session.getAttribute("MVS_microsite"); %>
<%micro.setMensajeError(null); %>
<%micro.setMensajeInfo(null); %>
<% session.setAttribute("MVS_microsite",micro);%>
</body>
</html>

<script type="text/javascript">
<!--

	<logic:present name="MVS_microsite">
	function previsualizar() {
		abrirWindow('<bean:message key="url.aplicacion" />index.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&stat=no');
	}
	</logic:present>

	function submitForm(){
		var accForm = document.getElementById('accFormulario');
		accForm.submit();
	}

	function submitForm(nom_accio) {
		var accForm = document.getElementById('accFormulario');
		accForm.accion.value = nom_accio;
		 if (nom_accio == "Traduir") {
			 accForm.accion.value = "<bean:message key='operacion.traducir'/>";
		 } else if (nom_accio == "Guardar") {
			 accForm.accion.value = "<bean:message key='operacion.guardar'/>";
		}
		accForm.submit();
	}

    var Rcajatemp;
    var divContenedor;
    function Rpopupurl(obj,divcontenedor) {
    	divContenedor=document.getElementById(divcontenedor);
    	Rcajatemp=document.microForm[obj];
		window.open('recursos.do','recursos','scrollbars=yes,width=700,height=400');
    }
	
	function Rmeterurl(laurl) {
		divContenedor.innerHTML="";
		Rcajatemp.value=laurl;
	}

	function abrirUA() {
        poprealcion = obrir("organigrama.jsp?idUA=0", "<bean:message key='boton.seleccionar'/>", 538, 440);
    }
    
    function obrir(url, name, x, y) {
   		nombre = window.open(url, name, 'scrollbars=no, resizable=yes, width=' + x + ',height=' + y);
   		return nombre;
	}
	
	function checkIdioma( theField) {
		if(theField.checked==true){
			if((theField.value != "ca") && (theField.value != "es")){
				var msg = "<bean:message key='micro.alert.traduir.idioma'/>";
				var title = "<bean:message key='micro.alert.informatiu'/>";
				jAlert(msg, title);
			}
		}
	}
	
	function actualizaUA(id, nombre) {
        eval("document.forms[0].idUA.value=id");
        eval("document.forms[0].nombreUA.value=nombre");
    }

	
// -->
</script>



<jsp:include page="/moduls/pieControl.jsp"/>