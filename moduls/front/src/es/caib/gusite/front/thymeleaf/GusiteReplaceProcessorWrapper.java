package es.caib.gusite.front.thymeleaf;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;
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
public class GusiteReplaceProcessorWrapper extends AbstractAttrProcessor {

	private StandardReplaceFragmentAttrProcessor standardProcessor = new StandardReplaceFragmentAttrProcessor();

	protected GusiteReplaceProcessorWrapper() {
		super(StandardReplaceFragmentAttrProcessor.ATTR_NAME);
	}

	@Override
	protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {

		String templateName = GusiteDialectUtil.computeTemplateName(arguments, element.getAttributeValue(attributeName));
		String thAttributeName = "th:" + StandardReplaceFragmentAttrProcessor.ATTR_NAME;
		element.setAttribute(thAttributeName, templateName);
		element.removeAttribute(attributeName);
		return this.standardProcessor.processAttribute(arguments, element, thAttributeName);

	}

	@Override
	public int getPrecedence() {
		// TODO fijar la propia
		return StandardReplaceFragmentAttrProcessor.ATTR_PRECEDENCE;
	}

}
