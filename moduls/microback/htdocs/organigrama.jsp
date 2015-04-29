<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>Gestor Microsites / Organigrama</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="js/jquery-ui-1.10.4/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="js/jqTree/tree.jquery.js"></script>
    <link rel="stylesheet" href="js/jqTree/jqtree.css"/>
    
	
</head>

<body>	
	<div id="tree1" data-url="ajaxOrganigrama.do"></div>		
	


</body>
<script>

$(function() {
    var $tree = $('#tree1');

    $tree.tree({
        dragAndDrop: false,
        saveState: false,
    });
    
    $tree.bind(
    	    'tree.dblclick',
    	    function(event) {
    	        //console.log(event.node);
    	    	window.opener.actualizaUA(event.node.id, event.node.name);
    	    	window.opener.focus();
    	    	window.close();
    	    }
    	);
    $tree.bind(
    );
    
    
});
</script>
</html>

