<%--
  Created by IntelliJ IDEA.
  User: tcerda
  Date: 16/12/2014
  Time: 14:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />

    <title><bean:message key="auditoria.lista" /> - Microsites - Govern de les Illes Balears</title>

    <!-- estils -->
    <link href="css/estils-v.4.1.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="css/jquery-ui-1.10.4.custom.css" rel="stylesheet" type="text/css" media="screen" />
    <!-- /estils -->

    <!-- js -->
    <script type="text/javascript" src="js/jquery-ui-1.10.4/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.10.4/jquery-ui-1.10.4.custom.min.js"></script>
    <!-- <script type="text/javascript" src="js/jquery-1.2.6.pack.js"></script> -->
    <script type="text/javascript" src="js/comuns.js"></script>
    <script type="text/javascript" src="js/llistat.js"></script>
      <script type="text/javascript" src="js/subMenus.js"></script>
    <script type="text/javascript">
      <!--
      // textos
      var txtCerca = '<bean:message key="menu2.microsites.general.buscar.cerca"/>';
      var txtCercador = '<bean:message key="menu2.microsites.general.buscar.alert"/>';
      var txtFecha = '<bean:message key="auditoria.fechaInvalida"/>';

      var nombresIdsMicrosites = [
        <logic:iterate id="i" name="listatodos">
        {'label': '<bean:write name="i" property="traduccion.titulo" filter="true" />', 'value': '<bean:write name="i" property="id" />'},
        </logic:iterate>
      ];

      $(document).ready(function() {



      });
      -->
    </script>
    <!-- /js -->

    <!--[if lt IE 7]>
    <link rel="stylesheet" type="text/css" href="css/estils-v.4.1_ie6.css" media="screen" />
    <![endif]-->
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

        <h1><img src="imgs/titulos/usuaris.gif" alt="Llistats" /><bean:message key="auditoria.gestion" /></h1>
        <logic:equal name="parametros_pagina" property="nreg" value="0">

          <!-- No hay ningun elemento -->
          <p>
            <!-- botonera -->
            <ul id="botonera">
              <li class="sep"><button id="btCercarAdvance" type="button" title="<bean:message key="op.3" />"><img src="imgs/botons/cercar.gif" alt="" /></button></li>
            </ul>
            <br/>&nbsp;<bean:message key="auditoria.vacio" />
            <!-- /botonera -->
            <!--
            <br/>&nbsp;<li id="vacioLink"><html:link href="auditorias.do"><bean:message key="auditoria.volver" /></html:link></li>
            -->
          </p>

          <!-- form -->
          <html:form action="/auditorias.do" styleId="formulari">
            <html:hidden property="entity" styleId="entity" />
            <html:hidden property="idEntity" styleId="idEntity" />
            <html:hidden property="user" styleId="user" />
          </html:form>
          <!-- /form -->

        </logic:equal>

        <logic:notEqual name="parametros_pagina" property="nreg" value="0">

          <!-- total -->
          <p>
            <bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="auditoria.plural" /> </strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.
            <logic:present name="BuscaOrdenaAuditoriasAction">
              <logic:notEmpty name="BuscaOrdenaAuditoriasAction" property="filtro">
                &nbsp;<a href="auditorias.do?filtro=">Veure totes les auditories</a>.
              </logic:notEmpty>
            </logic:present>
          </p>
          <!-- /total -->

          <!-- botonera -->
          <ul id="botonera">
            <li class="sep"><button id="btCercarAdvance" type="button" title="<bean:message key="op.3" />"><img src="imgs/botons/cercar.gif" alt="" /></button></li>
          </ul>
          <!-- /botonera -->

          <!-- form -->
          <html:form action="/auditorias.do" styleId="formulari">
            <html:hidden property="entity" styleId="entity" />
            <html:hidden property="idEntity" styleId="idEntity" />
            <html:hidden property="user" styleId="user" />
            <html:hidden property="date" styleId="date" />
            <html:hidden property="micro" styleId="micro" />
          </html:form>
          <!-- /form -->

          <!-- tabla listado -->
          <table id="llistat">
            <thead>
              <tr>
                <th class="id">Identificador</th>
                <th><bean:message key="auditoria.operacion" /></th>
                <th><bean:message key="auditoria.entidad" /></th>
                <th><bean:message key="auditoria.idEntidad" /></th>
                <th><bean:message key="auditoria.usuario" /></th>
                <th><bean:message key="auditoria.fecha" /></th>
              </tr>
            </thead>
            <tfoot>
              <tr>
                <td colspan="5">
                  <logic:present name="parametros_pagina" property="inicio">
                    &lt;&lt;<html:link action="/auditorias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
                  </logic:present>
                  <logic:present name="parametros_pagina" property="anterior">
                    &lt;<html:link action="/auditorias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;
                  </logic:present>
                  - <bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /> <bean:message key="pagina.de" /> <bean:write name="parametros_pagina" property="nreg" /> -
                  <logic:present name="parametros_pagina" property="siguiente">
                    <html:link action="/auditorias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;
                  </logic:present>
                  <logic:present name="parametros_pagina" property="final">
                    <html:link action="/auditorias.do" paramId="pagina" paramName="parametros_pagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;
                  </logic:present>
                </td>
              </tr>
            </tfoot>
            <tbody>
              <logic:iterate id="i" name="listado" indexId="indice">
                <tr>
                  <td class="operacio"><bean:write name="i" property="operacion"/></td>
                  <td class="entitat"><bean:write name="i" property="entidad"/></td>
                  <td class="idEntity"><bean:write name="i" property="idEntidad"/></td>
                  <td class="usuari"><bean:write name="i" property="usuario"/></td>
                  <td class="data"><bean:write name="i" property="fecha"/></td>
                </tr>
              </logic:iterate>
            </tbody>
          </table>
          <!-- /tabla listado -->

        </logic:notEqual>

      </div>
      <!-- /continguts -->
      <!-- peu -->
      <jsp:include page="peu.jsp"/>
      <!-- /peu -->
    </div>
    <!-- contenidor -->
  </body>
</html>