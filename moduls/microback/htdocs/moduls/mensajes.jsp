<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

		<!--  Mensajes de Informaci�n -->
		<logic:equal name="mensajes" value="si">
		<div class="info" style="color:#4986A5;">
				<html:messages id="message" message="true">
				<%= message %><br/>
				</html:messages>	
		</div>
		</logic:equal>
		
		<!--  Mensajes de Alerta -->
		<logic:equal name="alertas" value="si">
		<div class="alerta" style="color:#FF1111;">
				<html:messages id="message" message="true">
				<%= message %><br/>
				</html:messages>	
		</div>
		</logic:equal>
		
		<!--  Mensajes de Error -->
		<logic:equal name="errores" value="si">
		<div class="alerta" style="font-weight:bold; color:#FF1111;">
				<html:messages id="message" message="true">
				<%= message %><br/>
				</html:messages>	
		</div>
		</logic:equal>
		
		<!--  Mensajes de Validaci�n de Formulario -->
		<logic:equal name="validacion" value="si">
		<div class="alerta" style="font-weight:bold; color:#FF4400;">
		<html:errors/>
		</div>
		</logic:equal>
		
		<!--  Mensajes de Info Configuraci�n General de Microsite -->
		<logic:present name="MVS_microsite" property="mensajeInfo">
		<div class="info" style="color:#4986A5;">
		<bean:write name="MVS_microsite" property="mensajeInfo" />
		</div>
		</logic:present>
		<logic:present name="MVS_microsite" property="mensajeError">
		<div class="alerta" style="font-weight:bold; color:#FF1111;">
		<bean:write name="MVS_microsite" property="mensajeError" />
		</div>
		</logic:present>