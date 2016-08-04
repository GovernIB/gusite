<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:present name="MVS_microsite" property="id">	
<!-- controlamos que el microsite en memoria es el que está registrado en la cabecera -->
<script type="text/javascript"> 
	var idMicro_det = <bean:write name="MVS_microsite" property="id"/>;
</script>
<script type="text/javascript" src="js/controlSession.js"></script>
</logic:present>