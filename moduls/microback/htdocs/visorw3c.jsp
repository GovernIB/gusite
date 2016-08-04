<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="org.fundacionctic.taw.treetable.model.ProblemaNode"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<link href="css/accessibilitat.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<script type="text/javascript" src="js/accessibilitat.js"></script>
	<script type="text/javascript">
	<!--
		function ordenar(campo) {
			document.BuscaOrdenaEstadisticaGenActionForm.ordenacion.value=campo;
			document.BuscaOrdenaEstadisticaGenActionForm.submit();
		}
		function lanzaFiltros(formulario) {
			formulario.submit();
		}
	-->
	</script>
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
			<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
			<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
			<li><a href="procesow3c.do?idsite=<bean:write name="MVS_microsite" property="id"/>"><bean:message key="menu.ferramentes" /></a></li>
			<li class="pagActual">Accessibilitat d`un item del microsite</li>
	</ul>
	
	<logic:present name="MVS_accesibilidad">
			<!-- titol pagina -->
			<h1>
				Accessibilitat [ 
					<logic:present name="MVS_noticia">
						<em><bean:write name="MVS_noticia" property="traduce.titulo" filter="false"/></em>
					</logic:present>
					
					<logic:present name="MVS_agenda">
						<em><bean:write name="MVS_agenda" property="traduce.titulo" filter="false"/></em>
					</logic:present>
					
					<logic:present name="MVS_contenido">
						</em>Contenido id <bean:write name="MVS_contenido" property="id" filter="false"/></em>
					</logic:present>
				]
			</h1>
			<!-- continguts -->
			
			<form name="BuscaOrdenaEstadisticaGenActionForm" method="post" action="/sacmicroback/estadisticagen.do">
				<input type="hidden" name="ordenacion" value="" />
				<div id="botonera">
					<span class="grup">
						<button type="button" title="Veure contingut" onclick="previsualizar();"><img src="imgs/menu/visible.gif" alt="Veure contingut" /></button> 
					</span>
					<span class="grup">
						<button type="button" title="Editar" onclick="editar();"><img src="imgs/menu/editar.gif" alt="Editar" /></button> 
					</span>
				</div>
			</form>
			
			<div id="formulario">
			
				<ul id="submenu">
					<li class="selec"><a onclick="mostrarForm(this);" href="javascript:void(0);">Prioritat 1</a></li>
					<li><a onclick="mostrarForm(this);" href="javascript:void(0);">Prioritat 2</a></li>
					<li><a onclick="mostrarForm(this);" href="javascript:void(0);">XHTML</a></li>					
				</ul>
				
		
				<div id="capa_tabla1" class="capaFormIdioma" style="display:block;">
					<logic:present name="MVS_tawresultado" property="urlNode">
					<ul class="acc">
						<li class="cap">
							<span class="automatic">Automático</span>
							<span class="manual">Manual</span>
						</li>
						<li class="principal">
							<span class="automatic"><bean:write name="MVS_tawresultado" property="urlNode.prioridad1.automaticos" /></span>
							<span class="manual"><bean:write name="MVS_tawresultado" property="urlNode.prioridad1.manuales" /></span>
							<span class="text">Prioritat 1: http://www.caib.es</span>
						</li>
						
						<logic:iterate id="i" name="MVS_tawresultado" property="urlNode.prioridad1.childs" type="org.fundacionctic.taw.treetable.model.PuntoVerificacionNode" >
						<li>
							<p>
								<span class="automatic"><bean:write name="i" property="automaticos"/></span>
								<span class="manual"><bean:write name="i" property="manuales"/></span>
								<span class="text"><a href="javascript:;"><bean:write name="i" /></a></span>
							</p>
							<logic:iterate id="j" name="i" property="childVector" type="org.fundacionctic.taw.treetable.model.ComprobacionNode" >
								<p class="detall">
									<span class="automatic">&nbsp;</span>
									<span class="manual">&nbsp;</span>
									<logic:equal name="i" property="automaticos" value="0">
										<span class="text pag"><bean:write name="j" /></span>
									</logic:equal>
									<logic:notEqual name="i" property="automaticos" value="0">
										<span class="text mal"><bean:write name="j" /></span>
										<%
											String serializado="x";
											for (int x=0;x<j.getNumChildren();x++) {
							        			ProblemaNode prn = (ProblemaNode)j.getChild(x);
							        			serializado += " " + prn.toString();
							        		}
										%>
										<bean:define id="problemaserializado" ><%=serializado%></bean:define>
										<bean:write name="problemaserializado"/>
									</logic:notEqual>
									
								</p>
							</logic:iterate>
						</li>
						</logic:iterate>
						
					</ul>
					</logic:present>
					
				</div>
				
				<div id="capa_tabla2" class="capaFormIdioma" style="display:none;">
					<logic:present name="MVS_tawresultado" property="urlNode">
					<ul class="acc">
						<li class="cap">
							<span class="automatic">Automático</span>
							<span class="manual">Manual</span>
						</li>
						<li class="principal">
							<span class="automatic"><bean:write name="MVS_tawresultado" property="urlNode.prioridad2.automaticos" /></span>
							<span class="manual"><bean:write name="MVS_tawresultado" property="urlNode.prioridad2.manuales" /></span>
							<span class="text">Prioritat 2: http://www.caib.es</span>
						</li>
						
						<logic:iterate id="i" name="MVS_tawresultado" property="urlNode.prioridad2.childs" type="org.fundacionctic.taw.treetable.model.PuntoVerificacionNode" >
						<li>
							<p>
								<span class="automatic"><bean:write name="i" property="automaticos"/></span>
								<span class="manual"><bean:write name="i" property="manuales"/></span>
								<span class="text"><a href="javascript:;"><bean:write name="i" /></a></span>
							</p>
							<logic:iterate id="j" name="i" property="childVector" type="org.fundacionctic.taw.treetable.model.ComprobacionNode" >
								<p class="detall">
									<span class="automatic">&nbsp;</span>
									<span class="manual">&nbsp;</span>
									<logic:equal name="i" property="automaticos" value="0">
										<span class="text pag"><bean:write name="j" /></span>
									</logic:equal>
									<logic:notEqual name="i" property="automaticos" value="0">
										<span class="text mal"><bean:write name="j" /></span>
										
										<%
											String serializado2="x";
											for (int x=0;x<j.getNumChildren();x++) {
							        			ProblemaNode prn = (ProblemaNode)j.getChild(x);
							        			serializado2 += " " + prn.toString();
							        		}
										%>
										<bean:define id="problemaserializado2" ><%=serializado2%></bean:define>
										<bean:write name="problemaserializado2"/>
									</logic:notEqual>
								</p>
							</logic:iterate>
						</li>
						</logic:iterate>
						
					</ul>
					</logic:present>

					
				</div>
				
				<div id="capa_tabla0" class="capaFormIdioma" style="display:none;">
					<logic:equal name="MVS_accesibilidad" property="resultado" value="1">
						<strong>No hi ha cap error gramatical en el XHTML.</strong>
					</logic:equal>
					<logic:notEqual name="MVS_accesibilidad" property="resultado" value="1">					
						<ul class="xhtml">
							<logic:iterate id="i" name="MVS_tidyresultado" property="listaerrores">
								<li class="error">
									<bean:write name="i" />
								</li>
							</logic:iterate>
							<logic:iterate id="i" name="MVS_tidyresultado" property="listawarnings">
								<li class="warning">
									<bean:write name="i" />
								</li>
							</logic:iterate>
						</ul>
					</logic:notEqual>
				
				</div>				
			</div>
	</logic:present>
	
	<logic:notPresent name="MVS_accesibilidad">
		<logic:present name="MVS_errorpeticion">
			<h1><bean:write name="MVS_errorpeticion" filter="false" /></h1>
		</logic:present>
	</logic:notPresent>
</body>
</html>

<script type="text/javascript">
<!--

<logic:present name="MVS_accesibilidad">

	<logic:present name="MVS_noticia">
		function previsualizar() {
			abrirWindow('/sacmicrofront/noticia.do?lang=<bean:write name="MVS_accesibilidad" property="idioma"/>&mkey=M375&amp;cont=<bean:write name="MVS_noticia" property="id"/>&stat=no');
		}
	
		function editar() {
			document.location='/sacmicroback/noticiaEdita.do?id=<bean:write name="MVS_noticia" property="id"/>';
		}
	</logic:present>
					
	<logic:present name="MVS_agenda">

			<bean:define id="stfechainicio" name="MVS_agenda" property="finicio"/>
			<%	
				String outDate="";
				try {
					java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
					java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyyMMdd");
					java.util.Date lafecha = df.parse((String)stfechainicio);
					outDate = df2.format( lafecha );	
				} catch (Exception e) {
					outDate = "";	
				}
			%>
		function previsualizar() {
			abrirWindow('/sacmicrofront/agenda.do?lang=<bean:write name="MVS_accesibilidad" property="idioma"/>&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;cont=<%=outDate%>&stat=no');
		}
	
		function editar() {
			document.location='/sacmicroback/agendaEdita.do?id=<bean:write name="MVS_agenda" property="id"/>';
		}
	</logic:present>
	
	<logic:present name="MVS_contenido">
		function previsualizar() {
			abrirWindow('/sacmicrofront/contenido.do?lang=<bean:write name="MVS_accesibilidad" property="idioma"/>&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&cont=<bean:write name="MVS_contenido" property="id"/>&stat=no&tipo=alfa&previsual');
		}
	
		function editar() {
			document.location='/sacmicroback/contenidoEdita.do?id=<bean:write name="MVS_contenido" property="id"/>';
		}
	</logic:present>
					
</logic:present>

//-->
</script>

<jsp:include page="/moduls/pieControl.jsp"/>

