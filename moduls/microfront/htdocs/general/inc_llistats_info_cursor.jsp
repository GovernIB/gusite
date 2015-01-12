<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

											<logic:equal name="MVS_busqueda" value="false">
												<p><bean:message key="listarnoticias.find.encontrados"/> <strong><bean:write name="MVS_parametros_pagina" property="nreg" /> <bean:write name="MVS_tipolistado" /></strong>. <bean:message key="listarnoticias.find.mostrados"/> <strong><bean:write name="MVS_parametros_pagina" property="cursor" /> <bean:message key="listarnoticias.find.al"/> <bean:write name="MVS_parametros_pagina" property="cursor_final" /></strong>.</p>
											</logic:equal>
											<logic:equal name="MVS_busqueda" value="true">
												<p><bean:message key="listarnoticias.find.encontrados"/> <strong><bean:write name="MVS_parametros_pagina" property="nreg" /> <bean:write name="MVS_tipolistado" /></strong>. </p>
											</logic:equal>
	