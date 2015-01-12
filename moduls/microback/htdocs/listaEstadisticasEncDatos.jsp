<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Dades resposta. Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
</head>

<body>

	<!-- titol pagina -->
	<h1><bean:write name="MVS_encuesta" property="traduce.titulo"/></h1>
	
	
		<h2><bean:write name="MVS_pregunta" property="traduce.titulo"/></h2>
		<p>&nbsp;&nbsp;&nbsp;&nbsp;Resposta: <b><i><bean:write name="MVS_respuesta" property="traduce.titulo"/></i></b></p>
	
			<logic:notEmpty name="MVS_lista_encuesta_datos">
				
					<table cellpadding="0" cellspacing="0" class="llistat noPointer">
					<thead>
					<tr>
						<th width="67%">Texte</th>
						<th>Total</th>
					</tr>
					</thead>
					<tbody>
						<logic:iterate id="i" name="MVS_lista_encuesta_datos" indexId="indice">	
							<tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
								<td width="67%"><bean:write name="i" property="dato"/></td>
								<td><bean:write name="i" property="idrespueta"/></td>
							</tr>
						</logic:iterate>
					</tbody>
					</table>
				
			</logic:notEmpty>
	

	
</body>
</html>
<jsp:include page="/moduls/pieControl.jsp"/>


