package es.caib.gusite.front.thymeleaf;

import org.thymeleaf.Arguments;

import es.caib.gusite.front.service.TemplateNameFactory;
import es.caib.gusite.micromodel.Microsite;

public class GusiteDialectUtil {

	/**
	 * Obtiene la plantilla final configurada
	 * 
	 * @param arguments
	 * @param templateName
	 * @return
	 */
	public static String computeTemplateName(Arguments arguments, String templateName) {

		Microsite microsite = (Microsite) arguments.getContext().getVariables().get("MVS_microsite");
		TemplateNameFactory templateNameFactory = (TemplateNameFactory) arguments.getExpressionObjects().get("gustpl");
		if (microsite != null && templateNameFactory != null) {

			if (templateName.contains("::")) {
				// Se trata de un fragment
				int index = templateName.indexOf("::");
				String templateFile = templateName.substring(0, index).trim();
				String fragment = templateName.substring(index).trim();
				templateName = templateNameFactory.getPlantilla(microsite, templateFile) + fragment;
			} else {
				templateName = templateNameFactory.getPlantilla(microsite, templateName);
			}
		}

		return templateName;

	}

}
