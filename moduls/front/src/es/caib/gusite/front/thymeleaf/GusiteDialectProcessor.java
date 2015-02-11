package es.caib.gusite.front.thymeleaf;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.standard.processor.attr.AbstractStandardSingleAttributeModifierAttrProcessor;

public class GusiteDialectProcessor extends
		AbstractStandardSingleAttributeModifierAttrProcessor {

	/*
	 * SOLO DEFINIRLA Y USARLA SI QUEREMOS DEFINIR QUE EL NUEVO DIALECTO PUEDA
	 * USAR VARIABLES YA DEFINIDAS EN LOS .properties O EN .properties CREADOS
	 * POR NOSTROS
	 */
	// private static final String variableTest = "WEB_ILL067";

	public GusiteDialectProcessor() {
		// Only execute this processor for 'nhref' attributes.
		super("nhref");
	}

	public int getPrecedence() {
		// A value of 10000 is higher than any attribute in the
		// SpringStandard dialect. So this attribute will execute
		// after all other attributes from that dialect, if in the
		// same tag.
		return 10000;
	}

	@Override
	protected String getTargetAttributeName(final Arguments arguments,
			final Element element, final String attributeName) {

		final Configuration configuration = arguments.getConfiguration();

		final IStandardExpressionParser parser = StandardExpressions
				.getExpressionParser(configuration);

		final String attributeValue = element.getAttributeValue(attributeName);

		final IStandardExpression expression = parser.parseExpression(
				configuration, arguments, attributeValue);

		final String value = (String) expression.execute(configuration,
				arguments);

		element.setAttribute(attributeName, value);

		return "href";

	}

	@Override
	protected ModificationType getModificationType(final Arguments arguments,
			final Element element, final String attributeName,
			final String newAttributeName) {
		return ModificationType.SUBSTITUTION;
	}

	@Override
	protected boolean removeAttributeIfEmpty(final Arguments arguments,
			final Element element, final String attributeName,
			final String newAttributeName) {
		return false;
	}

}