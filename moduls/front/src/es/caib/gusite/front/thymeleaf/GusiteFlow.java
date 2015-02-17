package es.caib.gusite.front.thymeleaf;

import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.fragment.ElementAndAttributeNameFragmentSpec;
import org.thymeleaf.fragment.IFragmentSpec;

/**
 * TODO: el nombre de clase es temporal
 * 
 * @author agarcia
 * 
 */
@Component
public class GusiteFlow {

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private ServletContext servletContext;

	private static Log log = LogFactory.getLog(GusiteFlow.class);

	/**
	 * Procesa una plantilla usando el templateEngine
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param templateName
	 * @param request
	 */
	public String process(Map<String, Object> model, final String templateName,
			String lang, HttpServletRequest request,
			HttpServletResponse response) {

		Locale locale = new Locale(lang.toUpperCase(), lang.toUpperCase());
		IWebContext ctx = new WebContext(request, response,
				this.servletContext, locale, model);

		try {
			if (templateName.contains("::")) {
				// Se trata de un fragment
				int index = templateName.indexOf("::");
				String templateFile = templateName.substring(0, index - 1)
						.trim();
				String fragmentName = templateName.substring(index + 2).trim();
				IFragmentSpec fragmentSpec = new ElementAndAttributeNameFragmentSpec(
						null, "th:fragment", fragmentName, true);

				return this.templateEngine.process(templateFile, ctx,
						fragmentSpec);

			} else {
				return this.templateEngine.process(templateName, ctx);
			}
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException("Could not run template: "
					+ templateName, e);
		}
	}

}
