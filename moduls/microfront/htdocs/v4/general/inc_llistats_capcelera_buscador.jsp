<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

											<p id="navLlistat">
												<logic:notEmpty name="MVS_listadoanyos">
													<bean:message key="noticia.anyos"/> [
														<logic:iterate name="MVS_listadoanyos" id="i">
												              <a  id="<bean:write name="i" />"  href="noticias.do?tipo=<bean:write name="MVS_claseelemento" property="id" />&amp;tanyo=<bean:write name="i" />&amp;mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<bean:write name="MVS_idioma" />"
												                 onclick="onClickAnyo(<bean:write name="i" />)"
												              ><bean:write name="i" /></a>&nbsp;
												        </logic:iterate>
											        ]
										        </logic:notEmpty>
											</p>
												
									<h2 id="titolPagina"><bean:write name="MVS_tipolistado" /> <logic:notEmpty name="MVS_anyo" ><logic:notEqual name="MVS_anyo" value="null"><bean:write name="MVS_anyo" ignore="true"/></logic:notEqual></logic:notEmpty></h2>
												
									<logic:equal name="MVS_claseelemento" property="buscador" value="S">
										<html:form action="/noticias.do" >
											<input type="hidden" name="lang" value="<bean:write name="MVS_idioma" />" />
											<input type="hidden" name="idsite" value="<bean:write name="MVS_idsite" />" />
											<input type="hidden" name="tipo" value="<bean:write name="MVS_claseelemento" property="id" />" />
											<input type="hidden" name="tipoelemento" value="<bean:write name="MVS_claseelemento" property="id" />" />
											<logic:present name="MVS_anyo">
												<input type="hidden" id="tanyo" name="tanyo" value="<bean:write name="MVS_anyo"/>" />
											</logic:present>
											<p id="navLlistat">
												<bean:message key="listarnoticias.introduce"/>: <html:text property="filtro" />&nbsp;<input type="submit" name="btnSsearch" value="<bean:message key="general.buscar"/>" />
											</p>            
										</html:form>
								    </logic:equal>
								    <logic:present name="MVS_anyo">
										<script type="text/javascript">
										  anyo = "<bean:write name="MVS_anyo"/>";
										  document.getElementById(anyo).style.fontWeight= "bold"; 
										</script>
								     </logic:present>

								     <logic:equal name="MVS_parametros_pagina" property="nreg" value="0">
									    <p><bean:message key="listarnoticias.nohay"/> <bean:write name="MVS_tipolistado" />.</p>
								     </logic:equal>
	
	
	
	