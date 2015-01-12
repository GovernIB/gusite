<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>
	<script type="text/javascript">
	<!--
	<logic:present name="MVS_microsite" >
		<logic:equal name="MVS_microsite" property="tipomenu" value="1">
			<logic:present name="MVS_contenido">
					var nodo = 'p<bean:write name="MVS_contenido" property="id"/>';
					//window.onload = function() { menuJS(nodo); };
			</logic:present>
			<logic:notPresent name="MVS_contenido">
					var nodo = '0';
					//window.onload = function() { menuJS(nodo); };
			</logic:notPresent>
		</logic:equal>		
	</logic:present>
	-->
	</script>
	
	
	<script type="text/javascript">
	
	function onClickAnyo(anyo) {
		document.getElementById("filtro").value = ''; 
		establecerAnyoEnBuscador(anyo);
	}

	function establecerAnyoEnBuscador(anyo) {
		document.getElementById("tanyo").value = anyo; 
	}	
	</script>