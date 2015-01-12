<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


		<!-- cap -->
		<div id="cap">
			<div id="titol"><a href="http://intranet.caib.es"><img src="imgs/logo_caib.gif" alt="Govern de les Illes Balears" /></a><img src="imgs/logo_mini.gif" alt="Microsites" /></div>
			<div id="versio"></div>
						<!-- usuari -->
			<div id="usuari">
				<bean:message key="identificacion.usuario" />
				<bean:write name="MVS_usuario" property="nombre" />				
				(<bean:write name="username" ignore="true" />)
			</div>
			<!-- /usuari -->
		</div>
		
		<logic:present name="MVS_manteniment">
			<div align="center" style="border:2px solid; border-radius:5px; padding:10px; background:yellow">
			<bean:message key="mantenimiento"/>
			</div>
		</logic:present>
		<!-- /cap -->