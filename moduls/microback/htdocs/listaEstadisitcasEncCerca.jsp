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
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/jsDesplegables.js"></script>
</head>

	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.estadisticas" /></li>
		<li><a href="estadisticaenc.do"><bean:message key="menu.estadisticas" /></a></li>
		<li><a href="estadisticaenc.do?idenc=<bean:write name="MVS_encuesta" property="id"/>"><bean:write name="MVS_encuesta" property="traduce.titulo"/></a></li>
		<li class="pagActual"><bean:message key="stat.encuesta.mollapa.cerca" /></li>
	</ul>


	<!-- titol pagina -->
	<h1><img src="imgs/titulos/estadisticas.gif" alt="Estadistica encuesta" />
	<bean:message key="menu.estadisticas" />: 
		<span>
		    <bean:write name="MVS_encuesta" property="traduce.titulo"/>					
		</span>
	</h1>
	
	<p><bean:message key="stat.encuesta.fecha.publicacio" /> <em><bean:write name="MVS_encuesta" property="fpublicacion" format="dd/MM/yyyy"/>	</em>
		<logic:present name="MVS_encuesta" property="fcaducidad">&nbsp;&nbsp;&nbsp;&nbsp; Fecha finalización: <em><bean:write name="MVS_encuesta" property="fcaducidad" format="dd/MM/yyyy"/>	</em></logic:present>
	</p>

	<br/>
	<bean:message key="stat.encuesta.filtro.texto" />
	<br/>
	<div id="botonera">
	<bean:define id="k" value="0" />
	<bean:define id="idPregSelected" value="<%=(String)request.getAttribute("MVS_idPregSelected")%>" />
		 <form action="enccerca.do" name="cerca" id="encCercaPreg">
			  <p> 
		    	<select name="slctpregunta" onchange="slctdependDefault(this.form.slctpregunta,this.form.slctresposta,'')" style="width: 500px" >
		    	    <option>- - QÃ¼estions - -</option>
		    	    <%if(idPregSelected.equals("-1")){ %>
		    	    	<option selected value="-1"> TOTES</option>
		    	    <%}else{ %>	
		    	    	<option value="-1"> TOTES</option>
		    	    <%} %>
		  			<logic:iterate name="MVS_encuesta" property="preguntas" id="i" indexId="indice">
		  			 		<bean:define id="idPreg"><bean:write name="i" property="id"/></bean:define>
		  					  <%if(idPregSelected.equals(idPreg)){ %>
								<option selected value="<bean:write name="i" property="id"/>" ><bean:write name="i" property="traduce.titulo" ignore="true"/></option>
							   <%}else{ %>
								<option value="<bean:write name="i" property="id"/>" ><bean:write name="i" property="traduce.titulo" ignore="true"/></option>
							   <%} %>
					</logic:iterate>
		    	</select>
		    	 &nbsp;&nbsp;
		    	 <select name= "slctresposta" style="width: 200px">
		    	    <option>- - Respostes - -</option>
		    	</select>
		    	 &nbsp;&nbsp;
			    <input type="button" name="botonForm" value="Cerca" onclick="alertControl();"/>
			   
			   </p>
			   <input type="hidden" name="idencuesta" value="<bean:write name="MVS_encuesta" property="id"/>"></input>
		   </form>
	 </div>
     <br/>


		<logic:iterate name="MVS_Preguntas" id="i" indexId="indice">
		<div id="enquestaResultats" class="enquestaResultats" style="padding:.5em .5em .5em .5em; <%=((indice.intValue()%2==0) ? "background:#f8f8f0;" : "")%>">
								<h3><bean:write name="i" property="traduce.titulo" ignore="true"/></h3>
								<logic:present name="i" property="imagen">
								<p><img src="archivo.do?id=<bean:write name="i" property="imagen.id" /> " alt="" /></p>
								</logic:present>
									<bean:define id="esPregunta" value="S" />
									<bean:define id="total" value="0" />
									<logic:notEmpty name="i" property="nrespuestas" >	
										<bean:define id="total" ><bean:write name="i" property="nrespuestas"/></bean:define>
									</logic:notEmpty>
									<% total = (total.equals("0"))?"1":total; %>
									<ul>
										<logic:iterate name="i" id="j" property="respuestas"  indexId="indice">
											<bean:define id="esPregunta" value="N" />
											<bean:define id="numrespuestas" value="0" />
											<logic:notEmpty name="j" property="nrespuestas" >	
												<bean:define id="numrespuestas" ><bean:write name="j" property="nrespuestas"/></bean:define>
											</logic:notEmpty>										
											<li><bean:write name="j" property="traduce.titulo" ignore="true" /> <em>(<%= Integer.parseInt(""+numrespuestas)*100/Integer.parseInt(""+total) %> % - <bean:write name="j" property="nrespuestas" ignore="true" /> vots)</em>
													<bean:define id="titresp" name="j" property="traduce.titulo"/>
													<bean:define id="idrespu"><bean:write name="j" property="id" /></bean:define>
													<bean:define id="idpregu"><bean:write name="j" property="idpregunta"/></bean:define>
													<script language="JavaScript" >
													<!--
														var valor='<%=idrespu %>';
														var texto="<%=titresp %>";
														var idpadre='<%=idpregu %>';
														addarespuesta(valor,texto,idpadre);
													-->
													</script>
												<span class="barra" style="width:<%= Integer.parseInt(""+numrespuestas)*100/Integer.parseInt(""+total) %>%;">&nbsp;</span>
											</li>
										</logic:iterate>
									</ul>
									<logic:equal name="esPregunta" value="N">
										<p class="votsTotals">Vots totals: <strong><bean:write name="i" property="nrespuestas" ignore="true" /></strong></p>
									</logic:equal>
		</div>
	</logic:iterate>
	

</body>
</html>
<script type="text/javascript">
<!--
var idPregSelected = <%=(String)request.getAttribute("MVS_idPregSelected")%>
var idRespSelected = <%=(String)request.getAttribute("MVS_idRespSelected")%>;

if (idPregSelected != -1){
slctdependDefault(document.forms[0].slctpregunta,document.forms[0].slctresposta,idRespSelected);
}

-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
<jsp:include page="/moduls/pieControl.jsp"/>