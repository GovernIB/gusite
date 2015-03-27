
function gusReplaceProcessor(element, attr, thAttr) {

	alert(element);
	//var exprValue = thymol.getExpression(attr.nodeValue, element);
	return true; // We modified the DOM, return "true"
}

thymol.configurePreExecution( function() {
    thymol.addDialect({
	   	 prefix: 'gus',
	   	 attributeProcessors: [
	   	   {
	   	     name: 'replace',
	   	     processor: gusReplaceProcessor,
	   	     precedence : 600
	   	   }
	   	 ]
	   }); 	
});
