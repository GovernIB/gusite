package es.caib.gusite.front.thymeleaf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.templateresolver.TemplateResolver;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

import es.caib.gusite.front.general.ExceptionFront;
import es.caib.gusite.front.service.TemplateDataService;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;

/**
 * TemplateResolver para GUSITE.
 * 
 * @author at4.net
 * 
 */
public class GusiteTemplateResolver extends TemplateResolver {

	private final static String PREFIX = "db:";
	private final static String DEVPREFIX = "devdb:";
	protected static Log log = LogFactory.getLog(GusiteTemplateResolver.class);
	
	@Autowired
	private TemplateDataService templateDataService;

	public GusiteTemplateResolver() {
		setResourceResolver(new GusiteResourceResolver());
		setCharacterEncoding("UTF-8");
	}

	@Override
	public void setPrefix(String prefix) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSuffix(String suffix) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String computeResourceName(
			TemplateProcessingParameters templateProcessingParameters) {
		String templateName = templateProcessingParameters.getTemplateName();
		if (templateName.startsWith(DEVPREFIX)) {
			return templateName.substring(DEVPREFIX.length());
		} else {
			return templateName.substring(PREFIX.length());
		}
	}

	private class GusiteResourceResolver implements IResourceResolver {
		@Override
		public InputStream getResourceAsStream(
				TemplateProcessingParameters templateProcessingParameters,
				String resourceName) {
			
			PersonalizacionPlantilla template;
			try {
				template = templateDataService.getPlantilla(Long.valueOf(resourceName));
				if (template != null) {
					return new ByteArrayInputStream(template.getContenido().getBytes());
				}
			} catch (NumberFormatException e) {
				log.error("Problema resolviendo recurso:" + resourceName , e);
			} catch (ExceptionFront e) {
				log.error("Problema resolviendo recurso:" + resourceName , e);
			}
			return null;
		}

		@Override
		public String getName() {
			return "gusiteResourceResolver";
		}
	}

}
