<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>Gestor Microsites</title>
    <link href="css/index.css" rel="stylesheet" type="text/css" />
    <link href="css/estils.css" rel="stylesheet" type="text/css" />
	<link href="css/estils-v.4.1.css" rel="stylesheet" type="text/css" media="screen" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<script type="text/javascript" src="js/rArxius.js"></script>
    <script type="text/javascript" src="js/subMenus.js"></script>
	 <!--[if lt IE 7]>
		<link rel="stylesheet" type="text/css" href="css/estils-v.4.1_ie6.css" media="screen" />
	<![endif]-->
	<script type="text/javascript">
	
            function onMouseUp() { 
            	var accForm = document.getElementById('accFormulario');
				accForm.submit();
            }

            window.onload = function() {
	            document.getElementById('bntImport').onmouseup = onMouseUp;
	        }

 
	</script>		
</head>

<body>
<!-- contenidor -->
	<div id="contenidor">
	
		<!-- cap -->
		<jsp:include page="cabecera.jsp"/>
		<!-- /cap -->
		
		<!-- marc lateral -->
		<jsp:include page="menuLateralIzq.jsp"/>
		<!-- /marc lateral -->
		
		<!-- continguts -->
		<div id="continguts">
	 
	
	<!-- titol pagina -->
			<h1><img src="imgs/titulos/configuracion.gif" alt="Llistats" /><bean:message key="frontTemas.importar" />.</h1>

	</br>
        <!-- botonera -->
        <div id="botonera">
            <span class="grup">
                <button type="button" title='<bean:message key="frontTemas.volvermantenimiento"/>' onclick='document.location.href="temaFrontEdita.do?idtema=<bean:write name="idtema" filter="false"/>";'>
                    <img src="imgs/botons/tornar.gif" alt='<bean:message key="frontTemas.volvermantenimiento"/>' />
                </button>
            </span>
        </div>

	<logic:notPresent name="mensaje">
		<html:form action="importarTema.do" enctype="multipart/form-data"   styleId="accFormulario">
		<input type="hidden" name="idtema" value="<bean:write name="idtema" filter="false"/>"/>
		<!-- tabla listado -->
		<table id="llistatimportar">
			<thead>
				<tr>
					<th><bean:message key="frontTemas.importar.cap" /></th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td>
							<button type="button" id="bntImport" name="fichero" title="<bean:message key="boton.importar" />" ><img src="imgs/botons/importar.gif" alt="<bean:message key="boton.importar" />" /> &nbsp;<bean:message key="boton.importar" /></button>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<tr>
					<td>
					<table cellpadding="0" cellspacing="0" class="edicio">
						<tr class="par">
							<td class="etiqueta"><bean:message key="frontTemas.seleccimport" /></td>
							<td><html:file property="archi" size="40" /></td>
						</tr>
					</table>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- /tabla listado -->		
		</br>
		  	
		</html:form>
	</br>		
		
	</logic:notPresent>
	
	<logic:present name="mensaje">
  		<br/>
		<div class="alerta" style="font-weight:bold; color:#FF1111;">
			<bean:write name="mensaje" filter="false"/>	
			<br/><br/>
			<strong><bean:message key="frontTemas.importar.log" />:</strong><br/>
			<bean:write name="MVS_importprocessor" ignore="true" filter="false"/>
		</div>	  
	  
	</logic:present>
	</div>
		<!-- /continguts -->
		<!-- peu -->
		<jsp:include page="peu.jsp"/>
		<!-- /peu -->			
	</div>
	<!-- contenidor -->
</body>
</html>
 