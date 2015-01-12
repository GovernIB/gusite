<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

	<!-- cap�al -->
	<logic:present name="MVS_microsite">
		
		<bean:define id="tipocabecera" value="1" />
		<logic:empty name="MVS_microsite" property="tipocabecera" >
			<bean:define id="tipocabecera" value="1" />
		</logic:empty>
		<logic:equal name="MVS_microsite" property="tipocabecera" value="1">
			<bean:define id="tipocabecera" value="1" />
		</logic:equal>
		<logic:equal name="MVS_microsite" property="tipocabecera" value="0">
			<bean:define id="tipocabecera" value="0" />
		</logic:equal>
		<logic:equal name="MVS_microsite" property="tipocabecera" value="2">
			<bean:define id="tipocabecera" value="2" />
		</logic:equal>
		
		
		<% if (request.getHeader("Authorization")!= null){%> 
			<ul id="capsalpestanyes">
				<li><a href="../root">Internet</a></li>
				<li class="seleccionado">Intranet</li>
				<li><a href="http://portaldelpersonal.caib.es">Serveis del Personal</a></li>
			</ul>
		<% } %>		
		
		<logic:equal name="tipocabecera" value="1">
				<div id="capsal">
					<logic:equal name="MVS_microsite" property="restringido" value="S">
						<a accesskey="0" href="http://www.caib.es" ><img class="logo" src="v1/intranet/imgs/capsal/logo.gif" alt="Logo del Govern de les Illes Balears" /></a>
					</logic:equal>
					<logic:notEqual name="MVS_microsite" property="restringido" value="S">
						<a accesskey="0" href="http://www.caib.es" ><img class="logo" src="v1/imgs/capsal/logo.gif" alt="Logo del Govern de les Illes Balears" /></a>
					</logic:notEqual>
					
					<div id="organisme">
						<logic:present name="MVS_ua">
						<bean:write name="MVS_ua" filter="false"/>
						</logic:present>
					</div>
					<div id="capsalIdioma">
					
						<logic:equal name="MVS_microsite" property="buscador" value="S">
							<form name="cercadorForm" action="cercar.do" method="post">
								<input type="hidden" name="idsite" value="<bean:write name="MVS_idsite"/>">
								<input type="hidden" name="lang" value="<bean:write name="MVS_idioma"/>">
								<img src="/root/imgs/capsal/ico_cercador.gif" alt="<bean:message key="cercador.cercador"/>" /> 
								<label for="cercadorTxt">
									<span class="invisible">Cercar en www.caib.es:</span>
									<input name="cerca" id="cercadorTxt" type="text" value="" style="color:#777;" />
								</label>
								<input name="" type="submit" value="<bean:message key="cercador.cercau"/>" />
							</form> 
							<span class="sep">&nbsp;&nbsp;|&nbsp;&nbsp;</span>					
						</logic:equal>
					
						<span class="invisible">Idioma: </span>
						<ul>
							<logic:present name="MVS_seulet">
								<bean:define id="idi" name="MVS_idioma" />
								<% String idi2 = (String)idi; idi2=idi2.toLowerCase(); %>
								
								<bean:size id="tamano" name="MVS_listaidiomas"/>							
							    <logic:iterate id="i" name="MVS_listaidiomas" indexId="indice">
					    			<bean:define id="idiomaa" name="i" property="key"/>
					    			<li>
							    	<% if (idi2.equals(idiomaa)) { %>
									    <strong><bean:write name="i" property="value" filter="false"/></strong>
							    	<% } else { %>
										<a href="<bean:write name="MVS_seulet"/>&amp;lang=<bean:write name="i" property="key"/>"><bean:write name="i" property="value" filter="false"/></a>
							    	<% } %>
							    	<%=(tamano.intValue()==(indice.intValue()+1)?"":" . ")%>
							    	</li>
							    </logic:iterate>
							</logic:present>    
						</ul>
					</div>
				</div>
				
				<!-- titol -->
				<logic:present name="MVS_micrositetitulo">
				<h1><logic:present name="MVS_microsite"><a accesskey="2" href="home.do?idsite=<bean:write name="MVS_microsite" property="id"/>&amp;lang=<bean:write name="MVS_idioma"/>" title="<bean:message key="cabecera.volverinicio"/>">
					<bean:write name="MVS_micrositetitulo" filter="false"/></a></logic:present>
				</h1>
				</logic:present>
				<!-- enlla�os -->
				<ul id="capsalEnllasos">
					<logic:present name="MVS_listacabecera">
						<bean:size id="tamano2" name="MVS_listacabecera"/>
					    <logic:iterate id="i" name="MVS_listacabecera" indexId="indice2">
						    <li <logic:notEmpty name="i" property="value2">class="<bean:write name="i" property="value2"/>"</logic:notEmpty> >
							<logic:match name="i" property="value1" value="http">
							    <a href="<bean:write name="i" property="value1"/>"><bean:write name="i" property="key"/></a>
							</logic:match>
							<logic:notMatch name="i" property="value1" value="http">
							    <a href="<bean:write name="i" property="value1"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:write name="i" property="key"/></a>
						    </logic:notMatch>
						    <%=(tamano2.intValue()==(indice2.intValue()+1)?"":"&nbsp; |")%></li>
					    </logic:iterate>
					</logic:present> 
				</ul>
		</logic:equal>
	
		<logic:equal name="tipocabecera" value="2">	
			<bean:write name="MVS_microsite" property="traduce.cabecerapersonal" filter="false" />
		</logic:equal>
		
	
	
	</logic:present>

