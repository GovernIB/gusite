<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

	
				<logic:present name="MVS_menu">
				
					<bean:define id="idcontenido" value="-1" type="java.lang.Object"/>
					<logic:present name="MVS_contenido">
						<bean:define id="idcontenido" name="MVS_contenido" property="id"/>
					</logic:present>
					<logic:present name="MVS_menu_cont_notic">
						<bean:define id="idcontenido" name="MVS_menu_cont_notic" type="java.lang.String"/>
					</logic:present>
				
					<logic:equal name="MVS_microsite" property="tipomenu" value="1">
						<ul>
					    <logic:iterate id="i" name="MVS_menu">
					    
					    	<logic:present name="i" property="traduce">
					    
					    	<bean:define id="modo" name="i" property="modo"/>
					    	<li ><a href="#" class="<%=((""+modo).equals("C"))?"pareADon":"fijo"%>">
					    	<h3><bean:write name="i" property="traduce.nombre" filter="false"/></h3></a>
					    	<ul>
					    	<logic:iterate name="i" id="j" property="listacosas">
						    	
								   <bean:define id="objeto" name="j" type="Object"/>
							   		<%
							    	if (objeto instanceof es.caib.gusite.micromodel.Contenido) {
						    		%>
						    			<bean:define id="idconteactual1" name="j" property="id"/>
						    			<bean:define id="urlExterna1" name="j" property="urlExterna"/>
										<li <%=((""+idconteactual1).equals(""+idcontenido))?"id=\"p"+idconteactual1+"\" class=\"seleccionado\"":""%>>
							    		<a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica" />&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="j" property="id"/>" target="<%=((""+urlExterna1).equals("true"))?"_blank":"_self"%>" ><bean:write name="j" property="traduce.titulo"/></a>
							    		</li>
							    	<%
							    	} else {
							    	%>	
								    	<bean:size id="tamano" name="j" property="listacosas"/>
								    	<logic:notEqual name="tamano" value="1">
								    		<logic:present name="j" property="traduce">
										    	<li><a href="#" class="pareADon"><bean:write name="j" property="traduce.nombre"/></a>
											    	<ul>
													<logic:iterate name="j" id="k" property="listacosas">
																<bean:define id="idconteactual" name="k" property="id"/>
																<bean:define id="urlExterna" name="k" property="urlExterna"/>
																<li <%=((""+idconteactual).equals(""+idcontenido))?"id=\"p"+idconteactual+"\" class=\"seleccionado\"":""%>>
																	<a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="k" property="id"/>" target="<%=((""+urlExterna).equals("true"))?"_blank":"_self"%>"><bean:write name="k" property="traduce.titulo"/></a>
																</li>
													</logic:iterate>
													</ul>
												</li>
											</logic:present>
								    	</logic:notEqual>

								    	<logic:equal name="tamano" value="1">
											<bean:define id="idconteactual" name="j" property="listacosas[0].id"/>
											<bean:define id="urlExterna" name="j" property="listacosas[0].urlExterna"/>
											<li <%=((""+idconteactual).equals(""+idcontenido))?"id=\"p"+idconteactual+"\" class=\"seleccionado\"":""%>>
													<a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="j" property="listacosas[0].id"/>" target="<%=((""+urlExterna).equals("true"))?"_blank":"_self"%>"><bean:write name="j" property="traduce.nombre"/></a>
											</li> 	
								    	</logic:equal>
							    	<%
							    	}
							   		%>
							   	
					   		</logic:iterate>
					   		</ul>
					   		</li>
					   		</logic:present>
						</logic:iterate>
						</ul>						
					</logic:equal>
					
					<!-- menu con iconos -->
					<logic:equal name="MVS_microsite" property="tipomenu" value="2">
					    <logic:iterate id="i" name="MVS_menu">
					    
					    	<logic:present name="i" property="traduce">
					    
					    	<h3>
						    	<logic:present name="i" property="imagenmenu">
							    	<img src="archivopub.do?ctrl=MCRST<bean:write name="MVS_idsite" />ZI<bean:write name="i" property="imagenmenu.id"/>&id=<bean:write name="i" property="imagenmenu.id"/>" alt="<bean:write name="i" property="traduce.nombre"/>"/>
						    	</logic:present>
					    		<bean:write name="i" property="traduce.nombre" filter="false"/>
					    	</h3>
					    	<ul>
					    	<logic:iterate name="i" id="j" property="listacosas">
						    	
								   <bean:define id="objeto" name="j" type="Object"/>
							   		<%
							    	if (objeto instanceof es.caib.gusite.micromodel.Contenido) {
						    		%>
						    			<bean:define id="idconteactual1" name="j" property="id"/>
						    			<bean:define id="urlExterna1" name="j" property="urlExterna"/>
										<li <%=((""+idconteactual1).equals(""+idcontenido))?"id=\"p"+idconteactual1+"\" class=\"seleccionado\"":""%>>
							    		<a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="j" property="id"/>" target="<%=((""+urlExterna1).equals("true"))?"_blank":"_self"%>"><bean:write name="j" property="traduce.titulo"/></a>
							    		</li>
							    	<%
							    	} else {
							    	%>	
								    	<bean:size id="tamano" name="j" property="listacosas"/>
								    	<logic:notEqual name="tamano" value="1">
									    	<li><a href="#">
		    							    	<logic:present name="j" property="imagenmenu">
											    	<img src="archivopub.do?ctrl=MCRST<bean:write name="MVS_idsite" />ZI<bean:write name="j" property="imagenmenu.id"/>&id=<bean:write name="j" property="imagenmenu.id"/>" alt="<bean:write name="j" property="traduce.nombre"/>"/>
										    	</logic:present>
									    		<bean:write name="j" property="traduce.nombre"/>
									    		</a>
										    	<ul>
												<logic:iterate name="j" id="k" property="listacosas">
															<bean:define id="idconteactual" name="k" property="id"/>
															<bean:define id="urlExterna" name="k" property="urlExterna"/>
															<li <%=((""+idconteactual).equals(""+idcontenido))?"id=\"p"+idconteactual+"\" class=\"seleccionado\"":""%>>
																<a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="k" property="id"/>" target="<%=((""+urlExterna).equals("true"))?"_blank":"_self"%>"><bean:write name="k" property="traduce.titulo"/></a>
															</li>
												</logic:iterate>
												</ul>
											</li>
								    	</logic:notEqual>

								    	<logic:equal name="tamano" value="1">
											<bean:define id="idconteactual" name="j" property="listacosas[0].id"/>
											<bean:define id="urlExterna" name="j" property="listacosas[0].urlExterna"/>
											<li <%=((""+idconteactual).equals(""+idcontenido))?"id=\"p"+idconteactual+"\" class=\"seleccionado\"":""%>>
			    							    	<logic:present name="j" property="imagenmenu">
												    	<img src="archivopub.do?ctrl=MCRST<bean:write name="MVS_idsite" />ZI<bean:write name="j" property="imagenmenu.id"/>&id=<bean:write name="j" property="imagenmenu.id"/>" alt="<bean:write name="j" property="traduce.nombre"/>"/>
											    	</logic:present>
													<a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="j" property="listacosas[0].id"/>" target="<%=((""+urlExterna).equals("true"))?"_blank":"_self"%>"><bean:write name="j" property="traduce.nombre"/></a>
											</li> 	
								    	</logic:equal>
							    	<%
							    	}
							   		%>
							   	
					   		</logic:iterate>
					   		</ul>
					   		</logic:present>
						</logic:iterate>
					</logic:equal>					
					
					
				</logic:present> 



