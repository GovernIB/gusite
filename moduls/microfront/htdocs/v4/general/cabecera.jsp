<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

			<!-- capçal -->
			<logic:present name="MVS_microsite">
				
				<bean:define id="tipocabecera" value="1" />
				<logic:empty name="MVS_microsite" property="tipocabecera" ><bean:define id="tipocabecera" value="1" /></logic:empty>
				<logic:equal name="MVS_microsite" property="tipocabecera" value="1"><bean:define id="tipocabecera" value="1" /></logic:equal>
				<logic:equal name="MVS_microsite" property="tipocabecera" value="0"><bean:define id="tipocabecera" value="0" /></logic:equal>
				<logic:equal name="MVS_microsite" property="tipocabecera" value="2"><bean:define id="tipocabecera" value="2" /></logic:equal>
				
				<logic:equal name="tipocabecera" value="1">
						<div id="capsal">
							<logic:equal name="MVS_microsite" property="restringido" value="S">
								<a accesskey="0" href="http://www.caib.es/" class="destacat" >
									<img class="logo" src="v4/intranet/imgs/capsal/logo.gif" alt="Logo del Govern de les Illes Balears" />
								</a>
							</logic:equal>
							<logic:notEqual name="MVS_microsite" property="restringido" value="S">
								<a accesskey="0" href="http://www.caib.es/" class="destacat" >
									<img class="logo" src="v4/imgs/cap/logo.gif" alt="Logo del Govern de les Illes Balears" />
								</a>
							</logic:notEqual>
						</div>
						
						
						<!-- trazabilidad -->
						<logic:present name="MVS2_mollapan">
						<ul id="mollaPa">
							<bean:write name="MVS2_mollapan" filter="false"/>
						</ul>
						</logic:present>
						<!-- fin trazabilidad -->
						
						<!-- serveis -->
						<logic:equal name="MVS_microsite" property="menucorporativo" value="S">
						
						<bean:define id="ctrad">
							traduccionMap.<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>
						</bean:define>
						
						<ul id="serveis">
							<li><a href="/govern/index.do?lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>"><bean:message key="WEB_CIU003"/></a>
								<ul>
									<li><a href="/govern/presidencia.do?lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>"><bean:message key="WEB2_GOV001"/></a></li>															
									<li><a href="/govern/consellerias.do?lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>"><bean:message key="WEB_POR015"/></a></li>
									<logic:present name="MVS2_uos" >
									<logic:iterate id="i" name="MVS2_uos">
										<logic:notEqual name="i" property="id" value="2">						
											<li>&nbsp;-&nbsp;<a href="<bean:write name="i" property="url"/>"><bean:write name="i" property="abreviatura"/></a></li>
										</logic:notEqual>
									</logic:iterate>
									</logic:present>
									
								</ul>
							</li>
							<!-- EL PRESIDENT -->
                			<li>- <a href="/govern/president/index.do?lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>" accesskey="5"><bean:message key="WEB_ILL013"/></a></li>
                
                			<!-- DESCOBREIX BALEARS -->
                			<li>- <a href="http://www.illesbalears.es/" accesskey="6"><bean:message key="WEB2_TEM103"/></a></li>
                					
			                <!-- SALA DE PREMSA -->
							<li>-<a href="/pidip/comunicats.do?filtro=T&data=null&lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>"><bean:message key="WEB2_GEN141"/></a>                                        
								<ul>
			                        <li><a href="/pidip/comunicats.do?filtro=A&data=null&lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>"><bean:message key="WEB2_GEN119"/></a></li>
									<li><a href="/pidip/comunicats.do?filtro=C&data=null&lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>"><bean:message key="WEB2_GEN123"/></a></li>
									<li><a href="/pidip/consells.do?lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>"><bean:message key="COM_COG001"/></a></li>						
									<li><a href="/pidip/videoteca.do?lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>"><bean:message key="COM_VID001"/></a></li>
									<li><a href="/pidip/guia.do?lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>"><bean:message key="COM_COM013"/></a></li>												
									<li><a href="/pidip/directori.do?lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>"><bean:message key="COM_COM003" /></a></li>																		
			                     	<li><a href="http://www.caib.es/webcaib/twitter/twitter.pdf"><bean:message key="COM_COM018"/></a></li>   
			                     	<li><a href="http://mic.caib.es"><bean:message key="COM_COM019"/></a></li>   
								</ul>
							</li>                					
							
							<!-- SEU ELECTRONICA -->
							<li>-<a href="https://www.caib.es/seucaib"><bean:message key="WEB2_SEU"/></a>
							</li>
							
							<li>-<a href="http://www.caib.es/eboibfront"><bean:message key="WEB_CER020"/></a>
							</li>				
							
							<% if (request.getHeader("Authorization")!= null){%> 
							<!-- personal -->
							<li class="p">-<a href="http://intranet.caib.es">Intranet</a></li>
							<li class="p">-<a href="http://portaldelpersonal.caib.es">Serveis del Personal</a></li>
							<!-- /personal -->
							<% } %>
						</ul>
						
						</logic:equal>
						<!-- /serveis -->
						
						<!-- titol -->
						<logic:present name="MVS_microsite">
							<logic:present name="MVS_micrositetitulo">
							<h1 class="titol"><a accesskey="2" href="home.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<%=((String)request.getSession().getAttribute("MVS_idioma")).toLowerCase() %>" title="<bean:message key="cabecera.volverinicio"/>">
							<bean:write name="MVS_micrositetitulo" filter="false"/></a>
							</h1>
							</logic:present>
						</logic:present>
						<!-- /titol -->
				</logic:equal>
				<logic:equal name="tipocabecera" value="2">	
					<bean:write name="MVS_microsite" property="traduce.cabecerapersonal" filter="false" />
				</logic:equal>
		
			
			</logic:present>
	
	
	
	
	
	
	