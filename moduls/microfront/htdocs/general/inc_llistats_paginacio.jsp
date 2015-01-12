<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

				<logic:equal name="MVS_busqueda" value="false">
							<p id="navLlistat">
									<logic:present name="MVS_parametros_pagina" property="inicio">
							            	&lt;&lt; <a href="<bean:write name="MVS_seulet_sin"/>&pagina=<bean:write name="MVS_parametros_pagina" property="inicio"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:message key="general.param.inicio"/></a>&nbsp;
						        	</logic:present>
						        	<logic:present name="MVS_parametros_pagina" property="anterior">
								            &lt; <a href="<bean:write name="MVS_seulet_sin"/>&pagina=<bean:write name="MVS_parametros_pagina" property="anterior"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:message key="general.param.anterior"/></a>&nbsp;
						        	</logic:present>
								        	- <bean:write name="MVS_parametros_pagina" property="cursor" /> a <bean:write name="MVS_parametros_pagina" property="cursor_final" /> de <bean:write name="MVS_parametros_pagina" property="nreg" /> -  
						        	<logic:present name="MVS_parametros_pagina" property="siguiente">
									        <a href="<bean:write name="MVS_seulet_sin"/>&pagina=<bean:write name="MVS_parametros_pagina" property="siguiente"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:message key="general.param.siguiente"/></a> &gt;&gt;
							        </logic:present>
							        <logic:present name="MVS_parametros_pagina" property="final">
								            <a href="<bean:write name="MVS_seulet_sin"/>&pagina=<bean:write name="MVS_parametros_pagina" property="final"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:message key="general.param.final"/></a> &gt;
							        </logic:present>
							</p>
				</logic:equal>				
	
	
	
	