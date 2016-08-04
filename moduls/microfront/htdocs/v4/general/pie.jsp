<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
 
 

	 
	<logic:present name="MVS_microsite">
	
			<bean:define id="tipopie" value="1" />
			<logic:empty name="MVS_microsite" property="tipopie" >
				<bean:define id="tipopie" value="1" />
			</logic:empty>
			<logic:equal name="MVS_microsite" property="tipopie" value="1">
				<bean:define id="tipopie" value="1" />
			</logic:equal>
			<logic:equal name="MVS_microsite" property="tipopie" value="0">
				<bean:define id="tipopie" value="0" />
			</logic:equal>
			<logic:equal name="MVS_microsite" property="tipopie" value="2">
				<bean:define id="tipopie" value="2" />
			</logic:equal>
	
			<logic:equal name="tipopie" value="1">
			
				<!-- eines -->
				<div id="eines">
					<bean:define id="idi" name="MVS_idioma" />
					<% String idi2 = (String)idi; idi2=idi2.toLowerCase(); %>
					<div class="esquerra">
						<a accesskey="1" href="accessibilitat.do?idsite=<bean:write name="MVS_microsite" property="id"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:message key="general.accessibilitat"/></a>
					</div>
					<div class="centre">
						<div class="tamanyLletra"></div>
						<span class="idiomes">
							<logic:present name="MVS_seulet">										
							
							<bean:size id="tamano" name="MVS_listaidiomas"/>							
						    <logic:iterate id="i" name="MVS_listaidiomas" indexId="indice">
				    			<bean:define id="idiomaa" name="i" property="key"/>
						    	<% if (idi2.equals(idiomaa)) { %>
								    <strong><bean:write name="i" property="value" filter="false"/></strong>
						    	<% } else { %>
									<a href="<bean:write name="MVS_seulet"/>&amp;lang=<bean:write name="i" property="key"/>"><bean:write name="i" property="value" filter="false"/></a>
						    	<% } %>
						    	<%=(tamano.intValue()==(indice.intValue()+1)?"":" - ")%>
						    </logic:iterate>
							</logic:present> 
							</span>
					</div> 
				    <div class="dreta">
							<!-- enllaços -->
							<logic:present name="MVS_listacabecera">
								<bean:size id="tamano2" name="MVS_listacabecera"/>
							    <logic:iterate id="i" name="MVS_listacabecera" indexId="indice2">
									<logic:match name="i" property="value1" value="http">
									    <a href="<bean:write name="i" property="value1"/>"><bean:write name="i" property="key"/></a>
									</logic:match>
									<logic:notMatch name="i" property="value1" value="http">
									    <a href="<bean:write name="i" property="value1"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:write name="i" property="key"/></a>
								    </logic:notMatch>
								    <%=(tamano2.intValue()==(indice2.intValue()+1)?"":" - ")%>
							    </logic:iterate>
							</logic:present> 
				 	</div> 
			   </div>
			   <!-- /eines -->
			   
			  
					<bean:define id="langMVS" name="MVS_idioma" />
						<% String lang = (String)langMVS; lang=lang.toLowerCase(); %>
		   				<div id="traduccio">
							<div class="centre">
								<% if (!lang.equals("ca")) { %>
                     				<% if (!lang.equals("es")) { %>
                     				<span class="destacat" style="color: rgb(255, 0, 0);">
                             			<p align="center"><bean:message key="general.traduccion"/></p>
                             		</span>	
                 						<% } %>
									<% } %>
							</div>
					</div>

		
				<!-- peu -->
				<div id="peu">
								
					<div class="esquerra">&copy; <a href="http://www.caib.es">Govern de les Illes Balears</a></div>		
		
				 	<!-- contacte -->
					<div class="centre">
						<logic:present name="direccion" >
								<bean:write name="direccion" filter="false" /><br/>
						</logic:present>
					</div>	
					<!-- /contacte -->
					
					<div class="dreta">&nbsp;</div>
	
				</div>
				<!-- /peu -->
		 	
			
			</logic:equal>	
		
		<logic:equal name="tipopie" value="2">	
			<logic:present name="MVS_microsite" property="traduce">
				<bean:write name="MVS_microsite" property="traduce.piepersonal" filter="false" />
			</logic:present>
		</logic:equal>
		
	</logic:present>	