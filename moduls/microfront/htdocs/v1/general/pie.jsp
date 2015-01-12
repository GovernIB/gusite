<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

	<a accesskey="1" href="accessibilitat.do?idsite=<bean:write name="MVS_microsite" property="id"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:message key="general.accessibilitat"/></a>
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
			<div id="peu">
							
			<logic:equal name="lang" value="es">
				<a href="http://dgtic.caib.es/difusioad/index_es.html" title="Enllaç a la web d'Administració Digital"><img id="logoAD" src="v1/imgs/peu/logo_ad.gif" alt="Logo de l'Administraci&oacute; Digital" /></a>
			</logic:equal>
			<logic:notEqual name="lang" value="es">
				<a href="http://dgtic.caib.es/difusioad/index.html" title="Enllaç a la web d'Administració Digital"><img id="logoAD" src="v1/imgs/peu/logo_ad.gif" alt="Logo de l'Administraci&oacute; Digital" /></a>
			</logic:notEqual>

				<ul id="enllasos">
					<logic:present name="MVS_listapie">
						<bean:size id="tamano" name="MVS_listapie" />
					    <logic:iterate id="i" name="MVS_listapie" indexId="indice">
						    <li><a href="<bean:write name="i" property="value"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:write name="i" property="key"/></a>
							<%=(tamano.intValue()==(indice.intValue()+1)?"":"&nbsp; |")%>
						    </li>
						    
					    </logic:iterate>
					</logic:present>
				</ul>
				&copy; Govern de les Illes Balears
			</div>
		</logic:equal>	
		
		<logic:equal name="tipopie" value="2">	
			<logic:present name="MVS_microsite" property="traduce">
				<bean:write name="MVS_microsite" property="traduce.piepersonal" filter="false" />
			</logic:present>
		</logic:equal>
		
	</logic:present>	