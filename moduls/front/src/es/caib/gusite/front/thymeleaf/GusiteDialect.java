package es.caib.gusite.front.thymeleaf;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;
import org.thymeleaf.processor.IProcessor;

import es.caib.gusite.front.service.FrontUrlFactory;
import es.caib.gusite.front.service.TemplateNameFactory;

@Component("GusiteDialect")
public class GusiteDialect extends AbstractDialect implements
		IExpressionEnhancingDialect {

	@Autowired
	protected FrontUrlFactory urlFactory;

	@Autowired
	protected TemplateNameFactory templateNameFactory;

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
		processors.add(new GusiteReplaceProcessorWrapper());
		processors.add(new GusiteDecoratorProcessor());
		return processors;
	}

	@Override
	public Map<String, Object> getAdditionalExpressionObjects(
			IProcessingContext arg0) {
		Map<String, Object> objects = new HashMap<String, Object>();
		objects.put("gusuri", this.urlFactory);
		objects.put("gustpl", this.templateNameFactory);
		Map<String, Object> eo = Collections.unmodifiableMap(objects);
		return eo;
	}

}