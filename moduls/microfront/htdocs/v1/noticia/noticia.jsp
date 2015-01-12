<%@ page language="java"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">	
	<logic:present name="MVS_noticia" property="traduce">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:write name="MVS_noticia" property="traduce.titulo" filter="false"/></title>
	</logic:present>
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v1/js/globales.js"></script>
	
	<jsp:include page="/v1/general/scriptmenu.jsp"/>
	

	</head>

	
	<body>
		
		<div id="contenedor">
			<!-- cap�al -->
			<jsp:include page="/v1/general/cabecera.jsp"/>

			<!-- continguts -->
			<div id="continguts">
				<!-- trazabilidad -->
				<ul id="mollaPa">
					<li><a href="home.do?idsite=<bean:write name="MVS_idsite" />&amp;lang=<bean:write name="MVS_idioma" />"><bean:message key="general.inicio"/></a></li>
					<li><a href="noticias.do?idsite=<bean:write name="MVS_idsite" />&amp;lang=<bean:write name="MVS_idioma" />&amp;tipo=<bean:write name="MVS_noticia" property="tipo.id" />"><bean:write name="MVS_tiponoticia" /></a></li>
					<li><bean:message key="noticia.detalle"/></li>
				</ul>

				<!-- campanya -->
				<logic:present name="MVS_home_tmp_campanya">
					<bean:write name="MVS_home_tmp_campanya" filter="false"/>
					<!-- Capa separadora. NO quitar nunca  -->	
					<div id="enllasDestPeu"></div>
				</logic:present>
				<!-- fin campanya -->				


				<!-- menu -->
					<logic:present name="MVS_microsite">
							<logic:equal name="MVS_microsite" property="tipomenu" value="1">
								<div id="marcLateral">
								<h2 class="invisible">Men� general</h2>
									<jsp:include page="/v1/general/menu.jsp"/>
								</div>					
							</logic:equal>
							<logic:equal name="MVS_microsite" property="tipomenu" value="2">
								<div id="marcLateralAmbIcones">
								<h2 class="invisible">Men� general</h2>
									<jsp:include page="/v1/general/menu.jsp"/>
								</div>					
							</logic:equal>
						</logic:present>	
				<!-- fin menu -->
											 
				<!-- informacio con o sin menu -->
				<logic:equal name="MVS_microsite" property="tipomenu" value="0">
					<div id="infoNoMenu">
				</logic:equal>
				<logic:notEqual name="MVS_microsite" property="tipomenu" value="0">
					<div id="info">
				</logic:notEqual>	
							
							
									<!-- titol -->
									<h2 id="titolPagina"><bean:message key="noticia.detalle"/> <bean:write name="MVS_tiponoticia" /></h2>
									<!-- informacio -->
									<div id="infoNoticia">
										<logic:present name="MVS_noticia" property="traduce">
											<h3 id="titolNoticia"><bean:write name="MVS_noticia" property="traduce.titulo" filter="false"/></h3>
											<p id="subtitol"><bean:write name="MVS_noticia" property="traduce.subtitulo" filter="false"/></p>
											<logic:notEmpty name="MVS_noticia" property="imagen">
												<img id="imgNoticia" src="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="MVS_noticia" property="id" />ZI<bean:write name="MVS_noticia" property="imagen.id" />&amp;id=<bean:write name="MVS_noticia" property="imagen.id" />" alt="" />							
											</logic:notEmpty>
											<p id="fontNoticia"><bean:write name="MVS_noticia" property="fpublicacion" format="dd/MM/yyyy"/><logic:notEmpty name="MVS_noticia" property="traduce.fuente"> - <bean:write name="MVS_noticia" property="traduce.fuente" filter="false"/></logic:notEmpty></p>
											<p><bean:write name="MVS_noticia" property="traduce.texto" filter="false"/></p>
											<logic:notEmpty name="MVS_noticia" property="traduce.docu">
												<p><bean:message key="noticia.descdocumento"/></p>
												<p><bean:message key="general.archivo"/> <bean:write name="MVS_noticia" property="traduce.docu.mime" />, <bean:write name="MVS_noticia" property="traduce.docu.peso" /> bytes - 
												<a href="archivopub.do?ctrl=<bean:write name="MVS_servicio"/><bean:write name="MVS_noticia" property="id" />ZI<bean:write name="MVS_noticia" property="traduce.docu.id" />&amp;id=<bean:write name="MVS_noticia" property="traduce.docu.id" />" target="blank"><bean:write name="MVS_noticia" property="traduce.docu.nombre" /></a></p>
											</logic:notEmpty>
											<logic:notEmpty name="MVS_noticia" property="traduce.laurl">
												<p>
												   		 <bean:define id="externa" name="MVS_noticia" property="traduce.laurl" type="java.lang.String" />
														 <bean:define id="sitio" name="MVS_idsite"  />
														 <a href="<bean:write name="MVS_noticia" property="traduce.laurl"/>" <%if(externa.indexOf("idsite="+sitio)==-1){out.println("target='_blank'");}else{out.println("target='_self'");}%>>													
														 <logic:notEmpty name="MVS_noticia" property="traduce.urlnom" >
															<bean:write name="MVS_noticia" property="traduce.urlnom" ignore="true"/>
														 </logic:notEmpty>
														 <logic:empty name="MVS_noticia" property="traduce.urlnom" >
															<bean:message key="url.adicional"/>
														 </logic:empty>													
														</a>
												</p>
											</logic:notEmpty>
										</logic:present>
									</div>
													
				</div>	
				<!-- fin informacio -->
				

			</div>
			<!-- fin continguts -->
			
			
			<!-- peu -->
			<jsp:include page="/v1/general/pie.jsp"/>
			<!-- fin peu -->
		</div>	
	</body>
</html>
