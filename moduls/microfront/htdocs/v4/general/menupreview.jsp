<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<!-- menu -->
			
			
				
				<logic:present name="MVS_menu">
					<logic:equal name="MVS_microsite" property="tipomenu" value="1">
						<ul>
					    <logic:iterate id="i" name="MVS_menu">
					    	<li><bean:write name="i" property="traduce.nombre"/>
					    	<ul>
					    	<logic:iterate name="i" id="j" property="listacosas">
						    	
								   <bean:define id="objeto" name="j" type="Object"/>
							   		<%
							    	if (objeto instanceof es.caib.gusite.micromodel.Contenido) {
							    	%>
							    		<li><a href="#"><bean:write name="j" property="traduce.titulo"/></a></li>
							    	<%
							    	} else {
							    	%>	
								    	<bean:size id="tamano" name="j" property="listacosas"/>
								    	<logic:notEqual name="tamano" value="1">
								    	<li><a href="#" class="pareADon"><bean:write name="j" property="traduce.nombre"/></a>
									    	<ul>
											<logic:iterate name="j" id="k" property="listacosas">
														<li><a href="#"><bean:write name="k" property="traduce.titulo"/></a></li>
											</logic:iterate>
											</ul>
										</li>
								    	</logic:notEqual>

								    	<logic:equal name="tamano" value="1">
											<li><a href="#"><bean:write name="j" property="traduce.nombre"/></a></li>								    	
								    	</logic:equal>										
							    	<%
							    	}
							   		%>
							   	
					   		</logic:iterate>
					   		</ul>
						</logic:iterate>
						</ul>
					</logic:equal>
					
					<!-- menu con iconos -->
					<logic:equal name="MVS_microsite" property="tipomenu" value="2">
					    <logic:iterate id="i" name="MVS_menu">
					    	<h3>
						    	<logic:present name="i" property="imagenmenu">
							    	<img src="archivopub.do?ctrl=MCRST<bean:write name="MVS_idsite" />ZI<bean:write name="i" property="imagenmenu.id"/>&id=<bean:write name="i" property="imagenmenu.id"/>" alt="<bean:write name="i" property="traduce.nombre"/>"/>
						    	</logic:present>
					    		<bean:write name="i" property="traduce.nombre"/>
					    	</h3>
					    	<ul>
					    	<logic:iterate name="i" id="j" property="listacosas">
						    	
								   <bean:define id="objeto" name="j" type="Object"/>
							   		<%
							    	if (objeto instanceof es.caib.gusite.micromodel.Contenido) {
						    		%>
						    			<bean:define id="idconteactual1" name="j" property="id"/>
										<li>
							    		<a href="#"><bean:write name="j" property="traduce.titulo"/></a>
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
															<li>
																<a href="#"><bean:write name="k" property="traduce.titulo"/></a>
															</li>
												</logic:iterate>
												</ul>
											</li>
								    	</logic:notEqual>

								    	<logic:equal name="tamano" value="1">
											<bean:define id="idconteactual" name="j" property="listacosas[0].id"/>
											<li>
			    							    	<logic:present name="j" property="imagenmenu">
												    	<img src="archivopub.do?ctrl=MCRST<bean:write name="MVS_idsite" />ZI<bean:write name="j" property="imagenmenu.id"/>&id=<bean:write name="j" property="imagenmenu.id"/>" alt="<bean:write name="j" property="traduce.nombre"/>"/>
											    	</logic:present>
													<a href="#"><bean:write name="j" property="traduce.nombre"/></a>
											</li> 	
								    	</logic:equal>
							    	<%
							    	}
							   		%>
							   	
					   		</logic:iterate>
					   		</ul>
						</logic:iterate>
					</logic:equal>							
					
				</logic:present> 			