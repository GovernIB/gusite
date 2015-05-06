package es.caib.gusite.front.thymeleaf;

import nz.net.ultraq.thymeleaf.decorator.DecoratorProcessor;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.standard.processor.attr.StandardReplaceFragmentAttrProcessor;

/**
 * Equivalente a th:replace, pero se encarga de utilizar la plantilla
 * personalizada si ésta existe. Lo ideal sería extender
 * StandardReplaceFragmentAttrProcessor, pero no es posible. Así que se trata de
 * un wrapper.
 * 
 * @author at4.net
 * 
 */
public class GusiteDecoratorProcessor extends DecoratorProcessor {

	protected GusiteDecoratorProcessor() {
		super();
	}

	/**
	 * Modifica el decorator según las plantillas configuradas para el sitio
	 * 
	 * @param arguments
	 * @param element
	 * @param attributeName
	 * @return Result of the processing.
	 */
	@Override
	protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {

		String templateName = GusiteDialectUtil.computeTemplateName(arguments, element.getAttributeValue(attributeName));
		
		// String finalAttributeName = "layout:" +
		// DecoratorProcessor.PROCESSOR_NAME_DECORATOR;
		// element.setAttribute(thAttributeName, templateName);
		// element.removeAttribute(attributeName);

		element.setAttribute(attributeName, templateName);

		return super.processAttribute(arguments, element, attributeName);

	}

	@Override
	public int getPrecedence() {
		// TODO fijar la propia
		return StandardReplaceFragmentAttrProcessor.ATTR_PRECEDENCE;
	}

}
