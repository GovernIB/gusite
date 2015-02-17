package es.caib.gusite.front.thymeleaf;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;
import org.thymeleaf.processor.IProcessor;

import es.caib.gusite.front.service.FrontUrlFactory;

public class GusiteDialect extends AbstractDialect implements
		IExpressionEnhancingDialect {

	public GusiteDialect() {
		super();
	}

	// All of this dialect's attributes and/or tags
	// will start with 'gus:'
	@Override
	public String getPrefix() {
		return "gus";
	}

	// The processors.
	@Override
	public Set<IProcessor> getProcessors() {
		final Set<IProcessor> processors = new HashSet<IProcessor>();
		processors.add(new GusiteDialectProcessor());
		return processors;
	}

	private static final Map<String, Object> EXPRESSION_OBJECTS;

	static {
		Map<String, Object> objects = new HashMap<String, Object>();
		objects.put("gusuri", new FrontUrlFactory());
		EXPRESSION_OBJECTS = Collections.unmodifiableMap(objects);
	}

	@Override
	public Map<String, Object> getAdditionalExpressionObjects(
			IProcessingContext arg0) {
		return EXPRESSION_OBJECTS;
	}

}